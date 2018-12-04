package com.longbridge.services.implementations;

import com.longbridge.dto.BespokeRequestUpdateDTO;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.Designer;
import com.longbridge.models.User;
import com.longbridge.models.VendorBespokeFormDetails;
import com.longbridge.repository.DesignerRepository;
import com.longbridge.repository.VendorBespokeFormDetailsRepository;
import com.longbridge.security.JwtUser;
import com.longbridge.services.VendorBespokeFormDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Longbridge on 01/11/2018.
 */
@Service
public class VendorBespokeFormDetailsServiceImpl implements VendorBespokeFormDetailsService {
    @Autowired
    VendorBespokeFormDetailsRepository vendorBespokeFormDetailsRepository;

    @Autowired
    DesignerRepository designerRepository;

    @Override
    public String add(VendorBespokeFormDetails vendorBespokeFormDetails) {
        try {
            User user = getCurrentUser();
            vendorBespokeFormDetails.setDesignerId(designerRepository.findByUser(user).id);
            vendorBespokeFormDetailsRepository.save(vendorBespokeFormDetails);
            return "true";
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }



    @Override
    public List<VendorBespokeFormDetails> getAll() {
        List<VendorBespokeFormDetails> vList= new ArrayList<>();
        for (VendorBespokeFormDetails v:vendorBespokeFormDetailsRepository.findAll()) {
            getDesignerDetails(v);
            vList.add(v);
        }
        return vList;
    }


    @Override
    public VendorBespokeFormDetails getVendorBespokeFormDetails(Long id) {
        VendorBespokeFormDetails vendorBespokeFormDetails= vendorBespokeFormDetailsRepository.findOne(id);
        return getDesignerDetails(vendorBespokeFormDetails);
    }

    private VendorBespokeFormDetails getDesignerDetails(VendorBespokeFormDetails vendorBespokeFormDetails) {
        Designer designer = designerRepository.findById(vendorBespokeFormDetails.getDesignerId());
        vendorBespokeFormDetails.setStoreName(designer.getStoreName());
        vendorBespokeFormDetails.setPhoneNumber(designer.getUser().getPhoneNo());
        vendorBespokeFormDetails.setBespokeEligibility(designer.getBespokeEligible());
        vendorBespokeFormDetails.setRegistrationDate(designer.getCreatedOn().toString());
        return vendorBespokeFormDetails;
    }

    @Override
    public void updateBespokeRequest(BespokeRequestUpdateDTO bespokeRequestUpdateDTO) {
        try {

            VendorBespokeFormDetails vendorBespokeFormDetails =  vendorBespokeFormDetailsRepository.findOne(bespokeRequestUpdateDTO.getId());
            Designer designer = designerRepository.findById(vendorBespokeFormDetails.getDesignerId());
            if(bespokeRequestUpdateDTO.getBespokeEligibleFlag().equalsIgnoreCase("N")){
                vendorBespokeFormDetails.setBespokeApplicationRejectReason(bespokeRequestUpdateDTO.getReason());
                vendorBespokeFormDetailsRepository.save(vendorBespokeFormDetails);
            }
            designer.setBespokeEligible(bespokeRequestUpdateDTO.getBespokeEligibleFlag());
            designerRepository.save(designer);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() != "anonymousUser") {
            JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
            return jwtUser.getUser();
        }else {
            return null;
        }

    }
}
