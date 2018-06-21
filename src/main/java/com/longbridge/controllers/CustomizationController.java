package com.longbridge.controllers;

import com.longbridge.Util.UserUtil;
import com.longbridge.models.Measurement;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.services.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Longbridge on 12/03/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/customization")
public class CustomizationController {

    @Autowired
    UserUtil userUtil;

    @Autowired
    MeasurementService measurementService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @PostMapping(value = "/add")
    public Response customize(@RequestBody Measurement measurement, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);

        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        measurementService.customize(userTemp, measurement);
        Response response = new Response("00","Operation Successful","success");
        return response;
    }


    @PostMapping(value = "/update")
    public Response updateCustomize(@RequestBody Measurement measurement, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);

        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        measurementService.updateCustomization(userTemp, measurement);
        Response response = new Response("00","Operation Successful","success");
        return response;
    }


    @GetMapping(value = "/{id}/get")
    public Response getCustomization(@PathVariable Long id, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);

        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        Response response = new Response("00","Operation Successful",measurementService.getMeasurementById(userTemp, id));
        return response;
    }

    @GetMapping(value = "/{id}/delete")
    public Response deleteCustomization(@PathVariable Long id, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);

        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        Response response = null;
        if(measurementService.deleteMeasurement(userTemp, id)){
            response = new Response("00","Operation Successful","success");
        }else{
            response = new Response("99","Measurement is in use","failure");
        }
        return response;
    }



    @GetMapping(value = "/getusermeasurements")
    public Response getUserCustomization(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);

        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        //measurementService.getUserMeasurement(userTemp);
        Response response = new Response("00","Operation Successful",measurementService.getUserMeasurement(userTemp));
        return response;
    }

    @GetMapping(value = "/{productId}/getmandatorymeasurements")
    public Response getMandatoryMeasurements(@PathVariable Long productId, HttpServletRequest request){
//        String token = request.getHeader(tokenHeader);
//        User userTemp = userUtil.fetchUserDetails2(token);
//
//        if(token==null || userTemp==null){
//            return userUtil.tokenNullOrInvalidResponse(token);
//        }
        Response response = new Response("00","Operation Successful",measurementService.getMandatoryMeasurement(productId));
        return response;
    }


//    @GetMapping(value = "/{name}/getcustomizationbyname")
//    public Response getCustomizationByName(@PathVariable Long measurementId, HttpServletRequest request){
//        String token = request.getHeader(tokenHeader);
//        User userTemp = userUtil.fetchUserDetails2(token);
//
//        if(token==null || userTemp==null){
//            return userUtil.tokenNullOrInvalidResponse(token);
//        }
//        measurementService.getMeasurementById(userTemp, measurementId);
//        Response response = new Response("00","Operation Successful","success");
//        return response;
//    }
    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
