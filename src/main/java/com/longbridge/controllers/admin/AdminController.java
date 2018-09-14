package com.longbridge.controllers.admin;

import com.longbridge.Util.UserUtil;
import com.longbridge.models.Code;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.services.AdminService;
import com.longbridge.services.CodeService;
import com.longbridge.services.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Longbridge on 08/08/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/admin")
public class AdminController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    UserUtil userUtil;

    @Autowired
    AdminService adminService;

    @Autowired
    RefundService refundService;

    @Autowired
    CodeService codeService;

    @GetMapping(value = "/getdashboarddata")
    public Response getAdminDashboardData(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",adminService.getDashboardData(userTemp));

    }


    @GetMapping(value = "/getrefundinfo")
    public Response getRefundInfo(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        return new Response("00","Operation Successful",refundService.getAll());

    }


    @PostMapping(value = "/{id}/verifyrefund")
    public Response verifyRefund(HttpServletRequest request,@PathVariable Long id){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        refundService.verifyRefund(id);
        return new Response("00","Operation Successful","success");

    }

    @PostMapping(value = "/createcode")
    public Response createCode(@RequestBody Code code, HttpServletRequest request){

        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        return codeService.createCode(code);
    }

    @PostMapping(value = "/updatecode")
    public Response updateCode(@RequestBody Code code, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        return codeService.updateCode(code);
    }

    @GetMapping(value = "/findcodebytype/{type}")
    public Response findCodeByType(@PathVariable String type, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        return new Response("00", "Operation successful", codeService.findCodeByType(type));
    }

    @GetMapping(value = "/findcodebynameandtype/{name}/{type}")
    public Response findCodeByNameAndType(@PathVariable String name, @PathVariable String type, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        return new Response("00", "Operation successful", codeService.findCodeByNameAndType(name, type));
    }


    @GetMapping(value = "/findcodebyid/{id}")
    public Response findCodeById(@PathVariable Long id, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        return new Response("00", "Operation successful", codeService.findCodeById(id));
    }

    @GetMapping(value = "/deletecode/{id}")
    public Response findCoedByid(@PathVariable Long id, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        codeService.deleteCode(id);
        return new Response("00", "Operation successful", null);
    }

}
