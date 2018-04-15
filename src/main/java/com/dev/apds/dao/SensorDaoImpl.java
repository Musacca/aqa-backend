package com.dev.apds.dao;

import com.dev.apds.models.LogData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SensorDaoImpl implements SensorDao{

    private static final String CHECK_SENSOR_BY_ID = "SELECT count(*) FROM sensors WHERE sensor_id = :sensor_id";
    private static final String GET_SENSOR_STATUS_BY_ID = "SELECT IFNULL ((SELECT status FROM sensors WHERE sensor_id = :sensorId), 0)";

    private static final String INSERT_SENSOR_LOG = "INSERT INTO logs(sensor_id, log_value, log_type, log_time" +
            ", status) VALUES (:sensor_id, :log_value, :log_type, NOW(), 1)";
    private static final String INSERT_USER_SENSOR_DATA = "INSERT INTO user_sensors(msisdn, sensor_id, status)" +
            " VALUES(:msisdn, :sensor_id, 1)";
    private static final String INSERT_USER_SENSOR_DETAIL = "INSERT INTO user_sensor_details(user_sensor_id, type)" +
            " VALUES (:user_sensor_id, :type)";
    private static final String GET_LAST_DATA_BY_SENSOR_ID = "SELECT * FROM logs l " +
            "where l.log_time = (SELECT MAX(log_time) FROM logs WHERE sensor_id = :sensor_id)";

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public boolean checkSensorById(String sensorId){
        Map parameters = new HashMap();
        parameters.put("sensor_id", sensorId);
        int result = namedParameterJdbcTemplate.queryForObject(CHECK_SENSOR_BY_ID, parameters, Integer.class);
        if (result == 0) {
            return false;
        }
        return true;
    }

    @Override
    public int getSensorStatusById(String sensorId){
        Map parameters = new HashMap();
        parameters.put("sensorId", sensorId);
        int result = namedParameterJdbcTemplate.queryForObject(GET_SENSOR_STATUS_BY_ID, parameters, Integer.class);
        return result;
    }


    @Override
    public void insertSensorLog(String sensorId, double logValue, int logType) {
            Map parameters = new HashMap();
            parameters.put("sensor_id", sensorId);
            parameters.put("log_value", logValue);
            parameters.put("log_type", logType);
            namedParameterJdbcTemplate.update(INSERT_SENSOR_LOG, parameters);
    }

    @Override
    public int insertUserSensor(String msisdn, String sensorId) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        final KeyHolder holder = new GeneratedKeyHolder();
        parameters.addValue("sensor_id", sensorId);
        parameters.addValue("msisdn", msisdn);
        namedParameterJdbcTemplate.update(INSERT_USER_SENSOR_DATA, parameters,  holder, new String[] {"id" } );
        Number generatedId = holder.getKey();
        return generatedId.intValue();
    }


    @Override
    public void insertUserSensorDetail(int userSensorId, int type) {
        Map parameters = new HashMap();
        parameters.put("user_sensor_id", userSensorId);
        parameters.put("type", type);
        namedParameterJdbcTemplate.update(INSERT_USER_SENSOR_DETAIL, parameters);
    }
    @Override
    public List<LogData> getLastLogDataList(String sensorId) {
        Map parameters = new HashMap();
        parameters.put("sensor_id", sensorId);
        return namedParameterJdbcTemplate.query(GET_LAST_DATA_BY_SENSOR_ID, parameters, BeanPropertyRowMapper.newInstance(LogData.class));
    }
}
