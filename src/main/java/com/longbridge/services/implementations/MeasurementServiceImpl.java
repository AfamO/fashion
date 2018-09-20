package com.longbridge.services.implementations;

import com.longbridge.exception.WawoohException;
import com.longbridge.models.Measurement;
import com.longbridge.models.User;
import com.longbridge.repository.MeasurementRepository;
import com.longbridge.repository.ProductRepository;
import com.longbridge.security.JwtUser;
import com.longbridge.services.MeasurementService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


/**
 * Created by Longbridge on 12/03/2018.
 */
@Service
public class MeasurementServiceImpl implements MeasurementService {
    @Autowired
    MeasurementRepository measurementRepository;
    @Autowired
    ProductRepository productRepository;


    @Override
    public void customize(Measurement measurement) {
        try {
            Date date = new Date();
            measurement.setUser(getCurrentUser());
            measurement.setCreatedOn(date);
            measurement.setUpdatedOn(date);
            Measurement measurement1 = measurementRepository.findByUserAndNameAndDelFlag(getCurrentUser(),measurement.getName(),"N");
            if(measurement1 != null){
                measurement.setName(measurement.getName()+"1");
            }
            measurementRepository.save(measurement);

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }

    @Override
    public void updateCustomization( Measurement measurement) {
        try {
            Date date = new Date();
            measurement.setUser(getCurrentUser());
            Measurement measurementTemp = measurementRepository.findOne(measurement.id);
            BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
            BeanUtils.copyProperties(measurementTemp,measurement);
            measurementTemp.setUpdatedOn(date);
            measurementRepository.save(measurementTemp);

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public boolean deleteMeasurement(Long measurementId) {
        try {

            Measurement measurement = measurementRepository.findOne(measurementId);
            measurement.setDelFlag("Y");
            measurementRepository.save(measurement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }

    @Override
    public Measurement getMeasurementById(Long measurementId) {
        try {
            Measurement m = measurementRepository.findOne(measurementId);
            return m;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }

    @Override
    public List<Measurement> getUserMeasurement() {
        try {
            List<Measurement> m = measurementRepository.findByUserAndDelFlag(getCurrentUser(), "N");
            return m;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public String getMandatoryMeasurement(Long productId) {
        try {
            String m = productRepository.getMandatoryMeasurements(productId);
            return m;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }


    private User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        return jwtUser.getUser();
    }

}
