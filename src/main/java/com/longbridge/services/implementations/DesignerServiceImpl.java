package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.Util.SMSAlertUtil;
import com.longbridge.Util.SendEmailAsync;
import com.longbridge.Util.UserUtil;
import com.longbridge.dto.*;
import com.longbridge.exception.ObjectNotFoundException;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.security.JwtUser;
import com.longbridge.security.repository.UserRepository;
import com.longbridge.services.CloudinaryService;
import com.longbridge.services.DesignerService;
import com.longbridge.services.TokenService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;


@Service
public class DesignerServiceImpl implements DesignerService{

    @Autowired
    DesignerRepository designerRepository;

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private GeneralUtil generalUtil;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private SizeGuideRepository sizeGuideRepository;


    @Autowired
    private UserUtil userUtil;

    @Autowired
    public DesignerServiceImpl(GeneralUtil generalUtil) {
        this.generalUtil = generalUtil;
    }


    @Autowired
    ItemRepository itemRepository;

    @Autowired
    TokenService tokenService;


    @Override
    public List<DesignerDTO> getDesigners() {
        try {
            List<Designer> designerList = designerRepository.findAll();
            return generalUtil.convDesignerEntToDTOs(designerList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public DesignerDTO getDesignerById(Long designerId) {
        try {
            Designer designer = designerRepository.findOne(designerId);
            return generalUtil.convertDesigner2EntToDTO(designer);
        } catch (Exception e){
            e.printStackTrace();
           throw new WawoohException();
        }

    }


    @Override
    public Designer getDesignrById(Long designerId) {
            return designerRepository.findOne(designerId);

    }

    @Override
    public Response updateEmailAddress( UserEmailTokenDTO userEmailTokenDTO, Device device) {
        User userTemp = getCurrentUser();
        UserEmailTokenDTO userEmailTokenDTO1 = new UserEmailTokenDTO();
        userEmailTokenDTO1.setEmail(userTemp.getEmail());
        userEmailTokenDTO1.setToken(userEmailTokenDTO.getToken());
        if(userEmailTokenDTO.getToken() == null){

            User usert = userRepository.findByEmail(userEmailTokenDTO.getEmail());
            if(usert == null){
                userUtil.sendToken(userTemp.getEmail());

                userUtil.sendTokenAsMail(userTemp.getEmail());

                return new Response("50", "Verify email change", null);
            }else{
                return new Response("99", "Email already registered to another user", null);
            }
        }else{
            Response response = tokenService.validateToken(userEmailTokenDTO1, device);
            if(Objects.equals(response.getStatus(), "00")){
                userTemp.setEmail(userEmailTokenDTO.getEmail());
                userRepository.save(userTemp);
                return new Response("00", "Email changed successfullly", null);
            }else{ return new Response("99", "Invalid Token", null); }
        }
    }

    @Override
    public void updateDesignerPersonalInformation(UserDTO user) {
        try {
            User userTemp = getCurrentUser();
            if(userTemp.getRole().equalsIgnoreCase("designer")){
                userTemp.setFirstName(user.getFirstName());
                userTemp.setLastName(user.getLastName());
                userTemp.setGender(user.getGender());
                userTemp.setDateOfBirth(user.dateOfBirth);
                userRepository.save(userTemp);
            }else{
                throw new WawoohException();
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public SizeGuide updateDesignerBusinessInformation(UserDTO user) {
        try {
            SizeGuide sizeGuide = null;
            User userTemp = getCurrentUser();
            if(userTemp.getRole().equalsIgnoreCase("designer")){
                User currentUser = userTemp;
                Designer currentDesigner = designerRepository.findByUser(currentUser);
                currentDesigner.setAddress(user.getDesigner().address);
                currentDesigner.setCity(user.getDesigner().city);
                currentDesigner.setState(user.getDesigner().state);
                currentDesigner.setCountry(user.getDesigner().country);
                currentDesigner.setStoreName(user.getDesigner().storeName);
                currentDesigner.setStoreId(user.getDesigner().getStoreName().substring(0,3)+currentDesigner.id);
                currentDesigner.setLocalGovt(user.getDesigner().localGovt);
                currentDesigner.setSizeGuideFlag(user.getDesigner().sizeGuideFlag==null?"N":user.getDesigner().sizeGuideFlag);
                currentDesigner.setRegisteredFlag(user.getDesigner().registeredFlag);
                currentDesigner.setRegistrationProgress(20);
                currentDesigner.setThreshold(user.getDesigner().threshold);

                if(currentDesigner.getSizeGuide()!=null){
                    sizeGuide = currentDesigner.getSizeGuide();
                }else{
                    sizeGuide = new SizeGuide();
                }

                if(currentDesigner.getSizeGuideFlag().equalsIgnoreCase("Y")){
//                    if(currentDesigner.getSizeGuide() == null){
//                        sizeGuide = new SizeGuide();
//                        sizeGuideRepository.save(sizeGuide);
//                        currentDesigner.setSizeGuide(sizeGuide);
//                    }
                    if(user.getDesigner().femaleSizeGuide != null){
                        if(!isUrl(user.getDesigner().femaleSizeGuide)){
                            if(sizeGuide.getFemaleSizeGuide() != null){
                                cloudinaryService.deleteFromCloud(sizeGuide.getFemaleSizeGuidePublicId(),sizeGuide.getFemaleSizeGuide());
                            }
                            try {
                                String fileName = userTemp.getEmail().substring(0, 3) + generalUtil.getCurrentTime();
                                String base64Img = user.getDesigner().femaleSizeGuide;

                                CloudinaryResponse c = cloudinaryService.uploadToCloud(base64Img, fileName, "designersizeguides");
                                sizeGuide.setFemaleSizeGuide(c.getUrl());
                                sizeGuide.setFemaleSizeGuidePublicId(c.getPublicId());
                            }catch (Exception e){
                                e.printStackTrace();
                                throw new WawoohException();
                            }
                        }
                    }

                    if(user.getDesigner().maleSizeGuide != null){
                        if(!isUrl(user.getDesigner().maleSizeGuide)){
                            if(sizeGuide.getMaleSizeGuide() != null){
                                cloudinaryService.deleteFromCloud(sizeGuide.getMaleSizeGuidePublicId(), sizeGuide.getMaleSizeGuide());
                            }
                            try {
                                String fileName = userTemp.getEmail().substring(0, 3) + generalUtil.getCurrentTime();

                                String base64Img = user.getDesigner().maleSizeGuide;

                                CloudinaryResponse c = cloudinaryService.uploadToCloud(base64Img, fileName, "designersizeguides");
                                sizeGuide.setMaleSizeGuide(c.getUrl());
                                sizeGuide.setMaleSizeGuidePublicId(c.getPublicId());
                            }catch (Exception e){
                                e.printStackTrace();
                                throw new WawoohException();
                            }
                        }
                    }
                }else{
//                    if(currentDesigner.getSizeGuide() == null){
//                        sizeGuide = new SizeGuide();
//
//                        sizeGuideRepository.save(sizeGuide);
//                        currentDesigner.setSizeGuide(sizeGuide);
//                    }
                    sizeGuide.setMaleSizeGuide(user.getDesigner().maleSizeGuide);
                    sizeGuide.setFemaleSizeGuide(user.getDesigner().femaleSizeGuide);

                }

                sizeGuide.setDesigner(currentDesigner);
                sizeGuideRepository.save(sizeGuide);
                currentDesigner.setSizeGuide(sizeGuide);

                currentDesigner.setRegisteredFlag(user.getDesigner().registeredFlag);
                currentDesigner.setRegistrationNumber(user.getDesigner().registeredFlag);
                if(!isUrl(user.getDesigner().registrationDocument)){

                    if(currentDesigner.getRegistrationDocument() != null){
                        if(currentDesigner.getRegistrationDocument().equalsIgnoreCase("")){
                            cloudinaryService.deleteFromCloud(currentDesigner.getRegistrationDocumentPublicId(), currentDesigner.getRegistrationDocument());
                        }
                    }
                    try {
                        String fileName = userTemp.getEmail().substring(0, 3) + generalUtil.getCurrentTime();
                        String base64Img = user.getDesigner().registrationDocument;

                        CloudinaryResponse c = cloudinaryService.uploadToCloud(base64Img, fileName, "designerregistrationdocument");
                        currentDesigner.setRegistrationDocument(c.getUrl());
                        currentDesigner.setRegistrationDocumentPublicId(c.getPublicId());
                    }catch (Exception e){
                        e.printStackTrace();
                        throw new WawoohException();
                    }
                }
                designerRepository.save(currentDesigner);
                return sizeGuide;
            }else{
                throw new WawoohException();
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void updateDesignerAccountInformation(UserDTO user) {
        try {
            User userTemp = getCurrentUser();
            if(userTemp.getRole().equalsIgnoreCase("designer")){
                Designer currentDesigner = designerRepository.findByUser(userTemp);

                currentDesigner.setAccountNumber(user.getDesigner().accountNumber);
                currentDesigner.setBankName(user.getDesigner().bankName);
                currentDesigner.setCurrency(user.getDesigner().currency);
                currentDesigner.setAccountName(user.getDesigner().accountName);
                currentDesigner.setSwiftCode(user.getDesigner().swiftCode);
                currentDesigner.setSwiftCode(user.getDesigner().swiftCode);
                designerRepository.save(currentDesigner);

            }else{
                throw new WawoohException();
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void updateDesignerInformation(UserDTO user) {
        updateDesignerPersonalInformation( user);
        updateDesignerBusinessInformation(user);
        updateDesignerAccountInformation(user);
    }

    @Override
    public void  updateDesignerLogo(DesignerDTO passedDesigner) {
        try {
            User userTemp = getCurrentUser();
            System.out.println(passedDesigner.id);
            if(passedDesigner.id != null) {

                if(passedDesigner.logo != null) {
                    Designer d = designerRepository.findByUser(userTemp);
                    if(d.getPublicId() != null) {
                        cloudinaryService.deleteFromCloud(d.getPublicId(), d.getLogo());
                    }
                    try {
                        String fileName = userTemp.getEmail().substring(0, 3) + generalUtil.getCurrentTime();
                        String base64Img = passedDesigner.logo;

                        CloudinaryResponse c = cloudinaryService.uploadToCloud(base64Img,fileName,"designerlogos");
                        d.setLogo(c.getUrl());
                        d.setPublicId(c.getPublicId());

                        designerRepository.save(d);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        throw new WawoohException();
                    }
                }
            }
            else {
                throw new WawoohException();
            }

        } catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public void  updateDesignerBanner(DesignerDTO passedDesigner) {
        try {
            User userTemp = getCurrentUser();
            if(passedDesigner.id != null) {

                if(passedDesigner.banner != null) {
                    Designer d = designerRepository.findByUser(userTemp);
                    if(d.getBannerPublicId() != null) {
                        cloudinaryService.deleteFromCloud(d.getBannerPublicId(), d.getBanner());
                    }
                    try {
                        String fileName = userTemp.getEmail().substring(0, 3) + generalUtil.getCurrentTime();
                        String base64Img = passedDesigner.banner;

                        CloudinaryResponse c = cloudinaryService.uploadToCloud(base64Img,fileName,"designerlogos");
                        d.setLogo(c.getUrl());
                        d.setBannerPublicId(c.getPublicId());

                        designerRepository.save(d);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        throw new WawoohException();
                    }
                }
            }
            else {
                throw new WawoohException();
            }

        } catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public void rateDesigner(DesignerRatingDTO ratingDTO) {
        try {
            int ratingCount;
            int userCount;
            Double average;

            User user = designerRepository.findOne(ratingDTO.designerId).getUser();
            Rating rating=ratingRepository.findByUser(user);
            if (rating != null){
                ratingCount= rating.getRatingCount();
                ratingCount = ratingCount + ratingDTO.rating;
                rating.setRatingCount(ratingCount);

                userCount = rating.getUserCount();
                userCount = userCount + 1;
                rating.setUserCount(userCount);

                average = (double)ratingCount/userCount;
                rating.setAverageRating(average);
                ratingRepository.save(rating);

            }
            else {
             Rating rating1 = new Rating();
             rating1.setUser(user);
             rating1.setUserCount(1);
             rating1.setRatingCount(ratingDTO.rating);

             average = (double)ratingDTO.rating/rating1.getUserCount();
             rating1.setAverageRating(average);
             ratingRepository.save(rating1);
            }


        } catch (Exception e){
            e.printStackTrace();
          throw new WawoohException();
        }
    }

    @Override
    public void deleteDesigner(Long id) {

        try {
            Designer designer = designerRepository.findOne(id);
            designer.setDelFlag("Y");
            designerRepository.save(designer);


        } catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public void updateDesignerStatus(Long id,String status) {
        try {
            Designer designer = designerRepository.findOne(id);
            designer.setStatus(status);
            designer.getProducts().forEach(products -> {
                products.getProductStatuses().setDesignerStatus(status);
            });
            designerRepository.save(designer);
        } catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public DesignerDTO getDesigner() {
        try {
            Designer designer = designerRepository.findByUser(getCurrentUser());
            DesignerDTO dto = generalUtil.convertDesigner2EntToDTO(designer);
            return dto;
        } catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public DesignerDTO getDesignerWithSalesChart() {
        try {
            Designer designer = designerRepository.findByUser(getCurrentUser());
            DesignerDTO dto = generalUtil.convertDesigner2EntToDTO(designer);
            Date startDate= DateUtils.ceiling(DateUtils.addMonths(new Date(),-6),Calendar.MONTH);
            List<ISalesChart> salesCharts = itemRepository.getTotalSales(designer.id,startDate,"P");
            dto.setSalesChart(salesCharts);
            return dto;
        } catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }


    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        return jwtUser.getUser();
    }

    @Override
    public DesignerDTO getDesignerByStoreName(String storeName) {
        try {
            Designer designer = designerRepository.findByStoreName(storeName);
            DesignerDTO dto = generalUtil.convertDesignerEntToDTO(designer);
            return dto;
        } catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    public boolean isUrl(String url){
        try {
            new URL(url).toURI();
            return true;
        }catch (Exception e){
            return false;
        }
    }


}
