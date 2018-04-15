package com.dev.apds.utils;

import com.dev.apds.config.Config;
import com.dev.apds.config.ConfigProperties;
import com.dev.apds.config.Constants;
import com.dev.apds.models.OTPObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class OTPScheduler {

    @Autowired
    ConfigProperties configProperties;
    private static Timer timer;
    private static TimerTask timerTask;
    private static List<String> msisdnListToRemove = new ArrayList<>();
    public static void scheduleTaskToRemoveOTPs(){

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                removeExpiredOTPs();
            }
        };
        timer.schedule(timerTask, Constants.OTP_SCHEDULER_TIME_PERIOD * 60 * 1000, Constants.OTP_SCHEDULER_TIME_PERIOD * 60 * 1000);
    }

    private static void removeExpiredOTPs(){
        int counter = 0;
        Date date = new Date();

        for(Map.Entry<String, OTPObject> entry : Config.currentActiveOTPMap.entrySet()) {
            String key = entry.getKey();
            OTPObject value = entry.getValue();
            if(date.getTime() - value.getCreatedDate().getTime() >  Constants.OTP_EXPIRE_TIME * 60 * 1000){
                msisdnListToRemove.add(key);
                counter++;
            }
        }

        for(int i = 0; i<msisdnListToRemove.size(); i++){
            String msisdn = msisdnListToRemove.get(i);
            Config.currentActiveOTPMap.remove(msisdn);
        }

        msisdnListToRemove.clear();
        System.out.println("OTP remover task worked and removed " + counter + " otp codes");
    }



}
