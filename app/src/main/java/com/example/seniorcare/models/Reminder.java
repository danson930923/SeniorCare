package com.example.seniorcare.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.example.seniorcare.db.context.IDbTableModelConvertible;
import com.example.seniorcare.db.context.DataType;
import com.example.seniorcare.utils.HashMapUtils;

public class Reminder implements IDbTableModelConvertible {
    private HashMap<String, Object> _data = new HashMap<>();
    private final static String TABLE = "Reminder";
    private final static String COLUMN_TITLE = "Title";
    private final static String COLUMN_TIME = "Time";
    private final static String COLUMN_USER_ID = "UserId";
    private final static String COLUMN_INTENT_REQUEST_CODE= "IntentRequestCode";
    private final static String COLUMN_REMINDER_ID= "ReminderId";

    public String getTitle() {
        return (String) _data.get(COLUMN_TITLE);
    }

    public void setName(String newTitle) {
        _data.put(COLUMN_TITLE, newTitle);
    }

    public Date getTime() {
        String dateString = (String) _data.get(COLUMN_TIME);
        try {
            return getDateFormat().parse(dateString);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public void setTime(String newTime) {
        _data.put(COLUMN_TIME, newTime);
    }
    public void setTime(Date dateTime) {
        _data.put(COLUMN_TIME, getDateFormat().format(dateTime));
    }

    public String getUserId() {
        return (String) _data.get(COLUMN_USER_ID);
    }

    public void setUserId(String userId) {
        _data.put(COLUMN_USER_ID, userId);
    }

    public int getIntentRequestCode() {
        if (_data.get(COLUMN_INTENT_REQUEST_CODE) == null) {
            return -1;
        }
        return (int) _data.get(COLUMN_INTENT_REQUEST_CODE);
    }

    public void setIntentRequestCode(int intentRequestCode) {
        _data.put(COLUMN_INTENT_REQUEST_CODE, intentRequestCode);
    }

    @Override
    public String getTableName() {
        return TABLE;
    }

    @Override
    public String getPrimaryKey() {
        return COLUMN_REMINDER_ID;
    }

    @Override
    public String getPrimaryKeyValue() {
        return getUserId() + "_" + getTitle();
    }

    @Override
    public HashMap<String, DataType> getTableColumnsKeyType() {
        HashMap<String, DataType> list = new HashMap<>();
        list.put(COLUMN_TITLE, DataType.TEXT);
        list.put(COLUMN_TIME, DataType.TEXT);
        list.put(COLUMN_USER_ID, DataType.TEXT);
        list.put(COLUMN_INTENT_REQUEST_CODE, DataType.INTEGER);
        list.put(COLUMN_REMINDER_ID, DataType.TEXT);

        return list;
    }

    @Override
    public void setData(String key, Object value) {
        _data.put(key, value);
    }

    @Override
    public HashMap<String, String> getAllData() {
        HashMap<String, String> result = HashMapUtils.toString(_data);
        if (getIntentRequestCode() < 0) {
            result.put(COLUMN_INTENT_REQUEST_CODE, null);
        }
        if(getUserId() != null && getTitle() != null && getTime() != null) {
            result.put(COLUMN_REMINDER_ID, getPrimaryKeyValue());
        }
        return result;
    }

    @Override
    public Object getNewInstance() {
        return new Reminder();
    }

    private DateFormat getDateFormat() {
        return new SimpleDateFormat("HH:mm");
    }
}
