package com.longbridge.services;

import com.longbridge.dto.PageableDetailsDTO;
import com.longbridge.dto.PromoCodeDTO;
import com.longbridge.models.PromoCode;

import java.util.List;

public interface PromoCodeService {

    void addPromoCode(PromoCodeDTO promoCodeDTO);

    void updatePromoCode(PromoCodeDTO promoCodeDTO);

    void  deletePromoCode(Long id);

    PromoCode getPromoCode(Long id);

    List<PromoCodeDTO> getAllPromoCodes(PageableDetailsDTO pageableDetailsDTO);

    List<PromoCode> getUsedPromoCodes(PageableDetailsDTO pageableDetailsDTO);

    List<PromoCode> getUnUsedPromoCodes(PageableDetailsDTO pageableDetailsDTO);


}
