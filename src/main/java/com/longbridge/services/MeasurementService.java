package com.longbridge.services;

import com.longbridge.models.Measurement;
import com.longbridge.models.User;

import java.util.List;

/**
 * Created by Longbridge on 12/03/2018.
 */
public interface MeasurementService {

    void customize(Measurement measurement);
    void updateCustomization(Measurement measurement);
    boolean deleteMeasurement(Long measurementId);
    Measurement getMeasurementById(Long measurementId);
    List<Measurement> getUserMeasurement();

    String getMandatoryMeasurement(Long productId);

}
