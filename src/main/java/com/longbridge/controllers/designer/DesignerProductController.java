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
@RequestMapping("/fashion/secure/designer/product")
public class DesignerProductController {
    @Autowired
    ProductService productService;

    @Autowired
    HibernateSearchService searchService;
    @Value("${search.url}")
    private String host_api_url; //host_api_url

    @PostMapping(value = "/addproduct")
    public Object addProduct(@RequestBody ProductDTO productDTO){
        Map<String,Object> responseMap = new HashMap();
        productService.addProduct(productDTO,host_api_url);
        responseMap.put("success","success");
        return new Response("00","Operation Successful",responseMap);
    }


    @PostMapping(value = "/updateproduct")
    public Object updateProduct(@RequestBody ProductDTO productDTO){
        Map<String,Object> responseMap = new HashMap();
        productService.updateProduct(productDTO);
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", responseMap);

    }

    @PostMapping(value = "/updateproductstock")
    public Object updateProductStock(@RequestBody ProductDTO productDTO){
        Map<String,Object> responseMap = new HashMap();
        productService.updateProductStock(productDTO);
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", responseMap);

    }



    @PostMapping(value = "/updateproductimage")
    public Object updateProductImage(@RequestBody ProductDTO p){
        Map<String,Object> responseMap = new HashMap();

        productService.updateProductImages(p);
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", responseMap);

    }

    @PostMapping(value = "/updateproductartwork")
    public Object updateProdArtMaterial(@RequestBody ArtPicReqDTO artPicReqDTO){
        Map<String,Object> responseMap = new HashMap();
        productService.updateArtWorkImages(artPicReqDTO);
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", responseMap);

    }

    @PostMapping(value = "/updateproductmaterial")
    public Object updateProdArtMaterial(@RequestBody MatPicReqDTO matPicReqDTO){
        Map<String,Object> responseMap = new HashMap();
        productService.updateMaterialImages(matPicReqDTO);
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", responseMap);

    }



    @GetMapping(value = "/{id}/deleteproduct")
    public Object deleteProduct(@PathVariable Long id){
        Map<String,Object> responseMap = new HashMap();
        productService.deleteProduct(id);
        responseMap.put("success","success");
        return new Response("00","Operation Successful",responseMap);

    }



    @GetMapping(value = "/{id}/deleteproductimage")
    public Object deleteProductImages(@PathVariable Long id){
        Map<String,Object> responseMap = new HashMap();
        productService.deleteProductImage(id);
        responseMap.put("success","success");
        return new Response("00","Operation Successful",responseMap);

    }

    @PostMapping(value = "/deleteproductimage")
    public Object deleteProductImages(@RequestBody ProductPictureIdListDTO pictureIdListDTO){
        Map<String,Object> responseMap = new HashMap();
        productService.deleteProductImages(pictureIdListDTO);
        responseMap.put("success","success");
        return new Response("00","Operation Successful",responseMap);

    }


    @PostMapping(value = "/deleteartworkimage")
    public Object deleteArtWorkImages(@RequestBody ProductPictureIdListDTO pictureIdListDTO){
        Map<String,Object> responseMap = new HashMap();
        productService.deleteArtWorkImages(pictureIdListDTO);
        responseMap.put("success","success");
        return new Response("00","Operation Successful",responseMap);

    }

    @PostMapping(value = "/deletematerialimage")
    public Object deleteMaterialImages(@RequestBody ProductPictureIdListDTO pictureIdListDTO){
        Map<String,Object> responseMap = new HashMap();
        productService.deleteMaterialImages(pictureIdListDTO);
        responseMap.put("success","success");
        return new Response("00","Operation Successful",responseMap);

    }



    @GetMapping(value = {"/{id}/designer/getproductbyid"})
    public Object getDesignerProductById(@PathVariable Long id){

        ProductRespDTO products = productService.getDesignerProductById(id);

        return new Response("00","Operation Successful",products);


    }


    @GetMapping(value = "/getdesignerproducts")
    public Object getProductsByDesigner(){
        List<ProductRespDTO> products= productService.getProductsByDesigner();
        return new Response("00","Operation Successful",products);

    }



    @GetMapping(value = "/{id}/productvisibility/{status}")
    public Object updateProductVisibility(@PathVariable Long id, @PathVariable String status){
        Map<String,Object> responseMap = new HashMap();
        productService.updateProductVisibility(id,status);
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", responseMap);

    }


    @GetMapping(value = "/{search}/designerprodsearch")
    public Response searchProductsByDesigner(@PathVariable String search){
        List<ProductRespDTO> products=searchService.designerProductsFuzzySearch(search);
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
