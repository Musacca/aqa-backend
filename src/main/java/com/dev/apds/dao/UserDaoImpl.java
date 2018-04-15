package com.dev.apds.dao;

import com.dev.apds.config.ConfigProperties;
import com.dev.apds.models.LogData;
import com.dev.apds.models.SensorTypeData;
import com.dev.apds.models.User;
import com.dev.apds.models.UserSensorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserDaoImpl implements UserDao{

    private static final String REGISTER_USER_DETAILS = "UPDATE user_sensors us, users u set us.name = :name, us.place_name=:place_name, us.phone_number_1=:phone_number_1," +
            " us.phone_number_2=:phone_number_2, us.location_name=:location_name, us.location=:location, us.charged_status=1, us.status = 1, us.last_charged_date = NOW()," +
            " us.active_duration=:active_duration WHERE u.uuid=:uuid AND us.msisdn = u.msisdn AND us.sensor_id = :sensorId";

    private static final String UPDATE_NOTIFY_SETTINGS = "UPDATE user_sensor_details usd, user_sensors us, users u" +
            " SET usd.min_value_for_notify = :min_value_for_notify, usd.max_value_for_notify = :max_value_for_notify," +
            " notify_status = :notify_status WHERE usd.type = :type AND usd.user_sensor_id = us.id AND us.sensor_id = :sensor_id" +
            " AND us.msisdn = u.msisdn AND u.uuid = :uuid";

    private static final String UPDATE_CALL_SETTINGS = "UPDATE user_sensor_details usd, user_sensors us, users u" +
            " SET usd.min_value_for_call = :min_value_for_call, usd.max_value_for_call = :max_value_for_call," +
            " call_status = :call_status WHERE usd.type = :type AND usd.user_sensor_id = us.id AND us.sensor_id = :sensor_id" +
            " AND us.msisdn = u.msisdn AND u.uuid = :uuid";

    private static final String UPDATE_USER_SENSOR_STATUS = "UPDATE  user_sensors us, users u" +
            " SET us.status = :status WHERE  us.sensor_id = :sensor_id" +
            " AND us.msisdn = u.msisdn AND u.uuid = :uuid";

    private static final String INSERT_NEW_USER = "INSERT INTO users(msisdn, status)" +
            " VALUES(:msisdn, -1)";

    private static final String GET_FILTERED_LOGS = "SELECT * FROM logs WHERE sensor_id = :sensor_id" +
            " AND log_type = :log_type AND log_value >= :minValue AND log_value<=:maxValue" +
            " AND DATE(log_time) >= :startTime AND DATE(log_time) <= :endTime and status = 1 LIMIT 100 offset :offset";

    private static final String GET_LOG_FOR_DAY = "SELECT * FROM logs WHERE sensor_id = :sensor_id" +
            " AND DATE(log_time) = :selectedDate and status = 1";


    private static final String SELECT_USER_SENSOR_DATA_LIST_BY_UUID = "SELECT us.* from user_sensors us, users u" +
            " WHERE us.msisdn = u.msisdn AND u.uuid = :uuid";
    private static final String SELECT_USER_SENSOR_DATA_FOR_ONE_SENSOR = "SELECT us.* from user_sensors us, users u" +
            " WHERE us.msisdn = u.msisdn AND u.uuid = :uuid AND us.sensor_id = :sensorId";
    private static final String SELECT_SENSOR_TYPE_LIST = "SELECT * FROM user_sensor_details usd where usd.user_sensor_id = :user_sensor_id";
    private static final String SELECT_USER_BY_UUID = "SELECT * FROM users where uuid = :uuid";
    private static final String CHECK_USER_BY_MSISDN_AND_SENSOR = "SELECT count(1) from users u, user_sensors us, sensors s where u.msisdn = :msisdn AND us.sensor_id = :sensor_id and us.msisdn = u.msisdn AND s.sensor_id = us.sensor_id";
    private static final String CHECK_USER_BY_UUID = "SELECT count(1) from users where uuid = :uuid";
    private static final String CHECK_USER_BY_MSISDN= "SELECT count(1) from users where msisdn = :msisdn";
    private static final String CHECK_USER_AND_SENSOR = "SELECT count(1) FROM users u, user_sensors us WHERE u.uuid = :uuid AND u.msisdn = us.msisdn AND us.sensor_id = :sensor_id";
    private static final String CHECK_USER_AND_SENSOR_BY_MSISDN = "SELECT count(1) FROM user_sensors us WHERE  us.msisdn = :msisdn AND us.sensor_id = :sensor_id AND phone_number_1!=NULL ";
    private static final String UPDATE_USER_UUID_BY_MSISDN = "UPDATE users SET uuid = :uuid WHERE msisdn = :msisdn";
    private static final String GET_UUID_BY_MSISDN = "SELECT uuid FROM users where msisdn = :msisdn";
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    ConfigProperties configProperties;


    @Override
    public void registerUserDetails(String uuid, String sensorId, String fullName, String placeName, String phoneNumber1, String phoneNumber2, String location, String locationName) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", fullName);
        parameters.put("place_name", placeName);
        parameters.put("phone_number_1", phoneNumber1);
        parameters.put("phone_number_2", phoneNumber2);
        parameters.put("location", location);
        parameters.put("location_name", locationName);
        parameters.put("active_duration", configProperties.FERE_PERIOD);
        parameters.put("uuid", uuid);
        parameters.put("sensorId", sensorId);
        namedParameterJdbcTemplate.update(REGISTER_USER_DETAILS, parameters);
    }


    @Override
    public void insertNewUser(String msisdn) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("msisdn", msisdn);
        namedParameterJdbcTemplate.update(INSERT_NEW_USER, parameters);
    }

    @Override
    public User getUserByUUID(String uuid) {
        User user;
        Map parameters = new HashMap();
        parameters.put("uuid", uuid);
        user = namedParameterJdbcTemplate.queryForObject(SELECT_USER_BY_UUID, parameters, BeanPropertyRowMapper.newInstance(User.class));
        return user;
    }

    @Override
    public String getUUIDByMsisdn(String msisdn) {

        Map parameters = new HashMap();
        parameters.put("msisdn", msisdn);
        return namedParameterJdbcTemplate.queryForObject(GET_UUID_BY_MSISDN, parameters, String.class);

    }
    @Override
    public boolean checkUserByMsisdn(String msisdn, String sensorId) {
        Map parameters = new HashMap();
        parameters.put("msisdn", msisdn);
        parameters.put("sensor_id", sensorId);
        int result = namedParameterJdbcTemplate.queryForObject(CHECK_USER_BY_MSISDN_AND_SENSOR, parameters, Integer.class);
        if (result > 0) {
            return true;
        }
        return false;
    }


    @Override
    public boolean checkUserByMsisdn(String msisdn) {
        Map parameters = new HashMap();
        parameters.put("msisdn", msisdn);

        int result = namedParameterJdbcTemplate.queryForObject(CHECK_USER_BY_MSISDN, parameters, Integer.class);
        if (result > 0) {
            return true;
        }
        return false;
    }



    @Override
    public void updateUserUUIDByMSISDN(String msisdn, String uuid) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("msisdn", msisdn);
        parameters.put("uuid", uuid);
        namedParameterJdbcTemplate.update(UPDATE_USER_UUID_BY_MSISDN, parameters);
    }

    @Override
    public boolean checkUserByUUID(String uuid) {
        Map parameters = new HashMap();
        parameters.put("uuid", uuid);
        int result = namedParameterJdbcTemplate.queryForObject(CHECK_USER_BY_UUID, parameters, Integer.class);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkUserAndSensor(String uuid, String sensorId) {
        Map parameters = new HashMap();
        parameters.put("uuid", uuid);
        parameters.put("sensor_id", sensorId);
        int result = namedParameterJdbcTemplate.queryForObject(CHECK_USER_AND_SENSOR, parameters, Integer.class);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkUserAndSensorByMsisdn(String msisdn, String sensorId) {
        Map parameters = new HashMap();
        parameters.put("msisdn", msisdn);
        parameters.put("sensor_id", sensorId);
        boolean res = false;
        int result1 = namedParameterJdbcTemplate.queryForObject(CHECK_USER_AND_SENSOR_BY_MSISDN, parameters, Integer.class);
        if (result1 > 0) {
            return true;
        }
        return false;
    }


    @Override
    public List<UserSensorData> getUserSensorDataByUUID(String uuid) {
        List<UserSensorData> userSensorDataList = new ArrayList<>();
        Map parameters = new HashMap();
        parameters.put("uuid", uuid);
        userSensorDataList = namedParameterJdbcTemplate.query(SELECT_USER_SENSOR_DATA_LIST_BY_UUID, parameters, BeanPropertyRowMapper.newInstance(UserSensorData.class));


        return userSensorDataList;
    }

    @Override
    public UserSensorData getUserSensorDataForOneSensor(String uuid, String sensorId) {
        UserSensorData userSensorData;
        Map parameters = new HashMap();
        parameters.put("uuid", uuid);
        parameters.put("sensorId", sensorId);
        userSensorData = namedParameterJdbcTemplate.queryForObject(SELECT_USER_SENSOR_DATA_FOR_ONE_SENSOR, parameters, BeanPropertyRowMapper.newInstance(UserSensorData.class));


        return userSensorData;
    }

    @Override
    public  List<SensorTypeData> getUserSensorTypeDataByUserSensorId(int userSensorId){
        List<SensorTypeData> sensorTypeDataList = new ArrayList<>();
        Map parameters = new HashMap();
        parameters.put("user_sensor_id", userSensorId);
        sensorTypeDataList = namedParameterJdbcTemplate.query(SELECT_SENSOR_TYPE_LIST, parameters, BeanPropertyRowMapper.newInstance(SensorTypeData.class));


        return sensorTypeDataList;

    }

    @Override
    public void updateUserSensorDataTypeNotifySettings(String uuid, String sensorId, int type, double minValue, double maxValue, int status) {
        Map params = new HashMap();
        params.put("uuid", uuid);
        params.put("sensor_id", sensorId);
        params.put("type", type);
        params.put("min_value_for_notify", minValue);
        params.put("max_value_for_notify", maxValue);
        params.put("notify_status", status);

        namedParameterJdbcTemplate.update(UPDATE_NOTIFY_SETTINGS, params);
    }

    @Override
    public void updateUserSensorDataTypeCallSettings(String uuid, String sensorId, int type, double minValue, double maxValue, int status) {
        Map params = new HashMap();
        params.put("uuid", uuid);
        params.put("sensor_id", sensorId);
        params.put("type", type);
        params.put("min_value_for_call", minValue);
        params.put("max_value_for_call", maxValue);
        params.put("call_status", status);

        namedParameterJdbcTemplate.update(UPDATE_CALL_SETTINGS, params);
    }

    @Override
    public void updateUserSensorSetting(String uuid, String sensorId, int status) {
        Map params = new HashMap();
        params.put("uuid", uuid);
        params.put("sensor_id", sensorId);
        params.put("status", status);

        namedParameterJdbcTemplate.update(UPDATE_USER_SENSOR_STATUS, params);
    }

    @Override
    public List<LogData> getFilteredData(String sensorId, int type, double minValue, double maxValue, Date startDate, Date endDate, int page) {
        List<LogData> logs = new ArrayList<>();
        Map params = new HashMap();
        params.put("sensor_id", sensorId);
        params.put("log_type", type);
        params.put("minValue", minValue);
        params.put("maxValue", maxValue);
        params.put("startTime", startDate);
        params.put("endTime", endDate);
        params.put("offset", (page-1)*100);

        logs = namedParameterJdbcTemplate.query(GET_FILTERED_LOGS, params, BeanPropertyRowMapper.newInstance(LogData.class));
        return logs;
    }

    @Override
    public List<LogData> getDaysLog(String sensorId, Date selectedDate) {
        List<LogData> logs = new ArrayList<>();
        Map params = new HashMap();
        params.put("sensor_id", sensorId);
        params.put("selectedDate", selectedDate);

        logs = namedParameterJdbcTemplate.query(GET_LOG_FOR_DAY, params, BeanPropertyRowMapper.newInstance(LogData.class));
        return logs;
    }
}
