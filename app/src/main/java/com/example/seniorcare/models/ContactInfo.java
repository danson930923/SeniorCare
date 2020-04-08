package com.example.seniorcare.models;

import com.example.seniorcare.db.context.DataType;
import com.example.seniorcare.db.context.IDbTableModelConvertible;
import com.example.seniorcare.utils.HashMapUtils;

import java.util.HashMap;

public class ContactInfo implements IDbTableModelConvertible {
    private HashMap<String, Object> _data = new HashMap<>();
    private final static String TABLE_NAME = "ContactInfo";
    private final static String COLUMN_USER_ID = "UserId";
    private final static String COLUMN_PHONE_NUMBER = "PhoneNumber";

    public String getUserId() {
        return (String) _data.get(COLUMN_USER_ID);
    }

    public void setUserId(String userId) {
        _data.put(COLUMN_USER_ID, userId);
    }

    public String getPhoneNumber() {
        return (String) _data.get(COLUMN_PHONE_NUMBER);
    }

    public void setPhoneNumber(String phoneNumber) {
        _data.put(COLUMN_PHONE_NUMBER, phoneNumber);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getPrimaryKey() {
        return COLUMN_USER_ID;
    }

    @Override
    public String getPrimaryKeyValue() {
        return getUserId();
    }

    @Override
    public HashMap<String, DataType> getTableColumnsKeyType() {
        HashMap<String, DataType> list = new HashMap<>();
        list.put(COLUMN_USER_ID, DataType.TEXT);
        list.put(COLUMN_PHONE_NUMBER, DataType.TEXT);

        return list;
    }

    @Override
    public void setData(String key, Object value) {
        _data.put(key, value);
    }

    @Override
    public HashMap<String, String> getAllData() {
        return HashMapUtils.toString(_data);
    }

    @Override
    public Object getNewInstance() {
        return new ContactInfo();
    }
}
