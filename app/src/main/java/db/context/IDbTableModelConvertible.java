package db.context;

import java.security.KeyPair;
import java.util.HashMap;

public interface IDbTableModelConvertible {
    String getTableName();
    IDbKeyTypePair getPrimaryKeyType();
    Iterable<IDbKeyTypePair> getTableColumnsKeyType();
    // key should be column name
    void setData(Object key, Object value);
    // key should be column name
    HashMap<String, String> getAllData();
}
