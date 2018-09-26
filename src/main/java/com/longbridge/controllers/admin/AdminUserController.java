package com.longbridge.controllers.admin;

import com.longbridge.Util.UserUtil;
import com.longbridge.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Longbridge on 26/09/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/admin")
public class AdminUserController {

    @Autowired
    UserUtil userUtil;

    @PostMapping(value = "/signin")
    public Object Signin(@RequestBody User passedUser, Device device){
        return userUtil.validateUser(passedUser,device);
    }

    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
