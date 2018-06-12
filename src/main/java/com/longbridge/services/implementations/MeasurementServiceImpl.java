package com.longbridge.services.implementations;

import com.longbridge.exception.WawoohException;
import com.longbridge.models.Measurement;
import com.longbridge.models.User;
import com.longbridge.repository.CartRepository;
import com.longbridge.repository.ItemRepository;
import com.longbridge.repository.MeasurementRepository;
import com.longbridge.services.MeasurementService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.factory.annotation.Autowired;
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
    CartRepository cartRepository;
    @Autowired
    ItemRepository itemRepository;

    @Override
    public void customize(User userTemp, Measurement measurement) {
        try {
            Date date = new Date();
            measurement.setUser(userTemp);
            measurement.setCreatedOn(date);
            measurement.setUpdatedOn(date);
            Measurement measurement1 = measurementRepository.findByUserAndNameAndDelFlag(userTemp,measurement.getName(),"N");
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
    public void updateCustomization(User userTemp, Measurement measurement) {
        try {
            Date date = new Date();
            measurement.setUser(userTemp);
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
    public boolean deleteMeasurement(User userTemp, Long measurementId) {
        try {
            if(cartRepository.countByMeasurementId(measurementId) > 0 || itemRepository.countByMeasurementIdAndDeliveryStatusNot(measurementId, "D") > 0){
                return false;
            }
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
    public Measurement getMeasurementById(User user, Long measurementId) {
        try {
            Measurement m = measurementRepository.findOne(measurementId);
            return m;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }

    @Override
    public List<Measurement> getUserMeasurement(User user) {
        try {
            List<Measurement> m = measurementRepository.findByUserAndDelFlag(user, "N");
            return m;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public Measurement getMeasurementByName(User user, String measurementName) {
        try {
            Measurement m = measurementRepository.findByUserAndNameAndDelFlag(user,measurementName, "N");
            return m;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }
}
