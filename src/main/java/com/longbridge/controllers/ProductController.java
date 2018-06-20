package com.longbridge.controllers;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.*;
import com.longbridge.models.*;
import com.longbridge.respbodydto.ProductRespDTO;
import com.longbridge.security.JwtUser;
import com.longbridge.services.HibernateSearchService;
import com.longbridge.services.ProductRatingService;
import com.longbridge.services.ProductService;
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

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    UserUtil userUtil;

    @PostMapping(value = "/addcategory")
    public Response addCategory(@RequestBody CategoryDTO categoryDTO){
        productService.addCategory(categoryDTO);
        Response response = new Response("00","Operation Successful","success");
        return response;
    }

    @PostMapping(value = "/addsubcategory")
    public Response addCategory(@RequestBody SubCategoryDTO subCategoryDTO){
       productService.addSubCategory(subCategoryDTO);
        Response response = new Response("00","Operation Successful","success");
        return response;
    }

    @PostMapping(value = "/addstyle")
    public Response addStyle(@RequestBody StyleDTO styleDTO){
        productService.addStyle(styleDTO);
        Response response = new Response("00","Operation Successful","success");
        return response;
    }

    @GetMapping(value = "/{subCategoryId}/getstyles")
    public Object getStyles(@PathVariable Long subCategoryId){
        List<Style> styles= productService.getStyles(subCategoryId);
        Response response = new Response("00","Operation Successful",styles);
        return response;
    }


    @GetMapping(value = {"/{id}/getproductbyid/{reviews}","/{id}/getproductbyid"})
    public Object getEventById(@PathVariable Long id, HttpServletRequest request, @PathVariable("reviews") Optional<String> reviews){
//        String token = request.getHeader(tokenHeader);
//        JwtUser user = userUtil.getAuthenticationDetails(token);
//        if(token==null || user==null){
//            return userUtil.tokenNullOrInvalidResponse(token);
//        }

        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        ProductRespDTO products = null;
        if(reviews.isPresent()){
           products = productService.getProductByIdWithReviews(id,user);
        }else {
            products = productService.getProductById(id,user);
        }
        Response response = new Response("00","Operation Successful",products);
        return response;

    }

    @GetMapping(value = "/{id}/getproductbyid/getuserreview")
    public Response getUserReviewForProduct(@PathVariable Long id, HttpServletRequest request){

        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);

        ProductRating productRating = productRatingService.getUserRating(user, id);
        return new Response("00", "user review", productRating);
    }

    @GetMapping(value = "/getdesignerproducts")
    public Object getProductsByDesigner(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        Designer designer = user.designer;
        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        List<ProductRespDTO> products= productService.getProductsByDesigner(designer.id);
        Response response = new Response("00","Operation Successful",products);
        return response;
    }


    @GetMapping(value = "/{search}/designerprodsearch")
    public Response searchProductsByDesigner(@PathVariable String search, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        Designer designer = user.designer;
        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        List<ProductRespDTO> products=searchService.designerProductsFuzzySearch(search,designer);

        Response response = new Response("00","Operation Successful",products);
        return response;
    }


    @GetMapping(value = "/{designerId}/getdesignerproducts")
    public Object getProductsByDesignerId(@PathVariable Long designerId){
//        String token = request.getHeader(tokenHeader);
//        User user = userUtil.fetchUserDetails2(token);
//        Designer designer = user.designer;
//        if(token==null || user==null){
//            return userUtil.tokenNullOrInvalidResponse(token);
//        }
        List<ProductRespDTO> products= productService.getProductsByDesigner(designerId);
        Response response = new Response("00","Operation Successful",products);
        return response;
    }


    @PostMapping(value = "/getproducts")
    public Object getProducts(@RequestBody PageableDetailsDTO pageableDetailsDTO, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        List<ProductRespDTO> products = new ArrayList<>();
//        if(token!=null || userTemp!=null) {
//            edto = eventService.getEventById(id,userTemp);
//        }
//        else {
//            edto= eventService.getEventById(id);
//        }
        products= productService.getAllProducts(pageableDetailsDTO);
        Response response = new Response("00","Operation Successful",products);
        return response;
    }


    @PostMapping(value = "/getnewproducts")
    public Object getNewProducts(@RequestBody PageableDetailsDTO pageableDetailsDTO){
        List<ProductRespDTO> products= productService.getNewProducts(pageableDetailsDTO);
        Response response = new Response("00","Operation Successful",products);
        return response;
    }

//    @PostMapping(value = "/getacceptedproducts")
//    public Object getAcceptedProducts(@RequestBody PageableDetailsDTO pageableDetailsDTO){
//        List<ProductRespDTO> products= productService.getNewProducts(pageableDetailsDTO);
//        Response response = new Response("00","Operation Successful",products);
//        return response;
//    }
//
//    @PostMapping(value = "/getdeclinedproducts")
//    public Object getDeclinedProducts(@RequestBody PageableDetailsDTO pageableDetailsDTO){
//        List<ProductRespDTO> products= productService.getNewProducts(pageableDetailsDTO);
//        Response response = new Response("00","Operation Successful",products);
//        return response;
//    }

    @PostMapping(value = "/filter")
    public Object filterProductsByPrice(@RequestBody FilterProductDTO filterProductDTO){
        List<ProductRespDTO> products= productService.filterProductsByPrice(filterProductDTO);
        Response response = new Response("00","Operation Successful",products);
        return response;
    }


    @PostMapping(value = "/getproductsbysub")
    public Object getProductsBySub(@RequestBody ProdSubCategoryDTO p){
        List<ProductRespDTO> products= productService.getProductsBySubCatId(p);
        Response response = new Response("00","Operation Successful",products);
        return response;
    }


    @PostMapping(value = "/{search}/searchproductsbysub")
    public Object searhProductsBySub(@RequestBody ProdSubCategoryDTO p, @PathVariable String search){
        List<ProductRespDTO> products= productService.searchProductsBySubCat(search,p);
        Response response = new Response("00","Operation Successful",products);
        return response;
    }


    @PostMapping(value = "/gettagproducts")
    public Object getProductsBySub(@RequestBody PicTagDTO picTagDTO){
        List<ProductRespDTO> products= productService.getTagProducts(picTagDTO);
        Response response = new Response("00","Operation Successful",products);
        return response;
    }


    @PostMapping(value = "/getdesignerproductsbysub")
    public Object getDesignerProductsBySub(@RequestBody ProdSubCategoryDTO p, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User user = null;
        if(token != null){
            user = userUtil.fetchUserDetails2(token);
        }
        List<ProductRespDTO> products= productService.getDesignerProductsBySubCatId(p,user);
        Response response = new Response("00","Operation Successful",products);
        return response;
    }


    @GetMapping(value = "/getcategories")
    public Object getCategories(){
        List<Category> categories =  productService.getAllCategories();
        Response response = new Response("00","Operation Successful",categories);
        return response;
    }


    @GetMapping(value = "/{categoryId}/getsubcategories")
    public Object getSubCategories(@PathVariable Long categoryId){
        List<SubCategory> subCategories = productService.getSubCategories(categoryId);
        Response response = new Response("00","Operation Successful",subCategories);
        return response;
    }


    @PostMapping(value = "/addTag")
    public Response addTag(@RequestBody PictureTagDTO pictureTagDTO){
        productService.addPictureTag(pictureTagDTO);
        Response response = new Response("00","Operation Successful","success");
        return response;

    }

    @GetMapping(value = "/{id}/deletetag")
    public Response deleteTag(@PathVariable Long id){
        productService.deletePictureTag(id);
        Response response = new Response("00","Operation Successful","success");
        return response;
    }

    @GetMapping(value = "/{id}/gettag")
    public Response getTag(@PathVariable Long id){
        Response response = new Response("00","Operation Successful",productService.getPictureTagById(id));
        return response;
    }


    @GetMapping(value = "/{eventPictureId}/gettags")
    public Object getTags(@PathVariable Long eventPictureId){
        PictureTagDTO pictureTags=productService.getPictureTags(eventPictureId);
        Response response = new Response("00","Operation Successful",pictureTags);
        return response;
    }



    @PostMapping(value = "/addproduct")
    public Object addProduct(@RequestBody ProductDTO productDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        Designer designer = user.designer;
        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.addProduct(productDTO,designer);
        responseMap.put("success","success");
        Response response = new Response("00","Operation Successful",responseMap);
        return response;
    }

    @GetMapping(value = "/{id}/deleteproduct")
    public Object deleteProduct(@PathVariable Long id, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        JwtUser user = userUtil.getAuthenticationDetails(token);
        if(token==null || user.getUsername()==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.deleteProduct(id);
        responseMap.put("success","success");
        Response response = new Response("00","Operation Successful",responseMap);
        return response;
    }



    @GetMapping(value = "/{id}/deleteproductimage")
    public Object deleteProductImages(@PathVariable Long id, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        JwtUser user = userUtil.getAuthenticationDetails(token);
        if(token==null || user.getUsername()==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.deleteProductImage(id);
        responseMap.put("success","success");
        Response response = new Response("00","Operation Successful",responseMap);
        return response;
    }

    @PostMapping(value = "/deleteproductimage")
    public Object deleteProductImages(@RequestBody ProductPictureIdListDTO pictureIdListDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        JwtUser user = userUtil.getAuthenticationDetails(token);
        if(token==null || user.getUsername()==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.deleteProductImages(pictureIdListDTO);
        responseMap.put("success","success");
        Response response = new Response("00","Operation Successful",responseMap);
        return response;
    }


    @PostMapping(value = "/deleteartworkimage")
    public Object deleteArtWorkImages(@RequestBody ProductPictureIdListDTO pictureIdListDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        JwtUser user = userUtil.getAuthenticationDetails(token);
        if(token==null || user.getUsername()==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.deleteArtWorkImages(pictureIdListDTO);
        responseMap.put("success","success");
        Response response = new Response("00","Operation Successful",responseMap);
        return response;
    }

    @PostMapping(value = "/deletematerialimage")
    public Object deleteMaterialImages(@RequestBody ProductPictureIdListDTO pictureIdListDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        JwtUser user = userUtil.getAuthenticationDetails(token);
        if(token==null || user.getUsername()==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.deleteMaterialImages(pictureIdListDTO);
        responseMap.put("success","success");
        Response response = new Response("00","Operation Successful",responseMap);
        return response;
    }


    @GetMapping(value = "/{search}/searchproduct")
    public Response searchProducts(@PathVariable String search){
        Map<String,Object> responseMap = new HashMap();
        List<ProductRespDTO> products=searchService.productsFuzzySearch(search);
        responseMap.put("result",products);
        Response response = new Response("00","Operation Successful",responseMap);
        return response;
    }

    @PostMapping(value = "/filterproduct")
    public Response filterProduct(@RequestBody FilterProductDTO filterProductDTO){
        Map<String,Object> responseMap = new HashMap();
        List<ProductRespDTO> products=productService.filterProducts(filterProductDTO);
        responseMap.put("result",products);
        Response response = new Response("00","Operation Successful",responseMap);
        return response;
    }

    @PostMapping(value = "/updateproduct")
    public Object updateProduct(@RequestBody ProductDTO productDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        Designer designer = user.designer;
        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.updateProduct(productDTO, designer);
        responseMap.put("success", "success");
        Response response = new Response("00", "Operation Successful", responseMap);
        return response;
    }




    @PostMapping(value = "/updateproductimage")
    public Object updateProductImage(@RequestBody ProdPicReqDTO prodPicReqDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.updateProductImages(prodPicReqDTO);
        responseMap.put("success", "success");
        Response response = new Response("00", "Operation Successful", responseMap);
        return response;
    }

    @PostMapping(value = "/updateproductartwork")
    public Object updateProdArtMaterial(@RequestBody ArtPicReqDTO artPicReqDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.updateArtWorkImages(artPicReqDTO);
        responseMap.put("success", "success");
        Response response = new Response("00", "Operation Successful", responseMap);
        return response;
    }

    @PostMapping(value = "/updateproductmaterial")
    public Object updateProdArtMaterial(@RequestBody MatPicReqDTO matPicReqDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.updateMaterialImages(matPicReqDTO);
        responseMap.put("success", "success");
        Response response = new Response("00", "Operation Successful", responseMap);
        return response;
    }




    @GetMapping(value = "/{id}/productvisibility/{status}")
    public Object updateProductVisibility(@PathVariable Long id, @PathVariable String status, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        JwtUser user = userUtil.getAuthenticationDetails(token);
        if(token==null || user.getUsername()==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.updateProductVisibility(id,status);
        responseMap.put("success", "success");
        Response response = new Response("00", "Operation Successful", responseMap);
        return response;
    }

    @GetMapping(value = "/{id}/verifyproduct/{flag}")
    public Object updateProductStatus(@PathVariable Long id, @PathVariable String flag, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
//        String token = request.getHeader(tokenHeader);
//        JwtUser user = userUtil.getAuthenticationDetails(token);
//        if(token==null || user.getUsername()==null){
//            return userUtil.tokenNullOrInvalidResponse(token);
//        }
        productService.updateProductStatus(id,flag);
        responseMap.put("success", "success");
        Response response = new Response("00", "Operation Successful", responseMap);
        return response;
    }

    @GetMapping(value = "/{id}/sponsor/{flag}")
    public Object sponsorProduct(@PathVariable Long id, @PathVariable String flag, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
//        String token = request.getHeader(tokenHeader);
//        JwtUser user = userUtil.getAuthenticationDetails(token);
//        if(token==null || user.getUsername()==null){
//            return userUtil.tokenNullOrInvalidResponse(token);
//        }
        productService.sponsorProduct(id,flag);
        responseMap.put("success", "success");
        Response response = new Response("00", "Operation Successful", responseMap);
        return response;
    }

    @PostMapping(value = "/getuntagged")
    public Response getUntaggedPictures(@RequestBody PageableDetailsDTO pageableDetailsDTO){
        List<EventPicturesDTO> eventpictures = productService.getUntaggedPictures(pageableDetailsDTO);
        Response response = new Response("00", "Operation Successful", eventpictures);
        return response;
    }

    @PostMapping(value = "/gettagged")
    public Response getTaggedPictures(@RequestBody PageableDetailsDTO pageableDetailsDTO){
        List<EventPicturesDTO> eventpictures = productService.getTaggedPictures(pageableDetailsDTO);
        Response response = new Response("00", "Operation Successful", eventpictures);
        return response;
    }

    @GetMapping(value = "/{eventid}/getuntagged")
    public Response getUntaggedPicturesByEvent(@PathVariable Long eventid){
        List<EventPicturesDTO> eventpictures = productService.getUntaggedPicturesByEvents(eventid);
        Response response = new Response("00", "Operation Successful", eventpictures);
        return response;
    }

    @GetMapping(value = "/{eventid}/gettagged")
    public Response getTaggedPicturesByEvent(@PathVariable Long eventid){
        List<EventPicturesDTO> eventpictures = productService.getTaggedPicturesByEvents(eventid);
        Response response = new Response("00", "Operation Successful", eventpictures);
        return response;
    }

    //todo later
    //get top products based on the number of orders

    @GetMapping(value = "/gettopproducts")
    public Response getTopProducts(HttpServletRequest request){
//        String token = request.getHeader(tokenHeader);
//        User user = userUtil.fetchUserDetails2(token);
       // List<ProductRespDTO> products = new ArrayList<>();
        Response response = new Response("00", "Operation Successful", productService.getTopProducts());
        return response;
    }


    @GetMapping(value = "/getfreqboughtproducts")
    public Response getFreqBoughtProducts(HttpServletRequest request){
//        String token = request.getHeader(tokenHeader);
//        User user = userUtil.fetchUserDetails2(token);
        // List<ProductRespDTO> products = new ArrayList<>();
        Response response = new Response("00", "Operation Successful", productService.getFreqBoughtProducts());
        return response;
    }

    @GetMapping(value = "/getfeaturedproducts")
    public Response getFeaturedProducts(HttpServletRequest request){
//        String token = request.getHeader(tokenHeader);
//        User user = userUtil.fetchUserDetails2(token);
        Response response = new Response("00", "Operation Successful", productService.getFeaturedProducts());
        return response;
    }



    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }

}
