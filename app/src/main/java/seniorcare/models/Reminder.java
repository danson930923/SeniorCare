package seniorcare.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import seniorcare.db.context.DbKeyTypePair;
import seniorcare.db.context.IDbKeyTypePair;
import seniorcare.db.context.IDbTableModelConvertible;
import seniorcare.db.context.DataType;

public class Reminder implements IDbTableModelConvertible {
    private HashMap<String, Object> _data = new HashMap<>();
    private final static String TABLE = "Reminder";
    private final static String COLUMN_TITLE = "Title";
    private final static String COLUMN_TIME = "Time";
    private final static String COLUMN_USER_ID = "UserId";

    public String getTitle() {
        return (String) _data.get(COLUMN_TITLE);
    }

    public void setName(String newTitle) {
        _data.put(COLUMN_TITLE, newTitle);
    }

    public String getTime() {
        return (String) _data.get(COLUMN_TIME);
    }

    public void setTime(String newTime) {
        _data.put(COLUMN_TIME, newTime);
    }

    public Long getUserId() {
        return (Long) _data.get(COLUMN_USER_ID);
    }

    public void setUserId(long userId) {
        _data.put(COLUMN_USER_ID, userId);
    }

    @Override
    public String getTableName() {
        return TABLE;
    }

    @Override
    public String getPrimaryKey() {
        return COLUMN_USER_ID + COLUMN_TITLE + COLUMN_TIME;
    }

    @Override
    public Object getPrimaryKeyValue() {
        return getUserId() + "_" + getTitle() + "_" + getTime();
    }

    @Override
    public HashMap<String, DataType> getTableColumnsKeyType() {
        HashMap<String, DataType> list = new HashMap<>();
        list.put(COLUMN_TITLE, DataType.TEXT);
        list.put(COLUMN_TIME, DataType.TEXT);

        return list;
    }

    @Override
    public void setData(String key, Object value) {
        switch (key) {
            case COLUMN_TITLE:
            case COLUMN_TIME:
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
        return new Reminder();
    }
}
