package com.longbridge.services.implementations;


import com.longbridge.exception.WawoohException;
import com.longbridge.models.Address;
import com.longbridge.models.User;
import com.longbridge.repository.AddressRepository;
import com.longbridge.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Longbridge on 12/01/2018.
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public void addAddress(Address address, User user) {
        try {
            Date date = new Date();
            address.setUser(user);
            address.setCreatedOn(date);
            address.setUpdatedOn(date);
            addressRepository.save(address);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }

    }

    @Override
    public void updateAddress(Address address, User user) {
        try {
            Date date = new Date();
            Address add = addressRepository.findOne(address.id);
            add.setUser(user);
            add.setAddress(address.getAddress());
            add.setCountry(address.getCountry());
            add.setFirstName(address.getFirstName());
            add.setLastName(address.getLastName());
            add.setZipCode(address.getZipCode());
            add.setCity(address.getCity());
            add.setLocalGovt(address.getLocalGovt());
            add.setPostalCode(address.getPostalCode());
            add.setPhoneNo(address.getPhoneNo());
            add.setState(address.getState());
            add.setPreferred(address.getPreferred());
            add.setUpdatedOn(date);
            add.setUser(user);
            addressRepository.save(add);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public void deleteAddress(Long id) {
        try {
            addressRepository.delete(id);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }

    @Override
    public List<Address> getAddress(User user) {
        try {
            return addressRepository.findByUser(user);
        }catch (Exception ex){
            ex.printStackTrace();
            throw new WawoohException();
        }
    }
}
