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

    void addProduct(ProductDTO productDTO, Designer designer);

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

    List<ProductRespDTO> getProductsBySubCatId(ProdSubCategoryDTO p);

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

    //Boolean nameExists(String fileName);

}
