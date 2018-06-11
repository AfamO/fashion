package com.longbridge.services;

import com.longbridge.dto.StatusMessageDTO;

import java.util.List;

public interface ItemStatusService {

    public List<StatusMessageDTO> updateItemStatus(Long itemId, Long itemStatusId);
    public void updateItemStatusWithMessage(Long itemId, Long itemStatusId, Long statusMessageId);
    public void updateStatusWithResponse(Long itemId, Long responseId);
}
