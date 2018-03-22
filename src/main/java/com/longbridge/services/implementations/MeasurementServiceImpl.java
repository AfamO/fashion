package com.longbridge.services.implementations;

import com.longbridge.exception.WawoohException;
import com.longbridge.models.Measurement;
import com.longbridge.models.User;
import com.longbridge.repository.MeasurementRepository;
import com.longbridge.services.MeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Longbridge on 12/03/2018.
 */
@Service
public class MeasurementServiceImpl implements MeasurementService {
    @Autowired
    MeasurementRepository measurementRepository;

    @Override
    public void customize(User userTemp, Measurement measurement) {
        try {
            Date date = new Date();
            measurement.setUser(userTemp);
            measurement.setCreatedOn(date);
            measurement.setUpdatedOn(date);
            measurementRepository.save(measurement);

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }

    }

    @Override
    public void deleteMeasurement(User userTemp, Long measurementId) {
        try {
           //Measurement measurement = measurementRepository.findOne(measurementId);
            measurementRepository.delete(measurementId);

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
            List<Measurement> m = measurementRepository.findByUser(user);
            return m;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public Measurement getMeasurementByName(User user, String measurementName) {
        try {
            Measurement m = measurementRepository.findByUserAndName(user,measurementName);
            return m;

        } catch (Exception e) {
            e.printStackTrace();
            throw new WawoohException();
        }
    }
}
