package com.longbridge.services;

import com.longbridge.models.AnonymousUser;
import com.longbridge.models.Response;

public interface AnonymousUserService {

    Response createAnonymousUser(AnonymousUser anonymousUser);
    Response updateAnonymousUser(AnonymousUser anonymousUser);
    Response getAnonymousUserDetails(Long id);
}
