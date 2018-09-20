package com.longbridge.services;

import com.longbridge.models.Address;
import com.longbridge.models.User;

import java.util.List;

/**
 * Created by Longbridge on 12/01/2018.
 */
public interface AddressService {
    void addAddress(Address address);

    void updateAddress(Address address);

    void deleteAddress(Long id);

    List<Address> getAddress();
}
