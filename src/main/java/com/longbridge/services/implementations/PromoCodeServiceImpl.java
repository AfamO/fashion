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
import com.longbridge.security.repository.UserRepository;
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
import java.util.Objects;
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
    UserRepository userRepository;
    
    @Autowired
    CategoryRepository categoryRepository;

    Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private String promoCodeStatusMessage="Operation Successful";

    @Autowired
    CartRepository cartRepository;
    
    @Override
    public String addPromoCode(PromoCodeDTO promoCodeDTO) {

        try {
            
            if(promoCodeRepository.findUniqueUnExpiredPromoCode(promoCodeDTO.getCode())==null ||promoCodeRepository.findUniqueUnExpiredPromoCode(promoCodeDTO.getCode()).getExpiryDate().before(new Date())){
                Date expiryDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(promoCodeDTO.getExpiryDate());
                //Has this date expired?
                if(expiryDate.before(new Date())){
                    promoCodeStatusMessage="OOps! This promoCode creation date has already expired. Check your expiry Date";
                }
                else{
                    //Does the creatorId exist?
                    if(userRepository.findById(promoCodeDTO.getCreatorId())!=null){
                        User createdBy= userRepository.findById(promoCodeDTO.getCreatorId());
                        PromoCode promoCode= new PromoCode();
                        promoCode.setCode(promoCodeDTO.getCode());
                        promoCode.setValue(promoCodeDTO.getValue());
                        promoCode.setExpiryDate(expiryDate);
                        promoCode.setValueType(promoCodeDTO.getValueType());
                        promoCode.setIsUsedStatus("N");
                        promoCode.setNumberOfUsage(promoCodeDTO.getNumberOfUsage());
                        promoCode.setCreatedBy(createdBy);
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
                                Product product=productSize.getProductColorStyle().getProduct();
                                PromoItem promoItem= new PromoItem();
                                promoItem.setItemId(product.id);
                                promoItem.setItemType("product");
                                promoItem.setPromoCode(promoCode);
                                promoItemsRepository.save(promoItem);

                            }
                        }
                        promoCodeStatusMessage="Operation Successful";
                    }
                    else{
                        
                        promoCodeStatusMessage="OOps! Please send a valid and existing user to create the promocode!";
                        return promoCodeStatusMessage;
                    }
                        
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
                    //Are there maker checkers for this promoCode?
                    promoCode.setUpdatedOn(new Date());
                    promoCodeRepository.save(promoCode);
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
    public String updatePromoCode(PromoCodeDTO promoCodeDTO) {
        
        System.out.println("The Previous promoCodeStatusMessage Is::"+promoCodeStatusMessage);
        try{
            if(promoCodeDTO.getCode()!=null){
                //Since it has a promo Code, we check for it.
                PromoCode promoCode=promoCodeRepository.findUniqueUnExpiredPromoCode(promoCodeDTO.getCode());
                //Does it exist in the DB?
                if(promoCode!=null){
                    System.out.println("The PromoCode Is::"+promoCode.getCode());
                    Date expiryDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(promoCodeDTO.getExpiryDate());
                    //Is this sent date  expired-in the past?
                    if(!expiryDate.before(new Date())){
                        //Ensure the PromoCode hasn't been used(if it has single usage) and if true is multiple usage allowed or the usage counter  still less than number of usages allowed?
                        if((promoCode.getNumberOfUsage()==1|| promoCode.getNumberOfUsage()==-1) && promoCode.getIsUsedStatus().equalsIgnoreCase("N")
                                || promoCode.getUsageCounter()< promoCode.getNumberOfUsage()){
                            
                                promoCode.setValue(promoCodeDTO.getValue());
                                promoCode.setExpiryDate(expiryDate);
                                promoCode.setValueType(promoCodeDTO.getValueType());
                                promoCode.setIsUsedStatus("N");
                                promoCode.setNumberOfUsage(promoCodeDTO.getNumberOfUsage());
                                promoCode.setUpdatedOn(new Date());
                                
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
                                        Product product=productSize.getProductColorStyle().getProduct();
                                        PromoItem promoItem= new PromoItem();
                                        promoItem.setItemId(product.id);
                                        promoItem.setItemType("product");
                                        promoItem.setPromoCode(promoCode);
                                        promoItemsRepository.save(promoItem);
                                        
                                    }
                                }
                                promoCodeStatusMessage="Operation Successful";
                        }
                        else {
                            promoCodeStatusMessage= "OOOps! This PromoCode has already been used.";
                        }

                    }
                    else{
                        promoCodeStatusMessage="OOps! The date is in the past. Check your expiry Date";
                    }
                        
                    }
                    else {
                        System.out.println("The PromoCode Is Null::");
                        promoCodeStatusMessage= "OOOps! This PromoCode doesn't exist or  has  expired!";
                    }

                }
                else {
                    promoCodeStatusMessage= "OOOps! You did not send any promocode.";
                }
        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
        
        return promoCodeStatusMessage;
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
    public Object[] applyPromoCode(Cart cart) {

        User user = getCurrentUser();
        Product products = productRepository.findOne(cart.getProductId());
            Double amount;
            if(products.getProductPrice().getPriceSlash() != null && products.getProductPrice().getPriceSlash().getSlashedPrice()>0){
                amount=products.getProductPrice().getAmount()-products.getProductPrice().getPriceSlash().getSlashedPrice();
            }else {
                amount=products.getProductPrice().getAmount();
            }
            Double intialAmount=amount;
            PromoCodeApplyRespDTO promoCodeApplyRespDTO=null;
            System.out.println("The Initial Amount Is::"+amount);
            // Apply PromoCode Here if any
            if(cart.getPromoCode()!=null){
                //Since this item has a promo Code, we check for it before applying it.
                PromoCode promoCode=promoCodeRepository.findUniqueUnExpiredAndVerifiedPromoCode(promoCodeApplyReqDTO.getPromoCode(),"Y");
                //Does it exist in the DB?
                if(promoCode!=null){
                    //Has the promoCode expired?
                    if(promoCode.getExpiryDate().after(new Date())){
                        //Has the PromoCode been used
                        if((promoCode.getNumberOfUsage().equalsIgnoreCase("single") ||promoCode.getNumberOfUsage().equalsIgnoreCase("multiple")) && promoCode.getIsUsedStatus().equalsIgnoreCase("N")){

                            //Find out if this product has a promo code assigned to it.
                            for(PromoItem promoItem:promoCode.getPromoItems()){

                                System.out.println("The Size Of the PromoCode Items Is::"+promoCode.getPromoItems().size());
                                System.out.println("ItemType=="+promoItem.getItemType()+", ItemId=="+promoItem.getItemId()+" While ProductId=="+products.id);
                                if(promoItem.getItemType().equalsIgnoreCase("p") && promoItem.getItemId()==products.id){
                                    Double promoValue=Double.parseDouble(promoCode.getValue());
                                    // Is it percentage discount(pd)
                                    if(promoCode.getValueType().equalsIgnoreCase("pd")){
                                        //multiply
                                        amount= amount- (amount*(promoValue)/100);
                                        break;
                                    }
                                    //Is the promocode applied only to products of a particular id?
                                    else if(promoItem.getItemType().equalsIgnoreCase("PRODUCT") && Objects.equals(promoItem.getItemId(), product.id)){
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
                                                if(Objects.equals(subCategory.id, product.getSubCategory().id)){
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
                                }
                                // Is this promoCode applied to a category instead of to a product?
                                else if(promoItem.getItemType().equalsIgnoreCase("c")){
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
                                                if(promoCode.getValueType().equalsIgnoreCase("pd")){
                                                    //multiply
                                                    amount= amount- (amount*(promoValue/100));
                                                    break;
                                                }
                                                //Is it normal monetary value discount(vd)
                                                else if(promoCode.getValueType().equalsIgnoreCase("vd")){
                                                    //multiply
                                                    amount-= promoValue;
                                                    break;
                                                }
                                                //It must be free shipping.
                                                else {
                                                    // Apply free shipping discount here.
                                                    break;

                                                }

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
                                    if(!Objects.equals(amount, intialAmount)){
                                        break; // Break the outer loop to avoid deducting again a product id of same category or product
                                    }
                                }
                                else {
                                    promoCodeStatusMessage= "OOOps! The PromoCode You entered is not applicable to this item.";
                                }
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
                    promoCodeStatusMessage= "OOOps! The PromoCode You entered either doesn't exist Or Hasn't been verified.";
                }
                if(!Objects.equals(amount, intialAmount) || promoCodeStatusMessage.equalsIgnoreCase("fs")){
                    //The promoCode must have been applied successfully
                    promoCodeStatusMessage="Operation Successful";
                    // Update the used status if it is a 'single' use type

                     if(promoCode.getNumberOfUsage().equalsIgnoreCase("single")){
                     promoCode.setIsUsedStatus("Y");
                     promoCodeRepository.save(promoCode);
                     }

                    //Then save the new amount to the cart.
                    Date date =new Date();
                    cart.setAmount(amount*cart.getQuantity());
                    cart.setCreatedOn(date);
                    cart.setUpdatedOn(date);
                    cart.setExpiryDate(DateUtils.addDays(date,7));
                    cart.setUser(user);
                    //Indicate that this user has used the PromoCode
                    cart.setPromoCode("USER_USED");
                    cartRepository.save(cart);
                    // Set the new pricing details
                    promoCodeApplyRespDTO=new PromoCodeApplyRespDTO();
                    promoCodeApplyRespDTO.setPrice(amount);
                    promoCodeApplyRespDTO.setQuantity(cart.getQuantity());
                    promoCodeApplyRespDTO.setTotalPrice(cart.getAmount());


                }

                System.out.println("The Final Amount After Applying PromoCode Is::"+amount);
            }
            //Return the PromoCode Response DTO and the Status Message
            return new Object [] {promoCodeStatusMessage,promoCodeApplyRespDTO};
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

    @Override
    public String verifyPromoCode(PromoCodeDTO promoCodeDTO) {
        try {
            PromoCode promoCode= promoCodeRepository.findUniqueUnExpiredPromoCode(promoCodeDTO.getCode());
            if(promoCode!=null){
                
                //Is the expiry date still in the future?
                if(promoCode.getExpiryDate().after(new Date())){
                    //Are there maker checkers for this promoCode?
                    if(userRepository.findById(promoCodeDTO.getVerifierId())!=null){
                        User verifiedBy = userRepository.findById(promoCodeDTO.getVerifierId());
                        if(!verifiedBy.getRole().equalsIgnoreCase("admin") && !verifiedBy.getRole().equalsIgnoreCase("super_admin")){
                            promoCodeStatusMessage="OOps! This user named '"+verifiedBy.getFirstName()+" "+verifiedBy.getLastName()+"' is not permitted to carry out this action.";
                            return promoCodeStatusMessage;
                        }
                        else{
                            
                            if(Objects.equals(promoCodeDTO.getVerifierId(), promoCode.getCreatedBy().id)){
                            promoCodeStatusMessage="OOps! The promoCode cannot be verified by the same user that created it.";
                            return promoCodeStatusMessage;
                        }
                        else{
                                promoCode.setUpdatedOn(new Date());
                                promoCode.setVerifiedFlag("Y");
                                promoCode.setVerifiedBy(verifiedBy);
                                promoCodeRepository.save(promoCode);
                                promoCodeStatusMessage="Operation Successful";
                            }
                            
                        }
                    }
                    else{
                        promoCodeStatusMessage="OOps! Please send a valid and existing user to verify the promocode!";
                        return promoCodeStatusMessage;
                    }
                    
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
    public String unVerifyPromoCode(PromoCodeDTO promoCodeDTO) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
