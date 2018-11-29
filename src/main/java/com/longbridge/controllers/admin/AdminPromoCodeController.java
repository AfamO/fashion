package com.longbridge.controllers.admin;

import com.longbridge.dto.PageableDetailsDTO;
import com.longbridge.dto.PromoCodeDTO;
import com.longbridge.dto.VerifyDTO;
import com.longbridge.models.PromoCode;
import com.longbridge.models.Response;
import com.longbridge.respbodydto.ProductRespDTO;
import com.longbridge.services.ProductService;
import com.longbridge.services.PromoCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/secure/admin/promocode")
public class AdminPromoCodeController {
    @Autowired
    PromoCodeService promoCodeService;

    @PostMapping(value = "/getpromocodes")
    public Object getProducts(@RequestBody PageableDetailsDTO pageableDetailsDTO){

        return new Response("00","Operation Successful",promoCodeService.getAllPromoCodes(pageableDetailsDTO));
    }


    @PostMapping(value = "/getunusedpromocode")
    public Object getUnUsedPromoCodes(@RequestBody PageableDetailsDTO pageableDetailsDTO){
        List<PromoCodeDTO> promoCodes;
        promoCodes= promoCodeService.getUnUsedPromoCodes(pageableDetailsDTO);
        return new Response("00","Operation Successful",promoCodes);
    }

    @PostMapping(value = "/getusedpromocode")
    public Object getUsedPromoCodes(@RequestBody PageableDetailsDTO pageableDetailsDTO){
        List<PromoCodeDTO> promoCodes;
        promoCodes= promoCodeService.getUsedPromoCodes(pageableDetailsDTO);
        return new Response("00","Operation Successful",promoCodes);
    }
    

    @GetMapping(value = "/getpromocode/{id}")
    public Object getPromocode(@PathVariable Long id){
        Map<String,Object> responseMap = new HashMap();
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", promoCodeService.getPromoCode(id));
    }

    @GetMapping(value = "/getexpiredpromocode")
    public Object getExpiredPromocode(){
        PageableDetailsDTO pageableDetailsDTO = null;
        Map<String,Object> responseMap = new HashMap();
        List<PromoCodeDTO> promoCodes=promoCodeService.getExpiredPromoCodes(pageableDetailsDTO);
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", promoCodes);
    }
    @GetMapping(value = "/getunexpiredpromocode")
    public Object getActivePromocode(){
        PageableDetailsDTO pageableDetailsDTO = null;
        Map<String,Object> responseMap = new HashMap();
        List<PromoCodeDTO> promoCodes=promoCodeService.getActiveAndStillValidPromoCodes(pageableDetailsDTO);
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", promoCodes);
    }

    @PostMapping(value = "/addpromocode")
    public Object addPromoCode(@RequestBody PromoCodeDTO promoCodeDTO){
        Map<String,Object> responseMap = new HashMap();
        String statusMessage = promoCodeService.addPromoCode(promoCodeDTO);
        //responseMap.put("success", "success");
        return new Response("00", statusMessage, responseMap);
    }
    @PostMapping(value = "/updatepromocode")
    public Object updatePromoCode(@RequestBody PromoCodeDTO promoCodeDTO){
        Map<String,Object> responseMap = new HashMap();
        String statusMessage = promoCodeService.updatePromoCode(promoCodeDTO);
        //responseMap.put("success", "success");
        return new Response("00", statusMessage, responseMap);
    }
    @PostMapping(value = "/verifypromocode")
    public Object verifyPromoCode(@RequestBody PromoCodeDTO promoCodeDTO){
        Map<String,Object> responseMap = new HashMap();
        String statusMessage = promoCodeService.verifyPromoCode(promoCodeDTO);
        //responseMap.put("success", "success");
        return new Response("00", statusMessage, responseMap);
    }
    @PostMapping(value = "/updatePromocodeItems")
    public Object addPromoCodeItems(@RequestBody PromoCodeDTO promoCodeDTO){
        Map<String,Object> responseMap = new HashMap();
        String statusMessage = promoCodeService.updatePromoCodeItems(promoCodeDTO);
        //responseMap.put("success", "success");
        return new Response("00", statusMessage, responseMap);
    }

    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
