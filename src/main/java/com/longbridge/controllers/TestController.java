package com.longbridge.controllers;

import com.longbridge.dto.DesignerDTO;
import com.longbridge.models.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Longbridge on 19/09/2018.
 */

@RestController
@RequestMapping("/fashion/secure")
public class TestController {

    @GetMapping(value = "/designer")
    public Response getTest1(){

        return new Response("00","Operation Successful","can call designer");
    }


    @GetMapping(value = "/qa")
    public Response getDesigner2(){

        return new Response("00","Operation Successful","can call qa");
    }

    @GetMapping(value = "/admin")
    public Response getDesigner3(){
        return new Response("00","Operation Successful","can call admin");
    }

    @GetMapping(value = "/get4")
    public Response getDesigner4(){
        return new Response("00","Operation Successful","usersss");
    }

}
