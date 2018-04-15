package com.dev.apds.dao;

import com.dev.apds.models.LogData;

import java.util.List;

public interface SensorDao {
    boolean checkSensorById(String sensorId);
    int getSensorStatusById(String sensorId);
    void insertSensorLog(String sensorId, double logValue, int logType);
    int insertUserSensor(String msisdn, String sensorId);
    List<LogData> getLastLogDataList(String sensorId);
    void insertUserSensorDetail(int userSensorId, int type);
}
