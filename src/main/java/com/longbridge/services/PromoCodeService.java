package com.longbridge.services;

import com.longbridge.dto.PageableDetailsDTO;
import com.longbridge.dto.PromoCodeApplyReqDTO;
import com.longbridge.dto.PromoCodeDTO;
import com.longbridge.models.PromoCode;

import java.util.List;

public interface PromoCodeService {

    void addPromoCode(PromoCodeDTO promoCodeDTO);

    void updatePromoCode(PromoCodeDTO promoCodeDTO);

    void  deletePromoCode(Long id);

    PromoCodeDTO getPromoCode(Long id);

    Object[] applyPromoCode(PromoCodeApplyReqDTO promoCodeApplyReqDTO);

    List<PromoCodeDTO> getAllPromoCodes(PageableDetailsDTO pageableDetailsDTO);

    List<PromoCodeDTO> getUsedPromoCodes(PageableDetailsDTO pageableDetailsDTO);

    List<PromoCodeDTO> getUnUsedPromoCodes(PageableDetailsDTO pageableDetailsDTO);

    List<PromoCodeDTO> getExpiredPromoCodes(PageableDetailsDTO pageableDetailsDTO);

    List<PromoCodeDTO> getActiveAndStillValidPromoCodes(PageableDetailsDTO pageableDetailsDTO);


}
