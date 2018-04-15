package com.dev.apds.controllers;

import com.dev.apds.helpers.AndroidPushNotificationsService;
import com.dev.apds.helpers.FirebaseResponse;
import com.dev.apds.models.UserSensorData;
import com.dev.apds.services.SensorService;
import com.dev.apds.services.UserService;
import com.dev.apds.utils.ResponseUtil;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    SensorService sensorService;

    @Autowired
    AndroidPushNotificationsService androidPushNotificationsService;


    private Logger logger = Logger.getLogger(UserController.class);


    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
    public Map register(@RequestParam("fullName") String fullName, @RequestParam("placeName") String placeName,
                        @RequestParam("phoneNumber1") String phoneNumber1, @RequestParam("sensorId") String sensorId,
                        @RequestParam("phoneNumber2") String phoneNumber2, @RequestParam("location") String location,
                        @RequestParam("locationName") String locationName, @RequestParam("uuid") String uuid) {

        Map result;
        try {
            if (!userService.checkUserByUUID(uuid)) {
                result = ResponseUtil.createResult(false, ResponseUtil.NOT_AUTHORIZED_USER);
            } else {
                userService.registerUserDetails(uuid, sensorId, fullName, placeName, phoneNumber1, phoneNumber2, location, locationName);
                result = ResponseUtil.createResult(true, ResponseUtil.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = ResponseUtil.createResult(false, ResponseUtil.SERVER_ERROR);
        }

        logger.info("Response : start " + result.toString());
        return result;

    }



    @RequestMapping(value = "/me", method = RequestMethod.POST, produces = "application/json")
    public Map getMyData(@RequestParam("uuid") String uuid){
        logger.info("/user/me?uuid="+uuid);
        Map result;
        try{

            result = userService.getMyData(uuid);

        }catch (Exception e){
            e.printStackTrace();
            result = ResponseUtil.createResult(false, ResponseUtil.SERVER_ERROR);
        }

        return result;
    }



    @RequestMapping(value = "/me/currentSensor", method = RequestMethod.POST, produces = "application/json")
    public Map getMyDataWithSensor(@RequestParam("uuid") String uuid, @RequestParam("sensorId") String sensorId){
        logger.info("/user/me/currentSensor?uuid="+uuid+"&sensorId="+sensorId);
        Map result;
        try{

            result = userService.getMyDataWithSensor(uuid, sensorId);

        }catch (Exception e){
            e.printStackTrace();
            result = ResponseUtil.createResult(false, ResponseUtil.SERVER_ERROR);
        }
        return result;
    }



    @RequestMapping(value = "/register/sendMsisdn", method = RequestMethod.POST, produces = "application/json")
    public Map sendMsisdnForRegister(@RequestParam("msisdn") String msisdn){
        logger.info("/sendMsisdnForRegister?msisdn=" + msisdn);
        Map result;
        try {
            result = userService.startForRegister(msisdn);
        } catch (Exception e) {
            e.printStackTrace();
            result = ResponseUtil.createResult(false, ResponseUtil.SERVER_ERROR);
        }

        logger.info("Response : start " + result.toString());
        return result;
    }


    @RequestMapping(value = "/register/verify/sms", method = RequestMethod.POST, produces = "application/json")
    public Map verifySmsForRegister(@RequestParam("msisdn") String msisdn, @RequestParam("otp") String otp, @RequestParam("sensorId") String sensorId) {
        Map result;
        logger.info("/verify/sms?msisdn=" + msisdn + "&otp=" + otp);
        try {
            msisdn = msisdn.trim();
            otp = otp.trim();
            result = userService.verifySmsForRegister(msisdn, sensorId, otp);

        } catch (Exception e) {
            e.printStackTrace();
            result = ResponseUtil.createResult(false, ResponseUtil.SERVER_ERROR);
        }


        return result;
    }










    @RequestMapping(value = "/login/sendMsisdn", method = RequestMethod.POST, produces = "application/json")
    public Map sendMsisdn(@RequestParam("msisdn") String msisdn) {
        logger.info("/login/sendMsisdn?msisdn=" + msisdn);
        Map result;
        try {
            result = userService.startForLogin(msisdn);
        } catch (Exception e) {
            e.printStackTrace();
            result = ResponseUtil.createResult(false, ResponseUtil.SERVER_ERROR);
        }

        logger.info("Response : /login/sendMsisdn " + result.toString());
        return result;
    }



    @RequestMapping(value = "/login/verify/sms", method = RequestMethod.POST, produces = "application/json")
    public Map verifySmsForLogin(@RequestParam("msisdn") String msisdn, @RequestParam("otp") String otp) {
        Map result;
        logger.info("/verify/sms?msisdn=" + msisdn + "&otp=" + otp);
        try {
            msisdn = msisdn.trim();
            otp = otp.trim();
            result = userService.verifySmsForLogin(msisdn, otp);

        } catch (Exception e) {
            e.printStackTrace();
            result = ResponseUtil.createResult(false, ResponseUtil.SERVER_ERROR);
        }


        return result;
    }


    @RequestMapping(value = "/getLastData", method = RequestMethod.POST, produces = "application/json")
    public Map getLastData(@RequestParam("uuid") String uuid, @RequestParam("sensorId") String sensorId) {
        Map result;
        if(userService.checkUserByUUID(uuid)){
            if(userService.checkUserAndSensor(uuid, sensorId)){
                HashMap<String, Object> data = sensorService.getLastData(sensorId);
                result = ResponseUtil.createResult(true, ResponseUtil.SUCCESS, data);
            }else{
                result = ResponseUtil.createResult(false, ResponseUtil.USER_SENSOR_NOT_RELEVANT);
            }
        }else{
            result = ResponseUtil.createResult(false, ResponseUtil.NOT_AUTHORIZED_USER);
        }

        return  result;
    }


    @RequestMapping(value = "/update/notify", method = RequestMethod.POST, produces = "application/json")
    public Map updateNotify(@RequestParam("uuid") String uuid, @RequestParam("sensorId") String sensorId, @RequestParam("type") int type,
                            @RequestParam("minValue") double minValue, @RequestParam("maxValue") double maxValue, @RequestParam("status") int status){

        logger.info("/user/update/notify?uuid="+uuid+"&sensorId="+sensorId);
        Map result;
        try{

            result = userService.updateNotifySettings(uuid, sensorId, type, minValue, maxValue, status);

        }catch (Exception e){
            e.printStackTrace();
            result = ResponseUtil.createResult(false, ResponseUtil.SERVER_ERROR);
        }
        return result;



    }

    @RequestMapping(value = "/update/call", method = RequestMethod.POST, produces = "application/json")
    public Map updateCallSettings(@RequestParam("uuid") String uuid, @RequestParam("sensorId") String sensorId, @RequestParam("type") int type,
                            @RequestParam("minValue") double minValue, @RequestParam("maxValue") double maxValue, @RequestParam("status") int status){

        logger.info("/user/update/notify?uuid="+uuid+"&sensorId="+sensorId);
        Map result;
        try{

            result = userService.updateCallSettings(uuid, sensorId, type, minValue, maxValue, status);

        }catch (Exception e){
            e.printStackTrace();
            result = ResponseUtil.createResult(false, ResponseUtil.SERVER_ERROR);
        }
        return result;

    }



    @RequestMapping(value = "/me/changeStatus", method = RequestMethod.POST, produces = "application/json")
    public Map changeStatus(@RequestParam("uuid") String uuid,
                            @RequestParam("sensorId") String sensorId, @RequestParam("status") int status){

        logger.info("/me/changeStatus?uuid="+uuid+"&sensorId="+sensorId);
        Map result;
        try{

            result = userService.updateUserSensorStatus(uuid, sensorId, status);

        }catch (Exception e){
            e.printStackTrace();
            result = ResponseUtil.createResult(false, ResponseUtil.SERVER_ERROR);
        }
        return result;

    }

    @RequestMapping(value = "/getFilter", method = RequestMethod.POST, produces = "application/json")
    public Map getFilter(@RequestParam("uuid") String uuid, @RequestParam("sensorId") String sensorId,
                         @RequestParam("type") int type,
                         @RequestParam("minValue") double minValue,
                         @RequestParam("maxValue") double maxValue,
                         @RequestParam("startDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date startDate,
                         @RequestParam("endDate") @DateTimeFormat(pattern="yyyy-MM-dd") Date endDate,
                         @RequestParam("page") int page){

        logger.info("/user/getFilter?uuid="+uuid+"&sensorId="+sensorId);
        Map result;
        try{

            result = userService.getFilteredData(uuid, sensorId, type, minValue, maxValue, startDate, endDate, page);

        }catch (Exception e){
            e.printStackTrace();
            result = ResponseUtil.createResult(false, ResponseUtil.SERVER_ERROR);
        }
        return result;
    }

    @RequestMapping(value = "/getLogs", method = RequestMethod.POST, produces = "application/json")
    public Map getFilter(@RequestParam("uuid") String uuid, @RequestParam("sensorId") String sensorId,
                         @RequestParam("date") @DateTimeFormat(pattern="yyyy-MM-dd") Date selectedDate){

        logger.info("/user/getFilter?uuid="+uuid+"&sensorId="+sensorId);
        Map result;
        try{

            result = userService.getLogs(uuid, sensorId, selectedDate);

        }catch (Exception e){
            e.printStackTrace();
            result = ResponseUtil.createResult(false, ResponseUtil.SERVER_ERROR);
        }
        return result;
    }






    public void sendNotification(){

        try{

            JSONObject body = new JSONObject();
            // JsonArray registration_ids = new JsonArray();
            // body.put("registration_ids", registration_ids);
            body.put("to", "eqHefXoH8Zo:APA91bFCUAzfjYKH1H138wN6-wF4_XSsW1f27XY-qGjH5s8J6Ys8bPr1bhagZPFNAEOlmZSj9T_YDvKB_LO0P5nBhabgqA3GAt0C5WI3RFCIcGaPi5lI7_i50hlR9kHDxdZVHis4paTR");

            // body.put("dry_run", true);

            JSONObject notification = new JSONObject();
            notification.put("body", "body string here");
            notification.put("title", "title string here");
            // notification.put("icon", "myicon");

            JSONObject data = new JSONObject();
            data.put("key1", "value1");
            data.put("key2", "value2");

            body.put("data", data);

            HttpEntity<String> request = new HttpEntity<>(body.toString());

            CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
            CompletableFuture.allOf(pushNotification).join();

            try {
                String firebaseResponse = pushNotification.get();
//                if (firebaseResponse.getSuccess() == 1) {
//                    logger.info("push notification sent ok!");
//                } else {
//                    logger.error("error sending push notifications: " + firebaseResponse.toString());
//                }
                logger.info(firebaseResponse);


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
