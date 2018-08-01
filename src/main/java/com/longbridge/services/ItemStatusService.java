package com.longbridge.services;

import com.longbridge.dto.StatusMessageDTO;
import com.longbridge.models.ItemStatus;

import java.util.List;

public interface ItemStatusService {

     List<StatusMessageDTO> updateItemStatus(Long itemId, Long itemStatusId);
    void updateItemStatusWithMessage(Long itemId, Long itemStatusId, Long statusMessageId);
     void updateStatusWithResponse(Long itemId, Long responseId);
     List<ItemStatus> getAllStatuses();


}
