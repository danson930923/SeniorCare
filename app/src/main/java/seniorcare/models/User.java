package seniorcare.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import seniorcare.db.context.DbKeyTypePair;
import seniorcare.db.context.IDbKeyTypePair;
import seniorcare.db.context.IDbTableModelConvertible;
import seniorcare.db.context.DataType;

public class User implements IDbTableModelConvertible {
    private HashMap<String, Object> _data;
    private final static String TABLE = "User";
    private final static String COLUMN_NAME = "Name";
    private final static String COLUMN_PASSWORD = "Password";
    private final static String COLUMN_ROLE_ID = "RoleId";


    public String getName() {
        return (String) _data.get(COLUMN_NAME);
    }

    public void setName(String newName) {
        _data.put(COLUMN_NAME, newName);
    }

    public String getPassword() {
        return (String) _data.get(COLUMN_PASSWORD);
    }

    public void setPassword(String newPassword) {
        _data.put(COLUMN_PASSWORD, newPassword);
    }

    public long getRoleId() {
        return (long) _data.get(COLUMN_ROLE_ID);
    }

    public void setRoleId(long roleId) {
        _data.put(COLUMN_ROLE_ID, roleId);
    }

    @Override
    public String getTableName() {
        return TABLE;
    }

    @Override
    public String getPrimaryKey() {
        return COLUMN_NAME;
    }

    @Override
    public Object getPrimaryKeyValue() {
        return getName() + getPassword();
    }

    @Override
    public HashMap<String, DataType> getTableColumnsKeyType() {
        HashMap<String, DataType> list = new HashMap<>();
        list.put(COLUMN_NAME, DataType.TEXT);
        list.put(COLUMN_PASSWORD, DataType.TEXT);
        list.put(COLUMN_ROLE_ID, DataType.INTEGER);

        return list;
    }

    @Override
    public void setData(String key, Object value) {
        _data.put(key, value);
    }

    @Override
    public HashMap<String, String> getAllData() {
        return null;
    }
}
