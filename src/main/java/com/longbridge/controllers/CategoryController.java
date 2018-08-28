package com.longbridge.controllers;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.CategoryDTO;
import com.longbridge.dto.StyleDTO;
import com.longbridge.dto.SubCategoryDTO;
import com.longbridge.models.Category;
import com.longbridge.models.Response;
import com.longbridge.models.SubCategory;
import com.longbridge.models.User;
import com.longbridge.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Longbridge on 27/08/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/product")
public class CategoryController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    UserUtil userUtil;

    @Autowired
    ProductService productService;


    @PostMapping(value = "/addcategory")
    public Response addCategory(@RequestBody CategoryDTO categoryDTO, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.addCategory(categoryDTO);
        return new Response("00","Operation Successful","success");

    }

    @PostMapping(value = "/addsubcategory")
    public Response addCategory(@RequestBody SubCategoryDTO subCategoryDTO, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.addSubCategory(subCategoryDTO);
        return new Response("00","Operation Successful","success");

    }


    @PostMapping(value = "/{id}/deletesubcategory")
    public Response deleteSub(@PathVariable Long id, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.deleteSubCategory(id);
        return new Response("00","Operation Successful","success");

    }


    @PostMapping(value = "/addstyle")
    public Response addStyle(@RequestBody StyleDTO styleDTO, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.addStyle(styleDTO);
        return new Response("00","Operation Successful","success");

    }



    @GetMapping(value = "/getcategories")
    public Object getCategories(){
        List<Category> categories =  productService.getAllCategories();
        return new Response("00","Operation Successful",categories);

    }


    @GetMapping(value = "/{categoryId}/getsubcategories")
    public Object getSubCategories(@PathVariable Long categoryId){
        List<SubCategory> subCategories = productService.getSubCategories(categoryId);
        return new Response("00","Operation Successful",subCategories);

    }

    @GetMapping(value = "/getallsubcategories")
    public Object getAllSubCategories(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        List<SubCategory> subCategories = productService.getAllSubCategories();
        return new Response("00","Operation Successful",subCategories);

    }

}
