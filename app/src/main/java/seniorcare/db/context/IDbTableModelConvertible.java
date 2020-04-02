package seniorcare.db.context;

import java.util.HashMap;

public interface IDbTableModelConvertible {
    String getTableName();
    String getPrimaryKey();
    Object getPrimaryKeyValue();
    HashMap<String, DataType> getTableColumnsKeyType();
    // key should be column name
    void setData(String key, Object value);
    // key should be column name
    HashMap<String, Object> getAllData();
    Object getNewInstance();
}
