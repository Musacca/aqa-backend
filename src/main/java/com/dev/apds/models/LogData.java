package com.dev.apds.models;

import java.util.Date;

public class LogData {
    int id;
    String sensorId;
    double logValue;
    int logType;
    Date logTime;
    int status;

    public LogData() {
    }

    public LogData(int id, String sensorId, double logValue, int logType, Date logTime, int status) {
        this.id = id;
        this.sensorId = sensorId;
        this.logValue = logValue;
        this.logType = logType;
        this.logTime = logTime;
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

    public double getLogValue() {
        return logValue;
    }

    public void setLogValue(double logValue) {
        this.logValue = logValue;
    }

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LogData{" +
                "id=" + id +
                ", sensorId='" + sensorId + '\'' +
                ", logValue=" + logValue +
                ", logType=" + logType +
                ", logTime=" + logTime +
                ", status=" + status +
                '}';
    }
}
