package com.example.seniorcare.models;

import android.location.Location;

import com.example.seniorcare.db.context.DataType;
import com.example.seniorcare.db.context.IDbTableModelConvertible;
import com.example.seniorcare.utils.HashMapUtils;

import java.util.HashMap;

public class LocationInfo implements IDbTableModelConvertible {

    private HashMap<String, Object> _data = new HashMap<>();
    private final static String TABLE_NAME = "LocationInfo";
    private final static String COLUMN_USER_ID = "UserId";
    private final static String COLUMN_LOCATION_INFO = "LocationInfo";

    public String getUserId() {
        return (String) _data.get(COLUMN_USER_ID);
    }

    public void setUserId(String userId) {
        _data.put(COLUMN_USER_ID, userId);
    }

    public Location getLocationInfo() {
        String locationString = (String)_data.get(COLUMN_LOCATION_INFO);
        String[] latLng = locationString.split(",");
        Location location = new Location("db");
        location.setLatitude(Double.parseDouble(latLng[0]));
        location.setLongitude(Double.parseDouble(latLng[1]));
        return location;
    }

    public void setLocationInfo(Location locationInfo) {
        String locationString = locationInfo.getLatitude() + "," + locationInfo.getLongitude();
        _data.put(COLUMN_LOCATION_INFO, locationString);
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
        list.put(COLUMN_LOCATION_INFO, DataType.TEXT);

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
        return new LocationInfo();
    }
}
