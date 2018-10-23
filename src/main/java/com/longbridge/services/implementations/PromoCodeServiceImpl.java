package com.longbridge.services.implementations;

import com.longbridge.dto.PageableDetailsDTO;
import com.longbridge.dto.PromoCodeDTO;
import com.longbridge.dto.PromoItemDTO;
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
import sun.jvm.hotspot.debugger.Page;

import java.util.ArrayList;
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
    @Override
    public void addPromoCode(PromoCodeDTO promoCodeDTO) {

        PromoCode promoCode= new PromoCode();
        promoCode.setCode(promoCodeDTO.getCode());
        promoCode.setValue(promoCodeDTO.getValue());
        promoCode.setExpiryDate(promoCodeDTO.getExpiryDate());
        promoCode.setIsUsedStatus("N");

        promoCode.setPromoItems(promoCodeDTO.getPromoItems());

        promoCodeRepository.save(promoCode);
        for (PromoItem promoItem:promoCodeDTO.getPromoItems()){
             promoItem.setPromoCode(promoCode);
             promoItemsRepository.save(promoItem);
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
    public PromoCode getPromoCode(Long id) {
        return promoCodeRepository.findOne(id);
    }

    @Override
    public List<PromoCodeDTO> getAllPromoCodes(PageableDetailsDTO pageableDetailsDTO) {


       List<PromoCodeDTO> promoCodeDTOLists= this.convertToDTO(promoCodeRepository.findAll(new PageRequest(pageableDetailsDTO.getPage(),pageableDetailsDTO.getSize())).getContent());

         return promoCodeDTOLists;

    }

     private List<PromoCodeDTO> convertToDTO(List<PromoCode> promoCodeList){

                 List<PromoCodeDTO> promoCodeDTOLists= new ArrayList<>();
                 for(PromoCode promoCode :promoCodeList){
                     PromoCodeDTO promoCodeDTO=new PromoCodeDTO();
                     promoCodeDTO.setCode(promoCode.getCode());
                     List<PromoItemDTO> promoItemDTOLists = new ArrayList<>();
                     for(PromoItem promoItem: promoCode.getPromoItems())
                     {
                            PromoItemDTO promoItemDTO =new PromoItemDTO();
                            promoItemDTO.setItemId(promoItem.getItemId());
                            promoItemDTO.setItemType(promoItem.getItemType());
                            promoItemDTOLists.add(promoItemDTO);
                     }
                     promoCodeDTO.setPromoItemsDTO(promoItemDTOLists);
                     promoCodeDTO.setValue(promoCode.getValue());
                     promoCodeDTO.setExpiryDate(promoCode.getExpiryDate());
                     promoCodeDTO.setIsUsedStatus(promoCode.getIsUsedStatus());
                     promoCodeDTOLists.add(promoCodeDTO);
                 }
                 return promoCodeDTOLists;
     }
    @Override
    public List<PromoCode> getUsedPromoCodes(PageableDetailsDTO pageableDetailsDTO) {
        return promoCodeRepository.findByIsUsedStatusNot("N");
    }

    @Override
    public List<PromoCode> getUnUsedPromoCodes(PageableDetailsDTO pageableDetailsDTO) {
        return promoCodeRepository.findByIsUsedStatusNot("Y");
    }
}
