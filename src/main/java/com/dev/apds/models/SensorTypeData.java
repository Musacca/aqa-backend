package com.dev.apds.models;

public class SensorTypeData {


    private int id;
    private int type;
    private double minValueForCall;
    private double maxValueForCall;
    private double minValueForNotify;
    private double maxValueForNotify;
    private int callStatus;
    private int notifyStatus;


    public SensorTypeData() {
    }

    public SensorTypeData(int id, int type, double minValueForCall, double maxValueForCall, double minValueForNotify, double maxValueForNotify, int callStatus, int notifyStatus) {
        this.id = id;
        this.type = type;
        this.minValueForCall = minValueForCall;
        this.maxValueForCall = maxValueForCall;
        this.minValueForNotify = minValueForNotify;
        this.maxValueForNotify = maxValueForNotify;
        this.callStatus = callStatus;
        this.notifyStatus = notifyStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getMinValueForCall() {
        return minValueForCall;
    }

    public void setMinValueForCall(double minValueForCall) {
        this.minValueForCall = minValueForCall;
    }

    public double getMaxValueForCall() {
        return maxValueForCall;
    }

    public void setMaxValueForCall(double maxValueForCall) {
        this.maxValueForCall = maxValueForCall;
    }

    public double getMinValueForNotify() {
        return minValueForNotify;
    }

    public void setMinValueForNotify(double minValueForNotify) {
        this.minValueForNotify = minValueForNotify;
    }

    public double getMaxValueForNotify() {
        return maxValueForNotify;
    }

    public void setMaxValueForNotify(double maxValueForNotify) {
        this.maxValueForNotify = maxValueForNotify;
    }

    public int getCallStatus() {
        return callStatus;
    }

    public void setCallStatus(int callStatus) {
        this.callStatus = callStatus;
    }

    public int getNotifyStatus() {
        return notifyStatus;
    }

    public void setNotifyStatus(int notifyStatus) {
        this.notifyStatus = notifyStatus;
    }

    @Override
    public String toString() {
        return "SensorTypeData{" +
                "id=" + id +
                ", type=" + type +
                ", minValueForCall=" + minValueForCall +
                ", maxValueForCall=" + maxValueForCall +
                ", minValueForNotify=" + minValueForNotify +
                ", maxValueForNotify=" + maxValueForNotify +
                ", callStatus=" + callStatus +
                ", notifyStatus=" + notifyStatus +
                '}';
    }
}
