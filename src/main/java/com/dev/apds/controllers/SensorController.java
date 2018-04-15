package com.dev.apds.controllers;

import com.dev.apds.services.SensorService;
import com.dev.apds.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sensor")
public class SensorController{

    @Autowired
    SensorService sensorService;

    @RequestMapping(value = "/checkSensor", method = RequestMethod.POST, produces = "application/json")
    public Map checkSensor(@RequestParam("sensorId") String sensorId){
        Map result;
        try{
            HashMap data = sensorService.checkSensor(sensorId);
            result = ResponseUtil.createResult(true, ResponseUtil.SUCCESS, data);
        }catch (Exception e){
            result = ResponseUtil.createResult(false, ResponseUtil.SERVER_ERROR);
        }

        return result;
    }

    @RequestMapping(value = "/checkStatus", method = RequestMethod.POST, produces = "application/json")
    public Map checkStatus(@RequestParam("sensorId") String sensorId){
        Map result;
        try{
            HashMap data = sensorService.getSensorStatus(sensorId);
            result = ResponseUtil.createResult(true, ResponseUtil.SUCCESS, data);
        }catch (Exception e){
            result = ResponseUtil.createResult(false, ResponseUtil.SERVER_ERROR);
        }

        return result;
    }

    @RequestMapping(value = "/sendData", method = RequestMethod.POST, produces = "application/json")
    public Map sendData(@RequestParam("sensorId") String sensorId, @RequestParam("log") String logDataString){
        Map result;
        if(!sensorService.checkSensorById(sensorId)){
            result = ResponseUtil.createResult(false, ResponseUtil.UNKNOWN_SENSOR);
        }else{
            HashMap<Integer, Double> map = parseLogData(logDataString);
            for(Integer key : map.keySet()){
                sensorService.insertSensorLog(sensorId, map.get(key), key);
            }

            result = ResponseUtil.createResult(true, ResponseUtil.SUCCESS);
        }

        return result;
    }

    HashMap<Integer, Double> parseLogData(String logDataStr){
        HashMap<Integer, Double> map =  new HashMap<>();
        String[] logs = logDataStr.split("!");
        System.out.println(logs.length + " ------ >  income data size by #" );
        for(int i = 0; i<logs.length; i++){
            String[] log = logs[i].split("_");
            Integer type = Integer.parseInt(log[0]);
            Double value = Double.parseDouble(log[1]);
            System.out.println(log.length + " ------ >  income data size by _  " + i);
            map.put(type, value);
        }


        return map;
    }
}
