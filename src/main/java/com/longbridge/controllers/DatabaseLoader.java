package com.longbridge.controllers;

import com.longbridge.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by longbridge on 11/4/17.
 */
@Component
public class DatabaseLoader implements CommandLineRunner{
    @Autowired
    UserRepository helper;

    @Override
    public void run(String... strings) throws Exception {
//        helper.save(new User("Saheed","Yusuf","zeed@gmail.com"));
//        Logger.getLogger("user").info("Sizse is " + helper.findByEmail("zeed@gmail.com"));
    }
}
