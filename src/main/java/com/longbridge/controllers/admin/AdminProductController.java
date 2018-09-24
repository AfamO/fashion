package com.longbridge.controllers.admin;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.PageableDetailsDTO;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.respbodydto.ProductRespDTO;
import com.longbridge.services.HibernateSearchService;
import com.longbridge.services.ProductRatingService;
import com.longbridge.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Longbridge on 27/08/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/secure/admin/product")
public class AdminProductController {
    @Autowired
    ProductService productService;





    @PostMapping(value = "/getproducts")
    public Object getProducts(@RequestBody PageableDetailsDTO pageableDetailsDTO){
        List<ProductRespDTO> products;
        products= productService.getAllProducts(pageableDetailsDTO);
        return new Response("00","Operation Successful",products);
    }

    @PostMapping(value = "/getverifiedproducts")
    public Object getVerifiedProducts(@RequestBody PageableDetailsDTO pageableDetailsDTO){
        List<ProductRespDTO> products;
        products= productService.getVerifiedProducts(pageableDetailsDTO);
        return new Response("00","Operation Successful",products);

    }


    @PostMapping(value = "/getunverifiedproducts")
    public Object getUnVerifiedProducts(@RequestBody PageableDetailsDTO pageableDetailsDTO){
        List<ProductRespDTO> products;
        products= productService.getUnVerifiedProducts(pageableDetailsDTO);
        return new Response("00","Operation Successful",products);
    }


    @GetMapping(value = "/{id}/sponsor/{flag}")
    public Object sponsorProduct(@PathVariable Long id, @PathVariable String flag){
        Map<String,Object> responseMap = new HashMap();
        productService.sponsorProduct(id,flag);
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", responseMap);

    }

    @GetMapping(value = "/{id}/verifyproduct/{flag}")
    public Object updateProductStatus(@PathVariable Long id, @PathVariable String flag){
        Map<String,Object> responseMap = new HashMap();
        productService.updateProductStatus(id,flag);
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", responseMap);
    }


    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
