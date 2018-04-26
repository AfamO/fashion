package com.longbridge.services;

import com.longbridge.dto.*;
import com.longbridge.models.*;
import com.longbridge.respbodydto.ProductRespDTO;

import java.util.List;

/**
 * Created by Longbridge on 06/11/2017.
 */
public interface ProductService {

    ProductRespDTO getProductById(Long id);

    ProductRespDTO getProductByIdWithReviews(Long id);

    void addProduct(ProductDTO productDTO, Designer designer);

    void updateProductVisibility(Long id, String status);

    void updateProductStatus(Long id, String status);

    void updateProduct(ProductDTO productDTO, Designer designer);

    void updateProductImages(ProdPicReqDTO pictureDTO);

    void updateArtWorkImages(ArtPicReqDTO artPictureDTO);

    void updateMaterialImages(MatPicReqDTO materialPictureDTO);

    void deleteProduct(Long id);

    void deleteProductImages(Long id);

    void deleteArtWorkImages(Long id);

    void deleteMaterialImages(Long id);

    List<ProductRespDTO> getProductsByDesigner(Long designerId);

    List<ProductRespDTO> getAllProducts(PageableDetailsDTO pageableDetailsDTO);

    List<ProductRespDTO> filterProductsByPrice(FilterPriceDTO filterPriceDTO);

    List<ProductRespDTO> getProductsBySubCatId(ProdSubCategoryDTO p);

    List<ProductRespDTO> getTagProducts(PicTagDTO p);

    List<ProductRespDTO> getTopProducts();

    List<ProductRespDTO> getFeaturedProducts();

    List<ProductRespDTO> getDesignerProductsBySubCatId(ProdSubCategoryDTO p, User user);

    void addCategory(CategoryDTO categoryDTO);

    void addSubCategory(SubCategoryDTO subCategoryDTO);

    Category getCategoryById(Long id);

    void addPictureTag(PictureTagDTO pictureTagDTO);

    List<PicTagDTO> getPictureTags(Long eventPictureId);

    void addStyle(StyleDTO styleDTO);

    List<Category> getAllCategories();

    List<SubCategory> getSubCategories(Long categoryId);

    List<Style> getStyles(Long subCategoryId);

    List<EventPicturesDTO> getUntaggedPictures(PageableDetailsDTO pageableDetailsDTO);

    List<EventPicturesDTO> getTaggedPictures(PageableDetailsDTO pageableDetailsDTO);

    List<EventPicturesDTO> getUntaggedPicturesByEvents(PageableDetailsDTO pageableDetailsDTO, String search);

    List<EventPicturesDTO> getTaggedPicturesByEvents(PageableDetailsDTO pageableDetailsDTO, String search);

    int getTotalProducts(User user);

}
