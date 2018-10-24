/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longbridge.services;

import com.longbridge.dto.ArtPicReqDTO;
import com.longbridge.dto.EventPicturesDTO;
import com.longbridge.dto.MatPicReqDTO;
import com.longbridge.dto.PageableDetailsDTO;
import com.longbridge.dto.PictureTagDTO;
import com.longbridge.dto.ProductDTO;
import com.longbridge.dto.ProductPictureIdListDTO;
import com.longbridge.dto.TagDTO;
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
    void updateArtWorkImages(ArtPicReqDTO artPictureDTO);

    @PreAuthorize("hasAuthority('ROLE_DESIGNER')")
    void updateMaterialImages(MatPicReqDTO materialPictureDTO);
    
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
