package com.longbridge.controllers.designer;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.*;
import com.longbridge.models.Designer;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.models.VendorBespokeFormDetails;
import com.longbridge.services.DesignerService;
import com.longbridge.services.OrderService;
import com.longbridge.services.ProductService;
import com.longbridge.services.VendorBespokeFormDetailsService;
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
@RequestMapping("/fashion/secure/designer")
public class SecureDesignerController {


    @Autowired
    DesignerService designerService;

    @Autowired
    VendorBespokeFormDetailsService vendorBespokeFormDetailsService;

    @GetMapping(value = "/getdesigner")
    public Response getDesignerById(){
        DesignerDTO designer = designerService.getDesignerWithSalesChart();
        return new Response("00","Operation Successful",designer);
    }


    @PostMapping(value = "/updateemailaddress")
    public Response updateEmailAddress(@RequestBody UserEmailTokenDTO userEmailTokenDTO, Device device){

        return designerService.updateEmailAddress(userEmailTokenDTO,device);
    }


    @PostMapping(value = "/updatepersonalinformation")
    public Response updatePersonalInformation(@RequestBody UserDTO user){

        designerService.updateDesignerPersonalInformation(user);
        return new Response("00", "Profile updated", null);
    }

    @PostMapping(value = "/updatebusinessinformation")
    public Response updateBusinessInformation(@RequestBody UserDTO user){


        return new Response("00", "Profile updated", designerService.updateDesignerBusinessInformation(user));
    }

    @PostMapping(value = "/updateaccountinformation")
    public Response updateAccountInformation(@RequestBody UserDTO user){
        designerService.updateDesignerAccountInformation(user);
        return new Response("00", "Profile updated", null);
    }

    @PostMapping(value = "/updateinformation")
    public Response updateInformation(@RequestBody UserDTO user){
        designerService.updateDesignerInformation(user);
        return new Response("00", "Profile updated", null);
    }

    @PostMapping(value = "/updatedesignerlogo")
    public Response updateDesignerLogo(@RequestBody DesignerDTO designer){
        designerService.updateDesignerLogo(designer);
        return new Response("00","Operation Successful","success");

    }


    @PostMapping(value = "/updatedesignerbanner")
    public Response updateDesignerBanner(@RequestBody DesignerDTO designer){

        designerService.updateDesignerBanner(designer);
        return new Response("00","Operation Successful","success");

    }



    @PostMapping(value = "/bespoke/apply")
    public Response applyForBespoke(@RequestBody VendorBespokeFormDetails vendorBespokeFormDetails){

        vendorBespokeFormDetailsService.add(vendorBespokeFormDetails);
        return new Response("00", "Operation Successful", "success");
    }


    @PostMapping(value = "/ratedesigner")
    public Response rateDesigner(@RequestBody DesignerRatingDTO ratingDTO){
        designerService.rateDesigner(ratingDTO);
        return new Response("00","Operation Successful","success");
    }

    @GetMapping(value = "/{designerId}/deletedesigner")
    public Object deleteDesigner(@PathVariable Long designerId){
        designerService.deleteDesigner(designerId);
        return new Response("00","Operation Successful","success");
    }


    @GetMapping(value = "/{designerId}/{status}/changestatus")
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
