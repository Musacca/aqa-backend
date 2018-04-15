package com.dev.apds.models;

import java.util.Date;

public class OTPObject {
    private String otp;
    private Date createdDate;

    public OTPObject() {
    }

    public OTPObject(String otp, Date createdDate) {
        this.otp = otp;
        this.createdDate = createdDate;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "OTPObject{" +
                "otp='" + otp + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }
}
