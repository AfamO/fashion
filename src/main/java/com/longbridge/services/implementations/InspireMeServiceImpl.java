package com.longbridge.services.implementations;

import com.longbridge.dto.InspireMeDTO;
import com.longbridge.dto.OutfitRequestDTO;
import com.longbridge.models.Code;
import com.longbridge.models.InspireMe;
import com.longbridge.models.InspireMeProduct;
import com.longbridge.models.ProductColorStyles;
import com.longbridge.repository.CodeRepository;
import com.longbridge.repository.InspireMeProductRepository;
import com.longbridge.repository.InspireMeRepository;
import com.longbridge.repository.ProductAttributeRepository;
import com.longbridge.services.InspireMeService;
import org.apache.lucene.util.LongValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.*;

@Service
public class InspireMeServiceImpl implements InspireMeService {

    @Autowired
    CodeRepository codeRepository;

    @Autowired
    InspireMeRepository inspireMeRepository;

    /*@Autowired
    InspireMeProductRepository inspireMeProductRepository;*/

    @Autowired
    ProductAttributeRepository productAttributeRepository;

    @Override
    public List<Code> findEventsByGender(String gender) {

        if(gender != null){
            if(gender.equalsIgnoreCase("male")){
                return codeRepository.findByType("inspireme_event_male");
            }else if(gender.equalsIgnoreCase("female")){
                return codeRepository.findByType("inspireme_event_female");
            }
        }
        return null;
    }

    @Override
    public List<InspireMeDTO> findOutfitByGenderAndEvent(OutfitRequestDTO outfitRequestDTO) {

        int page = outfitRequestDTO.getPage();
        int size = outfitRequestDTO.getSize();

        if(outfitRequestDTO.getEvent() != null && outfitRequestDTO.getGender() != null){

            return convertEntityListToDto(inspireMeRepository.findByGenderAndEventsLike(outfitRequestDTO.getGender(), outfitRequestDTO.getEvent(), new PageRequest(page, size)).getContent());
        }
        return null;
    }

    @Override
    public List<InspireMeDTO> findOutfitByEventAndHashtag(OutfitRequestDTO outfitRequestDTO) {

        int page = outfitRequestDTO.getPage();
        int size = outfitRequestDTO.getSize();
        String gender = outfitRequestDTO.getGender();
        String event = outfitRequestDTO.getEvent();
        String hashTag = outfitRequestDTO.getHashTag();
        Pageable p = new PageRequest(page, size);

        if(event != null && gender != null && hashTag != null){

            return convertEntityListToDto(inspireMeRepository.findByGenderAndEventsLikeAndHashTagLike(gender, event, hashTag, p).getContent());
        }
        return null;
    }

    @Override
    public List<String> findHashtagsByGenderAndEvent(String gender, String event) {

        if(gender != null && event != null){

            System.out.println("ge"+ gender +" even"+event);
            List<String> hashTagsTemp = inspireMeRepository.getHashtagsByGenderAndEvent(gender, event);
            String hashTagsStringTemp = StringUtils.join(hashTagsTemp, ",");
            List<String> hashTags =  new LinkedList<>(Arrays.asList(hashTagsStringTemp.split(",")));
            Set<String> hs = new HashSet<>();
            hs.addAll(hashTags);
            hashTags.clear();
            hashTags.addAll(hs);

            return hashTags;
        }
        return null;
    }

    @Override
    public InspireMeDTO findInspireMebyId(Long inspireMeId) {

        InspireMe inspireMe = inspireMeRepository.findOne(inspireMeId);
        if(inspireMe != null){
            return converEntityToDto(inspireMe);
        }
        return null;
    }

    private InspireMeDTO converEntityToDto(InspireMe inspireMe){

        InspireMeDTO inspireMeDTO = new InspireMeDTO();
        inspireMeDTO.setId(inspireMe.id);
        inspireMeDTO.setName(inspireMe.getName());
        inspireMeDTO.setDescription(inspireMe.getDescription());
        inspireMeDTO.setGender(inspireMe.getGender());
        inspireMeDTO.setPrice(inspireMe.getPrice());
        inspireMeDTO.setEvents(Arrays.asList(inspireMe.getEvents().split(",")));
        inspireMeDTO.setHashTag(Arrays.asList(inspireMe.getHashTag().split(",")));
        inspireMeDTO.setProducts(inspireMe.getInspireMeProducts());
        inspireMeDTO.setInspireMePictures(inspireMe.getPicture());

        List<Long> accessories = new ArrayList<Long>();
        List<Long> clothe = new ArrayList<Long>();

        clothe.add(Long.valueOf(1));
        accessories.add(Long.valueOf(2));
        accessories.add(Long.valueOf(3));

        //List<InspireMeProduct> tempClotheProducts = inspireMeProductRepository.findByInspireMeAndProductTypeIdIn(inspireMe, clothe);
        //List<InspireMeProduct> tempAccessoriesProducts = inspireMeProductRepository.findByInspireMeAndProductTypeIdIn(inspireMe, accessories);

        //List<InspireMeProduct> clotheProducts = new ArrayList<InspireMeProduct>();
        //List<InspireMeProduct> accessoriesProducts = new ArrayList<InspireMeProduct>();

        /*for (InspireMeProduct clotheP : tempClotheProducts ){
            ProductAttribute productAttribute = productAttributeRepository.findOne(clotheP.getProductAttributeId());
            if(productAttribute != null){
                productAttribute.getProductSizes();
                clotheP.setProductAttribute(productAttribute);
                clotheProducts.add(clotheP);
            }
        }

        for (InspireMeProduct accessoryP : tempAccessoriesProducts ){
            ProductAttribute productAttribute = productAttributeRepository.findOne(accessoryP.getProductAttributeId());
            if(productAttribute != null){
                productAttribute.getProductSizes();
                accessoryP.setProductAttribute(productAttribute);
                accessoriesProducts.add(accessoryP);
            }
        }*/

        //inspireMeDTO.setProducts(clotheProducts);
        //inspireMeDTO.setAccessories(accessoriesProducts);

        return inspireMeDTO;
    }

    private List<InspireMeDTO> convertEntityListToDto(List<InspireMe> inspireMes){

        List<InspireMeDTO> inspireMeDTOS = new ArrayList<InspireMeDTO>();

        for (InspireMe i : inspireMes) {
            inspireMeDTOS.add(converEntityToDto(i));
        }

        return inspireMeDTOS;
    }
}
