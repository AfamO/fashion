package com.longbridge.services;

import com.longbridge.models.Measurement;
import com.longbridge.models.User;

/**
 * Created by Longbridge on 12/03/2018.
 */
public interface MeasurementService {

    void customize(User userTemp, Measurement measurement);
    Measurement getMeasurementById(User user, Long measurementId);
    Measurement getMeasurementByName(User user, String measurementName);
}
