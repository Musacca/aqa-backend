package com.dev.apds.models;

import java.util.Date;

public class User {
    private int id;
    private String msisdn;
    private int status;
    private String uuid;

    public User() {
    }

    public User(int id, String msisdn, int status, String uuid) {
        this.id = id;
        this.msisdn = msisdn;
        this.status = status;
        this.uuid = uuid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", msisdn='" + msisdn + '\'' +
                ", status=" + status +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
