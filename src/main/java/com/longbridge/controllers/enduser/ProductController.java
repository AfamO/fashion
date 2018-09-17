package com.longbridge.controllers.enduser;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.*;
import com.longbridge.models.*;
import com.longbridge.repository.DesignerRepository;
import com.longbridge.respbodydto.ProductRespDTO;
import com.longbridge.security.JwtUser;
import com.longbridge.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Longbridge on 06/11/2017.
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/fashion/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    HibernateSearchService searchService;

    @Autowired
    ProductRatingService productRatingService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    DesignerService designerService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    UserUtil userUtil;


    @GetMapping(value = {"/{id}/getproductbyid/{reviews}","/{id}/getproductbyid"})
    public Object getProductById(@PathVariable Long id, HttpServletRequest request, @PathVariable("reviews") Optional<String> reviews){

        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        ProductRespDTO products;
        if(reviews.isPresent()){
           products = productService.getProductById(id,user,true);
        }else {
            products = productService.getProductById(id,user,false);
        }
        return new Response("00","Operation Successful",products);
    }


    @GetMapping(value = {"/{id}/getproductattributebyid}"})
    public Object getProductPictureById(@PathVariable Long id){

        return new Response("00","Operation Successful",productService.getProductAttributesById(id));
    }




    @GetMapping(value = "/{id}/getproductbyid/getuserreview")
    public Response getUserReviewForProduct(@PathVariable Long id, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        ProductRating productRating = productRatingService.getUserRating(user, id);
        return new Response("00", "user review", productRating);
    }


    @GetMapping(value = "/{designerId}/getdesignerproducts")
    public Object getProductsByDesignerId(@PathVariable Long designerId){


        List<ProductRespDTO> products= productService.getProductsByDesignerId(designerId);
        return new Response("00","Operation Successful",products);
    }


    @GetMapping(value = "/getdesignerproducts")
    public Object getProductsByDesigner(HttpServletRequest request){

        Map<String,Object> responseMap = new HashMap();

        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);

        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }


        if(!user.getRole().equalsIgnoreCase("designer")){
            return new Response("99","Operation Failed, Not a designer",responseMap);
        }


        Long id = designerService.getDesigner(user).getId();
        List<ProductRespDTO> products= productService.getProductsByDesigner(user);
        return new Response("00","Operation Successful",products);
    }

    @PostMapping(value = "/getproducts")
    public Object getProducts(@RequestBody PageableDetailsDTO pageableDetailsDTO, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        List<ProductRespDTO> products;
        products= productService.getAllProducts(pageableDetailsDTO);
        return new Response("00","Operation Successful",products);
    }


    @PostMapping(value = "/getnewproducts")
    public Object getNewProducts(@RequestBody PageableDetailsDTO pageableDetailsDTO){
        List<ProductRespDTO> products= productService.getNewProducts(pageableDetailsDTO);
        return new Response("00","Operation Successful",products);
    }


    @PostMapping(value = "/filter")
    public Object filterProductsByPrice(@RequestBody FilterProductDTO filterProductDTO){
        List<ProductRespDTO> products= productService.filterProducts(filterProductDTO);
        return new Response("00","Operation Successful",products);

    }

    @PostMapping(value = "/getproductsbysub")
    public Object getProductsBySub(@RequestBody ProdSubCategoryDTO p){
        List<ProductRespDTO> products= productService.getProductsBySubCatId(p);
        return new Response("00","Operation Successful",products);

    }

    @PostMapping(value = "/getsubcatbyproducttype")
    public Object getSubCategories(@RequestBody SubCategoryDTO subCategoryDTO){
        List<SubCategory> subCategories = categoryService.getSubCategoriesByProductType(subCategoryDTO);
        return new Response("00","Operation Successful",subCategories);
    }


    @GetMapping(value = "/{search}/searchproduct")
    public Response searchProducts(@PathVariable String search){
        Map<String,Object> responseMap = new HashMap();
        List<ProductRespDTO> products=searchService.productsFuzzySearch(search);
        responseMap.put("result",products);
        return new Response("00","Operation Successful",responseMap);

    }

    @PostMapping(value = "/filterproduct")
    public Response filterProduct(@RequestBody FilterProductDTO filterProductDTO){
        Map<String,Object> responseMap = new HashMap();
        List<ProductRespDTO> products=productService.filterProducts(filterProductDTO);
        responseMap.put("result",products);
        Response response = new Response("00","Operation Successful",responseMap);
        return response;
    }



    //todo later
    //get top products based on the number of orders

    @GetMapping(value = "/gettopproducts")
    public Response getTopProducts(HttpServletRequest request){
        Response response = new Response("00", "Operation Successful", productService.getTopProducts());
        return response;
    }

    @GetMapping(value = "/getfreqboughtproducts")
    public Response getFreqBoughtProducts(HttpServletRequest request){
        return new Response("00", "Operation Successful", productService.getFreqBoughtProducts());
    }

    @GetMapping(value = "/getfeaturedproducts")
    public Response getFeaturedProducts(HttpServletRequest request){
        return new Response("00", "Operation Successful", productService.getFeaturedProducts());

    }


    @PostMapping(value = "/getdesignerproductsbysub")
    public Object getDesignerProductsBySub(@RequestBody ProdSubCategoryDTO p, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User user = null;
        if(token != null){
            user = userUtil.fetchUserDetails2(token);
        }
        List<ProductRespDTO> products= productService.getDesignerProductsBySubCatId(p,user);
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
