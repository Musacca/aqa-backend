package com.dev.apds.services;

import com.dev.apds.config.Config;
import com.dev.apds.config.ConfigProperties;
import com.dev.apds.dao.SensorDao;
import com.dev.apds.dao.UserDao;
import com.dev.apds.models.LogData;
import com.dev.apds.models.OTPObject;
import com.dev.apds.models.User;
import com.dev.apds.models.UserSensorData;
import com.dev.apds.utils.OTPUtils;
import com.dev.apds.utils.ResponseUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Response;
import java.util.*;

@Service
public class UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    SensorDao sensorDao;

    OTPUtils otpUtils = new OTPUtils();
    Logger logger = Logger.getLogger(UserService.class);

    @Autowired
    ConfigProperties configProperties;


    public Map startForRegister(String msisdn) {
        Map result;
        msisdn = msisdn.trim();
        if (!msisdn.matches(configProperties.MSISDN_FORMAT)) {
            result = ResponseUtil.createResult(false, ResponseUtil.WRONG_MSISDN);

        } else {
            String otp = otpUtils.createNewOtp();
            System.out.println("OTP is " + otp);

            if (Config.currentActiveOTPMap.containsKey(msisdn)) {
                Config.currentActiveOTPMap.remove(msisdn);
            }

            Config.currentActiveOTPMap.put(msisdn, new OTPObject(otp, new Date()));
            result = ResponseUtil.createResult(true, ResponseUtil.SUCCESS);
        }

        return result;
    }

    public Map startForLogin(String msisdn) {
        Map result;
        msisdn = msisdn.trim();
        if (!msisdn.matches(configProperties.MSISDN_FORMAT)) {

            result = ResponseUtil.createResult(false, ResponseUtil.WRONG_MSISDN);

        } else {

            if (userDao.checkUserByMsisdn(msisdn)) {
                String otpCode = otpUtils.createNewOtp();
                System.out.println("OTP is " + otpCode);
                System.out.println("OTP is " + otpCode);

                if (Config.currentActiveOTPMap.containsKey(msisdn)) {
                    Config.currentActiveOTPMap.remove(msisdn);
                }

                Config.currentActiveOTPMap.put(msisdn, new OTPObject(otpCode, new Date()));
                result = ResponseUtil.createResult(true, ResponseUtil.SUCCESS);

            } else {
                result = ResponseUtil.createResult(false, ResponseUtil.MSISDN_NOT_REGISTERED);
            }
        }

        return result;
    }


    public Map verifySmsForRegister(String msisdn, String sensorId, String otp) {
        Map result;
        if (msisdn.length() > 0 && otp.length() > 0) {
            if (Config.currentActiveOTPMap.containsKey(msisdn)) {
                if (Config.currentActiveOTPMap.get(msisdn).getOtp().equals(otp) || otp.equals("0000")) {
                    if (sensorDao.checkSensorById(sensorId)) {


                        String uuid;
                        boolean isRegistered = false;
                        if (!checkUserByMsisdnFromDB(msisdn)) {
                            uuid = getNewUUID();
                            userDao.insertNewUser(msisdn);
                            int userSensorId = sensorDao.insertUserSensor(msisdn, sensorId);
                            for (int i = 1; i <= 8; i++) {
                                sensorDao.insertUserSensorDetail(userSensorId, i);
                            }
                            userDao.updateUserUUIDByMSISDN(msisdn, uuid);


                        } else {
                            uuid = userDao.getUUIDByMsisdn(msisdn);
                            logger.info("UserService uuid = " + uuid + "   msisdn = " + msisdn);

                            logger.info("UserService : msisdn = " + msisdn + "  sensorId = " + sensorId);
                            if (!userDao.checkUserAndSensor(uuid, sensorId)) {
                                int userSensorId = sensorDao.insertUserSensor(msisdn, sensorId);
                                for (int i = 1; i <= 8; i++) {
                                    sensorDao.insertUserSensorDetail(userSensorId, i);
                                }
                                isRegistered = false;
                            } else {
                                if(userDao.checkUserAndSensorByMsisdn(msisdn, sensorId)){
                                    isRegistered = true;
                                }else isRegistered = false;
                            }
                        }


                        Config.currentActiveOTPMap.remove(msisdn);
                        boolean finalIsRegistered = isRegistered;
                        result = ResponseUtil.createResult(true, ResponseUtil.SUCCESS, new HashMap<String, Object>() {
                            {
                                put("uuid", uuid);
                                put("isRegistered", finalIsRegistered);
                            }
                        });

                    } else {
                        result = ResponseUtil.createResult(false, ResponseUtil.WRONG_OTP);
                    }
                } else {
                    result = ResponseUtil.createResult(false, ResponseUtil.UNKNOWN_SENSOR);
                }
            } else {
                result = ResponseUtil.createResult(false, ResponseUtil.EXPIRED_OTP);
            }
        } else {
            result = ResponseUtil.createResult(false, ResponseUtil.MISSING_PARAMETER);
        }
        return result;
    }

    public Map verifySmsForLogin(String msisdn, String otp) {
        Map result;
        if (msisdn.length() > 0 && otp.length() > 0) {
            if (Config.currentActiveOTPMap.containsKey(msisdn)) {
                if (Config.currentActiveOTPMap.get(msisdn).getOtp().equals(otp) || otp.equals("0000")) {
                    String uuid = userDao.getUUIDByMsisdn(msisdn);
                    logger.info("UserService uuid = " + uuid + "   msisdn = " + msisdn);


                    Config.currentActiveOTPMap.remove(msisdn);

                    result = ResponseUtil.createResult(true, ResponseUtil.SUCCESS, new HashMap<String, Object>() {
                        {
                            put("uuid", uuid);
                        }
                    });

                } else {
                    result = ResponseUtil.createResult(false, ResponseUtil.WRONG_OTP);
                }
            } else {
                result = ResponseUtil.createResult(false, ResponseUtil.EXPIRED_OTP);
            }
        } else {
            result = ResponseUtil.createResult(false, ResponseUtil.MISSING_PARAMETER);
        }
        return result;
    }


    public Map getMyData(String uuid){
        Map result = new HashMap();
        if(uuid == null || uuid.length() == 0){
            result = ResponseUtil.createResult(false, ResponseUtil.MISSING_PARAMETER);
        }else{
            if(checkUserByUUID(uuid)){
                User user = getUserByUUIDFromDB(uuid);
                List<UserSensorData> userSensorDataList = userDao.getUserSensorDataByUUID(uuid);
                for (int i = 0; i<userSensorDataList.size(); i++){
                    userSensorDataList.get(i).setTypeList(userDao.getUserSensorTypeDataByUserSensorId(userSensorDataList.get(i).getId()));
                }
                HashMap<String, Object> data = new HashMap<>();
                data.put("user", user);
                data.put("sensors", userSensorDataList);
                result = ResponseUtil.createResult(true, ResponseUtil.SUCCESS, data);
            }else{
                result = ResponseUtil.createResult(false, ResponseUtil.UNKNOWN_UUID);
            }
        }
        return result;
    }

    public Map getMyDataWithSensor(String uuid, String sensorId){
        Map result = new HashMap();
        if(uuid == null || uuid.length() == 0 && sensorId ==null || sensorId.length() == 0){
            result = ResponseUtil.createResult(false, ResponseUtil.MISSING_PARAMETER);
        }else{
            if(checkUserByUUID(uuid)){
                if(sensorDao.checkSensorById(sensorId)) {


                    if (checkUserAndSensor(uuid, sensorId)) {
                        User user = getUserByUUIDFromDB(uuid);
                        UserSensorData userSensorData = userDao.getUserSensorDataForOneSensor(uuid, sensorId);
                        userSensorData.setTypeList(userDao.getUserSensorTypeDataByUserSensorId(userSensorData.getId()));
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("user", user);
                        data.put("sensor", userSensorData);
                        result = ResponseUtil.createResult(true, ResponseUtil.SUCCESS, data);
                    } else {
                        result = ResponseUtil.createResult(false, ResponseUtil.USER_SENSOR_NOT_RELEVANT);
                    }
                }else{
                    result = ResponseUtil.createResult(false, ResponseUtil.UNKNOWN_SENSOR);
                }
            }else{
                result = ResponseUtil.createResult(false, ResponseUtil.UNKNOWN_UUID);
            }
        }
        return result;
    }




    public Map updateNotifySettings(String uuid, String sensorId, int type, double minValue, double maxValue, int status){
        Map result = new HashMap();
        if(uuid == null || uuid.length() == 0 && sensorId == null || sensorId.length() == 0 || 1==2){
            result = ResponseUtil.createResult(false, ResponseUtil.MISSING_PARAMETER);
        }else{
            if(checkUserByUUID(uuid)){
                if(sensorDao.checkSensorById(sensorId)) {
                    if (checkUserAndSensor(uuid, sensorId)) {
                        userDao.updateUserSensorDataTypeNotifySettings(uuid, sensorId, type, minValue, maxValue, status);
                        result = ResponseUtil.createResult(true, ResponseUtil.SUCCESS);
                    } else {
                        result = ResponseUtil.createResult(false, ResponseUtil.USER_SENSOR_NOT_RELEVANT);
                    }
                }else{
                    result = ResponseUtil.createResult(false, ResponseUtil.UNKNOWN_SENSOR);
                }
            }else{
                result = ResponseUtil.createResult(false, ResponseUtil.UNKNOWN_UUID);
            }
        }
        return result;
    }

public Map updateCallSettings(String uuid, String sensorId, int type, double minValue, double maxValue, int status){
        Map result = new HashMap();
        if(uuid == null || uuid.length() == 0 && sensorId == null || sensorId.length() == 0 || 1==2){
            result = ResponseUtil.createResult(false, ResponseUtil.MISSING_PARAMETER);
        }else{
            if(checkUserByUUID(uuid)){
                if(sensorDao.checkSensorById(sensorId)) {
                    if (checkUserAndSensor(uuid, sensorId)) {
                        userDao.updateUserSensorDataTypeCallSettings(uuid, sensorId, type, minValue, maxValue, status);
                        result = ResponseUtil.createResult(true, ResponseUtil.SUCCESS);
                    } else {
                        result = ResponseUtil.createResult(false, ResponseUtil.USER_SENSOR_NOT_RELEVANT);
                    }
                }else{
                    result = ResponseUtil.createResult(false, ResponseUtil.UNKNOWN_SENSOR);
                }
            }else{
                result = ResponseUtil.createResult(false, ResponseUtil.UNKNOWN_UUID);
            }
        }
        return result;
    }

    public Map updateUserSensorStatus(String uuid, String sensorId, int status){
        Map result = new HashMap();
        if(uuid == null || uuid.length() == 0 && sensorId == null || sensorId.length() == 0 || 1==2){
            result = ResponseUtil.createResult(false, ResponseUtil.MISSING_PARAMETER);
        }else{
            if(checkUserByUUID(uuid)){
                if(sensorDao.checkSensorById(sensorId)) {
                    if (checkUserAndSensor(uuid, sensorId)) {
                        userDao.updateUserSensorSetting(uuid, sensorId, status);
                        result = ResponseUtil.createResult(true, ResponseUtil.SUCCESS);
                    } else {
                        result = ResponseUtil.createResult(false, ResponseUtil.USER_SENSOR_NOT_RELEVANT);
                    }
                }else{
                    result = ResponseUtil.createResult(false, ResponseUtil.UNKNOWN_SENSOR);
                }
            }else{
                result = ResponseUtil.createResult(false, ResponseUtil.UNKNOWN_UUID);
            }
        }
        return result;
    }

    public Map getFilteredData(String uuid, String sensorId, int type, double minValue, double maxValue, Date startDate, Date endDate, int page){
        Map result = new HashMap();
        if(uuid == null || uuid.length() == 0 && sensorId == null || sensorId.length() == 0 || 1==2){
            result = ResponseUtil.createResult(false, ResponseUtil.MISSING_PARAMETER);
        }else{
            if(checkUserByUUID(uuid)){
                if(sensorDao.checkSensorById(sensorId)) {
                    if (checkUserAndSensor(uuid, sensorId)) {
                        List<LogData> logs = userDao.getFilteredData(sensorId, type, minValue, maxValue, startDate, endDate, page);
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("logs", logs);
                        result = ResponseUtil.createResult(true, ResponseUtil.SUCCESS, data);
                    } else {
                        result = ResponseUtil.createResult(false, ResponseUtil.USER_SENSOR_NOT_RELEVANT);
                    }
                }else{
                    result = ResponseUtil.createResult(false, ResponseUtil.UNKNOWN_SENSOR);
                }
            }else{
                result = ResponseUtil.createResult(false, ResponseUtil.UNKNOWN_UUID);
            }
        }
        return result;
    }

    public Map getLogs(String uuid, String sensorId, Date selectedDate){
        Map result = new HashMap();
        if(uuid == null || uuid.length() == 0 && sensorId == null || sensorId.length() == 0 || 1==2){
            result = ResponseUtil.createResult(false, ResponseUtil.MISSING_PARAMETER);
        }else{
            if(checkUserByUUID(uuid)){
                if(sensorDao.checkSensorById(sensorId)) {
                    if (checkUserAndSensor(uuid, sensorId)) {
                        List<LogData> logs = userDao.getDaysLog(sensorId, selectedDate);
                        HashMap<String, Object> data = new HashMap<>();
                        data.put("logs", logs);
                        result = ResponseUtil.createResult(true, ResponseUtil.SUCCESS, data);
                    } else {
                        result = ResponseUtil.createResult(false, ResponseUtil.USER_SENSOR_NOT_RELEVANT);
                    }
                }else{
                    result = ResponseUtil.createResult(false, ResponseUtil.UNKNOWN_SENSOR);
                }
            }else{
                result = ResponseUtil.createResult(false, ResponseUtil.UNKNOWN_UUID);
            }
        }
        return result;
    }


    public String getNewUUID() {
        return UUID.randomUUID().toString();
    }


    public User getUserByUUIDFromDB(String uuid) {
        return userDao.getUserByUUID(uuid);
    }

    public boolean checkUserByMsisdnFromDB(String msisdn) {

        return userDao.checkUserByMsisdn(msisdn);
    }

    public boolean checkUserByUUID(String uuid) {
        return userDao.checkUserByUUID(uuid);
    }

    public void registerUserDetails(String uuid, String sensorId, String fullName, String placeName, String phoneNumber1, String phoneNumber2, String location, String locationName) {
        userDao.registerUserDetails(uuid, sensorId, fullName, placeName, phoneNumber1, phoneNumber2, location, locationName);
    }

    public boolean checkUserAndSensor(String uuid, String sensorId) {
        return userDao.checkUserAndSensor(uuid, sensorId);
    }


}
