package com.longbridge.services.implementations;

import com.longbridge.models.AnonymousUser;
import com.longbridge.models.Response;
import com.longbridge.repository.AnonymousUserRepository;
import com.longbridge.services.AnonymousUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnonymousUserServiceImpl implements AnonymousUserService {

    @Autowired
    AnonymousUserRepository anonymousUserRepository;

    @Override
    public Response createAnonymousUser(AnonymousUser anonymousUser) {

        if(anonymousUser.getEmail() == null || anonymousUser.getPhoneNo() == null || anonymousUser.getCity() == null){
            return new Response("99", "Email, Phone number and City are required", null);
        }

        anonymousUserRepository.save(anonymousUser);
        return new Response("00", "Operation successful", anonymousUser.id);
    }

    @Override
    public Response updateAnonymousUser(AnonymousUser anonymousUser) {

        AnonymousUser currentAnonymousUser = anonymousUserRepository.findOne(anonymousUser.id);

        if(currentAnonymousUser == null){
            return new Response("99", "", null);
        }

        if(anonymousUser.getEmail() == null || anonymousUser.getPhoneNo() == null || anonymousUser.getCity() == null){
            return new Response("99", "Email, Phone number and City are required", null);
        }

        currentAnonymousUser.setEmail(anonymousUser.getEmail());
        currentAnonymousUser.setPhoneNo(anonymousUser.getPhoneNo());
        currentAnonymousUser.setCountry(anonymousUser.getCountry());
        currentAnonymousUser.setState(anonymousUser.getState());
        currentAnonymousUser.setCity(anonymousUser.getCity());
        currentAnonymousUser.setLocalGovt(anonymousUser.getLocalGovt());
        currentAnonymousUser.setPostalCode(anonymousUser.getPostalCode());
        anonymousUserRepository.save(currentAnonymousUser);

        return new Response("00", "Operation successful", null);
    }

    @Override
    public Response getAnonymousUserDetails(Long id) {

        AnonymousUser anonymousUser = anonymousUserRepository.findOne(id);

        if(anonymousUser == null){
            return new Response("99", "Anonymous user not found", null);
        }else{
            return new Response("00", "Operation successful", anonymousUser);
        }
    }
}
