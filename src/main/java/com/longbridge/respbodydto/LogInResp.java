package com.longbridge.respbodydto;

/**
 * Created by Longbridge on 30/04/2018.
 */
public class LogInResp {
    private String token;
    private int role;


    public LogInResp(String token, int role) {
        this.token = token;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public LogInResp() {
    }
}
