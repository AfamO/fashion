package com.longbridge.security.service;

import com.longbridge.models.User;
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
         User user1 =  userRepository.findByEmail(username);
        if (user1 == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        else {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        String role = "";
        if(user1.getRole().equalsIgnoreCase("admin")){
            role = "ROLE_ADMIN";
        }
        if(user1.getRole().equalsIgnoreCase("designer")){
            role = "ROLE_DESIGNER";
        }
        if(user1.getRole().equalsIgnoreCase("user")){
            role = "ROLE_USER";
        }
        if(user1.getRole().equalsIgnoreCase("qa")){
            role="ROLE_QA";
        }

        authorities.add(new SimpleGrantedAuthority(role));
        user = new JwtUser(user1.getEmail(),user1.getPassword(),authorities,true,null);
         }
        return user;
    }


}
