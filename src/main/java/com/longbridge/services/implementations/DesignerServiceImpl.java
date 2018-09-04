package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.dto.*;
import com.longbridge.exception.ObjectNotFoundException;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.respbodydto.ProductRespDTO;
import com.longbridge.security.repository.UserRepository;
import com.longbridge.services.CloudinaryService;
import com.longbridge.services.DesignerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private AddressRepository addressRepository;


    @Autowired
    public DesignerServiceImpl(GeneralUtil generalUtil) {
        this.generalUtil = generalUtil;
    }


    @Autowired
    ItemRepository itemRepository;


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

//
//    @Override
//    public List<SalesChart> getSalesChart(Long designerId) {
//        try {
//
//            Date current = new Date();
//            List<SalesChart> salesCharts = new ArrayList<>();
////            List<Date> months = new ArrayList<>();
////            for(int i= 0; i < 6; i++ ){
////                Calendar cal = Calendar.getInstance();
////                cal.setTime(current);
////                cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH)-i));
////                months.add(cal.getTime());
////            }
//
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(current);
//            cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH)-6));
//
//            Date lastSixMonthDate = cal.getTime();
//            List<Object[]> salesChart = itemRepository.getSalesChart(designerId,lastSixMonthDate,current);
//            for (Object[] s: salesChart) {
//                SalesChart salesChart1 = new SalesChart();
//                salesChart1.setAmount((Double)s[0]);
//                Date date = (Date)s[1];
//                Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                salesChart1.setDate(formatter.format(date));
//                salesChart1.setMonth((Integer.toString(date.getMonth()+1)));
//                salesCharts.add(salesChart1);
//            }
//
//
//            return salesCharts;
//        } catch (Exception e){
//            e.printStackTrace();
//            throw new WawoohException();
//        }
//
//
//    }

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
            designer1.accountNumber=designer.accountNumber;
            designer1.threshold=designer.threshold;
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
    public void updateDesignerPersonalInformation(User userTemp, User user, Designer designer) {
        try {
            if(user.designer != null && userTemp.designer != null){
                userTemp.firstName = user.firstName;
                userTemp.lastName = user.lastName;
                userTemp.phoneNo = user.phoneNo;
                userTemp.gender = user.gender;
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
    public void updateDesignerBusinessInformation(User userTemp, User user, Designer designer) {
        try {
            if(user.designer != null && userTemp.designer != null){
                User currentUser = userTemp;
                Designer currentDesigner = designerRepository.findOne(userTemp.designer.id);

                currentDesigner.address = designer.address;
                currentDesigner.city = designer.city;
                currentDesigner.state = designer.state;
                currentDesigner.country = designer.country;
                currentDesigner.storeName = designer.storeName;
                currentDesigner.localGovt = designer.localGovt;
                currentDesigner.sizeGuideFlag = designer.sizeGuideFlag;
                currentDesigner.registeredFlag = designer.registeredFlag;

                if(designer.sizeGuideFlag == "Y"){
                    if(designer.sizeGuide != null){
                        if(designer.sizeGuide != ""){
                            cloudinaryService.deleteFromCloud(currentDesigner.sizeGuidePublicId, currentDesigner.sizeGuide);
                        }
                    }

                    try {
                        String fileName = userTemp.email.substring(0, 3) + generalUtil.getCurrentTime();
                        String base64Img = designer.sizeGuide;

                        CloudinaryResponse c = cloudinaryService.uploadToCloud(base64Img, fileName, "designersizeguides");
                        currentDesigner.sizeGuide = c.getUrl();
                        currentDesigner.sizeGuidePublicId = c.getPublicId();
                    }catch (Exception e){
                        e.printStackTrace();
                        throw new WawoohException();
                    }
                }

                if(currentDesigner.registeredFlag == "Y"){
                    currentDesigner.registrationNumber = designer.registrationNumber;
                    if(designer.registrationDocument != null){
                        if(designer.registrationDocument != ""){
                            cloudinaryService.deleteFromCloud(currentDesigner.registrationDocumentPublicId, currentDesigner.registrationDocument);
                        }
                    }

                    try {
                        String fileName = userTemp.email.substring(0, 3) + generalUtil.getCurrentTime();
                        String base64Img = designer.registrationDocument;

                        CloudinaryResponse c = cloudinaryService.uploadToCloud(base64Img, fileName, "designerregistrationdocument");
                        currentDesigner.registrationDocument = c.getUrl();
                        currentDesigner.registrationDocumentPublicId = c.getPublicId();
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
    public void updateDesignerAccountInformation(User userTemp, User user, Designer designer) {
        try {
            if(user.designer != null && userTemp.designer != null){
                Designer currentDesigner = designerRepository.findOne(userTemp.designer.id);

                currentDesigner.accountNumber = designer.accountNumber;
                currentDesigner.bankName = designer.bankName;
                currentDesigner.currency = designer.currency;

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
    public void  updateDesignerLogo(User userTemp, Designer passedDesigner) {
        try {
            if(userTemp.designer.id != null) {

                if(passedDesigner.logo != null) {
                    Designer d = designerRepository.findOne(userTemp.designer.id);
                    if(userTemp.designer.publicId != null) {
                        cloudinaryService.deleteFromCloud(userTemp.designer.publicId, userTemp.designer.logo);
                    }
                    try {
                        String fileName = userTemp.email.substring(0, 3) + generalUtil.getCurrentTime();
                        String base64Img = passedDesigner.logo;
//                        byte[] imgBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Img);
//                        ByteArrayInputStream bs = new ByteArrayInputStream(imgBytes);
//                        File imgfilee = new File(designerLogoFolder + fileName);
                        CloudinaryResponse c = cloudinaryService.uploadToCloud(base64Img,fileName,"designerlogos");
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
            int ratingCount;
            int userCount;
            Double average;
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
            designer.products.forEach(products -> {
                products.designerStatus="D";
            });
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
            DesignerDTO dto = generalUtil.convertDesigner2EntToDTO(designer);
            return dto;

        } catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public DesignerDTO getDesigner(User user, MonthsDTO months) {
        try {
            Designer designer = user.designer;
            List<SalesChart> salesCharts = new ArrayList<>();
            DesignerDTO dto = generalUtil.convertDesigner2EntToDTO(designer);
            for (String month:months.getMonths()) {
                YearMonth d = YearMonth.parse(month);
                LocalDate startDateMonth = d.atDay(1);
                LocalDate endDateMonth = d.atEndOfMonth();
                //converting local date to date format
                Date date1 = Date.from(startDateMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date date2 = Date.from(endDateMonth.atStartOfDay(ZoneId.systemDefault()).toInstant());

                SalesChart salesChart = new SalesChart();
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(date1);
//                salesChart.setMonth(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH ));
                    salesChart.setMonth(month.split("-")[1]);
                    salesChart.setYear(month.split("-")[0]);
                Double amnt = itemRepository.getSalesChart(designer.id,date1,date2,"C");
                if(amnt != null){
                salesChart.setAmount(amnt);
                }
                else {
                    salesChart.setAmount(0.0);
                }
                salesCharts.add(salesChart);
            }

           dto.setSalesChart(salesCharts);
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
            DesignerDTO dto = generalUtil.convertDesignerEntToDTO(designer);
            return dto;

        } catch (Exception e){
            e.printStackTrace();
            throw new WawoohException();
        }
    }


}
