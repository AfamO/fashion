package com.longbridge.services;


import com.longbridge.dto.UserEmailTokenDTO;
import com.longbridge.models.Response;
import com.longbridge.models.Token;
import com.longbridge.models.User;
import org.springframework.mobile.device.Device;

/**
 * Created by Longbridge on 01/11/2017.
 */
public interface TokenService {
    Token saveToken(Token token);

    Token getToken(User user);

    Response validateToken(UserEmailTokenDTO userEmailTokenDTO, Device device);

    boolean isValidated(User user, String token);

}
