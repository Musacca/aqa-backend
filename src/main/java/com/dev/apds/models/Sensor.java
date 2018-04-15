package com.dev.apds.models;

import javax.xml.crypto.Data;

public class Sensor {
    private int id;
    private String sensorId;
    private Data lastEnabledDate;
    private int status;

    public Sensor() {
    }

    public Sensor(int id, String sensorId, Data lastEnabledDate, int status) {
        this.id = id;
        this.sensorId = sensorId;
        this.lastEnabledDate = lastEnabledDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public Data getLastEnabledDate() {
        return lastEnabledDate;
    }

    public void setLastEnabledDate(Data lastEnabledDate) {
        this.lastEnabledDate = lastEnabledDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", sensorId='" + sensorId + '\'' +
                ", lastEnabledDate=" + lastEnabledDate +
                ", status=" + status +
                '}';
    }
}
