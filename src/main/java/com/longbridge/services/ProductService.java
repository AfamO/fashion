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

    ProductRespDTO getProductById(Long id,User user, boolean reviews);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    ProductRespDTO getDesignerProductById(Long id,User user);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void addProduct(ProductDTO productDTO, User user) throws WawoohException;

    void updateProductVisibility(Long id, String status);

    void updateProductStatus(Long id, String status);

    void sponsorProduct(Long id, String status);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void updateProduct(ProductDTO productDTO, User user);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void updateProductStock(ProductDTO productDTO, User user);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void updateProductImages(ProductDTO p);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void updateArtWorkImages(ArtPicReqDTO artPictureDTO);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void updateMaterialImages(MatPicReqDTO materialPictureDTO);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void deleteProduct(Long id);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void deleteProductImages(ProductPictureIdListDTO pictureIdListDTO);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void deleteProductImage(Long id);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void deleteArtWorkImages(ProductPictureIdListDTO pictureIdListDTO);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void deleteMaterialImages(ProductPictureIdListDTO pictureIdListDTO);

    List<ProductRespDTO> getProductsByDesigner(User user);

    List<ProductRespDTO> getAllProducts(PageableDetailsDTO pageableDetailsDTO);

    List<ProductRespDTO> getVerifiedProducts(PageableDetailsDTO pageableDetailsDTO);

    List<ProductRespDTO> getUnVerifiedProducts(PageableDetailsDTO pageableDetailsDTO);

    List<ProductRespDTO> getNewProducts(PageableDetailsDTO pageableDetailsDTO);

    //List<ProductRespDTO> getVerifiedProducts(PageableDetailsDTO pageableDetailsDTO);

    List<ProductRespDTO> filterProductsByPrice(FilterProductDTO filterProductDTO);

    List<ProductRespDTO> filterProducts(FilterProductDTO filterProductDTO);

    List<ProductRespDTO> getProductsBySubCatId(ProdSubCategoryDTO p);

    //List<ProductRespDTO> searchProductsBySubCat(String search, ProdSubCategoryDTO p);

    List<ProductRespDTO> getTagProducts(PicTagDTO p);

    List<ProductRespDTO> getTopProducts();

    List<ProductRespDTO> getFreqBoughtProducts();

    List<ProductRespDTO> getFeaturedProducts();

    List<ProductRespDTO> getDesignerProductsBySubCatId(ProdSubCategoryDTO p, User user);

    void addCategory(CategoryDTO categoryDTO);

    void addSubCategory(SubCategoryDTO subCategoryDTO);

    void deleteSubCategory(Long id);

    void addPictureTag(PictureTagDTO pictureTagDTO);

    void deletePictureTag(Long id);

    TagDTO getPictureTagById(Long id);

    PictureTagDTO getPictureTags(Long eventPictureId);

    void addStyle(StyleDTO styleDTO);


    List<Style> getStyles(Long subCategoryId);

    List<EventPicturesDTO> getUntaggedPictures(PageableDetailsDTO pageableDetailsDTO);

    List<EventPicturesDTO> getTaggedPictures(PageableDetailsDTO pageableDetailsDTO);

    List<EventPicturesDTO> getUntaggedPicturesByEvents(Long id);

    List<EventPicturesDTO> getTaggedPicturesByEvents(Long id);

    int getTotalProducts(User user);

   ProductAttributeDTO getProductAttributesById(Long id);

}
