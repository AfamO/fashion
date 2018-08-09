package com.longbridge.services;

import com.longbridge.dto.AdminDashBoardDTO;
import com.longbridge.models.Response;
import com.longbridge.models.User;

/**
 * Created by Longbridge on 08/08/2018.
 */
public interface AdminService {
    AdminDashBoardDTO getDashboardData(User user);
}
