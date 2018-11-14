/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longbridge.services;

import com.longbridge.dto.*;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 *
 * @author longbridge
 */
public interface ProductPictureService {
    
    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void updateProductImages(ProductDTO p);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void updateArtWorkImages(BespokeProductDTO bespokeProductDTO);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void updateMaterialImages(BespokeProductDTO BespokeProductDTO);
    
    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void deleteProductImages(ProductPictureIdListDTO pictureIdListDTO);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void deleteProductImage(Long id);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void deleteArtWorkImages(ProductPictureIdListDTO pictureIdListDTO);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void deleteMaterialImages(ProductPictureIdListDTO pictureIdListDTO);
    
    void addPictureTag(PictureTagDTO pictureTagDTO);

    void deletePictureTag(Long id);

    TagDTO getPictureTagById(Long id);

    PictureTagDTO getPictureTags(Long eventPictureId);
    
    List<EventPicturesDTO> getUntaggedPictures(PageableDetailsDTO pageableDetailsDTO);

    List<EventPicturesDTO> getTaggedPictures(PageableDetailsDTO pageableDetailsDTO);

    List<EventPicturesDTO> getUntaggedPicturesByEvents(Long id);

    List<EventPicturesDTO> getTaggedPicturesByEvents(Long id);
    
}
