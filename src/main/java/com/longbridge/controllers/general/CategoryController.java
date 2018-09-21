package com.longbridge.controllers.general;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.CategoryDTO;
import com.longbridge.dto.StyleDTO;
import com.longbridge.dto.SubCategoryDTO;
import com.longbridge.models.*;
import com.longbridge.services.CategoryService;
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

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;


    @GetMapping(value = "/getcategories")
    public Object getCategories(){
        List<Category> categories =  categoryService.getAllCategories();
        return new Response("00","Operation Successful",categories);
    }


    @GetMapping(value = "/{categoryId}/getsubcategories")
    public Object getSubCategories(@PathVariable Long categoryId){
        List<SubCategory> subCategories = categoryService.getSubCategories(categoryId);
        return new Response("00","Operation Successful",subCategories);
    }


    @GetMapping(value = "/{subCategoryId}/getstyles")
    public Object getStyles(@PathVariable Long subCategoryId, HttpServletRequest request){
        List<Style> styles= productService.getStyles(subCategoryId);
        return new Response("00","Operation Successful",styles);

    }


}
