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
@RequestMapping("/fashion/secure")
public class SecureUserController {

    UserUtil userUtil;


    @PostMapping(value = "/edituser")
    public Response updateUser(@RequestBody UserDTO passedUser){
        //======================================================

        userUtil.updateUser(passedUser);
        return new Response("00", "Operation Successful", "success");
    }


    @PostMapping(value = "/validatepassword")
    public Object validatePassword(@RequestBody User user){
        return userUtil.validatePassword(user);
    }



    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
