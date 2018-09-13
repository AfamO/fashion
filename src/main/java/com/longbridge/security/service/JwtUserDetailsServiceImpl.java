package com.longbridge.security.service;

import com.longbridge.security.JwtUser;
import com.longbridge.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;



@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JwtUser user = null;
         com.longbridge.models.User user1 =  userRepository.findByEmail(username);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        String role = "";
        if(user1.role.equalsIgnoreCase("admin")){
            role = "ROLE_ADMIN";
        }
        if(user1.role.equalsIgnoreCase("designer")){
            role = "ROLE_DESIGNER";
        }
        if(user1.role.equalsIgnoreCase("user")){
            role = "ROLE_USER";
        }
        if(user1.role.equalsIgnoreCase("qa")){
            role="ROLE_QA";
        }

        authorities.add(new SimpleGrantedAuthority(role));

         if(user1!=null){
             user = new JwtUser(user1.email,user1.password,authorities,true,null);
         }
        if (user1 == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return user;
        }
    }


}
