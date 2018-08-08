package com.longbridge.services.implementations;

import com.longbridge.models.Response;
import com.longbridge.models.User;
import com.longbridge.services.AdminService;
import org.springframework.stereotype.Service;

/**
 * Created by Longbridge on 08/08/2018.
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Override
    public Response getDashboardData(User user) {
        return null;
    }
}
