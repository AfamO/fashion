package com.longbridge.services.implementations;

import com.longbridge.Util.GeneralUtil;
import com.longbridge.dto.PageableDetailsDTO;
import com.longbridge.dto.PromoCodeDTO;
import com.longbridge.dto.PromoItemDTO;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.PromoCode;
import com.longbridge.models.PromoItem;
import com.longbridge.repository.EventRepository;
import com.longbridge.repository.ProductRepository;
import com.longbridge.repository.PromoCodeRepository;
import com.longbridge.repository.PromoItemsRepository;
import com.longbridge.services.PromoCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public void addPromoCode(PromoCodeDTO promoCodeDTO) {

        try {

            PromoCode promoCode= new PromoCode();
            promoCode.setCode(promoCodeDTO.getCode());
            promoCode.setValue(promoCodeDTO.getValue());
            promoCode.setExpiryDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(promoCodeDTO.getExpiryDate()));
            promoCode.setValueType(promoCodeDTO.getValueType());
            promoCode.setIsUsedStatus("N");
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
