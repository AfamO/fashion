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
        return new Response("00","Operation Successful","success");

    }

    @PostMapping(value = "/addsubcategory")
    public Response addCategory(@RequestBody SubCategoryDTO subCategoryDTO){
       productService.addSubCategory(subCategoryDTO);
        return new Response("00","Operation Successful","success");

    }

    @PostMapping(value = "/addstyle")
    public Response addStyle(@RequestBody StyleDTO styleDTO){
        productService.addStyle(styleDTO);
        return new Response("00","Operation Successful","success");

    }

    @GetMapping(value = "/{subCategoryId}/getstyles")
    public Object getStyles(@PathVariable Long subCategoryId){
        List<Style> styles= productService.getStyles(subCategoryId);
        return new Response("00","Operation Successful",styles);

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
        ProductRespDTO products;
        if(reviews.isPresent()){
           products = productService.getProductById(id,user,true);
        }else {
            products = productService.getProductById(id,user,false);
        }

        return new Response("00","Operation Successful",products);


    }


    @GetMapping(value = {"/{id}/designer/getproductbyid"})
    public Object getDesignerProductById(@PathVariable Long id, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        JwtUser user = userUtil.getAuthenticationDetails(token);
        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        User user1 = userUtil.fetchUserDetails2(token);

            ProductRespDTO products = productService.getDesignerProductById(id,user1);

        return new Response("00","Operation Successful",products);


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

        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        Designer designer = user.designer;
        List<ProductRespDTO> products= productService.getProductsByDesigner(designer.id);
        return new Response("00","Operation Successful",products);

    }


    @GetMapping(value = "/{search}/designerprodsearch")
    public Response searchProductsByDesigner(@PathVariable String search, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);

        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        Designer designer = user.designer;
        List<ProductRespDTO> products=searchService.designerProductsFuzzySearch(search,designer);

        return new Response("00","Operation Successful",products);

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
        return new Response("00","Operation Successful",products);

    }


    @PostMapping(value = "/getproducts")
    public Object getProducts(@RequestBody PageableDetailsDTO pageableDetailsDTO, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        List<ProductRespDTO> products;
//        if(token!=null || userTemp!=null) {
//            edto = eventService.getEventById(id,userTemp);
//        }
//        else {
//            edto= eventService.getEventById(id);
//        }
        products= productService.getAllProducts(pageableDetailsDTO);
        return new Response("00","Operation Successful",products);

    }


    @PostMapping(value = "/getnewproducts")
    public Object getNewProducts(@RequestBody PageableDetailsDTO pageableDetailsDTO){
        List<ProductRespDTO> products= productService.getNewProducts(pageableDetailsDTO);
        return new Response("00","Operation Successful",products);

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

//    @PostMapping(value = "/filter")
//    public Object filterProductsByPrice(@RequestBody FilterProductDTO filterProductDTO){
//        List<ProductRespDTO> products= productService.filterProductsByPrice(filterProductDTO);
//        Response response = new Response("00","Operation Successful",products);
//        return response;
//    }
//

    @PostMapping(value = "/filter")
    public Object filterProductsByPrice(@RequestBody FilterProductDTO filterProductDTO){
        System.out.println("i'm here");
        System.out.println(filterProductDTO.getSubCategoryId());
        List<ProductRespDTO> products= productService.filterProducts(filterProductDTO);
        return new Response("00","Operation Successful",products);

    }

    @PostMapping(value = "/getproductsbysub")
    public Object getProductsBySub(@RequestBody ProdSubCategoryDTO p){
        List<ProductRespDTO> products= productService.getProductsBySubCatId(p);
        return new Response("00","Operation Successful",products);

    }


//    @PostMapping(value = "/{search}/searchproductsbysub")
//    public Object searhProductsBySub(@RequestBody ProdSubCategoryDTO p, @PathVariable String search){
//        List<ProductRespDTO> products= productService.searchProductsBySubCat(search,p);
//        Response response = new Response("00","Operation Successful",products);
//        return response;
//    }


    @PostMapping(value = "/gettagproducts")
    public Object getProductsBySub(@RequestBody PicTagDTO picTagDTO){
        List<ProductRespDTO> products= productService.getTagProducts(picTagDTO);
        return new Response("00","Operation Successful",products);

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


    @PostMapping(value = "/addTag")
    public Response addTag(@RequestBody PictureTagDTO pictureTagDTO){
        productService.addPictureTag(pictureTagDTO);
        return new Response("00","Operation Successful","success");


    }

    @GetMapping(value = "/{id}/deletetag")
    public Response deleteTag(@PathVariable Long id){
        productService.deletePictureTag(id);
        return new Response("00","Operation Successful","success");

    }

    @GetMapping(value = "/{id}/gettag")
    public Response getTag(@PathVariable Long id){
        return new Response("00","Operation Successful",productService.getPictureTagById(id));

    }


    @GetMapping(value = "/{eventPictureId}/gettags")
    public Object getTags(@PathVariable Long eventPictureId){
        PictureTagDTO pictureTags=productService.getPictureTags(eventPictureId);
        return new Response("00","Operation Successful",pictureTags);

    }



    @PostMapping(value = "/addproduct")
    public Object addProduct(@RequestBody ProductDTO productDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);

        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        Designer designer = user.designer;
        productService.addProduct(productDTO,designer);
        responseMap.put("success","success");
        return new Response("00","Operation Successful",responseMap);

    }


    @PostMapping(value = "/addproduct2")
    public Object addProduct2(@RequestBody ProductDTO productDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);

        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        Designer designer = user.designer;
        productService.addProduct(productDTO,designer);
        responseMap.put("success","success");
        return new Response("00","Operation Successful",responseMap);

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
        return new Response("00","Operation Successful",responseMap);

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
        return new Response("00","Operation Successful",responseMap);

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
        return new Response("00","Operation Successful",responseMap);

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
        return new Response("00","Operation Successful",responseMap);

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
        return new Response("00","Operation Successful",responseMap);

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

    @PostMapping(value = "/updateproduct")
    public Object updateProduct(@RequestBody ProductDTO productDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);

        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        Designer designer = user.designer;
        productService.updateProduct(productDTO, designer);
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", responseMap);

    }

    @PostMapping(value = "/updateproductstock")
    public Object updateProductStock(@RequestBody ProductDTO productDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);

        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        Designer designer = user.designer;
        productService.updateProductStock(productDTO, designer);
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", responseMap);

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
        return new Response("00", "Operation Successful", responseMap);

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
        return new Response("00", "Operation Successful", responseMap);

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
        return new Response("00", "Operation Successful", responseMap);

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
        return new Response("00", "Operation Successful", responseMap);

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
        return new Response("00", "Operation Successful", responseMap);

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
        return new Response("00", "Operation Successful", responseMap);

    }

    @PostMapping(value = "/getuntagged")
    public Response getUntaggedPictures(@RequestBody PageableDetailsDTO pageableDetailsDTO){
        List<EventPicturesDTO> eventpictures = productService.getUntaggedPictures(pageableDetailsDTO);
        return new Response("00", "Operation Successful", eventpictures);

    }

    @PostMapping(value = "/gettagged")
    public Response getTaggedPictures(@RequestBody PageableDetailsDTO pageableDetailsDTO){
        List<EventPicturesDTO> eventpictures = productService.getTaggedPictures(pageableDetailsDTO);
        return new Response("00", "Operation Successful", eventpictures);

    }

    @GetMapping(value = "/{eventid}/getuntagged")
    public Response getUntaggedPicturesByEvent(@PathVariable Long eventid){
        List<EventPicturesDTO> eventpictures = productService.getUntaggedPicturesByEvents(eventid);
        return new Response("00", "Operation Successful", eventpictures);

    }

    @GetMapping(value = "/{eventid}/gettagged")
    public Response getTaggedPicturesByEvent(@PathVariable Long eventid){
        List<EventPicturesDTO> eventpictures = productService.getTaggedPicturesByEvents(eventid);
        return new Response("00", "Operation Successful", eventpictures);

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
        return new Response("00", "Operation Successful", productService.getFreqBoughtProducts());

    }

    @GetMapping(value = "/getfeaturedproducts")
    public Response getFeaturedProducts(HttpServletRequest request){
//        String token = request.getHeader(tokenHeader);
//        User user = userUtil.fetchUserDetails2(token);
        return new Response("00", "Operation Successful", productService.getFeaturedProducts());

    }



    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }

}
