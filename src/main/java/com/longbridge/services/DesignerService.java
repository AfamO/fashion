package com.longbridge.services;

import com.longbridge.dto.*;
import com.longbridge.models.Designer;
import com.longbridge.models.Response;
import com.longbridge.models.SizeGuide;
import com.longbridge.models.User;
import org.springframework.mobile.device.Device;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

/**
 * Created by Longbridge on 15/11/2017.
 */
public interface DesignerService {

    List<DesignerDTO> getDesigners();

    Response updateEmailAddress(UserEmailTokenDTO userEmailTokenDTO, Device device);

    void updateDesignerPersonalInformation(UserDTO user);

    SizeGuide updateDesignerBusinessInformation(UserDTO user);

    void updateDesignerAccountInformation(UserDTO user);

    void updateDesignerInformation(UserDTO user);

    void updateDesignerLogo(DesignerDTO passedDesigner);

    void updateDesignerBanner(DesignerDTO passedDesigner);

    void rateDesigner(DesignerRatingDTO ratingDTO);

    void deleteDesigner(Long id);

    void updateDesignerStatus(Long id,String status);

    DesignerDTO getDesignerById(Long designerId);

//    List<SalesChart> getSalesChart(Long designerId);

    Designer getDesignrById(Long designerId);

    DesignerDTO getDesigner();

    DesignerDTO getDesignerWithSalesChart();



    DesignerDTO getDesignerByStoreName(String storeName);


}
