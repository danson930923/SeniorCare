package db.context.sqlite.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import db.context.IDbKeyTypePair;
import db.context.IDbTableModelConvertible;
import db.context.sqlite.SqlLiteDataType;


public class DbContext<T extends IDbTableModelConvertible> extends SQLiteOpenHelper {
    private T sample;

    // for better performance
    private HashMap<String, String> tableKeyTypeHashMap;

    public DbContext(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);

        sample = getInstance();
        tableKeyTypeHashMap = new HashMap<>();

        IDbKeyTypePair primaryKeyTypePair = sample.getPrimaryKeyType();
        tableKeyTypeHashMap.put(primaryKeyTypePair.getName(), primaryKeyTypePair.getType());
        for(IDbKeyTypePair dbKeyTypePair : sample.getTableColumnsKeyType()) {
            tableKeyTypeHashMap.put(dbKeyTypePair.getName(), dbKeyTypePair.getType());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        deleteTable(db);
        onCreate(db);
    }


    public long insertData(T newData) {
        SQLiteDatabase db = getWritableDatabase();

        HashMap<String, String> allNewData = newData.getAllData();
        ContentValues values = new ContentValues();
        for (String key :
             allNewData.keySet()) {
            values.put(key, allNewData.get(key));
        }
        return db.insert(sample.getTableName(), null, values);
    }

    public List<T> getDtatList() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(sample.getTableName(), null, null, null, null, null, null);
        return getDataList(cursor);
    }

    public List<T> serachData(T criteria) {
        HashMap<String, String> allData = criteria.getAllData();
        StringBuilder selectionQueryBuilder = null;
        List<String> selectionArgs = new ArrayList<>();
        for (String key : allData.keySet()) {
            String value = allData.get(key);
            if (value == null || value == "") {
                continue;
            }

            if (selectionQueryBuilder == null) {
                selectionQueryBuilder = new StringBuilder();
            } else  {
                selectionQueryBuilder.append(" OR ");
            }

            selectionQueryBuilder.append(key);
            selectionQueryBuilder.append(" = ?");
            selectionArgs.add(value);
        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(sample.getTableName(), null, selectionQueryBuilder.toString(), (String[])selectionArgs.toArray(), null, null, null);
        return getDataList(cursor);
    }

    public int deleteData(T criteria) {
        HashMap<String, String> allData = criteria.getAllData();
        StringBuilder selectionQueryBuilder = null;
        List<String> selectionArgs = new ArrayList<>();
        for (String key : allData.keySet()) {
            String value = allData.get(key);
            if (value == null || value == "") {
                continue;
            }

            if (selectionQueryBuilder == null) {
                selectionQueryBuilder = new StringBuilder();
            } else  {
                selectionQueryBuilder.append(" OR ");
            }

            selectionQueryBuilder.append(key);
            selectionQueryBuilder.append(" = ?");
            selectionArgs.add(value);
        }


        SQLiteDatabase db = getWritableDatabase();
        return db.delete(sample.getTableName(), selectionQueryBuilder.toString(), (String[])selectionArgs.toArray());
    }


    public int updateData(T criteria, T newData) {
        HashMap<String, String> allCriteriaData = criteria.getAllData();

        StringBuilder selectionQueryBuilder = null;
        List<String> selectionArgs = new ArrayList<>();
        for (String key : allCriteriaData.keySet()) {
            String value = allCriteriaData.get(key);
            if (value == null || value == "") {
                continue;
            }

            if (selectionQueryBuilder == null) {
                selectionQueryBuilder = new StringBuilder();
            } else  {
                selectionQueryBuilder.append(" OR ");
            }

            selectionQueryBuilder.append(key);
            selectionQueryBuilder.append(" = ?");
            selectionArgs.add(value);
        }

        HashMap<String, String> allNewData = newData.getAllData();
        ContentValues values = new ContentValues();
        for (String key :
                allNewData.keySet()) {
            values.put(key, allNewData.get(key));
        }

        SQLiteDatabase db = getWritableDatabase();
        return db.update(sample.getTableName(), values, selectionQueryBuilder.toString(), (String[])selectionArgs.toArray());
    }

    public long getId(T data) {
        HashMap<String, String> allData = data.getAllData();
        StringBuilder selectionQueryBuilder = null;
        List<String> selectionArgs = new ArrayList<>();
        for (String key : allData.keySet()) {
            String value = allData.get(key);
            if (value == null || value == "") {
                value = "null";
            }

            if (selectionQueryBuilder == null) {
                selectionQueryBuilder = new StringBuilder();
            } else  {
                selectionQueryBuilder.append(" OR ");
            }

            selectionQueryBuilder.append(key);
            selectionQueryBuilder.append(" = ?");
            selectionArgs.add(value);
        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(sample.getTableName(), null, selectionQueryBuilder.toString(), (String[])selectionArgs.toArray(), null, null, null);
        return cursor.getColumnIndexOrThrow(BaseColumns._ID);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private T getInstance() {
        Class<T> classT = null;

        try {
            return classT.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createTable(SQLiteDatabase db) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("Create Table ");
        queryBuilder.append(sample.getTableName());
        queryBuilder.append(" (");

        IDbKeyTypePair primaryKeyTypePair = sample.getPrimaryKeyType();
        queryBuilder.append(primaryKeyTypePair.getName());
        queryBuilder.append(primaryKeyTypePair.getType());
        queryBuilder.append(" PRIMARY KEY");

        Iterable<IDbKeyTypePair> tableColumnsKeyTypePair = sample.getTableColumnsKeyType();
        for(IDbKeyTypePair tableColumnKeyTypePair : tableColumnsKeyTypePair) {
            queryBuilder.append(",");
            queryBuilder.append(tableColumnKeyTypePair.getName());
            queryBuilder.append(" ");
            queryBuilder.append(tableColumnKeyTypePair.getType());
        }

        db.execSQL(queryBuilder.toString());
    }

    private void deleteTable(SQLiteDatabase db) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("DROP TABLE IF EXISTS ");
        queryBuilder.append(sample.getTableName());
        db.execSQL(queryBuilder.toString());
    }

    private Object readCursor (Cursor cursor, String columnName, SqlLiteDataType sqlLiteDataType) {
        int columnIndex = cursor.getColumnIndex(columnName);
        switch(sqlLiteDataType) {
            case INTEGER:
                return cursor.getInt(columnIndex);
            case TEXT:
                return cursor.getString(columnIndex);
        }
        return null;
    }

    private List<T> getDataList(Cursor cursor) {
        List<T> dataList = new ArrayList<>();
        while (cursor.moveToNext()) {
            T newInstance = getInstance();

            IDbKeyTypePair primaryKeyTypePair = newInstance.getPrimaryKeyType();
            newInstance.setData(
                    primaryKeyTypePair.getName(),
                    readCursor(
                            cursor,
                            primaryKeyTypePair.getName(),
                            SqlLiteDataType.valueOf(primaryKeyTypePair.getType())
                    )
            );

            dataList.add(newInstance);
        }
        return dataList;
    }
}
