package com.longbridge.controllers;
import com.longbridge.Util.UserUtil;
import com.longbridge.dto.UserDTO;
import com.longbridge.dto.UserEmailTokenDTO;
import com.longbridge.exception.AppException;
import com.longbridge.models.MailError;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.repository.MailErrorRepository;
import com.longbridge.security.JwtUser;
import com.longbridge.security.repository.UserRepository;
import com.longbridge.services.TokenService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by longbridge on 11/4/17.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion")
public class UserController {
    Logger logger = Logger.getLogger(UserController.class);

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserUtil userUtil;

    @Autowired
    MailErrorRepository mailErrorRepository;

    @Autowired
    TokenService tokenService;

    @PostMapping(value = "/Signin")
    public Object Signin(@RequestBody User passedUser,Device device){
        return userUtil.validateUser(passedUser,device);
    }

    @PostMapping(value = "/Register")
    public Object Register(@RequestBody User passedUser){
        try {
            return userUtil.registerUser(passedUser);
        }catch (AppException e){
            e.printStackTrace();
            String recipient = e.getRecipient();
            String subject = e.getSubject();
            MailError mailError = new MailError();
            mailError.setName(e.getName());
            mailError.setRecipient(recipient);
            mailError.setSubject(subject);
            mailError.setLink(e.getLink());
            mailError.setMailType("welcome");
            mailErrorRepository.save(mailError);
            return new Response("00", "Registration successful, Trying to send welcome email", "success");
        }
    }


//    @PostMapping(value = "/requestlink")
//    public Object requestLink(@RequestBody User passedUser){
//        try {
//            return userUtil.getActivationLink(passedUser);
//        }catch (AppException e){
//            e.printStackTrace();
//            String recipient = e.getRecipient();
//            String subject = e.getSubject();
//            MailError mailError = new MailError();
//            mailError.setName(e.getName());
//            mailError.setRecipient(recipient);
//            mailError.setSubject(subject);
//            mailError.setLink(e.getLink());
//            mailError.setMailType("welcome");
//            mailErrorRepository.save(mailError);
//            return new Response("00", "Registration successful, Trying to send welcome email", "success");
//        }
//    }



    @PostMapping(value = "/createadmin")
    public Object createAdmin(@RequestBody User passedUser){
        try {
            if (passedUser.role.equalsIgnoreCase("admin")){
//todo later
            }
            else {
//todo later
            }

            return userUtil.registerUser(passedUser);
        }catch (AppException e){
            e.printStackTrace();
            String recipient = e.getRecipient();
            String subject = e.getSubject();
            MailError mailError = new MailError();
            mailError.setName(e.getName());
            mailError.setRecipient(recipient);
            mailError.setSubject(subject);
            mailError.setMailType("welcome");
            mailErrorRepository.save(mailError);
            return new Response("00", "Registration successful, Trying to send email to admin", "success");
        }
    }



    @PostMapping(value = "/edituser")
    public Response updateUser(@RequestBody UserDTO passedUser, HttpServletRequest request){
        //======================================================
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        userUtil.updateUser(passedUser,userTemp);
        return new Response("00", "Operation Successful", "success");
    }

    @PostMapping(value = "/editpassword")
    public Response updateUser(@RequestBody UserDTO passedUser, Device device){
        //======================================================


        return new Response("00", "Operation Successful", userUtil.updatePassword(passedUser,device));
    }


    @PostMapping(value = "/activateaccount")
    public Response activateAccount(@RequestBody UserDTO passedUser){
        //======================================================


        return new Response("00", "Operation Successful", userUtil.activateAccount(passedUser));
    }

    @GetMapping(value = "/getuserdetails")
    public Object fetchUserDetails(HttpServletRequest request){
        /*
        This is needed on any Endpoint that requires authorization.
         Any method you want to implement this should
        have the HttpServletRequest as param.
         */
        //======================================================
        String token = request.getHeader(tokenHeader);
        JwtUser user = userUtil.getAuthenticationDetails(token);
        if(token==null || user.getUsername()==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        //======================================================
        return userUtil.fetchUserDetails(user.getUsername(),token);
    }


    @PostMapping(value = "/forgotpassword")
    public Object forgotPassword(@RequestBody UserDTO user) {
        /*
        This is needed on any Endpoint that requires authorization.
         Any method you want to implement this should
        have the HttpServletRequest as param.
         */
        //======================================================

        //String token = request.getHeader(tokenHeader);
        // User user = userUtil.fetchUserDetails2(token);
        //if (token == null || user == null) {
        // return userUtil.tokenNullOrInvalidResponse(token);
        // }
        Map<String, Object> responseMap = new HashMap();
        try {
            return userUtil.forgotPassword(user);
        } catch (AppException e) {
            e.printStackTrace();
            String recipient = e.getRecipient();
            String subject = e.getSubject();

            MailError mailError = new MailError();
            mailError.setNewPassword(e.getNewPassword());
            mailError.setName(e.getName());
            mailError.setRecipient(recipient);
            mailError.setSubject(subject);
            mailError.setLink(e.getLink());
            mailError.setMailType("userpassword");
            mailErrorRepository.save(mailError);
            return new Response("00", "Operation Successful, Trying to send password to email", responseMap);
            //======================================================

        }
    }

    @PostMapping(value = "/validatepassword")
    public Object validatePassword(HttpServletRequest request, @RequestBody User user){


        String token = request.getHeader(tokenHeader);
        if (token == null || user == null) {
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return userUtil.validatePassword(user);


    }


    @GetMapping(value = "/getusers")
    public List<User> getUsers(HttpServletRequest request){

        return userUtil.getUsers();
    }

    @GetMapping(value = "/getallusers")
    public List<User> getAllUsers(HttpServletRequest request){

        return userUtil.getAllUsers();
    }

    @PostMapping(value = "/validatetoken")
    public Object validateToken(@RequestBody UserEmailTokenDTO userEmailTokenDTO,Device device){

        return tokenService.validateToken(userEmailTokenDTO, device);
    }

    @PostMapping(value = "/resendtoken")
    public Object validateToken(@RequestBody User user){
        try {
            userUtil.sendToken(user.email);
            return new Response("00", "Operation Successful", "success");

        }catch (Exception e){
            e.printStackTrace();
            return new Response("99", "Error occurred while sending token", "error");
        }
    }



//
//    @PostMapping(value = "/getUserDetailsByToken")
//    public User fetchUserDetails2(HttpServletRequest request){
//        String token = request.getHeader(tokenHeader);
//        JwtUser user = userUtil.getAuthenticationDetails(token);
//        if(user!=null){
//            return userRepository.findByEmail(user.getUsername());
//        }
//        else{
//            return null;
//        }
//    }


    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
