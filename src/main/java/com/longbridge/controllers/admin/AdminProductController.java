package com.longbridge.controllers.admin;

import com.longbridge.dto.PageableDetailsDTO;
import com.longbridge.dto.VerifyDTO;
import com.longbridge.models.Response;
import com.longbridge.respbodydto.ProductRespDTO;
import com.longbridge.services.ProductService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @PostMapping(value = "/unverifyproduct")
    public Object unverifyProduct(@RequestBody VerifyDTO verifyDTO){
        Map<String,Object> responseMap = new HashMap();
        productService.unVerifyProduct(verifyDTO);
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", responseMap);
    }
    @GetMapping(value = {"/getproductbyid/{id}"})
    public Object getProductById(@PathVariable Long id){

        ProductRespDTO products = productService.getProductById(id,false);

        return new Response("00","Operation Successful",products);


    }





    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }
}
