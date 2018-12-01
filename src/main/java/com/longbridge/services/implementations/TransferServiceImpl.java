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
                    TransferInfo transferInfo = transferInfoRepository.findByOrders(orders);
                    if(transferInfo != null){
                        transferInfo.setPaymentDate(new Date(transferInfoDTO.getPaymentDate()));
                        transferInfo.setAccountName(transferInfoDTO.getAccountName());
                        transferInfo.setAmountPayed(transferInfoDTO.getAmountPayed());
                        transferInfo.setBank(transferInfoDTO.getBank());
                        transferInfo.setPaymentNote(transferInfoDTO.getPaymentNote());
                    }
                    else {
                        transferInfo = new TransferInfo();
                        transferInfo.setOrders(orders);
//                        transferInfo.setPaymentDate(new Date(transferInfoDTO.getPaymentDate()));
                        try {
                            transferInfo.setPaymentDate(dateFormatter.parse(transferInfoDTO.getPaymentDate()));
                        } catch (ParseException e) {
                            transferInfo.setPaymentDate(new Date());
                            e.printStackTrace();
                        }
                        transferInfo.setAccountName(transferInfoDTO.getAccountName());
                        transferInfo.setAmountPayed(transferInfoDTO.getAmountPayed());
                        transferInfo.setBank(transferInfoDTO.getBank());
                        transferInfo.setPaymentNote(transferInfoDTO.getPaymentNote());
                    }

                    transferInfoRepository.save(transferInfo);
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
    public TransferInfoDTO getOrderTransferInfo(String orderNum) {

        Orders orders = orderRepository.findByOrderNum(orderNum);
        if(orders != null){
            TransferInfo transferInfo = transferInfoRepository.findByOrders(orders);
            TransferInfoDTO transferInfoDTO = new TransferInfoDTO();
            transferInfoDTO.setPaymentDate(formatter.format(transferInfo.getPaymentDate()));
            transferInfoDTO.setAccountName(transferInfo.getAccountName());
            transferInfoDTO.setAmountPayed(transferInfo.getAmountPayed());
            transferInfoDTO.setBank(transferInfo.getBank());
            transferInfoDTO.setPaymentNote(transferInfo.getPaymentNote());

            return transferInfoDTO;
        }
        return null;
    }

    @Override
    public List<TransferInfoDTO> getAllTransferInfo() {

        List<TransferInfo> transferInfos = transferInfoRepository.findAll();
        List<TransferInfoDTO> transferInfoDTOS = new ArrayList<TransferInfoDTO>();
        for (TransferInfo transferInfo : transferInfos) {
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

            transferInfoDTOS.add(transferInfoDTO);
        }

        return transferInfoDTOS;
    }

}
