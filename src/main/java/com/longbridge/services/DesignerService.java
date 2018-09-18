package com.longbridge.services;

import com.longbridge.dto.*;
import com.longbridge.models.Designer;
import com.longbridge.models.Response;
import com.longbridge.models.User;
import org.springframework.mobile.device.Device;

import java.util.List;

/**
 * Created by Longbridge on 15/11/2017.
 */
public interface DesignerService {

    List<DesignerDTO> getDesigners();

    Response updateEmailAddress(User userTemp, UserEmailTokenDTO userEmailTokenDTO, Device device);

    void updateDesignerPersonalInformation(User userTemp, UserDTO user);

    void updateDesignerBusinessInformation(User userTemp, UserDTO user);

    void updateDesignerAccountInformation(User userTemp, UserDTO user);

    void updateDesignerInformation(User userTemp, UserDTO user);

    void updateDesignerLogo(User userTemp, DesignerDTO passedDesigner);

    void rateDesigner(DesignerRatingDTO ratingDTO);

    void deleteDesigner(Long id);

    void updateDesignerStatus(Long id,String status);

    DesignerDTO getDesignerById(Long designerId);

//    List<SalesChart> getSalesChart(Long designerId);

    Designer getDesignrById(Long designerId);

    DesignerDTO getDesigner(User user);

    DesignerDTO getDesigner(User user, MonthsDTO months);

    DesignerDTO getDesigner(MonthsDTO months);

    DesignerDTO getDesignerByStoreName(String storeName);


}
