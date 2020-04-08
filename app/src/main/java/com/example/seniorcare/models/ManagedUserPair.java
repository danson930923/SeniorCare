package com.example.seniorcare.models;

import com.example.seniorcare.db.context.DataType;
import com.example.seniorcare.db.context.IDbTableModelConvertible;
import com.example.seniorcare.utils.HashMapUtils;

import java.util.HashMap;

public class ManagedUserPair implements IDbTableModelConvertible {
    private HashMap<String, Object> _data = new HashMap<>();
    private final static String TABLE_NAME = "ManagedUserPair";
    private final static String COLUMN_MANAGER_ID = "ManagerId";
    private final static String COLUMN_MANAGED_ID = "ManagedId";
    private final static String COLUMN_MANAGED_USER_PAIR_ID = "ManagedUserPairId";

    public String getManagerId() {
        return (String) _data.get(COLUMN_MANAGER_ID);
    }

    public void setManagerId(String managerId) {
        _data.put(COLUMN_MANAGER_ID, managerId);
    }

    public String getManagedId() {
        return (String) _data.get(COLUMN_MANAGED_ID);
    }

    public void setManagedId(String managedId) {
        _data.put(COLUMN_MANAGED_ID, managedId);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getPrimaryKey() {
        return COLUMN_MANAGED_USER_PAIR_ID;
    }

    @Override
    public String getPrimaryKeyValue() {
        return getManagerId() + getManagedId();
    }

    @Override
    public HashMap<String, DataType> getTableColumnsKeyType() {
        HashMap<String, DataType> list = new HashMap<>();
        list.put(COLUMN_MANAGER_ID, DataType.TEXT);
        list.put(COLUMN_MANAGED_ID, DataType.TEXT);
        list.put(COLUMN_MANAGED_USER_PAIR_ID, DataType.TEXT);

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
        return new ManagedUserPair();
    }
}
