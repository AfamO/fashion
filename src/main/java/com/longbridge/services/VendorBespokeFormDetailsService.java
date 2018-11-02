package com.longbridge.services;

import com.longbridge.models.VendorBespokeFormDetails;

import java.util.List;

/**
 * Created by Longbridge on 01/11/2018.
 */
public interface VendorBespokeFormDetailsService {
    String add(VendorBespokeFormDetails vendorBespokeFormDetails);
    List<VendorBespokeFormDetails> getAll();
}
