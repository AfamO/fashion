package com.longbridge.controllers.enduser;
import com.longbridge.Util.UserUtil;
import com.longbridge.dto.UserDTO;
import com.longbridge.dto.UserEmailTokenDTO;
import com.longbridge.exception.AppException;
import com.longbridge.models.MailError;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.repository.MailErrorRepository;
import com.longbridge.security.JwtUser;
import com.longbridge.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion")
public class UserController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    UserUtil userUtil;

    @Autowired
    MailErrorRepository mailErrorRepository;

    @Autowired
    TokenService tokenService;

    @PostMapping(value = "/signin")
    public Object Signin(@RequestBody User passedUser,Device device){
        return userUtil.validateUser(passedUser,device);
    }

    @PostMapping(value = "/register")
    public Object Register(@RequestBody UserDTO passedUser){
        try {
            return userUtil.registerUser(passedUser);
        }catch (AppException e){
            e.printStackTrace();
            String recipient = e.getRecipient();
            String subject = e.getSubject();
            MailError mailError = new MailError();
            mailError.setName(e.getName());
            mailError.setRecipient(recipient);
            mailError.setLink(e.getLink());
            mailError.setSubject(subject);
            mailError.setMailType("welcome");
            mailErrorRepository.save(mailError);
            return new Response("00", "Registration successful, Trying to send welcome email", "success");
        }
    }

    @PostMapping(value = "/validatemail")
    public Object validateEmail(@RequestBody UserDTO userDTO){
        try {
            return userUtil.checkEmail(userDTO);
        }catch (AppException e){
            e.printStackTrace();
            return new Response("96", "Error occurred validating Email", null);
        }
    }


//this code handles it better.. for cases when d email fails
    @PostMapping(value = "/resendemailverification")
    public Object requestLink(@RequestBody User passedUser){
        try {
            return userUtil.getActivationLink(passedUser);
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



    @PostMapping(value = "/validatetoken")
    public Object validateToken(@RequestBody UserEmailTokenDTO userEmailTokenDTO,Device device){

        return tokenService.validateToken(userEmailTokenDTO, device);
    }

    @PostMapping(value = "/resendtoken")
    public Object validateToken(@RequestBody User user){
        try {
            userUtil.sendToken(user.getEmail());
            return new Response("00", "Operation Successful", "success");

        }catch (Exception e){
            e.printStackTrace();
            return new Response("99", "Error occurred while sending token", "error");
        }
    }


    //rewrite this code and don't do any checking in ur controller.. move it to your service. thanks
    @PostMapping(value = "/forgotemail")
    public Object forgotEmail(@RequestBody User user){

        if(user.getPhoneNo() != null){
            userUtil.forgotEmail(user.getPhoneNo());
            return new Response("00", "Email address has been sent to "+user.getPhoneNo(), null);
        }else{
            return new Response("99", "No phone number found", null);
        }
    }


    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
