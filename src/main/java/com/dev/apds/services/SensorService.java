package com.dev.apds.services;

import com.dev.apds.dao.SensorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Service
public class SensorService {

    @Autowired
    SensorDao sensorDao;
    public HashMap<String, Object> checkSensor(String sensorId){
        HashMap<String, Object> result = new HashMap<>();
        result.put("sensorExist", checkSensorById(sensorId));
        return  result;
    }


    public HashMap<String, Object> getSensorStatus(String sensorId){
        HashMap<String, Object> result = new HashMap<>();
        result.put("status", checkStatusById(sensorId));
        return  result;
    }


    public HashMap<String, Object> getLastData(String sensorId){
        HashMap<String, Object> data = new HashMap<>();
        data.put("lastLog", sensorDao.getLastLogDataList(sensorId));
        return data;
    }

    public void insertSensorLog(String sensorId, double logValue, int logType){
            sensorDao.insertSensorLog(sensorId,logValue, logType);

    }
    public boolean checkSensorById(String sensorId){
        return sensorDao.checkSensorById(sensorId);
    }
    public int checkStatusById(String sensorId){
        return sensorDao.getSensorStatusById(sensorId);
    }
}
