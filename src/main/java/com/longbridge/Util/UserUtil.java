package com.longbridge.Util;
import com.longbridge.dto.CloudinaryResponse;
import com.longbridge.dto.UserDTO;
import com.longbridge.exception.AppException;
import com.longbridge.exception.PasswordException;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.DesignerRepository;
import com.longbridge.repository.MailErrorRepository;
import com.longbridge.repository.TokenRepository;
import com.longbridge.respbodydto.LogInResp;
import com.longbridge.security.JwtTokenUtil;
import com.longbridge.security.JwtUser;
import com.longbridge.security.repository.UserRepository;
import com.longbridge.security.service.JwtAuthenticationResponse;
import com.longbridge.services.MailService;
import org.apache.commons.lang3.RandomStringUtils;
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

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
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
    MailErrorRepository mailErrorRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    MailService mailService;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    MessageSource messageSource;

    @Autowired
    GeneralUtil generalUtil;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    private Locale locale = LocaleContextHolder.getLocale();

    @Value("${s.designer.logo.folder}")
    private String designerLogoFolder;


    public Response registerUser(User passedUser){
        Map<String,Object> responseMap = new HashMap();
        try {

            Date date = new Date();
            //Address address = Address.createAddress(passedUser,passedUser.address,"Y");
            //List<Address> addresses = new ArrayList<>();
            User user = userRepository.findByEmail(passedUser.email);
            if(user==null){
                //passedUser.addresses = addresses;
                //passedUser.addresses.add(address);
                passedUser.password = Hash.createPassword(passedUser.password);

                System.out.println("this is"+passedUser.designer);
                if(passedUser.designer!=null){
                    passedUser.designer.setCreatedOn(date);
                    passedUser.designer.setUpdatedOn(date);
                    //passedUser.designer.userId = passedUser.id;
                    passedUser.designer.user=passedUser;
                    if(passedUser.designer.logo != null) {
                        try {
                            String fileName = passedUser.email.substring(0, 3) + getCurrentTime();
                            String base64Img = passedUser.designer.logo;
                            CloudinaryResponse c = generalUtil.uploadToCloud(base64Img,fileName,"designerlogos");
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
                String name = passedUser.firstName + " " + passedUser.lastName;
                String mail = passedUser.email;
                String encryptedMail = Base64.getEncoder().encodeToString(mail.getBytes());
                String message="";
                String activationLink="";
                try {
                    Context context = new Context();
                    context.setVariable("name", name);
                    activationLink = messageSource.getMessage("activation.url.link",null,locale)+encryptedMail;
                    System.out.println(activationLink);
                    context.setVariable("link", activationLink);
                    if(passedUser.designer != null) {
                        message = templateEngine.process("designerwelcomeemail", context);
                    }
                    else {
                        message = templateEngine.process("welcomeemail", context);
                    }
                    mailService.prepareAndSend(message,mail,messageSource.getMessage("user.welcome.subject", null, locale));

                }catch (MailException me){
                    me.printStackTrace();
                    throw new AppException("",user.firstName + user.lastName,user.email,messageSource.getMessage("user.welcome.subject", null, locale),activationLink);

                }
                Response response = new Response("00","Registration successful",responseMap);
                return response;
            }else{
                Response response = new Response("99","Email already exists",responseMap);
                return response;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Response response = new Response("99","Error occured internally",responseMap);
            return response;

        }

    }


    public Response createAdmin(User passedUser){
        Map<String,Object> responseMap = new HashMap();
        try {
            Date date = new Date();
            User user = userRepository.findByEmail(passedUser.email);
            if(user==null){
                passedUser.password = Hash.createPassword(passedUser.password);
                user.createdOn=date;
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
                    throw new AppException(user.firstName + user.lastName,user.email,messageSource.getMessage("user.welcome.subject", null, locale));

                }
                Response response = new Response("00","Registration successful",responseMap);
                return response;
            }else{
                Response response = new Response("99","Email already exists",responseMap);
                return response;
            }

        } catch (Exception e) {
            e.printStackTrace();
            Response response = new Response("99","Error occured internally",responseMap);
            return response;

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
                Response response = new Response("00","Operation Successful, Password successfully sent to email",responseMap);
                return response;
            }else{
                Response response = new Response("99","Email does not exist",responseMap);
                return response;
            }
        }catch (MailException me) {
            me.printStackTrace();
            throw new AppException(newPassword,name,mail,messageSource.getMessage("password.reset.subject", null, locale),changePasswordLink);

        }
        catch (Exception e) {
            e.printStackTrace();
            Response response = new Response("99", "Error occured internally", responseMap);
            return response;
        }
    }



    public Object validatePassword(User passedUser){
        Map<String,Object> responseMap = new HashMap();

        try {

            User user = userRepository.findByEmail(passedUser.email);
            if (user != null) {

                if (passedUser.password == user.password) {
                    Response response = new Response("00", "Operation Successful", responseMap);
                    return response;
                } else {
                    Response response = new Response("99", "Error occurred", responseMap);
                    return response;
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
            Response response = new Response("99", "Error occured internally", responseMap);
            return response;
        }
        Response response = new Response("99", "Error occurred", responseMap);
        return response;
    }



    public Response validateUser(User passedUser, Device device){
       // Map<String,Object> responseMap = new HashMap();
        System.out.println("i got hr");
        LogInResp logInResp=new LogInResp();
        try {
            User user = userRepository.findByEmail(passedUser.email);
            boolean valid = false;
            if(user!=null){
                try{
                   // check if(user.socialFlag) is Y and set valid as true
                    if(user.socialFlag.equalsIgnoreCase("Y")){
                        valid=true;
                    }
                    else {
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
                Response response = new Response("00","Login successful",logInResp);
                return response;
            }else{
                Response response = new Response("99","Invalid username/password",logInResp);
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Response response = new Response("99","Error occurred internally",logInResp);
        return response;
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
                /*
                todo : refreshing token needs to be discussed if necessary
                 */
//                refreshAuthenticationDetails(user,token);
                responseMap.put("userDetails",user);
                Response response = new Response("00","User found",responseMap);
                return response;
            }else{
                Response response = new Response("99","User not found",responseMap);
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Response response = new Response("99","Error occurred internally",responseMap);
        return response;
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
        Response response = new Response("56","Invalid token passed",responseMap);
        return response;
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

        if(passedUser.getNewPassword() != null && passedUser.getNewPassword() != "") {
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
            if(passedUser.getNewPassword() != null && passedUser.getNewPassword() != "") {
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
                Response response = new Response("00","Thank you for verifying your account",responseMap);
                return response;
            }
            else {
                Response response = new Response("00","Account already activated",responseMap);
                return response;
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
