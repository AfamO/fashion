package com.longbridge.dto;

/**
 * Created by Longbridge on 03/10/2018.
 */
public class VerifyDTO {
    private String unverifyReason;
    private Long id;
    private String flag;

    public VerifyDTO() {
    }

    public String getUnverifyReason() {
        return unverifyReason;
    }

    public void setUnverifyReason(String unverifyReason) {
        this.unverifyReason = unverifyReason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
