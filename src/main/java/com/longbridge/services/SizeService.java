package com.longbridge.services;

import com.longbridge.models.Response;
import com.longbridge.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Longbridge on 03/07/2018.
 */
@Service
public class SizeService {

    @Autowired
    SizeRepository sizeRepository;


    public Response getSizes(){
        Map<String,Object> responseMap = new HashMap();
        try {

            Response response= new Response("00","Successful",sizeRepository.findAll());
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            Response response = new Response("99","Error occured internally",responseMap);
            return response;

        }
    }

}
