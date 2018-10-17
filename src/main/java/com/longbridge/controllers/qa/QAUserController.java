package com.longbridge.controllers.qa;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.DesignerDTO;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.services.DesignerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/secure/qa")
public class QAUserController {

    @Autowired
    UserUtil userUtil;

    @Autowired
    DesignerService designerService;

    @GetMapping(value = "/getallusers")
    public List<User> getAllUsers(){
        return userUtil.getAllUsers();
    }

    @GetMapping(value = "/getalldesigners")
    public Response getDesigners(){
        List<DesignerDTO> designerList= designerService.getDesigners();
        return new Response("00","Operation Successful",designerList);
    }
}
