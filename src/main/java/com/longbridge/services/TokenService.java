package com.longbridge.services;


import com.longbridge.models.Response;
import com.longbridge.models.Token;
import com.longbridge.models.User;

/**
 * Created by Longbridge on 01/11/2017.
 */
public interface TokenService {
    Token saveToken(Token token);

    Token getToken(User user);

    Response validateToken(User user, String token);

    boolean isValidated(User user, String token);

}
