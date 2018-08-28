package com.longbridge.services;

import com.longbridge.dto.*;
import com.longbridge.models.*;
import com.longbridge.respbodydto.ProductRespDTO;

import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
public interface ProductService {

    ProductRespDTO getProductById(Long id,User user, boolean reviews);

    ProductRespDTO getDesignerProductById(Long id,User user);

    void addProduct(ProductDTO productDTO, Designer designer);

    void updateProductVisibility(Long id, String status);

    void updateProductStatus(Long id, String status);

    void sponsorProduct(Long id, String status);

    void updateProduct(ProductDTO productDTO, Designer designer);

    void updateProductStock(ProductDTO productDTO, Designer designer);

    void updateProductImages(ProductDTO p);

    void updateArtWorkImages(ArtPicReqDTO artPictureDTO);

    void updateMaterialImages(MatPicReqDTO materialPictureDTO);

    void deleteProduct(Long id);

    void deleteProductImages(ProductPictureIdListDTO pictureIdListDTO);

    void deleteProductImage(Long id);

    void deleteArtWorkImages(ProductPictureIdListDTO pictureIdListDTO);

    void deleteMaterialImages(ProductPictureIdListDTO pictureIdListDTO);

    List<ProductRespDTO> getProductsByDesigner(Long designerId);

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

    List<Category> getAllCategories();

    List<SubCategory> getSubCategories(Long categoryId);

    List<SubCategory> getAllSubCategories();

    List<SubCategory> getSubCategoriesByProductType(SubCategoryDTO subCategoryDTO);

    List<Style> getStyles(Long subCategoryId);

    List<EventPicturesDTO> getUntaggedPictures(PageableDetailsDTO pageableDetailsDTO);

    List<EventPicturesDTO> getTaggedPictures(PageableDetailsDTO pageableDetailsDTO);

    List<EventPicturesDTO> getUntaggedPicturesByEvents(Long id);

    List<EventPicturesDTO> getTaggedPicturesByEvents(Long id);

    int getTotalProducts(User user);

}
