package com.example.seniorcare.models;

import com.example.seniorcare.db.context.DataType;
import com.example.seniorcare.db.context.IDbTableModelConvertible;
import com.example.seniorcare.utils.HashMapUtils;

import java.util.HashMap;

public class UserPassCode implements IDbTableModelConvertible {
    private HashMap<String, Object> _data = new HashMap<>();
    private final static String TABLE_NAME = "UserPassCode";
    private final static String COLUMN_USER_ID = "UserId";
    private final static String COLUMN_PASS_CODE = "PassCode";

    public String getUserId() {
        return (String) _data.get(COLUMN_USER_ID);
    }

    public void setUserId(String userId) {
        _data.put(COLUMN_USER_ID, userId);
    }

    public String getPassCode() {
        return (String) _data.get(COLUMN_PASS_CODE);
    }

    public void setPassCode(String passCode) {
        _data.put(COLUMN_PASS_CODE, passCode);
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
        list.put(COLUMN_PASS_CODE, DataType.TEXT);

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
        return new UserPassCode();
    }
}
