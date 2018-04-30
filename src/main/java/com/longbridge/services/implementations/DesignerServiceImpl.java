package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.dto.CloudinaryResponse;
import com.longbridge.dto.DesignerDTO;
import com.longbridge.dto.DesignerRatingDTO;
import com.longbridge.exception.ObjectNotFoundException;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.respbodydto.ProductRespDTO;
import com.longbridge.security.repository.UserRepository;
import com.longbridge.services.DesignerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
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

//    @Value("${s.designer.logo.folder}")
//    private String designerLogoFolder;
//
//    @Value("${designer.logo.folder}")
//    private String designerLogoPath;

    @Autowired
    public DesignerServiceImpl(GeneralUtil generalUtil) {
        this.generalUtil = generalUtil;
    }

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ItemRepository itemRepository;

    @Override
    public List<DesignerDTO> getDesigners() {
        try {
            List<Designer> designerList = designerRepository.findAll();
            List<DesignerDTO> designerDTOS=convDesignerEntToDTOs(designerList);
            return designerDTOS;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public DesignerDTO getDesignerById(Long designerId) {
        try {

            Designer designer = designerRepository.findOne(designerId);
            return convertDesigner2EntToDTO(designer);
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
    public void updateDesigner(User userTemp,User passedUser, Designer designer) {
        try {

        if(passedUser.designer != null) {
            User user1 = userRepository.findById(userTemp.id);
            user1.firstName=passedUser.firstName;
            user1.lastName=passedUser.lastName;
            user1.phoneNo=passedUser.phoneNo;
            userRepository.save(user1);
            Designer designer1 = designerRepository.findOne(userTemp.designer.id);
            designer1.storeName=designer.storeName;
            designer1.address=designer.address;
            designerRepository.save(designer1);

            }
            else {
            throw new ObjectNotFoundException();
        }

        } catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public void  updateDesignerLogo(User userTemp, Designer passedDesigner) {
        try {
            if(userTemp.designer.id != null) {

                if(passedDesigner.logo != null) {
                    Designer d = designerRepository.findOne(userTemp.designer.id);
                    if(userTemp.designer.publicId != null) {
                        generalUtil.deleteFromCloud(userTemp.designer.publicId, userTemp.designer.logo);
                    }
                    try {
                        String fileName = userTemp.email.substring(0, 3) + generalUtil.getCurrentTime();
                        String base64Img = passedDesigner.logo;
//                        byte[] imgBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Img);
//                        ByteArrayInputStream bs = new ByteArrayInputStream(imgBytes);
//                        File imgfilee = new File(designerLogoFolder + fileName);
                        CloudinaryResponse c = generalUtil.uploadToCloud(base64Img,fileName,"designerlogos");
                        d.logo = c.getUrl();
                        d.publicId=c.getPublicId();
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
            int ratingCount = 0;
            int userCount = 0;
            Double average = 0.0;
            //Long userId = designerRepository.findOne(ratingDTO.designerId).userId;
            //User user = userRepository.findOne(userId);
            User user = designerRepository.findOne(ratingDTO.designerId).user;
            Rating rating=ratingRepository.findByUser(user);
            if (rating != null){
                ratingCount= rating.ratingCount;
                ratingCount = ratingCount + ratingDTO.rating;
                rating.ratingCount = ratingCount;

                userCount = rating.userCount;
                userCount = userCount + 1;
                rating.userCount = userCount;

                average = (double)ratingCount/userCount;
                rating.averageRating = average;
                ratingRepository.save(rating);

            }
            else {
             Rating rating1 = new Rating();
             rating1.user = user;
             rating1.userCount = 1;
             rating1.ratingCount = ratingDTO.rating;

             average = (double)ratingDTO.rating/rating1.userCount;
             rating1.averageRating=average;
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
            designer.status = status;
            designerRepository.save(designer);


        } catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public DesignerDTO getDesigner(User user) {
        try {
            Designer designer = user.designer;
            DesignerDTO dto = convertDesigner2EntToDTO(designer);
            return dto;

        } catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }


    @Override
    public DesignerDTO getDesignerByStoreName(String storeName) {
        try {

            Designer designer = designerRepository.findByStoreName(storeName);
            DesignerDTO dto = convertDesignerEntToDTO(designer);
            return dto;

        } catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    private DesignerDTO convertDesigner2EntToDTO(Designer d){
        DesignerDTO dto = new DesignerDTO();
        dto.id=d.id;
//        dto.userId=d.userId;
        dto.userId = d.user.id;
        dto.logo=d.logo;
        dto.storeName=d.storeName;
        dto.address=d.address;
        //User u = userRepository.findById(d.userId);
        User u = d.user;
        dto.firstName=u.firstName;
        dto.lastName=u.lastName;
        dto.phoneNo=u.phoneNo;
        dto.email=u.email;
        dto.gender=u.gender;
        dto.setStatus(d.status);
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dto.createdDate = formatter.format(d.createdOn);
        List<ProductRespDTO> products= generalUtil.convertProdEntToProdRespDTOs(productRepository.findFirst8ByDesignerAndVerifiedFlag(d,"Y"));
        dto.setProducts(products);
        dto.noOfPendingOders= itemRepository.countByDesignerIdAndDeliveryStatus(d.id,"P");
        dto.noOfDeliveredOrders=itemRepository.countByDesignerIdAndDeliveryStatus(d.id,"D");
        dto.noOfCancelledOrders=itemRepository.countByDesignerIdAndDeliveryStatus(d.id, "X");
        dto.noOfConfirmedOrders=itemRepository.countByDesignerIdAndDeliveryStatus(d.id,"C");
        dto.noOfReadyToShipOrders=itemRepository.countByDesignerIdAndDeliveryStatus(d.id,"R");
        dto.noOfShippedOrders=itemRepository.countByDesignerIdAndDeliveryStatus(d.id,"S");
       dto.amountOfPendingOrders=itemRepository.findSumOfPendingOrders(d.id,"P");
        return dto;

    }



    private DesignerDTO convertDesignerEntToDTO(Designer d){
        DesignerDTO dto = new DesignerDTO();
        dto.id=d.id;
//        dto.userId=d.userId;
        dto.userId = d.user.id;
        dto.logo=d.logo;
        dto.storeName=d.storeName;
        dto.address=d.address;
        //User u = userRepository.findById(d.userId);
        User u = d.user;
        dto.firstName=u.firstName;
        dto.lastName=u.lastName;
        dto.phoneNo=u.phoneNo;
        dto.email=u.email;
        dto.gender=u.gender;
        dto.setStatus(d.status);
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dto.createdDate = formatter.format(d.createdOn);
        if(d.status.equalsIgnoreCase("A")) {
            List<ProductRespDTO> products = generalUtil.convertProdEntToProdRespDTOs(productRepository.findFirst8ByDesignerAndVerifiedFlag(d, "Y"));
            dto.setProducts(products);
        }
//        dto.noOfPendingOders= itemRepository.countByDesignerIdAndDeliveryStatus(d.id,"P");
//        dto.noOfDeliveredOrders=itemRepository.countByDesignerIdAndDeliveryStatus(d.id,"D");
//        dto.noOfCancelledOrders=itemRepository.countByDesignerIdAndDeliveryStatus(d.id, "X");
//        dto.noOfConfirmedOrders=itemRepository.countByDesignerIdAndDeliveryStatus(d.id,"C");
//        dto.noOfReadyToShipOrders=itemRepository.countByDesignerIdAndDeliveryStatus(d.id,"R");
//        dto.noOfShippedOrders=itemRepository.countByDesignerIdAndDeliveryStatus(d.id,"S");
//        dto.amountOfPendingOrders=itemRepository.findSumOfPendingOrders(d.id,"P");
        //dto.noOfConfirmedOrders=itemRepository.countByDesignerIdAndDeliveryStatus(d.id,"C");
       // dto.noOfDeliveredOrders=itemRepository.countByDesignerIdAndDeliveryStatus(d.id,"D");
        return dto;

    }




    private List<DesignerDTO> convDesignerEntToDTOs(List<Designer> designers){
        List<DesignerDTO> designerDTOS = new ArrayList<DesignerDTO>();

        for(Designer designer: designers){
            DesignerDTO designerDTO = convertDesignerEntToDTO(designer);
            designerDTOS.add(designerDTO);
        }
        return designerDTOS;
    }
}
