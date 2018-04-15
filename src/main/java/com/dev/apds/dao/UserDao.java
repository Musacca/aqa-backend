package com.dev.apds.dao;

import com.dev.apds.models.LogData;
import com.dev.apds.models.SensorTypeData;
import com.dev.apds.models.User;
import com.dev.apds.models.UserSensorData;

import java.util.Date;
import java.util.List;

public interface UserDao {

    void registerUserDetails(String uuid, String sensorId, String fullName,
                             String placeName, String phoneNumber1, String phoneNumber2,
                             String location, String locationName);

    void insertNewUser(String msisdn);
    User getUserByUUID(String uuid);
    boolean checkUserByMsisdn(String msisdn, String sensorId);
    void updateUserUUIDByMSISDN(String msisdn, String uuid);
    boolean checkUserByUUID(String uuid);
    boolean checkUserAndSensor(String uuid, String sensorId);
    boolean checkUserAndSensorByMsisdn(String msisdn, String sensorId);
    boolean checkUserByMsisdn(String msisdn);
    String getUUIDByMsisdn(String msisdn);
    List<UserSensorData> getUserSensorDataByUUID(String uuid);
    UserSensorData getUserSensorDataForOneSensor(String uuid, String sensorId);
    List<SensorTypeData> getUserSensorTypeDataByUserSensorId(int userSensorId);
    void updateUserSensorDataTypeNotifySettings(String uuid, String sensorId, int type,
                                                double minValue, double maxValue, int status);
    void updateUserSensorDataTypeCallSettings(String uuid, String sensorId, int type,
                                                double minValue, double maxValue, int status);
    void updateUserSensorSetting(String uuid, String sensorId, int status);
    List<LogData> getFilteredData(String sensorId, int type, double minVale, double maxValue, Date startDate, Date endDate, int page);
    List<LogData> getDaysLog(String sensorId, Date selectedDate);
}
