package com.dev.apds.models;

import java.util.Date;
import java.util.List;

public class UserSensorData {
    private int id;
    private String msisdn;
    private String sensorId;
    private String name;
    private String placeName;
    private String phoneNumber1;
    private String phoneNumber2;
    private String locationName;
    private String location;
    private int chargedStatus;
    private Date lastChargedDate;
    private int activeDuration;
    private int status;
    List<SensorTypeData> typeList;


    public UserSensorData() {
    }

    public UserSensorData(int id, String msisdn, String sensorId, String name, String placeName, String phoneNumber1, String phoneNumber2, String locationName, String location, int chargedStatus, Date lastChargedDate, int activeDuration, int status) {
        this.id = id;
        this.msisdn = msisdn;
        this.sensorId = sensorId;
        this.name = name;
        this.placeName = placeName;
        this.phoneNumber1 = phoneNumber1;
        this.phoneNumber2 = phoneNumber2;
        this.locationName = locationName;
        this.location = location;
        this.chargedStatus = chargedStatus;
        this.lastChargedDate = lastChargedDate;
        this.activeDuration = activeDuration;
        this.status = status;
    }

    public UserSensorData(int id, String msisdn, String sensorId, String name, String placeName, String phoneNumber1, String phoneNumber2, String locationName, String location, int chargedStatus, Date lastChargedDate, int activeDuration, int status, List<SensorTypeData> typeList) {
        this.id = id;
        this.msisdn = msisdn;
        this.sensorId = sensorId;
        this.name = name;
        this.placeName = placeName;
        this.phoneNumber1 = phoneNumber1;
        this.phoneNumber2 = phoneNumber2;
        this.locationName = locationName;
        this.location = location;
        this.chargedStatus = chargedStatus;
        this.lastChargedDate = lastChargedDate;
        this.activeDuration = activeDuration;
        this.status = status;
        this.typeList = typeList;
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

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getChargedStatus() {
        return chargedStatus;
    }

    public void setChargedStatus(int chargedStatus) {
        this.chargedStatus = chargedStatus;
    }

    public Date getLastChargedDate() {
        return lastChargedDate;
    }

    public void setLastChargedDate(Date lastChargedDate) {
        this.lastChargedDate = lastChargedDate;
    }

    public int getActiveDuration() {
        return activeDuration;
    }

    public void setActiveDuration(int activeDuration) {
        this.activeDuration = activeDuration;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<SensorTypeData> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<SensorTypeData> typeList) {
        this.typeList = typeList;
    }

    @Override
    public String toString() {
        return "UserSensorData{" +
                "id=" + id +
                ", msisdn='" + msisdn + '\'' +
                ", sensorId='" + sensorId + '\'' +
                ", name='" + name + '\'' +
                ", placeName='" + placeName + '\'' +
                ", phoneNumber1='" + phoneNumber1 + '\'' +
                ", phoneNumber2='" + phoneNumber2 + '\'' +
                ", locationName='" + locationName + '\'' +
                ", location='" + location + '\'' +
                ", chargedStatus=" + chargedStatus +
                ", lastChargedDate=" + lastChargedDate +
                ", activeDuration=" + activeDuration +
                ", status=" + status +
                ", typeList=" + typeList +
                '}';
    }
}
