package com.longbridge.services;

import com.longbridge.dto.DesignerDTO;
import com.longbridge.dto.DesignerRatingDTO;
import com.longbridge.models.Designer;
import com.longbridge.models.User;

import java.util.List;

/**
 * Created by Longbridge on 15/11/2017.
 */
public interface DesignerService {

    List<DesignerDTO> getDesigners();

    void updateDesigner(User userTemp,User passedUser, Designer designer);

    void updateDesignerLogo(User userTemp, Designer passedDesigner);

    void rateDesigner(DesignerRatingDTO ratingDTO);

    void deleteDesigner(Long id);

    Designer getDesignerById(Long designerId);

    Designer getDesignrById(Long designerId);

    DesignerDTO getDesigner(User user);

    DesignerDTO getDesignerByStoreName(String storeName);


}
