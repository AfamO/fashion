package com.longbridge.Util;
import com.longbridge.dto.CloudinaryResponse;
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
import com.longbridge.services.CloudinaryService;
import com.longbridge.services.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
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
    CloudinaryService cloudinaryService;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    MessageSource messageSource;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    SendEmailAsync sendEmailAsync;

    @Autowired
    private UserDetailsService userDetailsService;

    private Locale locale = LocaleContextHolder.getLocale();

    @Value("${s.designer.logo.folder}")
    private String designerLogoFolder;


    public Response registerUser(User passedUser){
        Map<String,Object> responseMap = new HashMap();
        try {
            Date date = new Date();
            User user = userRepository.findByEmail(passedUser.email);
            User user1 = userRepository.findByPhoneNo(passedUser.phoneNo);
            List<String> errors = new ArrayList<String>();


            if(user != null){
                errors.add("Email already exists");
            }
            if(user1 != null){
                errors.add("Phone number already exist");
            }

            if(errors.size() > 0){
                return new Response("99","an error occurred",errors);
            }

            passedUser.password = Hash.createPassword(passedUser.password);
            if(passedUser.designer!=null){
                passedUser.designer.setCreatedOn(date);
                passedUser.designer.setUpdatedOn(date);
                passedUser.designer.user=passedUser;
                if(passedUser.designer.logo != null) {
                    try {
                        String fileName = passedUser.email.substring(0, 3) + getCurrentTime();
                        String base64Img = passedUser.designer.logo;
                        CloudinaryResponse c = cloudinaryService.uploadToCloud(base64Img,fileName,"designerlogos");
                        passedUser.designer.logo = c.getUrl();
                        passedUser.designer.publicId=c.getPublicId();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Response response = new Response("99","Error occurred internally",responseMap);
                        return response;
                    }
                }
                designerRepository.save(passedUser.designer);
            }
            userRepository.save(passedUser);
            sendToken(passedUser.email);
            sendEmailAsync.sendWelcomeEmailToUser(passedUser);

            return new Response("00","Registration successful",responseMap);

        } catch (Exception e) {
            e.printStackTrace();
            return new Response("99","Error occured internally",responseMap);

        }

    }

    public void sendToken(String email){
        try {
            User passedUser = userRepository.findByEmail(email);
            String name = passedUser.designer.storeName;
            char[] token = uniqueNumberUtil.OTP(5);
            List<String> phonenumbers = new ArrayList<>();
            phonenumbers.add(passedUser.phoneNo);
            String message = String.format(messageSource.getMessage("user.sendtoken.message", null, locale), name, String.valueOf(token));
            smsAlertUtil.sms(phonenumbers, message);
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
            tokenRepository.save(token);
        }
        else {
            token= new Token();
            token.setToken(tokenString);
            token.setUser(user);
            tokenRepository.save(token);
        }
    }


    public Response getActivationLink(User passedUser){
        Map<String,Object> responseMap = new HashMap();
        try {

            User user = userRepository.findByEmail(passedUser.email);
            if(user!=null){
                String name = user.firstName + " " + user.lastName;
                String mail = user.email;
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
                    throw new AppException("",passedUser.firstName + passedUser.lastName,passedUser.email,messageSource.getMessage("user.welcome.subject", null, locale),activationLink);

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
            User user = userRepository.findByEmail(passedUser.email);
            if(user==null){
                passedUser.password = Hash.createPassword(passedUser.password);
                passedUser.createdOn=date;
                userRepository.save(passedUser);
                String name = passedUser.firstName + " " + passedUser.lastName;
                String mail = passedUser.email;
                String message="";
                try {
                    Context context = new Context();
                    context.setVariable("name", name);
                    if(passedUser.role.equalsIgnoreCase("admin")) {
                        message = templateEngine.process("adminwelcomeemail", context);
                    }
                    mailService.prepareAndSend(message,mail,messageSource.getMessage("user.welcome.subject", null, locale));
                }catch (MailException me){
                    me.printStackTrace();
                    throw new AppException(passedUser.firstName + passedUser.lastName,passedUser.email,messageSource.getMessage("user.welcome.subject", null, locale));

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
                String message = String.format(messageSource.getMessage("user.retrieveemail", null, locale), user.email);
                List<String> phoneNumbers = new ArrayList<>();
                phoneNumbers.add(user.phoneNo);
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
            if(!user.activationFlag.equalsIgnoreCase("Y")){
                return new Response("57","Account not verified, Kindly click the link sent to your email to verify your account",responseMap);
            }

            if(user!=null){
                //newPassword = generalUtil.getCurrentTime();
                //newPassword = RandomStringUtils.randomAlphanumeric(10);
                newPassword=UUID.randomUUID().toString().substring(0,10);
                user.password = Hash.createPassword(newPassword);
                user.linkClicked = "N";

                name = user.firstName +" " + user.lastName;
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

            User user = userRepository.findByEmail(passedUser.email);
            if (user != null) {

                if (passedUser.password .equalsIgnoreCase(user.password)) {
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

            User user = userRepository.findByEmail(passedUser.email);
            if(!user.activationFlag.equalsIgnoreCase("Y")){
                return new Response("57","Account not verified",logInResp);
            }
            boolean valid = false;
            if(user!=null){
                try{
                   // check if(user.socialFlag) is Y and set valid as true
                    if(user.socialFlag != null){
                        if(user.socialFlag.equalsIgnoreCase("Y")){
                            valid=true;
                        }
                    }

                    if (!valid) {
                        //If N, validate password
                        valid = Hash.checkPassword(passedUser.password, user.password);
                    }
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
            if(user!=null && valid){
                if(user.designer != null){
                    logInResp.setRole(2);
                }
                else if(user.role.equalsIgnoreCase("admin")){
                    logInResp.setRole(3);
                }
                else if(user.role.equalsIgnoreCase("superadmin")){
                    logInResp.setRole(4);
                }
                else if(user.role.equalsIgnoreCase("qa")){
                    logInResp.setRole(5);
                }
                else {
                    logInResp.setRole(1);
                }
                /*
                    Generating Token for user and this will be required for all request.
                 */
                final UserDetails userDetails = userDetailsService.loadUserByUsername(passedUser.email);
                System.out.println("userdetails is "+userDetails.toString());
                final String token = jwtTokenUtil.generateToken(userDetails, device);
                System.out.println("Token is "+token);
                //implement sessionid

                logInResp.setToken(token);
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
            return userRepository.findByDesignerIsNull();
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Object fetchUserDetails(String email, String token){
        Map<String,Object> responseMap = new HashMap();
        try {
            User user = userRepository.findByEmail(email);
            if(user!=null){
                if(user.addresses!=null) {
                    user.addresses.forEach(address ->
                    {
                        if (address.user != null) {
                            address.user = null;
                        }
                    });
                }

                user.designer=null;
                /*
                todo : refreshing token needs to be discussed if necessary
                 */
//                refreshAuthenticationDetails(user,token);

                Wallet wallet = walletRepository.findByUser(user);
                if(wallet==null){
                    user.wallet= new Wallet();
                }else {
                    user.wallet=wallet;
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
            passedUser.passed_token = token.replace("Bearer ","");
            String username = jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ", ""));
            user = (JwtUser) userDetailsService.loadUserByUsername(username);
            if (user!=null) {
                if (jwtTokenUtil.canTokenBeRefreshed(token.replace("Bearer ",""), user.getLastPasswordResetDate())) {
                    String refreshedToken = jwtTokenUtil.refreshToken(token.replace("Bearer ", ""));
                    ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
                    passedUser.refreshed_token = refreshedToken;
                }
                else{
                    System.out.println("Token cannot be refreshed");
                }
            }
        }
    }

    public void updateUser(UserDTO passedUser, User userTemp){
    try {
        Date date = new Date();
        userTemp.phoneNo=passedUser.getPhoneNo();
        userTemp.lastName = passedUser.getLastName();
        userTemp.firstName=passedUser.getFirstName();

        if(passedUser.getNewPassword() != null && !passedUser.getNewPassword().equalsIgnoreCase("")) {
            if(Hash.checkPassword(passedUser.getOldPassword(),userTemp.password)) {
                userTemp.password = Hash.createPassword(passedUser.getNewPassword());
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
                if(Hash.checkPassword(passedUser.getOldPassword(),userTemp.password)) {
                    userTemp.password = Hash.createPassword(passedUser.getNewPassword());
                    userTemp.linkClicked="Y";

                    final UserDetails userDetails = userDetailsService.loadUserByUsername(passedUser.getEmail());
                    System.out.println("userdetails is "+userDetails.toString());
                    final String token = jwtTokenUtil.generateToken(userDetails, device);
                    System.out.println("Token is "+token);
                    //implement sessionid

                    logInResp.setToken(token);

                }
                else if (userTemp.linkClicked.equalsIgnoreCase( "Y")){

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
            if(!userTemp.activationFlag.equalsIgnoreCase("Y")) {
                userTemp.activationDate=date;
                userTemp.setUpdatedOn(date);
                userTemp.activationFlag="Y";
                userRepository.save(userTemp);
                return new Response("00","Thank you for verifying your account",responseMap);
            }
            else {
                return new Response("00","Account already activated",responseMap);
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
