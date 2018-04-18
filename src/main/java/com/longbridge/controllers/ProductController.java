package com.longbridge.controllers;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.*;
import com.longbridge.models.*;
import com.longbridge.respbodydto.ProductRespDTO;
import com.longbridge.security.JwtUser;
import com.longbridge.services.HibernateSearchService;
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


    @GetMapping(value = "/{id}/getproductbyid")
    public Object getEventById(@PathVariable Long id, HttpServletRequest request){
//        String token = request.getHeader(tokenHeader);
//        JwtUser user = userUtil.getAuthenticationDetails(token);
//        if(token==null || user==null){
//            return userUtil.tokenNullOrInvalidResponse(token);
//        }
        ProductRespDTO products = productService.getProductById(id);
        Response response = new Response("00","Operation Successful",products);
        return response;

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
    public Object getProducts(@RequestBody PageableDetailsDTO pageableDetailsDTO){
        List<ProductRespDTO> products= productService.getAllProducts(pageableDetailsDTO);
        Response response = new Response("00","Operation Successful",products);
        return response;
    }

    @PostMapping(value = "/getproductsbysub")
    public Object getProductsBySub(@RequestBody ProdSubCategoryDTO p){
        List<ProductRespDTO> products= productService.getProductsBySubCatId(p);
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

    @GetMapping(value = "/{eventPictureId}/gettags")
    public Object getTags(@PathVariable Long eventPictureId){
        List<PicTagDTO> pictureTags=productService.getPictureTags(eventPictureId);
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



    @PostMapping(value = "/{id}/deleteproductimage")
    public Object deleteProductImages(@PathVariable Long id, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        JwtUser user = userUtil.getAuthenticationDetails(token);
        if(token==null || user.getUsername()==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.deleteProductImages(id);
        responseMap.put("success","success");
        Response response = new Response("00","Operation Successful",responseMap);
        return response;
    }


    @PostMapping(value = "/{id}/deleteartworkimage")
    public Object deleteArtWorkImages(@PathVariable Long id, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        JwtUser user = userUtil.getAuthenticationDetails(token);
        if(token==null || user.getUsername()==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.deleteArtWorkImages(id);
        responseMap.put("success","success");
        Response response = new Response("00","Operation Successful",responseMap);
        return response;
    }

    @PostMapping(value = "/{id}/deletematerialimage")
    public Object deleteMaterialImages(@PathVariable Long id, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        JwtUser user = userUtil.getAuthenticationDetails(token);
        if(token==null || user.getUsername()==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.deleteMaterialImages(id);
        responseMap.put("success","success");
        Response response = new Response("00","Operation Successful",responseMap);
        return response;
    }


    @GetMapping(value = "/{search}/searchproduct")
    public Response searchEvents(@PathVariable String search){
        Map<String,Object> responseMap = new HashMap();
        List<ProductRespDTO> products=searchService.productsFuzzySearch(search);
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
    public Object updateProductStatus(@PathVariable Long id, @PathVariable String status, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        JwtUser user = userUtil.getAuthenticationDetails(token);
        if(token==null || user.getUsername()==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.updateProductStatus(id,status);
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

    @PostMapping(value = "/{eventname}/getuntagged")
    public Response getUntaggedPicturesByEvent(@RequestBody PageableDetailsDTO pageableDetailsDTO,@PathVariable String eventname){
        List<EventPicturesDTO> eventpictures = productService.getUntaggedPicturesByEvents(pageableDetailsDTO, eventname);
        Response response = new Response("00", "Operation Successful", eventpictures);
        return response;
    }


    //todo later
    //get top products based on the number of orders

    @GetMapping(value = "/gettopproducts")
    public Response getTopProducts(HttpServletRequest request){
        Response response = new Response("00", "Operation Successful", productService.getTopProducts());
        return response;
    }

    @GetMapping(value = "/getfeaturedproducts")
    public Response getFeaturedProducts(HttpServletRequest request){
        Response response = new Response("00", "Operation Successful", productService.getTopProducts());
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
