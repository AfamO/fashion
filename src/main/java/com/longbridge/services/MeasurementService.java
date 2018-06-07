package com.longbridge.services;

import com.longbridge.models.Measurement;
import com.longbridge.models.User;

import java.util.List;

/**
 * Created by Longbridge on 12/03/2018.
 */
public interface MeasurementService {

    void customize(User userTemp, Measurement measurement);
    void updateCustomization(User userTemp, Measurement measurement);
    boolean deleteMeasurement(User userTemp, Long measurementId);
    Measurement getMeasurementById(User user, Long measurementId);
    List<Measurement> getUserMeasurement(User user);
    Measurement getMeasurementByName(User user, String measurementName);
}
