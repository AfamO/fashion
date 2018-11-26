package com.longbridge.services;

import com.longbridge.dto.PageableDetailsDTO;
import com.longbridge.dto.PromoCodeApplyReqDTO;
import com.longbridge.dto.PromoCodeDTO;
import com.longbridge.models.Cart;
import com.longbridge.models.PromoCode;
import com.longbridge.models.PromoCodeUserStatus;

import java.util.List;

public interface PromoCodeService {

    String addPromoCode(PromoCodeDTO promoCodeDTO);

    String updatePromoCodeItems(PromoCodeDTO promoCodeDTO);

    String updatePromoCode(PromoCodeDTO promoCodeDTO);

    void  deletePromoCode(Long id);

    PromoCodeDTO getPromoCode(Long id);

    PromoCodeUserStatus getPromoCodeUserStatus();

    Object[] applyPromoCode(PromoCodeApplyReqDTO promoCodeApplyReqDTO);

    String generatePromoCode();

    List<PromoCodeDTO> getAllPromoCodes(PageableDetailsDTO pageableDetailsDTO);

    List<PromoCodeDTO> getUsedPromoCodes(PageableDetailsDTO pageableDetailsDTO);

    List<PromoCodeDTO> getUnUsedPromoCodes(PageableDetailsDTO pageableDetailsDTO);

    List<PromoCodeDTO> getExpiredPromoCodes(PageableDetailsDTO pageableDetailsDTO);

    List<PromoCodeDTO> getActiveAndStillValidPromoCodes(PageableDetailsDTO pageableDetailsDTO);


}
