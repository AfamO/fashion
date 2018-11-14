package com.longbridge.controllers.designer;

import com.longbridge.dto.*;

import com.longbridge.models.Product;
import com.longbridge.models.Response;

import com.longbridge.respbodydto.ProductRespDTO;

import com.longbridge.services.ProductPictureService;

import com.longbridge.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

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
    ProductPictureService productPictureService;



    @PostMapping(value = "/addproduct")
    public Object addProduct(@RequestBody ProductDTO productDTO){
        Map<String,Object> responseMap = new HashMap();
        productService.addProduct(productDTO);
        productService.indexProductForSearch();
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

     productPictureService.updateProductImages(p);
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", responseMap);

    }

    @PostMapping(value = "/updateproductartwork")
    public Object updateProdArtWork(@RequestBody BespokeProductDTO bespokeProductDTO){
        Map<String,Object> responseMap = new HashMap();
        productPictureService.updateArtWorkImages(bespokeProductDTO);
        responseMap.put("success", "success");
        return new Response("00", "Operation Successful", responseMap);

    }

    @PostMapping(value = "/updateproductmaterial")
    public Object updateProdArtMaterial(@RequestBody BespokeProductDTO bespokeProductDTO){
        Map<String,Object> responseMap = new HashMap();
        productPictureService.updateMaterialImages(bespokeProductDTO);
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
        productPictureService.deleteProductImage(id);
        responseMap.put("success","success");
        return new Response("00","Operation Successful",responseMap);

    }

    @PostMapping(value = "/deleteproductimage")
    public Object deleteProductImages(@RequestBody ProductPictureIdListDTO pictureIdListDTO){
        Map<String,Object> responseMap = new HashMap();
        productPictureService.deleteProductImages(pictureIdListDTO);
        responseMap.put("success","success");
        return new Response("00","Operation Successful",responseMap);

    }


    @PostMapping(value = "/deleteartworkimage")
    public Object deleteArtWorkImages(@RequestBody ProductPictureIdListDTO pictureIdListDTO){
        Map<String,Object> responseMap = new HashMap();
        productPictureService.deleteArtWorkImages(pictureIdListDTO);
        responseMap.put("success","success");
        return new Response("00","Operation Successful",responseMap);

    }

    @PostMapping(value = "/deletematerialimage")
    public Object deleteMaterialImages(@RequestBody ProductPictureIdListDTO pictureIdListDTO){
        Map<String,Object> responseMap = new HashMap();
        productPictureService.deleteMaterialImages(pictureIdListDTO);
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


//    @GetMapping(value = "/{search}/designerprodsearch")
//    public Response searchProductsByDesigner(@PathVariable String search){
//        List<ProductRespDTO> products=searchService.designerProductsFuzzySearch(search);
//        return new Response("00","Operation Successful",products);
//    }



    @RequestMapping(
            value = "/**",
            method = RequestMethod.OPTIONS
    )
    public ResponseEntity handle() {
        return new ResponseEntity(HttpStatus.OK);
    }

}
