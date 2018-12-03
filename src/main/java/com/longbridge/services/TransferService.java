package com.longbridge.services;

import com.longbridge.dto.TransferInfoDTO;

import java.util.List;

/**
 * Created by Longbridge on 20/09/2018.
 */
public interface TransferService {
    void saveOrderTransferInfo(TransferInfoDTO transferInfoDTO);
    List<TransferInfoDTO> getOrderTransferInfo(String orderNum);
    List<TransferInfoDTO> getAllTransferInfo();
    TransferInfoDTO getTransferInfoById(Long id);
}
