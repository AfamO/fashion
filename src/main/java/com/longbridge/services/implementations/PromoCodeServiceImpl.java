package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.dto.PageableDetailsDTO;
import com.longbridge.dto.PromoCodeApplyReqDTO;
import com.longbridge.dto.PromoCodeDTO;
import com.longbridge.dto.PromoItemDTO;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.*;
import com.longbridge.repository.*;
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

    Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private String promoCodeStatusMessage="Operation Successful";

    @Autowired
    CartRepository cartRepository;
    
    @Override
    public void addPromoCode(PromoCodeDTO promoCodeDTO) {

        try {

            PromoCode promoCode= new PromoCode();
            promoCode.setCode(promoCodeDTO.getCode());
            promoCode.setValue(promoCodeDTO.getValue());
            promoCode.setExpiryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(promoCodeDTO.getExpiryDate()));
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

        }
        catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }




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
    public Object[] applyPromoCode(PromoCodeApplyReqDTO promoCodeApplyReqDTO) {

        Cart cart=promoCodeApplyReqDTO.getCart();
        User user = getCurrentUser();
        Products products = productRepository.findOne(cart.getProductId());
            Double amount;
            if(products.getPriceSlash() != null && products.getPriceSlash().getSlashedPrice()>0){
                amount=products.getAmount()-products.getPriceSlash().getSlashedPrice();
            }else {
                amount=products.getAmount();
            }
            Double intialAmount=amount;
            System.out.println("The Initial Amount Is::"+amount);
            // Apply PromoCode Here if any
            if(promoCodeApplyReqDTO.getPromoCode()!=null){
                //Since this item has a promo Code, we check for it before applying it.
                PromoCode promoCode=promoCodeRepository.findByCode(promoCodeApplyReqDTO.getPromoCode());
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
                    promoCodeStatusMessage= "OOOps! The PromoCode You entered does not exist.";
                }
                if(amount!=intialAmount){
                    //The promoCode must have been applied successfully

                    // Update the used status if it is a 'single' use type

                     if(promoCode.getNumberOfUsage().equalsIgnoreCase("Y")){
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
                    cartRepository.save(cart);


                }

                System.out.println("The Final Amount After Applying PromoCode Is::"+amount);
            }
            return new Object [] {promoCodeStatusMessage,amount};
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
