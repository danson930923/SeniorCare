package com.example.seniorcare.models;

import java.util.HashMap;

import com.example.seniorcare.db.context.IDbTableModelConvertible;
import com.example.seniorcare.db.context.DataType;
import com.example.seniorcare.utils.HashMapUtils;

public class User implements IDbTableModelConvertible {
    private HashMap<String, Object> _data = new HashMap<>();
    private final static String TABLE = "User";
    private final static String COLUMN_NAME = "Name";
    private final static String COLUMN_PASSWORD = "Password";
    private final static String COLUMN_TYPE = "Type";
    private final static String COLUMN_USER_ID = "UserId";

    // auto generated field
    public String getUserId() {
        return (String) getPrimaryKeyValue();
    }

    private void udpateUserId() {
        _data.put(COLUMN_USER_ID, getUserId());
    }

    public String getName() {
        return (String) _data.get(COLUMN_NAME);
    }

    public void setName(String newName) {
        _data.put(COLUMN_NAME, newName);
        udpateUserId();
    }

    public String getPassword() {
        return (String) _data.get(COLUMN_PASSWORD);
    }

    public void setPassword(String newPassword) {
        _data.put(COLUMN_PASSWORD, newPassword);
        udpateUserId();
    }

    public String getType() {
        return (String) _data.get(COLUMN_TYPE);
    }

    public void setType(String type) {
        _data.put(COLUMN_TYPE, type);
    }

    @Override
    public String getTableName() {
        return TABLE;
    }

    @Override
    public String getPrimaryKey() {
        return COLUMN_USER_ID;
    }

    @Override
    public String getPrimaryKeyValue() {
        return getName() + "_"+ getPassword();
    }

    @Override
    public HashMap<String, DataType> getTableColumnsKeyType() {
        HashMap<String, DataType> list = new HashMap<>();
        list.put(COLUMN_NAME, DataType.TEXT);
        list.put(COLUMN_PASSWORD, DataType.TEXT);
        list.put(COLUMN_TYPE, DataType.TEXT);
        list.put(COLUMN_USER_ID, DataType.TEXT);

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
        return new User();
    }
}
