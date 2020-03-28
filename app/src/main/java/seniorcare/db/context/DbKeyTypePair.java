package seniorcare.db.context;

public class DbKeyTypePair implements IDbKeyTypePair {
    private String name;
    private String type;

    public DbKeyTypePair(String name, DataType type) {
        this.name = name;
        this.type = type.name();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getType() {
        return type;
    }
}
