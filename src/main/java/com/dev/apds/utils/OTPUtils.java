package com.dev.apds.utils;

import java.util.Random;


public class OTPUtils {


    public String createNewOtp(){
        Random random = new Random();
        int otp = random.nextInt(9000) + 1000;
        return String.valueOf(otp);
    }

}
