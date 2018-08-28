package com.longbridge.controllers.admin;

import com.longbridge.Util.UserUtil;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.services.AdminService;
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



}
