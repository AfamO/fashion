package com.longbridge.security.controller;

import com.longbridge.security.JwtTokenUtil;
import com.longbridge.security.JwtUser;
import com.longbridge.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        if (token!=null) {
            System.out.println("Token "+token.replace("Bearer ",""));
            String username = jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ",""));
            System.out.println("username "+username );
            JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
            System.out.println("User is  "+user );
            return user;
        }
        else{return null;}
    }
    @RequestMapping(value = "userDetails", method = RequestMethod.GET)
    public JwtUser getAuthenticatedUserDetails(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        JwtUser user = null;
        if(token!=null) {
            String username = jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ", ""));
            user = (JwtUser) userDetailsService.loadUserByUsername(username);
        }
        return user;
    }

}


