package com.example.seniorcare.db.sqlite.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.seniorcare.db.context.IDbTableModelConvertible;
import com.example.seniorcare.db.context.DataType;


public class SqLiteLocalDbContext<T extends IDbTableModelConvertible> extends SQLiteOpenHelper {
    private T sample;

    public SqLiteLocalDbContext(Context context, T sample) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);

        this.sample = (T) sample.getNewInstance();
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

    @Override
    public void onOpen(SQLiteDatabase db) {
        if (db.isReadOnly()) {
            return;
        }

        createTable(db);
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

    public T getByPrimarykey(T data) {
        String selectionQuery = data.getPrimaryKey() + " = ?";
        List<String> selectionArgs = new ArrayList<>();
        selectionArgs.add(data.getPrimaryKeyValue());

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(sample.getTableName(), null, selectionQuery, selectionArgs.toArray(new String[0]), null, null, null);
        return getDataList(cursor).get(0);
    }


    public T getByPrimarykey(String primaryKeyValue) {
        if (primaryKeyValue == null) {
            return null;
        }

        T data = getInstance();
        String selectionQuery = data.getPrimaryKey() + " = ?";
        List<String> selectionArgs = new ArrayList<>();
        selectionArgs.add(primaryKeyValue);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(sample.getTableName(), null, selectionQuery, selectionArgs.toArray(new String[0]), null, null, null);

        List<T> dataList = getDataList(cursor);
        if (dataList.size() <= 0) {
            return null;
        }
        return dataList.get(0);
    }

    public List<T> searchData(T criteria) {
        HashMap<String, String> allData = criteria.getAllData();
        StringBuilder selectionQueryBuilder = null;
        List<String> selectionArgs = new ArrayList<>();
        for (String key : allData.keySet()) {
            String value = allData.get(key);
            if (value == null || value == "" || value == "null") {
                continue;
            }

            if (selectionQueryBuilder == null) {
                selectionQueryBuilder = new StringBuilder();
            } else  {
                selectionQueryBuilder.append(" AND ");
            }

            selectionQueryBuilder.append(key);
            selectionQueryBuilder.append(" = ?");
            selectionArgs.add(value);
        }
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(sample.getTableName(), null, selectionQueryBuilder.toString(), selectionArgs.toArray(new String[0]), null, null, null);
        return getDataList(cursor);
    }

    public int deleteData(T criteria) {
        String selectionQuery = criteria.getPrimaryKey() + " = ?";
        List<String> selectionArgs = new ArrayList<>();
        selectionArgs.add(criteria.getPrimaryKeyValue());

        SQLiteDatabase db = getWritableDatabase();
        return db.delete(sample.getTableName(), selectionQuery, selectionArgs.toArray(new String[0]));
    }


    public int updateData(T data) {
        String selectionQuery = data.getPrimaryKey() + " = ?";
        List<String> selectionArgs = new ArrayList<>();
        selectionArgs.add(data.getPrimaryKeyValue());

        HashMap<String, String> allNewData = data.getAllData();
        ContentValues contentValues = new ContentValues();
        for (String key :
                allNewData.keySet()) {
            contentValues.put(key, allNewData.get(key));
        }

        SQLiteDatabase db = getWritableDatabase();
        return db.update(sample.getTableName(), contentValues, selectionQuery, selectionArgs.toArray(new String[0]));
    }

//    public long getId(T data) {
//        HashMap<String, String> allData = data.getAllData();
//        StringBuilder selectionQueryBuilder = null;
//        List<String> selectionArgs = new ArrayList<>();
//        for (String key : allData.keySet()) {
//            String value = allData.get(key);
//            if (value == null || value == "") {
//                value = "null";
//            }
//
//            if (selectionQueryBuilder == null) {
//                selectionQueryBuilder = new StringBuilder();
//            } else  {
//                selectionQueryBuilder.append(" OR ");
//            }
//
//            selectionQueryBuilder.append(key);
//            selectionQueryBuilder.append(" = ?");
//            selectionArgs.add(value);
//        }
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.query(sample.getTableName(), null, selectionQueryBuilder.toString(), selectionArgs.toArray(new String[0]), null, null, null);
//        if (!cursor.moveToNext()) {
//            return -1;
//        }
//
//        return cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
//    }
//
//    public T getById(long id) {
//        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = db.query(sample.getTableName(), null, BaseColumns._ID + " = ?", new String[] { Long.toString(id) }, null, null, null);
//        List<T> dataList = getDataList(cursor);
//        return dataList.get(0);
//    };

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private T getInstance() {
        return (T) sample.getNewInstance();
    }

    private void createTable(SQLiteDatabase db) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("Create Table if not exists ");
        queryBuilder.append(sample.getTableName());
        queryBuilder.append(" (");

        queryBuilder.append(BaseColumns._ID);
        queryBuilder.append(" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL");

        HashMap<String, DataType> tableColumnsKeyTypePair = sample.getTableColumnsKeyType();
        for(String columnName : tableColumnsKeyTypePair.keySet()) {
            queryBuilder.append(" ,");
            queryBuilder.append(columnName);
            queryBuilder.append(" ");
            queryBuilder.append(tableColumnsKeyTypePair.get(columnName));
        }

        queryBuilder.append(")");

        db.execSQL(queryBuilder.toString());
    }

    private void deleteTable(SQLiteDatabase db) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("DROP TABLE IF EXISTS ");
        queryBuilder.append(sample.getTableName());
        db.execSQL(queryBuilder.toString());
    }

    private Object readCursor (Cursor cursor, String columnName, DataType dataType) {
        int columnIndex = cursor.getColumnIndexOrThrow(columnName);
        switch(dataType) {
            case INTEGER:
                return cursor.isNull(columnIndex) ? null : cursor.getInt(columnIndex);
            case TEXT:
                return cursor.getString(columnIndex);
        }
        return null;
    }

    private List<T> getDataList(Cursor cursor) {
        List<T> dataList = new ArrayList<>();
        while (cursor.moveToNext()) {
            T newInstance = getInstance();
            newInstance.setData(BaseColumns._ID, readCursor(cursor, BaseColumns._ID, DataType.INTEGER));

            for (String columnName : newInstance.getTableColumnsKeyType().keySet()) {
                newInstance.setData(
                        columnName,
                        readCursor(
                                cursor,
                                columnName,
                                newInstance.getTableColumnsKeyType().get(columnName)
                        )
                );
            }

            dataList.add(newInstance);
        }
        return dataList;
    }
}
