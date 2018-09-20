package com.longbridge.controllers.admin;

import com.longbridge.dto.CategoryDTO;
import com.longbridge.dto.StyleDTO;
import com.longbridge.dto.SubCategoryDTO;
import com.longbridge.models.Response;
import com.longbridge.models.SubCategory;
import com.longbridge.services.CategoryService;
import com.longbridge.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Longbridge on 19/09/2018.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/secure/admin/category")
public class AdminCategoryController {
    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;


    @PostMapping(value = "/addcategory")
    public Response addCategory(@RequestBody CategoryDTO categoryDTO){
        productService.addCategory(categoryDTO);
        return new Response("00","Operation Successful","success");

    }

    @GetMapping(value = "/getallsubcategories")
    public Object getAllSubCategories(HttpServletRequest request){

        List<SubCategory> subCategories = categoryService.getAllSubCategories();
        return new Response("00","Operation Successful",subCategories);

    }

    @PostMapping(value = "/addsubcategory")
    public Response addCategory(@RequestBody SubCategoryDTO subCategoryDTO){
        productService.addSubCategory(subCategoryDTO);
        return new Response("00","Operation Successful","success");

    }


    @PostMapping(value = "/{id}/deletesubcategory")
    public Response deleteSub(@PathVariable Long id){
        productService.deleteSubCategory(id);
        return new Response("00","Operation Successful","success");

    }


    @PostMapping(value = "/addstyle")
    public Response addStyle(@RequestBody StyleDTO styleDTO){
        productService.addStyle(styleDTO);
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
