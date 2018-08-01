package com.longbridge.services.implementations;

import com.longbridge.dto.StatusMessageDTO;
import com.longbridge.models.*;
import com.longbridge.repository.ItemRepository;
import com.longbridge.repository.ItemStatusRepository;
import com.longbridge.repository.StatusMessageRepository;
import com.longbridge.repository.StatusResponseMessageRepository;
import com.longbridge.services.ItemStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemStatusServiceImpl implements ItemStatusService {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemStatusRepository itemStatusRepository;

    @Autowired
    StatusMessageRepository statusMessageRepository;

    @Autowired
    StatusResponseMessageRepository statusResponseMessageRepository;

    @Override
    public List<StatusMessageDTO> updateItemStatus(Long itemId, Long itemStatusId) {

        ItemStatus itemStatus = itemStatusRepository.findOne(itemStatusId);
        Items item = itemRepository.findOne(itemId);

        if(item != null){
            if(itemStatus.getStatusMessages().size() > 0){
                List<StatusMessageDTO> statusMessageDTOS = new ArrayList<StatusMessageDTO>();

                for(StatusMessage sdt : itemStatus.getStatusMessages()){
                    StatusMessageDTO sd = new StatusMessageDTO();
                    sd.id = sdt.id;
                    sd.statusId = sdt.getItemStatus().id;
                    sd.message = sdt.getMessage();
                    statusMessageDTOS.add(sd);
                }

                return statusMessageDTOS;
            }else{
                item.setItemStatus(itemStatus);
                itemRepository.save(item);
            }
        }

        return null;
    }

    @Override
    public void updateItemStatusWithMessage(Long itemId, Long itemStatusId, Long statusMessageId) {

        ItemStatus itemStatus = itemStatusRepository.findOne(itemStatusId);
        Items item = itemRepository.findOne(itemId);
        if(item != null){
            StatusMessage statusMessage = statusMessageRepository.findOne(statusMessageId);

            item.setItemStatus(itemStatus);
            item.setStatusMessage(statusMessage);
            itemRepository.save(item);
        }
    }

    @Override
    public void updateStatusWithResponse(Long itemId, Long responseId) {
        Items item = itemRepository.findOne(itemId);
        StatusResponseMessage statusResponseMessage = statusResponseMessageRepository.findOne(responseId);

        if(item != null && statusResponseMessage != null){
            ItemStatus newStatus = statusResponseMessage.getItemStatus();

            item.setItemStatus(newStatus);
            //check if refund and add to refund table
            itemRepository.save(item);
        }
    }

    @Override
    public List<ItemStatus> getAllStatuses() {
        return itemStatusRepository.findAll();
    }
}
