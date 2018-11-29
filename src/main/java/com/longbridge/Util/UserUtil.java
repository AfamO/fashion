package com.longbridge.Util;
import com.longbridge.dto.UserDTO;
import com.longbridge.exception.AppException;
import com.longbridge.exception.PasswordException;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.respbodydto.LogInResp;
import com.longbridge.security.JwtTokenUtil;
import com.longbridge.security.JwtUser;
import com.longbridge.security.repository.UserRepository;
import com.longbridge.security.service.JwtAuthenticationResponse;
import com.longbridge.services.MailService;
import com.longbridge.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mobile.device.Device;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.util.StringUtils;

import java.util.*;

/**
 * Created by longbridge on 11/5/17.
 */
@Service
public class UserUtil {
    @Autowired
    UserRepository userRepository;
    @Autowired
    DesignerRepository designerRepository;

    @Autowired
    SMSAlertUtil smsAlertUtil;

    @Autowired
    UniqueNumberUtil uniqueNumberUtil;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    MailService mailService;

    @Autowired
    WalletService walletService;


    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    MessageSource messageSource;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    PocketRepository pocketRepository;

    @Autowired
    SendEmailAsync sendEmailAsync;

    @Autowired
    private UserDetailsService userDetailsService;

    private Locale locale = LocaleContextHolder.getLocale();


    public Response registerUser(UserDTO passedUser){
        Map<String,Object> responseMap = new HashMap();
        try {
            Date date = new Date();
            User user = userRepository.findByEmail(passedUser.getEmail());
            List<String> errors = new ArrayList<String>();

            if(user != null){
                errors.add("Email already exists");
            }
            if(passedUser.getRole() == null){
                return new Response("99", "User has no role", null);
            }
            if(passedUser.getRole().equalsIgnoreCase("designer")){
                User user2 = userRepository.findByPhoneNo(passedUser.getPhoneNo());
                if(user2 != null){
                    errors.add("Phone number already exist");
                }
            }
            if(errors.size() > 0){
                return new Response("99",StringUtils.join(errors, ","), null);
            }else{
                user=new User();
                user.setEmail(passedUser.getEmail());
                user.setPhoneNo(passedUser.getPhoneNo());
                user.setPassword(Hash.createPassword(passedUser.getPassword()));
                user.setFirstName(passedUser.getFirstName());
                user.setLastName(passedUser.getLastName());
                user.setDateOfBirth(passedUser.getDateOfBirth());
                user.setGender(passedUser.getGender());
                user.setRole(passedUser.getRole());

                //todo create user wallet, call wallet api
                //walletService.createWallet(passedUser,user);
            }

            if(passedUser.getRole().equalsIgnoreCase("designer")){
                Designer designer = new Designer();
                designer.setCreatedOn(date);
                designer.setUpdatedOn(date);
                designer.setUser(user);
                designerRepository.save(designer);
                sendToken(passedUser.getEmail());
                sendEmailAsync.notifyCustomerCare(user);
            }
            getActivationLink(user);
            sendEmailAsync.sendWelcomeEmailToUser(user);
            userRepository.save(user);
            return new Response("00","Registration successful",responseMap);

        } catch (Exception e) {
            e.printStackTrace();
            return new Response("99","Error occurred internally",responseMap);

        }

    }

    public void sendToken(String email){
        try {
            User passedUser = userRepository.findByEmail(email);
            char[] token = uniqueNumberUtil.OTP(5);
            List<String> phonenumbers = new ArrayList<>();
            phonenumbers.add(passedUser.getPhoneNo());
            String message = String.format(messageSource.getMessage("user.sendtoken.message", null, locale), String.valueOf(token));
            smsAlertUtil.sms(phonenumbers, message);
            System.out.println(String.valueOf(token));
            saveToken(String.valueOf(token), passedUser);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }



    private void saveToken(String tokenString,User user){
        Token token = tokenRepository.findByUser(user);
        if(token != null){
            token = tokenRepository.findByUser(user);
            token.setToken(tokenString);
            token.setValidated(false);
            tokenRepository.save(token);
        }
        else {
            token= new Token();
            token.setToken(tokenString);
            token.setUser(user);
            tokenRepository.save(token);
        }
    }

@Async
    public Response getActivationLink(User passedUser){
        Map<String,Object> responseMap = new HashMap();
        try {
            User user = userRepository.findByEmail(passedUser.getEmail());
            if(user!=null){
                String name = user.getFirstName() + " " + user.getLastName();
                String mail = user.getEmail();
                String encryptedMail = Base64.getEncoder().encodeToString(mail.getBytes());
                String message="";
                String activationLink="";
                try {
                    Context context = new Context();
                    context.setVariable("name", name);
                    activationLink = messageSource.getMessage("activation.url.link",null,locale)+encryptedMail;
                    System.out.println(activationLink);
                    context.setVariable("link", activationLink);

                    message = templateEngine.process("activationlinktemplate", context);

                    mailService.prepareAndSend(message,mail,messageSource.getMessage("user.welcome.subject", null, locale));

                }catch (MailException me){
                    me.printStackTrace();
                    //throw new AppException("",passedUser.getFirstName() + passedUser.getLastName(),passedUser.getEmail(),messageSource.getMessage("user.welcome.subject", null, locale),activationLink);

                }
                return new Response("00","Link successfully sent",responseMap);
            }else{
                return new Response("56","User does not exist",responseMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Response("99","Error occured internally",responseMap);

        }

    }


    public Response createAdmin(User passedUser){
        Map<String,Object> responseMap = new HashMap();
        try {
            Date date = new Date();
            User user = userRepository.findByEmail(passedUser.getEmail());
            if(user==null){
                passedUser.setPassword(Hash.createPassword(passedUser.getPassword()));
                passedUser.createdOn=date;
                userRepository.save(passedUser);
                String name = passedUser.getFirstName() + " " + passedUser.getLastName();
                String mail = passedUser.getEmail();
                String message="";
                try {
                    Context context = new Context();
                    context.setVariable("name", name);
                    if(passedUser.getRole().equalsIgnoreCase("admin")) {
                        message = templateEngine.process("adminwelcomeemail", context);
                    }
                    mailService.prepareAndSend(message,mail,messageSource.getMessage("user.welcome.subject", null, locale));
                }catch (MailException me){
                    me.printStackTrace();
                    throw new AppException(passedUser.getFirstName() + passedUser.getLastName(),passedUser.getEmail(),messageSource.getMessage("user.welcome.subject", null, locale));

                }
                return new Response("00","Registration successful",responseMap);
            }else{
                return new Response("99","Email already exists",responseMap);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new Response("99","Error occured internally",responseMap);

        }
    }


    public void forgotEmail(String userPhoneNumber){

        try{
            User user = userRepository.findByPhoneNo(userPhoneNumber);
            System.out.println(user);
            if(user != null){
                String message = String.format(messageSource.getMessage("user.retrieveemail", null, locale), user.getEmail());
                List<String> phoneNumbers = new ArrayList<>();
                phoneNumbers.add(user.getPhoneNo());
                smsAlertUtil.sms(phoneNumbers, message);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    public Object forgotPassword(UserDTO passedUser){
        Map<String,Object> responseMap = new HashMap();
        String newPassword="";
        String name = "";
        String mail = "";
        String changePasswordLink="";
        try {
            User user = userRepository.findByEmail(passedUser.getEmail());
            if(user!=null){
                if(!"Y".equalsIgnoreCase(user.getActivationFlag())){
                return new Response("57","Account not verified, Kindly click the link sent to your email to verify your account",responseMap);
                }
                newPassword=UUID.randomUUID().toString().substring(0,10);
                user.setPassword(Hash.createPassword(newPassword));
                user.setLinkClicked("N");

                name = user.getFirstName() +" " + user.getLastName();
                mail = passedUser.getEmail();
                String encryptedMail = Base64.getEncoder().encodeToString(mail.getBytes());
                String currentPageUrl = passedUser.getCurrentUrl();
                String encryptedNewPassword=Base64.getEncoder().encodeToString(newPassword.getBytes());
                //String encryptedMail = Hash.createEncryptedLink(mail);
                changePasswordLink = messageSource.getMessage("change.password.link",null,locale)+encryptedMail+"&url="+currentPageUrl+"&newPassword="+encryptedNewPassword;
                System.out.println(changePasswordLink);

                Context context = new Context();
                context.setVariable("password", newPassword);
                context.setVariable("name", name);
                context.setVariable("link", changePasswordLink);
                String message = templateEngine.process("emailtemplate", context);

                mailService.prepareAndSend(message,mail,messageSource.getMessage("password.reset.subject", null, locale));

                userRepository.save(user);
                return new Response("00","Operation Successful, Password successfully sent to email",responseMap);
            }else{
                return new Response("99","Email does not exist",responseMap);
            }
        }catch (MailException me) {
            me.printStackTrace();
            throw new AppException(newPassword,name,mail,messageSource.getMessage("password.reset.subject", null, locale),changePasswordLink);

        }
        catch (Exception e) {
            e.printStackTrace();
            return new Response("99", "Error occured internally", responseMap);
        }
    }

    public Object validatePassword(User passedUser){
        Map<String,Object> responseMap = new HashMap();

        try {

            User user = userRepository.findByEmail(passedUser.getEmail());
            if (user != null) {

                if (passedUser.getPassword() .equalsIgnoreCase(user.getPassword())) {
                    return new Response("00", "Operation Successful", responseMap);
                } else {
                    return new Response("99", "Error occurred", responseMap);
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
            return new Response("99", "Error occured internally", responseMap);
        }
        return new Response("99", "Error occurred", responseMap);
    }

    public Response validateUser(User passedUser, Device device){
        LogInResp logInResp = new LogInResp();
        try {
            User user = userRepository.findByEmail(passedUser.getEmail());
            boolean valid = false;

            if(user!=null){
                try{

                    if(user.getRole().equalsIgnoreCase("designer")){
                        if(!user.getActivationFlag().equalsIgnoreCase("Y")){
                            return new Response("57","Account not verified",logInResp);
                        }
                    }

                   // check if(user.socialFlag) is Y and set valid as true
                    if(user.getSocialFlag() != null){
                        if(user.getSocialFlag().equalsIgnoreCase("Y")){
                            valid=true;
                        }
                    }

                    if (!valid) {
                        //If N, validate password
                        valid = Hash.checkPassword(passedUser.getPassword(), user.getPassword());
                        if(user.getUserWalletId() != null) {
                            Response resp = walletService.getWalletBalance(user);
                            if (resp.getStatus().equalsIgnoreCase("00")) {
                                user.setWalletBalance((Double) resp.getData());
                                userRepository.save(user);
                            }
                        }else {
                            user.setWalletBalance(0.0);
                            userRepository.save(user);
                        }

                    }
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            if(user!=null && valid){
                if(user.getRole().equalsIgnoreCase("designer")){
                    logInResp.setRole(2);
                }
                else if(user.getRole().equalsIgnoreCase("admin")||user.getRole().equalsIgnoreCase("super_admin")){
                    logInResp.setRole(3);
                }
                else if(user.getRole().equalsIgnoreCase("superadmin")){
                    logInResp.setRole(4);
                }
                else if(user.getRole().equalsIgnoreCase("qa")){
                    logInResp.setRole(5);
                }
                else {
                    logInResp.setRole(1);
                }
                /*
                    Generating Token for user and this will be required for all request.
                 */

                final UserDetails userDetails = userDetailsService.loadUserByUsername(passedUser.getEmail());
                System.out.println("userdetails is "+userDetails.toString());
                final String token = jwtTokenUtil.generateToken(userDetails, device);
                System.out.println("Token is "+token);
                //implement sessionid

                logInResp.setToken(token);
                logInResp.setId(user.id);
               // responseMap.put("token",token);
                return new Response("00","Login successful",logInResp);
            }else{
                return new Response("99","Invalid username/password",logInResp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response("99","Error occurred internally",logInResp);
    }



    public User fetchUserDetails2(String token){
        JwtUser user = getAuthenticationDetails(token);
        if(user!=null){
            return userRepository.findByEmail(user.getUsername());
        }
        else{
            return null;
        }
    }


    public List<User> getUsers(){
            return userRepository.findByRole("user");
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Object fetchUserDetails(String email, String token){
        Map<String,Object> responseMap = new HashMap();
        try {
            User user = userRepository.findByEmail(email);
            if(user!=null){
                if(user.getAddresses()!=null) {
                    user.getAddresses().forEach(address ->
                    {
                        if (address.user != null) {
                            address.user = null;
                        }
                    });
                }


                /*
                todo : refreshing token needs to be discussed if necessary
                 */
//                refreshAuthenticationDetails(user,token);

                Pocket pocket = pocketRepository.findByUser(user);
                if(pocket==null){
                    user.setPocket(new Pocket());
                }else {
                    user.setPocket(pocket);
                }
                responseMap.put("userDetails",user);
                return new Response("00","User found",responseMap);
            }else{
                return new Response("99","User not found",responseMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response("99","Error occurred internally",responseMap);
    }

    public User getUserByEmail(String email){
        User user = userRepository.findByEmail(email);
        return  user;
    }

    public JwtUser getAuthenticationDetails(String token){
        JwtUser user = null;
        if(token!=null) {
            String username = jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ", ""));
            user = (JwtUser) userDetailsService.loadUserByUsername(username);
        }
        return user;
    }

    public Response tokenNullOrInvalidResponse(String token){
        Map<String,Object> responseMap = new HashMap();
        if(token!=null){
            responseMap.put("passed_token",token.replace("Bearer ", ""));
        }else{
            responseMap.put("passed_token",null);
        }
        return new Response("56","Invalid token passed",responseMap);
    }

    public void refreshAuthenticationDetails(User passedUser, String token){
        JwtUser user = null;
        if(token!=null) {
            passedUser.setPassed_token(token.replace("Bearer ",""));
            String username = jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ", ""));
            user = (JwtUser) userDetailsService.loadUserByUsername(username);
            if (user!=null) {
                if (jwtTokenUtil.canTokenBeRefreshed(token.replace("Bearer ",""), user.getLastPasswordResetDate())) {
                    String refreshedToken = jwtTokenUtil.refreshToken(token.replace("Bearer ", ""));
                    ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
                    passedUser.setRefreshed_token(refreshedToken);
                }
                else{
                    System.out.println("Token cannot be refreshed");
                }
            }
        }
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        return jwtUser.getUser();
    }

    public void updateUser(UserDTO passedUser){
    try {
        User userTemp = getCurrentUser();
        Date date = new Date();
        userTemp.setPhoneNo(passedUser.getPhoneNo());
        userTemp.setLastName(passedUser.getLastName());
        userTemp.setFirstName(passedUser.getFirstName());
        userTemp.setGender(passedUser.getGender());

        if(passedUser.getNewPassword() != null && !passedUser.getNewPassword().equalsIgnoreCase("")) {
            if(Hash.checkPassword(passedUser.getOldPassword(),userTemp.getPassword())) {
                userTemp.setPassword(Hash.createPassword(passedUser.getNewPassword()));
            }
            else {
                throw new PasswordException("password mismatch");
            }
        }

        userTemp.setUpdatedOn(date);
        userRepository.save(userTemp);

    } catch (Exception e) {
        e.printStackTrace();
        throw new WawoohException();
    }

}

    public LogInResp updatePassword(UserDTO passedUser,Device device){
        try {
            Date date = new Date();
            LogInResp logInResp = new LogInResp();
            User userTemp = userRepository.findByEmail(passedUser.getEmail());
            if(passedUser.getNewPassword() != null && !passedUser.getNewPassword().equalsIgnoreCase("")) {
                if(Hash.checkPassword(passedUser.getOldPassword(),userTemp.getPassword())) {
                    userTemp.setPassword(Hash.createPassword(passedUser.getNewPassword()));
                    userTemp.setLinkClicked("Y");

                    final UserDetails userDetails = userDetailsService.loadUserByUsername(passedUser.getEmail());
                    final String token = jwtTokenUtil.generateToken(userDetails, device);
                    //implement sessionid

                    logInResp.setToken(token);

                }
                else if (userTemp.getLinkClicked().equalsIgnoreCase( "Y")){

                    throw new PasswordException("Link expired");
                }
                else {
                    throw new PasswordException("password mismatch");
                }
            }

            userTemp.setUpdatedOn(date);
            userRepository.save(userTemp);
            return logInResp;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }

    public Response activateAccount(UserDTO passedUser){
        Map<String,Object> responseMap = new HashMap();
        try {
            Date date = new Date();
            User userTemp = userRepository.findByEmail(passedUser.getEmail());
            if(!userTemp.getEmailVerificationFlag().equalsIgnoreCase("Y")) {
                userTemp.setUpdatedOn(date);
                userTemp.setEmailVerificationFlag("Y");
                userRepository.save(userTemp);
                return new Response("00","Thank you for verifying your email",responseMap);
            }
            else {
                return new Response("00","email already verified",responseMap);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }



    private String getCurrentTime(){
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int day = now.get(Calendar.DAY_OF_MONTH);
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int second = now.get(Calendar.SECOND);
        int millis = now.get(Calendar.MILLISECOND);
        String cTime = year+""+month+""+day+""+hour+""+minute+""+second+""+millis;
        return cTime;
    }
}
