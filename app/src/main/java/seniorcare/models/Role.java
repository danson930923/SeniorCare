package seniorcare.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import seniorcare.db.context.IDbKeyTypePair;
import seniorcare.db.context.IDbTableModelConvertible;
import seniorcare.db.context.DbKeyTypePair;
import seniorcare.db.context.DataType;

public class Role implements IDbTableModelConvertible {
    private HashMap<String, Object> _data = new HashMap<>();
    private final static String TABLE = "Role";
    private final static String COLUMN_ROLE_NAME = "RoleName";

    public String getRoleName() {
        return (String) _data.get(COLUMN_ROLE_NAME);
    }

    public void setRoleName(String newName) {
        _data.put(COLUMN_ROLE_NAME, newName);
    }

    @Override
    public String getTableName() {
        return TABLE;
    }

    @Override
    public String getPrimaryKey() {
        return COLUMN_ROLE_NAME;
    }

    @Override
    public Object getPrimaryKeyValue() {
        return getRoleName();
    }

    @Override
    public HashMap<String, DataType> getTableColumnsKeyType() {
        HashMap<String, DataType> list = new HashMap<>();
        list.put(COLUMN_ROLE_NAME, DataType.TEXT);

        return list;
    }

    @Override
    public void setData(String key, Object value) {
        switch (key) {
            case COLUMN_ROLE_NAME:
                _data.put(key, value);
                break;
            default:
                break;
        }
    }

    @Override
    public HashMap<String, Object> getAllData() {
        return _data;
    }

    @Override
    public Object getNewInstance() {
        return new Role();
    }
}
