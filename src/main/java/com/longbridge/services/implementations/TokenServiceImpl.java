package com.longbridge.services.implementations;

import com.longbridge.dto.UserEmailTokenDTO;
import com.longbridge.models.Response;
import com.longbridge.models.Token;
import com.longbridge.models.User;
import com.longbridge.repository.TokenRepository;
import com.longbridge.security.JwtTokenUtil;
import com.longbridge.security.repository.UserRepository;
import com.longbridge.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Longbridge on 01/11/2017.
 */
@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @Autowired
    private UserDetailsService userDetailsService;

    private Locale locale = LocaleContextHolder.getLocale();

    @Override
    public Token saveToken(Token token) {
        return tokenRepository.save(token);
    }

    @Override
    public Token getToken(User host) {
        return tokenRepository.findByUser(host);
    }

    @Override
    public Response validateToken(UserEmailTokenDTO userEmailTokenDTO, Device device) {
        Map<String, Object> responseMap = new HashMap();
        User user = userRepository.findByEmail(userEmailTokenDTO.getEmail());
        System.out.println(userEmailTokenDTO.getEmail());
        Token token1 = tokenRepository.findByUserAndToken(user,userEmailTokenDTO.getToken());
        System.out.println(token1.getToken());
        Date date = new Date();
        if (token1 == null) {
            Response response = new Response("99", "Error occurred while validating token", responseMap);
            return response;
        }
        else {
            if(token1.isValidated()){

                Response response = new Response("56", "Token already validated", responseMap);
                return response;
            }
            else {
                System.out.println("Token validated => yes");
                token1.setValidated(true);
                tokenRepository.save(token1);
                user.setActivationDate(date);
                user.setUpdatedOn(date);
                user.setActivationFlag("Y");
                userRepository.save(user);

                /*
                    Generating Token for user and this will be required for all request.
                 */
                final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
                System.out.println("userdetails is "+userDetails.toString());
                final String token = jwtTokenUtil.generateToken(userDetails, device);
                System.out.println("Token is "+token);
                Response response = new Response("00", "Token successfully validated", token);
                return response;
            }
        }

    }


    @Override
    public boolean isValidated(User user, String token) {
        try {

           return  tokenRepository.findByUserAndTokenAndValidated(user, token,true) ? true : false ;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

    }

}
