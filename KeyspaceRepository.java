import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.metadata.schema.ColumnMetadata;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import com.datastax.oss.driver.api.querybuilder.select.Select;

import java.util.*;
import java.util.function.Consumer;

public class KeyspaceRepository {
    private CqlSession session;
    public KeyspaceRepository(CqlSession session)
    {
        this.session = session;
    }
    public void createKeyspace(String keyspaceName, int numberOfReplicas) {
        CreateKeyspace createKeyspace = SchemaBuilder.createKeyspace(keyspaceName)
                .ifNotExists()
                .withSimpleStrategy(numberOfReplicas);

        session.execute(createKeyspace.build());
    }
    public void useKeyspace(String keyspace) {
        session.execute("USE " + CqlIdentifier.fromCql(keyspace));
    }

    /**
     * Gets the list of Keyspaces on the Cluster.
     * @return The list of all keyspaces as a List Object
     */
    public List<String> getKeyspaceList()
    {
        Select select = QueryBuilder.selectFrom("system_schema", "keyspaces").column("keyspace_name");
        ResultSet y = session.execute(select.build());
        List<String> result = new ArrayList<>();
        y.forEach(x -> result.add(x.getString("keyspace_name")));
        return result;
    }
    /**
     * Gets the list of Tables within a given Keyspace, or all tables
     * @param keyspace The keyspace from which the list of tables is to be retrieved.
     *                 If null, all tables will be returned
     * @return The list of all keyspaces within the specified keyspace.
     */
    public List<String> getTableList(String keyspace)
    {
        Select select = QueryBuilder.selectFrom("system_schema", "tables").column("table_name");
        if (keyspace != null) {
            keyspace = keyspace.toLowerCase();
            select = select.where(Relation.column("keyspace_name").isEqualTo(QueryBuilder.literal(keyspace)));
        }
        ResultSet y = session.execute(select.build());
        List<String> result = new ArrayList<>();
        y.forEach(x -> result.add(x.getString("table_name")));
        return result;
    }
    public Set<String> getPartitions(String keyspace, String table)
    {
        List<ColumnMetadata> ace = session.getMetadata().getKeyspace(keyspace).get().getTable(table).get().getPartitionKey();
        List<String> colNames = new ArrayList<>();
        List<DataType> colTypes = new ArrayList<>();
        for(ColumnMetadata base : ace)
        {
            colNames.add(base.getName().toString());
            colTypes.add(base.getType());
        }
        keyspace = keyspace.toLowerCase();
        table = table.toLowerCase();
        Select select = QueryBuilder.selectFrom(keyspace, table).columns(colNames);
        ResultSet y = session.execute(select.build());
        Set<String> diff = new TreeSet<>();
        y.forEach(x -> {StringBuilder sb = new StringBuilder();
        for(int i=0; i < colNames.size(); i++)    {sb.append(stringConv(colTypes.get(i), x, colNames.get(i)));}
        diff.add(sb.toString());});
        return diff;
    }
    private String stringConv(DataType a, Row b, String col)
    {
        if(a.equals(DataTypes.ASCII))
        {
            return b.getString(col);
        }
        else if(a.equals(DataTypes.BIGINT))
        {
            return String.valueOf(b.getLong(col));
        }
        else if(a.equals(DataTypes.BLOB))
        {
            return "String.valueOf(b.getLong(col))";// TO BE IMPLEMENTED
        }
        else if(a.equals(DataTypes.BOOLEAN))
        {
            return String.valueOf(b.getBoolean(col));
        }
        else if(a.equals(DataTypes.COUNTER))
        {
            return "String.valueOf(b.getLong(col))";// TO BE IMPLEMENTED
        }
        else if(a.equals(DataTypes.BIGINT))
        {
            return String.valueOf(b.getLong(col));
        }
        else if(a.equals(DataTypes.DATE))
        {
            return "String.valueOf(b.getString(col))"; // TO BE IMPLEMENTED
        }
        else if(a.equals(DataTypes.DECIMAL))
        {
            return b.getBigDecimal(col).toString();
        }
        else if(a.equals(DataTypes.DOUBLE))
        {
            return String.valueOf(b.getDouble(col));
        }
        else if(a.equals(DataTypes.FLOAT))
        {
            return String.valueOf(b.getFloat(col));
        }
        else if(a.equals(DataTypes.INET))
        {
            return b.getInetAddress(col).toString();
        }
        else if(a.equals(DataTypes.INT))
        {
            return String.valueOf(b.getInt(col));
        }
        else if(a.equals(DataTypes.TIMESTAMP))
        {
            return b.getInstant(col).toString();
        }
        else if(a.equals(DataTypes.TEXT))
        {
            return b.getString(col);
        }
        // SOME TYPES YET TO BE IMPLEMENTED
        return "";
    }
}
