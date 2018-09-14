package com.longbridge.services;

import com.longbridge.dto.InspireMeDTO;
import com.longbridge.dto.OutfitRequestDTO;
import com.longbridge.models.Code;
import com.longbridge.models.InspireMe;

import java.util.List;

public interface InspireMeService {

    List<Code> findEventsByGender(String gender);
    List<InspireMeDTO> findOutfitByGenderAndEvent(OutfitRequestDTO outfitRequestDTO);
    List<InspireMeDTO> findOutfitByEventAndHashtag(OutfitRequestDTO outfitRequestDTO);
    List<String> findHashtagsByGenderAndEvent(String gender, String event);
    InspireMeDTO findInspireMebyId(Long inspireMeId);
}
