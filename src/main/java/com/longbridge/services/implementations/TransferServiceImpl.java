package com.longbridge.services.implementations;

import com.longbridge.dto.TransferInfoDTO;
import com.longbridge.exception.WawoohException;
import com.longbridge.models.Orders;
import com.longbridge.models.TransferInfo;
import com.longbridge.repository.OrderRepository;
import com.longbridge.repository.TransferInfoRepository;
import com.longbridge.services.TransferService;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Longbridge on 20/09/2018.
 */
@Service
public class TransferServiceImpl implements TransferService{

    @Autowired
    TransferInfoRepository transferInfoRepository;

    @Autowired
    OrderRepository orderRepository;

    Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void saveOrderTransferInfo(TransferInfoDTO transferInfoDTO) {
        if(transferInfoDTO.getOrderNum() != null){
            Orders orders = orderRepository.findByOrderNum(transferInfoDTO.getOrderNum());
            if(orders != null){
                if(orders.getDeliveryStatus().equalsIgnoreCase("P")) {
                    if(transferInfoDTO.getId() != null){
                        TransferInfo transferInfo1=transferInfoRepository.findOne(transferInfoDTO.getId());
                        try {
                            transferInfo1.setPaymentDate(dateFormatter.parse(transferInfoDTO.getPaymentDate()));
                        } catch (ParseException e) {
                            transferInfo1.setPaymentDate(new Date());
                            e.printStackTrace();
                        }
                        transferInfo1.setAccountName(transferInfoDTO.getAccountName());
                        transferInfo1.setAmountPayed(transferInfoDTO.getAmountPayed());
                        transferInfo1.setBank(transferInfoDTO.getBank());
                        transferInfo1.setPaymentNote(transferInfoDTO.getPaymentNote());
                        transferInfoRepository.save(transferInfo1);
                    }
                    else {
                        TransferInfo transInfo = new TransferInfo();
                        transInfo.setOrders(orders);
                        try {
                            transInfo.setPaymentDate(dateFormatter.parse(transferInfoDTO.getPaymentDate()));
                        } catch (ParseException e) {
                            transInfo.setPaymentDate(new Date());
                            e.printStackTrace();
                        }
                        transInfo.setAccountName(transferInfoDTO.getAccountName());
                        transInfo.setAmountPayed(transferInfoDTO.getAmountPayed());
                        transInfo.setBank(transferInfoDTO.getBank());
                        transInfo.setPaymentNote(transferInfoDTO.getPaymentNote());
                        transferInfoRepository.save(transInfo);
                    }
                }
                else {
                    //means admin has updated tp PC. it cant be updated....
                    return;
                }
            }
            else {
                throw new WawoohException();
            }

        }
    }

    @Override
    public List<TransferInfoDTO> getOrderTransferInfo(String orderNum) {
        List<TransferInfoDTO> transferInfoDTOS= new ArrayList<>();
        Orders orders = orderRepository.findByOrderNum(orderNum);
        if(orders != null){
            List<TransferInfo> transferInfo = transferInfoRepository.findByOrders(orders);
            transferInfoDTOS=convertTransferInfoEntitiesToDTOs(transferInfo);
        }
        return transferInfoDTOS;
    }

    private TransferInfoDTO convertTransferInfoToDTO(TransferInfo transferInfo) {
        TransferInfoDTO transferInfoDTO = new TransferInfoDTO();
        transferInfoDTO.setId(transferInfo.id);
        if(transferInfo.getPaymentDate() != null) {
            transferInfoDTO.setPaymentDate(formatter.format(transferInfo.getPaymentDate()));
        }
        transferInfoDTO.setAccountName(transferInfo.getAccountName());
        transferInfoDTO.setAmountPayed(transferInfo.getAmountPayed());
        transferInfoDTO.setBank(transferInfo.getBank());
        transferInfoDTO.setPaymentNote(transferInfo.getPaymentNote());
        transferInfoDTO.setOrderNum(transferInfo.getOrders().getOrderNum());
        return transferInfoDTO;
    }

    @Override
    public List<TransferInfoDTO> getAllTransferInfo() {

        List<TransferInfo> transferInfos = transferInfoRepository.findAll();
        return convertTransferInfoEntitiesToDTOs(transferInfos);
    }

    @Override
    public TransferInfoDTO getTransferInfoById(Long id) {
        return convertTransferInfoToDTO(transferInfoRepository.findOne(id));
    }

    private List<TransferInfoDTO> convertTransferInfoEntitiesToDTOs(List<TransferInfo> transferInfos) {
        List<TransferInfoDTO> transferInfoDTOS = new ArrayList<>() ;
        for (TransferInfo transferInfo : transferInfos) {
            TransferInfoDTO transferInfoDTO = convertTransferInfoToDTO(transferInfo);
            transferInfoDTOS.add(transferInfoDTO);
        }
        return transferInfoDTOS;
    }

}
