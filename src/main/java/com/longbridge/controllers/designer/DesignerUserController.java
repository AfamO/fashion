package com.longbridge.controllers.designer;

import com.longbridge.Util.UserUtil;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Longbridge on 26/09/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/designer")
public class DesignerUserController {

    @Autowired
    UserUtil userUtil;

    @PostMapping(value = "/signin")
    private Response designerLogin(User user, Device device){
        return userUtil.validateUser(user,device);
    }

}
