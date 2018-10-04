package com.longbridge.controllers.admin;

import com.longbridge.Util.UserUtil;
import com.longbridge.models.Code;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.services.AdminService;
import com.longbridge.services.CodeService;
import com.longbridge.services.DesignerService;
import com.longbridge.services.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Longbridge on 08/08/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/secure/admin")
public class AdminController {


    @Autowired
    AdminService adminService;

    @Autowired
    RefundService refundService;

    @Autowired
    CodeService codeService;

    @Autowired
    UserUtil userUtil;

    @Autowired
    DesignerService designerService;


    @GetMapping(value = "/getusers")
    public List<User> getUsers(){

        return userUtil.getUsers();
    }

    @GetMapping(value = "/getallusers")
    public List<User> getAllUsers(){
        return userUtil.getAllUsers();
    }

    @GetMapping(value = "/getdashboarddata")
    public Response getAdminDashboardData(){
        return new Response("00","Operation Successful",adminService.getDashboardData());
    }


    @GetMapping(value = "/getrefundinfo")
    public Response getRefundInfo(){
        return new Response("00","Operation Successful",refundService.getAll());

    }


    @PostMapping(value = "/{id}/verifyrefund")
    public Response verifyRefund(@PathVariable Long id){
        refundService.verifyRefund(id);
        return new Response("00","Operation Successful","success");
    }

    @PostMapping(value = "/createcode")
    public Response createCode(@RequestBody Code code){
        return codeService.createCode(code);
    }

    @PostMapping(value = "/updatecode")
    public Response updateCode(@RequestBody Code code){
        return codeService.updateCode(code);
    }

    @GetMapping(value = "/findcodebytype/{type}")
    public Response findCodeByType(@PathVariable String type){
        return new Response("00", "Operation successful", codeService.findCodeByType(type));
    }

    @GetMapping(value = "/findcodebynameandtype/{name}/{type}")
    public Response findCodeByNameAndType(@PathVariable String name, @PathVariable String type){
        return new Response("00", "Operation successful", codeService.findCodeByNameAndType(name, type));
    }


    @GetMapping(value = "/findcodebyid/{id}")
    public Response findCodeById(@PathVariable Long id){
        return new Response("00", "Operation successful", codeService.findCodeById(id));
    }

    @GetMapping(value = "/deletecode/{id}")
    public Response findCoedByid(@PathVariable Long id){
        codeService.deleteCode(id);
        return new Response("00", "Operation successful", null);
    }


    @GetMapping(value = "/{designerId}/{status}/update")
    public Object updateDesignerStatus(@PathVariable Long designerId, @PathVariable String status){
        designerService.updateDesignerStatus(designerId,status);
        return new Response("00","Operation Successful","success");

    }



    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }

}
