package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.dto.PageableDetailsDTO;
import com.longbridge.dto.PromoCodeApplyReqDTO;
import com.longbridge.dto.PromoCodeDTO;
import com.longbridge.dto.PromoItemDTO;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.*;
import com.longbridge.respbodydto.PromoCodeApplyRespDTO;
import com.longbridge.security.JwtUser;
import com.longbridge.services.PromoCodeService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PromoCodeServiceImpl implements PromoCodeService {

    @Autowired
    PromoCodeRepository promoCodeRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    PromoItemsRepository promoItemsRepository;

    @Autowired
    GeneralUtil generalUtil;
    
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PromoCodeUserStatusRepository promoCodeUserStatusRepository;

    Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private String promoCodeStatusMessage="Operation Successful";

    private PromoCodeUserStatus promoCodeUserStatus=null;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductAttributeRepository productAttributeRepository;

    @Autowired
    ProductSizesRepository productSizesRepository;
    
    @Override
    public String addPromoCode(PromoCodeDTO promoCodeDTO) {

        try {

            System.out.println("My Current  Time Is::"+new Date());
            if(promoCodeRepository.findUniqueUnExpiredPromoCode(promoCodeDTO.getCode())==null ||promoCodeRepository.findUniqueUnExpiredPromoCode(promoCodeDTO.getCode()).getExpiryDate().before(new Date())){
                Date expiryDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(promoCodeDTO.getExpiryDate());
                //Has this date expired?
                if(expiryDate.before(new Date())){
                    promoCodeStatusMessage="OOps! This promoCode has already expired. Check your expiry Date";
                }
                else{
                    PromoCode promoCode= new PromoCode();
                    promoCode.setCode(promoCodeDTO.getCode());
                    promoCode.setValue(promoCodeDTO.getValue());
                    promoCode.setExpiryDate(expiryDate);
                    promoCode.setValueType(promoCodeDTO.getValueType());
                    promoCode.setIsUsedStatus("N");
                    promoCode.setNumberOfUsage(promoCodeDTO.getNumberOfUsage());
                    promoCode.setCreatedOn(new Date());
                    promoCode.setPromoItems(promoCodeDTO.getPromoItems());
                    promoCodeRepository.save(promoCode);
                    for (PromoItem promoItem:promoCodeDTO.getPromoItems()){
                        promoItem.setPromoCode(promoCode);
                        promoItemsRepository.save(promoItem);
                    }

                    // Are the items sizes sent?: I mean find out if the promocode is assigned to items of  particular size(s) range
                    if(promoCodeDTO.getPromoItemSizes()!=null && promoCodeDTO.getPromoItemSizes().size()>0){
                        List<ProductSizes> productSizesList= productSizesRepository.findByNamesOfSizes(promoCodeDTO.getPromoItemSizes());
                        for(ProductSizes productSize:productSizesList)
                        {
                            // Then save the corresponding items details.
                            Products products=productSize.getProductAttribute().getProducts();
                            PromoItem promoItem= new PromoItem();
                            promoItem.setItemId(products.id);
                            promoItem.setItemType("p");
                            promoItem.setPromoCode(promoCode);
                            promoItemsRepository.save(promoItem);

                        }
                    }
                    promoCodeStatusMessage="Operation Successful";
                }

            }
            else {
                promoCodeStatusMessage="OOps! This promoCode already exists.";
            }

        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }

        return promoCodeStatusMessage;


    }

    @Override
    public String updatePromoCodeItems(PromoCodeDTO promoCodeDTO) {

        try {
            PromoCode promoCode= promoCodeRepository.findUniqueUnExpiredPromoCode(promoCodeDTO.getCode());
            if(promoCode!=null){

                //Is the expiry date still in the future?
                if(promoCode.getExpiryDate().after(new Date())){

                    for (PromoItem promoItem:promoCodeDTO.getPromoItems()){
                        promoItem.setPromoCode(promoCode);
                        promoItemsRepository.save(promoItem);
                    }
                    promoCodeStatusMessage="Operation Successful";
                }
                else {

                    promoCodeStatusMessage="OOps! This promoCode has already expired!";
                }

            }
            else {
                promoCodeStatusMessage="OOps! This promoCode does not exist or has expired!";
            }

        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }

        return promoCodeStatusMessage;

    }
    @Override
    public void updatePromoCode(PromoCodeDTO promoCodeDTO) {

    }

    @Override
    public void deletePromoCode(Long id) {

        promoCodeRepository.delete(id);

    }

    @Override
    public PromoCodeDTO getPromoCode(Long id) {
        PromoCodeDTO promoCodeDTO=null;
        try {
            promoCodeDTO=generalUtil.convertSinglePromoCodeToDTO(promoCodeRepository.findOne(id));
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
        return promoCodeDTO;
    }

    @Override
    public PromoCodeUserStatus getPromoCodeUserStatus() {
        return promoCodeUserStatus;
    }

    @Override
    public Object[] applyPromoCode(PromoCodeApplyReqDTO promoCodeApplyReqDTO) {

        User user = getCurrentUser();
        Products products = productRepository.findOne(promoCodeApplyReqDTO.getProductId());
            Double amount;
            if(products.getPriceSlash() != null && products.getPriceSlash().getSlashedPrice()>0){
                amount=products.getPriceSlash().getSlashedPrice();
            }else {
                amount=products.getAmount();
            }
            Double intialAmount = amount;
            PromoCodeApplyRespDTO promoCodeApplyRespDTO=null;
            System.out.println("The Initial Amount Is::"+amount);
            // Apply PromoCode Here if any
            if(promoCodeApplyReqDTO.getPromoCode()!=null){
                //Since this item has a promo Code, we check for it before applying it.
                PromoCode promoCode=promoCodeRepository.findUniqueUnExpiredPromoCode(promoCodeApplyReqDTO.getPromoCode());
                //Does it exist in the DB?
                if(promoCode!=null){
                    //Has the promoCode expired?
                    if(promoCode.getExpiryDate().after(new Date())){
                        //Ensure the PromoCode hasn't been used(if it has single usage) and if true is multiple usage allowed or the usage counter  still less than number of usages allowed?
                        if((promoCode.getNumberOfUsage()==1|| promoCode.getNumberOfUsage()==-1) && promoCode.getIsUsedStatus().equalsIgnoreCase("N")
                        || promoCode.getUsageCounter()< promoCode.getNumberOfUsage()){
                            PromoCodeUserStatus promoCodeUserStatus= promoCodeUserStatusRepository.findByUserAndPromoCode(user,promoCode);
                            //This has not been used this user?
                            if(promoCodeUserStatus==null ) {

                                //Find out if this product has a promo code assigned to it.
                                for(PromoItem promoItem:promoCode.getPromoItems()){

                                    System.out.println("The Size Of the PromoCode Items Is::"+promoCode.getPromoItems().size());
                                    System.out.println("ItemType=="+promoItem.getItemType()+", ItemId=="+promoItem.getItemId()+" While ProductId=="+products.id);
                                    //Is the promocode applied only to ALL products ?
                                    if(promoItem.getItemType().equalsIgnoreCase("ALL") && promoItem.getItemId()==-1){
                                        Double promoValue=Double.parseDouble(promoCode.getValue());
                                        // Is it percentage discount(pd)
                                        if(promoCode.getValueType().equalsIgnoreCase("PD")){
                                            //multiply
                                            amount= amount- (amount*(promoValue/100));
                                            break;
                                        }
                                        //Is it normal monetary value discount(vd)
                                        else if(promoCode.getValueType().equalsIgnoreCase("VD")){
                                            //multiply
                                            amount-= promoValue;
                                            break;
                                        }
                                        //It must be free shipping.
                                        else {
                                            // Apply free shipping discount here.
                                            promoCodeStatusMessage="fs";
                                            break;

                                        }
                                    }
                                    //Is the promocode applied only to products of a particular id?
                                    else if(promoItem.getItemType().equalsIgnoreCase("PRODUCT") && promoItem.getItemId()==products.id){
                                        Double promoValue=Double.parseDouble(promoCode.getValue());
                                        // Is it percentage discount(pd)
                                        if(promoCode.getValueType().equalsIgnoreCase("PD")){
                                            //multiply
                                            amount= amount- (amount*(promoValue/100));
                                            break;
                                        }
                                        //Is it normal monetary value discount(vd)
                                        else if(promoCode.getValueType().equalsIgnoreCase("VD")){
                                            //multiply
                                            amount-= promoValue;
                                            break;
                                        }
                                        //It must be free shipping.
                                        else {
                                            // Apply free shipping discount here.
                                            promoCodeStatusMessage="fs";
                                            break;

                                        }
                                    }
                                    // Is this promoCode applied to a category instead of to a product?
                                    else if(promoItem.getItemType().equalsIgnoreCase("CATEGORY")){
                                        Category category= categoryRepository.findOne(promoItem.getItemId());
                                        if(category!=null){
                                            //look for the sub category
                                            for(SubCategory subCategory: category.subCategories){
                                                promoCodeStatusMessage=null;
                                                //Does the product belong to the subcategory of this category?
                                                if(subCategory.id==products.getSubCategory().id){
                                                    System.out.println("Product Subcategory is same as PromoCode Item SubCategory ");
                                                    //Then apply the promocode discount
                                                    Double promoValue=Double.parseDouble(promoCode.getValue());
                                                    // Is it percentage discount(pd)
                                                    if(promoCode.getValueType().equalsIgnoreCase("PD")){
                                                        //multiply
                                                        amount= amount- (amount*(promoValue/100));
                                                        //break;
                                                    }
                                                    //Is it normal monetary value discount(vd)
                                                    else if(promoCode.getValueType().equalsIgnoreCase("VD")){
                                                        //multiply
                                                        amount-= promoValue;
                                                        // break;
                                                    }
                                                    //It must be free shipping.
                                                    else {
                                                        // Apply free shipping discount here.
                                                        promoCodeStatusMessage="fs";
                                                        //break;

                                                    }
                                                    System.out.println("The current deducted balance is "+amount);
                                                    break;  // To avoid deducting a product id of same category

                                                }
                                                else {
                                                    promoCodeStatusMessage= "OOOps! The PromoCode You entered is not applicable to this item.";
                                                }

                                            }
                                        }
                                        else
                                        {
                                            promoCodeStatusMessage= "OOOps! The item category for this PromoCode can't be found .";
                                        }
                                    }
                                    else {
                                        promoCodeStatusMessage= "OOOps! The PromoCode You entered is not applicable to this item.";
                                    }
                                    if(amount!=intialAmount){
                                        break; // Break the outer loop to avoid deducting again a product id of same category or product
                                    }
                                }
                            }
                            else {
                                promoCodeStatusMessage="OOOps! This PromoCode has already been used by you.";
                            }

                        }
                        else {
                            promoCodeStatusMessage= "OOOps! This PromoCode has already been used.";
                        }

                    }
                    else {
                        promoCodeStatusMessage= "OOOps! The PromoCode You entered has expired!";
                    }

                }
                else {
                    promoCodeStatusMessage= "OOOps! The PromoCode You entered does not exist.";
                }
                if(amount!=intialAmount){
                    //The promoCode must have been applied successfully
                    promoCodeStatusMessage="Operation Successful";


                    // Update the used status if it is a 'single' usage  type
                     if(promoCode.getNumberOfUsage()==1){
                     promoCode.setIsUsedStatus("Y");

                     }
                     //Increment the usage counter
                     promoCode.setUsageCounter(promoCode.getUsageCounter()+1);
                     promoCode.setUpdatedOn(new Date());
                     promoCodeRepository.save(promoCode);// Update the PromoCode DB
                    //Then save the new amount to the cart.
                    Date date =new Date();
                    //Indicate that this user has used the PromoCode by setting the status to 'Y'
                    promoCodeUserStatus= new PromoCodeUserStatus();
                    promoCodeUserStatus.setUser(user);
                    promoCodeUserStatus.setCreatedOn(date);
                    promoCodeUserStatus.setUpdatedOn(date);
                    promoCodeUserStatus.setPromoCode(promoCode);
                    promoCodeUserStatus.setIsPromoCodeUsedByUser("Y");
                    promoCodeUserStatus.setProductId(promoCodeApplyReqDTO.getProductId());
                    promoCodeUserStatus.setDiscountedAmount(amount);
                    promoCodeUserStatusRepository.save(promoCodeUserStatus);
                    promoCodeUserStatus=null;// Since it will be used during 'addOrder' method when checking out. This ensures that it is the same user this object.
                    // Set the new pricing details
                    promoCodeApplyRespDTO=new PromoCodeApplyRespDTO();
                    promoCodeApplyRespDTO.setPrice(amount);
                    promoCodeApplyRespDTO.setTotalPrice(amount);


                }

                System.out.println("The Final Amount After Applying PromoCode Is::"+amount);
            }
            //Return the PromoCode Response DTO and the Status Message
            return new Object [] {promoCodeStatusMessage,promoCodeApplyRespDTO};
    }

    @Override
    public String generatePromoCode() {
        String initials="WAW-";
        initials+=UUID.randomUUID().toString().substring(0,6).toUpperCase();
        return initials;

    }

    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        return jwtUser.getUser();
    }

    @Override
    public List<PromoCodeDTO> getAllPromoCodes(PageableDetailsDTO pageableDetailsDTO) {


       List<PromoCodeDTO> promoCodeDTOLists= generalUtil.convertPromoCodeListsToDTO(promoCodeRepository.findAll(new PageRequest(pageableDetailsDTO.getPage(),pageableDetailsDTO.getSize())).getContent());

         return promoCodeDTOLists;

    }

    @Override
    public List<PromoCodeDTO> getUsedPromoCodes(PageableDetailsDTO pageableDetailsDTO) {
        return generalUtil.convertPromoCodeListsToDTO(promoCodeRepository.findByIsUsedStatusNot("N"));
    }

    @Override
    public List<PromoCodeDTO> getUnUsedPromoCodes(PageableDetailsDTO pageableDetailsDTO) {
        return generalUtil.convertPromoCodeListsToDTO(promoCodeRepository.findByIsUsedStatusNot("Y"));
    }

    @Override
    public List<PromoCodeDTO> getExpiredPromoCodes(PageableDetailsDTO pageableDetailsDTO) {
        return generalUtil.convertPromoCodeListsToDTO(promoCodeRepository.findExpiredPromoCodes());
    }

    @Override
    public List<PromoCodeDTO> getActiveAndStillValidPromoCodes(PageableDetailsDTO pageableDetailsDTO) {
        return generalUtil.convertPromoCodeListsToDTO(promoCodeRepository.findUnExpiredPromoCodes());
    }


}
