package com.longbridge.controllers.designer;

import com.longbridge.Util.UserUtil;
import com.longbridge.dto.*;
import com.longbridge.models.Designer;
import com.longbridge.models.Response;
import com.longbridge.models.Style;
import com.longbridge.models.User;
import com.longbridge.repository.DesignerRepository;
import com.longbridge.respbodydto.ProductRespDTO;
import com.longbridge.security.JwtUser;
import com.longbridge.services.HibernateSearchService;
import com.longbridge.services.MeasurementService;
import com.longbridge.services.ProductRatingService;
import com.longbridge.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/fashion/product/designer")
public class DesignerProductController {
    @Autowired
    ProductService productService;

    @Autowired
    HibernateSearchService searchService;

    @Autowired
    MeasurementService measurementService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    UserUtil userUtil;

    @Autowired
    DesignerRepository designerRepository;



    @PostMapping(value = "/addproduct")
    public Object addProduct(@RequestBody ProductDTO productDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);

        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        if(!user.getRole().equalsIgnoreCase("designer")){
            return new Response("99","Operation Failed, Not a designer",responseMap);
        }
        productService.addProduct(productDTO,user);
        responseMap.put("success","success");
        return new Response("00","Operation Successful",responseMap);

    }


    @PostMapping(value = "/updateproduct")
    public Object updateProduct(@RequestBody ProductDTO productDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);

        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        if(!user.getRole().equalsIgnoreCase("designer")){
            return new Response("99","Operation Failed, Not a designer",responseMap);
        }
        productService.updateProduct(productDTO, user);
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

        if(!user.getRole().equalsIgnoreCase("designer")){
            return new Response("99","Operation Failed, Not a designer",responseMap);
        }

        productService.updateProductStock(productDTO, user);
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", responseMap);

    }




    @PostMapping(value = "/updateproductimage")
    public Object updateProductImage(@RequestBody ProductDTO p, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User userTemp = userUtil.fetchUserDetails2(token);
        if(token==null || userTemp==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        productService.updateProductImages(p);
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
        if(!userTemp.getRole().equalsIgnoreCase("designer")){
            return new Response("99","Operation Failed, Not a designer",responseMap);
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
        if(!userTemp.getRole().equalsIgnoreCase("designer")){
            return new Response("99","Operation Failed, Not a designer",responseMap);
        }
        productService.updateMaterialImages(matPicReqDTO);
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", responseMap);

    }



    @GetMapping(value = "/{id}/deleteproduct")
    public Object deleteProduct(@PathVariable Long id, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        if(!user.getRole().equalsIgnoreCase("designer")){
            return new Response("99","Operation Failed, Not a designer",responseMap);
        }
        productService.deleteProduct(id);
        responseMap.put("success","success");
        return new Response("00","Operation Successful",responseMap);

    }



    @GetMapping(value = "/{id}/deleteproductimage")
    public Object deleteProductImages(@PathVariable Long id, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        if(!user.getRole().equalsIgnoreCase("designer")){
            return new Response("99","Operation Failed, Not a designer",responseMap);
        }
        productService.deleteProductImage(id);
        responseMap.put("success","success");
        return new Response("00","Operation Successful",responseMap);

    }

    @PostMapping(value = "/deleteproductimage")
    public Object deleteProductImages(@RequestBody ProductPictureIdListDTO pictureIdListDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        if(!user.getRole().equalsIgnoreCase("designer")){
            return new Response("99","Operation Failed, Not a designer",responseMap);
        }
        productService.deleteProductImages(pictureIdListDTO);
        responseMap.put("success","success");
        return new Response("00","Operation Successful",responseMap);

    }


    @PostMapping(value = "/deleteartworkimage")
    public Object deleteArtWorkImages(@RequestBody ProductPictureIdListDTO pictureIdListDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        if(!user.getRole().equalsIgnoreCase("designer")){
            return new Response("99","Operation Failed, Not a designer",responseMap);
        }
        productService.deleteArtWorkImages(pictureIdListDTO);
        responseMap.put("success","success");
        return new Response("00","Operation Successful",responseMap);

    }

    @PostMapping(value = "/deletematerialimage")
    public Object deleteMaterialImages(@RequestBody ProductPictureIdListDTO pictureIdListDTO, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        if(!user.getRole().equalsIgnoreCase("designer")){
            return new Response("99","Operation Failed, Not a designer",responseMap);
        }
        productService.deleteMaterialImages(pictureIdListDTO);
        responseMap.put("success","success");
        return new Response("00","Operation Successful",responseMap);

    }



    @GetMapping(value = {"/{id}/getproductbyid"})
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


    @GetMapping(value = "/getdesignerproducts")
    public Object getProductsByDesigner(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);

        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        List<ProductRespDTO> products= productService.getProductsByDesigner(user);
        return new Response("00","Operation Successful",products);

    }






    @GetMapping(value = "/{id}/productvisibility/{status}")
    public Object updateProductVisibility(@PathVariable Long id, @PathVariable String status, HttpServletRequest request){
        Map<String,Object> responseMap = new HashMap();
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);
        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }
        if(!user.getRole().equalsIgnoreCase("designer")){
            return new Response("99","Operation Failed, Not a designer",responseMap);
        }

        productService.updateProductVisibility(id,status);
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", responseMap);

    }



    @GetMapping(value = "/{search}/designerprodsearch")
    public Response searchProductsByDesigner(@PathVariable String search, HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        User user = userUtil.fetchUserDetails2(token);

        if(token==null || user==null){
            return userUtil.tokenNullOrInvalidResponse(token);
        }

        if(!user.getRole().equalsIgnoreCase("designer")){
            return new Response("99","Operation Failed, Not a designer",null);
        }

        List<ProductRespDTO> products=searchService.designerProductsFuzzySearch(search,designerRepository.findByUser(user));

        return new Response("00","Operation Successful",products);

    }


    @GetMapping(value = "/{productId}/getmandatorymeasurements")
    public Response getMandatoryMeasurements(@PathVariable Long productId){
        return new Response("00","Operation Successful",measurementService.getMandatoryMeasurement(productId));
    }


}
