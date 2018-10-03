package com.longbridge.controllers.designer;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.*;
import com.longbridge.models.Designer;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.services.DesignerService;
import com.longbridge.services.OrderService;
import com.longbridge.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Longbridge on 15/11/2017.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/designer")
public class DesignerController {


    @Autowired
    DesignerService designerService;


    @GetMapping(value = "/getdesigners")
    public Response getDesigners(){
        List<DesignerDTO> designerList= designerService.getDesigners();
        return new Response("00","Operation Successful",designerList);
    }


    @GetMapping(value = "/getdesignerbystorename/{storename}")
    public Response getDesignerByStoreName(@PathVariable String storename){
        DesignerDTO designer = designerService.getDesignerByStoreName(storename);
        return new Response("00","Operation Successful",designer);
    }


    @GetMapping(value = "/{id}/getdesignerbyid")
    public Response getDesignerById(@PathVariable Long id){
        DesignerDTO designer = designerService.getDesignerById(id);
        return new Response("00","Operation Successful",designer);
    }




    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
