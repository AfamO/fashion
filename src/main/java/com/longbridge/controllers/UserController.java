package com.longbridge.controllers;
import com.longbridge.Util.UserUtil;
import com.longbridge.dto.UserDTO;
import com.longbridge.dto.UserEmailTokenDTO;
import com.longbridge.exception.AppException;
import com.longbridge.models.MailError;
import com.longbridge.models.Response;
import com.longbridge.models.Token;
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
import java.util.List;

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
        return userUtil.registerUser(passedUser);
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
        Response response = new Response("00", "Operation Successful", "success");
        return response;
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
    public Object forgotPassword(@RequestBody User user){
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
        System.out.println(user);
            return userUtil.forgotPassword(user);

        //======================================================

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

    @PostMapping(value = "/validateToken")
    public Object validateToken(@RequestBody UserEmailTokenDTO userEmailTokenDTO){
        User user = userUtil.getUserByEmail(userEmailTokenDTO.getEmail());
        return tokenService.validateToken(user,userEmailTokenDTO.getToken());
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
