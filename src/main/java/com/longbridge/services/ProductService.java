package com.longbridge.services;

import com.longbridge.dto.*;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.respbodydto.ProductRespDTO;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
public interface ProductService {

    ProductRespDTO getProductById(Long id,boolean reviews);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    ProductRespDTO getDesignerProductById(Long id);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    String addProduct(ProductDTO productDTO) throws WawoohException;

    void updateProductVisibility(Long id, String status);

    void updateProductStatus(Long id, String status);

    void unVerifyProduct(VerifyDTO verifyDTO);

    void sponsorProduct(Long id, String status);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void updateProduct(ProductDTO productDTO);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void updateProductStock(ProductDTO productDTO);
    
    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void deleteProduct(Long id);
    
    List<ProductRespDTO> getProductsByDesigner(Long designerId);

    List<ProductRespDTO> getProductsByDesigner();

    List<ProductRespDTO> getAllProducts(PageableDetailsDTO pageableDetailsDTO);

    List<ProductRespDTO> getVerifiedProducts(PageableDetailsDTO pageableDetailsDTO);

    List<ProductRespDTO> getUnVerifiedProducts(PageableDetailsDTO pageableDetailsDTO);

    List<ProductRespDTO> getNewProducts(PageableDetailsDTO pageableDetailsDTO);

    //List<ProductRespDTO> getVerifiedProducts(PageableDetailsDTO pageableDetailsDTO);

    List<ProductRespDTO> filterProductsByPrice(FilterProductDTO filterProductDTO);

    List<ProductRespDTO> filterProducts(FilterProductDTO filterProductDTO);

    List<ProductRespDTO> getProductsBySubCatId(ProdSubCategoryDTO p);

    List<ProductRespDTO> getProductsByCatId(ProdSubCategoryDTO p);

    //List<ProductRespDTO> searchProductsBySubCat(String search, ProdSubCategoryDTO p);

    List<ProductRespDTO> getTagProducts(PicTagDTO p);

    List<ProductRespDTO> getTopProducts();

    List<ProductRespDTO> getFreqBoughtProducts();

    List<ProductRespDTO> getFeaturedProducts(PageableDetailsDTO pageableDetailsDTO);

    List<ProductRespDTO> getAllFeaturedProducts();

    List<ProductRespDTO> getDesignerProductsBySubCatId(ProdSubCategoryDTO p);

    void addCategory(CategoryDTO categoryDTO);

    void addSubCategory(SubCategoryDTO subCategoryDTO);

    void editSubCategory(SubCategoryDTO subCategoryDTO);

    void deleteSubCategory(Long id);

    void addStyle(StyleDTO styleDTO);

    List<Style> getStyles(Long subCategoryId);

    int getTotalProducts();

   ProductColorStyleDTO getProductAttributesById(Long id);

}
