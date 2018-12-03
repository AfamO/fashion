package com.longbridge.dto;

/**
 * Created by Longbridge on 03/12/2018.
 */
public class BespokeRequestUpdateDTO {
    private Long id;
    private String bespokeEligibleFlag;
    private String reason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBespokeEligibleFlag() {
        return bespokeEligibleFlag;
    }

    public void setBespokeEligibleFlag(String bespokeEligibleFlag) {
        this.bespokeEligibleFlag = bespokeEligibleFlag;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BespokeRequestUpdateDTO(Long id, String bespokeEligibleFlag, String reason) {
        this.id = id;
        this.bespokeEligibleFlag = bespokeEligibleFlag;
        this.reason = reason;
    }

    public BespokeRequestUpdateDTO() {
    }
}
