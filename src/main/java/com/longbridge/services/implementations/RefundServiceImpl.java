package com.longbridge.services.implementations;

import com.longbridge.exception.WawoohException;
import com.longbridge.models.Refund;
import com.longbridge.repository.RefundRepository;
import com.longbridge.services.RefundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Longbridge on 09/08/2018.
 */
@Service
public class RefundServiceImpl implements RefundService{

    @Autowired
    RefundRepository refundRepository;

    @Override
    public List<Refund> getAll() {
        try {
            return refundRepository.findAll();
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }

    }
}
