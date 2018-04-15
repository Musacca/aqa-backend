package com.dev.apds.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    public static final int SUCCESS = 200;
    public static final int SERVER_ERROR = 500;
    public static final int WRONG_MSISDN = 403;
    public static final int MSISDN_NOT_REGISTERED = 401;
    public static final int WRONG_OTP = 402;
    public static final int EXPIRED_OTP = 405;
    public static final int MISSING_PARAMETER = 400;
    public static final int NOT_AUTHORIZED_USER = 406;
    public static final int UNKNOWN_SENSOR = 407;
    public static final int USER_SENSOR_NOT_RELEVANT = 408;
    public static final int UNKNOWN_UUID = 409;
    private static Map<Integer, String> RESULT_CODE_AND_MESSAGES = new HashMap<>();

    static {
        RESULT_CODE_AND_MESSAGES.put(SUCCESS, "successfully resulted");
        RESULT_CODE_AND_MESSAGES.put(SERVER_ERROR, "server error");
        RESULT_CODE_AND_MESSAGES.put(WRONG_MSISDN, "wrong msisdn");
        RESULT_CODE_AND_MESSAGES.put(WRONG_OTP, "wrong OTP");
        RESULT_CODE_AND_MESSAGES.put(EXPIRED_OTP, "OTP not requested or expired");
        RESULT_CODE_AND_MESSAGES.put(MISSING_PARAMETER, "missing parameter");
        RESULT_CODE_AND_MESSAGES.put(NOT_AUTHORIZED_USER, "not authorized user");
        RESULT_CODE_AND_MESSAGES.put(UNKNOWN_SENSOR, "sensor not exists");
        RESULT_CODE_AND_MESSAGES.put(USER_SENSOR_NOT_RELEVANT, "user can not see this sensor");
        RESULT_CODE_AND_MESSAGES.put(MSISDN_NOT_REGISTERED, "msisdn not registered");
        RESULT_CODE_AND_MESSAGES.put(UNKNOWN_UUID, "uuid is not correct");
    }

    public static Map createResult(boolean success,  int code, HashMap<String, Object> data){
        Map<String, Object> result = new HashMap<>();

        if(success){
            data.put("code", code);
            result.put("success", true);
            result.put("payload", data);
        }else{
            data.put("code", code);
            result.put("success", false);
            result.put("error", data);
        }

        return result;
    }

    public static Map createResult(boolean success, int code){
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        if(success){
            data.put("code", code);
            data.put("message", RESULT_CODE_AND_MESSAGES.get(code));
            result.put("success", true);
            result.put("payload", data);
        }else{
            data.put("code", code);
            data.put("message", RESULT_CODE_AND_MESSAGES.get(code));
            result.put("success", false);
            result.put("error", data);
        }
        return result;
    }

}
