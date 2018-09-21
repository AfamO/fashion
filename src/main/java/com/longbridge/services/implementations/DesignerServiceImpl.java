package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.Util.UserUtil;
import com.longbridge.dto.*;
import com.longbridge.exception.ObjectNotFoundException;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.respbodydto.ProductRespDTO;
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

/**
 * Created by Longbridge on 15/11/2017.
 */
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
            Designer designer = designerRepository.findOne(designerId);
            return designer;
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
                return new Response("50", "Verify email change", null);
            }else{
                return new Response("99", "Email already registered to another user", null);
            }
        }else{
            Response response = tokenService.validateToken(userEmailTokenDTO1, device);
            if(Objects.equals(response.status, "00")){
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
    public void updateDesignerBusinessInformation(UserDTO user) {
        try {
            User userTemp = getCurrentUser();
            if(userTemp.getRole().equalsIgnoreCase("designer")){
                User currentUser = userTemp;
                Designer currentDesigner = designerRepository.findByUser(currentUser);

                currentDesigner.setAddress(user.getDesignerDTO().address);
                currentDesigner.setCity(user.getDesignerDTO().city);
                currentDesigner.setState(user.getDesignerDTO().state);
                currentDesigner.setCountry(user.getDesignerDTO().country);
                currentDesigner.setStoreName(user.getDesignerDTO().storeName);
                currentDesigner.setLocalGovt(user.getDesignerDTO().localGovt);
                currentDesigner.setSizeGuideFlag(user.getDesignerDTO().sizeGuideFlag);
                currentDesigner.setRegisteredFlag(user.getDesignerDTO().registeredFlag);

                if(currentDesigner.getSizeGuideFlag().equalsIgnoreCase("Y")){

                    if(currentDesigner.getSizeGuide() == null){
                        SizeGuide currentSizeGuide = new SizeGuide();
                        sizeGuideRepository.save(currentSizeGuide);
                        currentDesigner.setSizeGuide(currentSizeGuide);
                    }

                    SizeGuide currentSizeGuide = currentDesigner.getSizeGuide();

                    if(user.getDesignerDTO().femaleSizeGuide != null){
                        if(!isUrl(user.getDesignerDTO().femaleSizeGuide)){
                            if(currentSizeGuide.getFemaleSizeGuide() != null){
                                cloudinaryService.deleteFromCloud(currentSizeGuide.getFemaleSizeGuidePublicId(), currentSizeGuide.getFemaleSizeGuide());
                            }

                            try {
                                String fileName = userTemp.getEmail().substring(0, 3) + generalUtil.getCurrentTime();
                                String base64Img = user.getDesignerDTO().femaleSizeGuide;

                                CloudinaryResponse c = cloudinaryService.uploadToCloud(base64Img, fileName, "designersizeguides");
                                currentSizeGuide.setFemaleSizeGuide(c.getUrl());
                                currentSizeGuide.setFemaleSizeGuidePublicId(c.getPublicId());
                            }catch (Exception e){
                                e.printStackTrace();
                                throw new WawoohException();
                            }
                        }
                    }

                    if(user.getDesignerDTO().maleSizeGuide != null){
                        if(!isUrl(user.getDesignerDTO().maleSizeGuide)){
                            if(currentSizeGuide.getMaleSizeGuide() != null){
                                cloudinaryService.deleteFromCloud(currentSizeGuide.getMaleSizeGuidePublicId(), currentSizeGuide.getMaleSizeGuide());
                            }

                            try {
                                String fileName = userTemp.getEmail().substring(0, 3) + generalUtil.getCurrentTime();
                                String base64Img = user.getDesignerDTO().maleSizeGuide;

                                CloudinaryResponse c = cloudinaryService.uploadToCloud(base64Img, fileName, "designersizeguides");
                                currentSizeGuide.setMaleSizeGuide(c.getUrl());
                                currentSizeGuide.setMaleSizeGuidePublicId(c.getPublicId());
                            }catch (Exception e){
                                e.printStackTrace();
                                throw new WawoohException();
                            }
                        }
                    }
                }else{
                    if(currentDesigner.getSizeGuide() == null){
                        SizeGuide currentSizeGuide = new SizeGuide();
                        sizeGuideRepository.save(currentSizeGuide);
                        currentDesigner.setSizeGuide(currentSizeGuide);
                    }

                    SizeGuide currentSizeGuide = currentDesigner.getSizeGuide();

                    currentSizeGuide.setMaleSizeGuide(user.getDesignerDTO().maleSizeGuide);
                    currentSizeGuide.setFemaleSizeGuide(user.getDesignerDTO().femaleSizeGuide);
                }

                currentDesigner.setRegisteredFlag(user.getDesignerDTO().registeredFlag);
                currentDesigner.setRegistrationNumber(user.getDesignerDTO().registeredFlag);
                if(!isUrl(user.getDesignerDTO().registrationDocument)){
                    if(currentDesigner.getRegistrationDocument() != null){
                        if(currentDesigner.getRegistrationDocument().equalsIgnoreCase("")){
                            cloudinaryService.deleteFromCloud(currentDesigner.getRegistrationDocumentPublicId(), currentDesigner.getRegistrationDocument());
                        }
                    }

                    try {
                        String fileName = userTemp.getEmail().substring(0, 3) + generalUtil.getCurrentTime();
                        String base64Img = user.getDesignerDTO().registrationDocument;

                        CloudinaryResponse c = cloudinaryService.uploadToCloud(base64Img, fileName, "designerregistrationdocument");
                        currentDesigner.setRegistrationDocument(c.getUrl());
                        currentDesigner.setRegistrationDocumentPublicId(c.getPublicId());
                    }catch (Exception e){
                        e.printStackTrace();
                        throw new WawoohException();
                    }
                }

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
    public void updateDesignerAccountInformation(UserDTO user) {
        try {
            User userTemp = getCurrentUser();
            if(userTemp.getRole().equalsIgnoreCase("designer")){
                Designer currentDesigner = designerRepository.findByUser(userTemp);
                currentDesigner.setAccountNumber(user.getDesignerDTO().accountNumber);
                currentDesigner.setBankName(user.getDesignerDTO().bankName);
                currentDesigner.setCurrency(user.getDesignerDTO().currency);
                currentDesigner.setAccountName(user.getDesignerDTO().accountName);
                currentDesigner.setSwiftCode(user.getDesignerDTO().swiftCode);
                currentDesigner.setSwiftCode(user.getDesignerDTO().swiftCode);

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
            if(passedDesigner.id != null) {

                if(passedDesigner.logo != null) {
                    Designer d = designerRepository.findByUser(userTemp);
                    if(d.getPublicId() != null) {
                        cloudinaryService.deleteFromCloud(d.getPublicId(), d.getLogo());
                    }
                    try {
                        String fileName = userTemp.getEmail().substring(0, 3) + generalUtil.getCurrentTime();
                        String base64Img = passedDesigner.logo;
//                        byte[] imgBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Img);
//                        ByteArrayInputStream bs = new ByteArrayInputStream(imgBytes);
//                        File imgfilee = new File(designerLogoFolder + fileName);
                        CloudinaryResponse c = cloudinaryService.uploadToCloud(base64Img,fileName,"designerlogos");
                        d.setLogo(c.getUrl());
                        d.setPublicId(c.getPublicId());
//                        FileOutputStream f = new FileOutputStream(imgfilee);
//                        int rd = 0;
//                        final byte[] byt = new byte[1024];
//                        while ((rd = bs.read(byt)) != -1) {
//                            f.write(byt, 0, rd);
//                        }
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
            //Long userId = designerRepository.findOne(ratingDTO.designerId).userId;
            //User user = userRepository.findOne(userId);
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
                products.setDesignerStatus("D");
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
