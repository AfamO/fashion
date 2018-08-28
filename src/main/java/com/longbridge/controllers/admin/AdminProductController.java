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
@RequestMapping("/fashion/admin/product")
public class AdminProductController {
    @Autowired
    ProductService productService;

    @Autowired
    HibernateSearchService searchService;

    @Autowired
    ProductRatingService productRatingService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    UserUtil userUtil;

    @PostMapping(value = "/getverifiedproducts")
    public Object getVerifiedProducts(@RequestBody PageableDetailsDTO pageableDetailsDTO, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        List<ProductRespDTO> products;

        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        products= productService.getVerifiedProducts(pageableDetailsDTO);
        return new Response("00","Operation Successful",products);

    }


    @PostMapping(value = "/getunverifiedproducts")
    public Object getUnVerifiedProducts(@RequestBody PageableDetailsDTO pageableDetailsDTO, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        List<ProductRespDTO> products;

        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        products= productService.getUnVerifiedProducts(pageableDetailsDTO);
        return new Response("00","Operation Successful",products);

    }


    @GetMapping(value = "/{id}/sponsor/{flag}")
    public Object sponsorProduct(@PathVariable Long id, @PathVariable String flag, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        if(!user.role.equalsIgnoreCase("superadmin")){
            return new Response("99","Operation Failed, Not a designer",responseMap);
        }
        productService.sponsorProduct(id,flag);
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", responseMap);

    }

    @GetMapping(value = "/{id}/verifyproduct/{flag}")
    public Object updateProductStatus(@PathVariable Long id, @PathVariable String flag, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        if(!user.role.equalsIgnoreCase("superadmin")){
            return new Response("99","Operation Failed, Not a designer",responseMap);
        }
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
