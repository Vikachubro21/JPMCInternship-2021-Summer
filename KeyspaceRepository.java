import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.metadata.schema.ColumnMetadata;
import com.datastax.oss.driver.api.core.metrics.Metrics;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import com.datastax.oss.driver.api.querybuilder.select.Select;

import java.util.*;

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
     * @return The list of all tables within the specified keyspace.
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
    /**
     * Gets the list of Column Names that are Partition Keys
     * @param keyspace The keyspace from which the Partition Column List is to be retrieved.
     * @param table The table from which the Partition Column List is to be retrieved.
     * @return The list of all Columns that are Partition Keys within the specified table.
     */
    public List<String> getPartitionColList(String keyspace, String table)
    {
        List<ColumnMetadata> ace = session.getMetadata().getKeyspace(keyspace).get().getTable(table).get().getPartitionKey();
        List<String> colNames = new ArrayList<>();
        for(ColumnMetadata base : ace)
        {
            colNames.add(base.getName().toString());
        }
        return colNames;
    }
    /**
     * Gets the list of Column Data Types of Columns that are Partition Keys
     * @param keyspace The keyspace from which the Partition Column Type List is to be retrieved.
     * @param table The table from which the Partition Column Type List is to be retrieved.
     * @return The list of all Column Types of Columns that are Partition Keys within the specified table.
     */
    public List<DataType> getPartitionColTypeList(String keyspace, String table)
    {
        List<ColumnMetadata> ace = session.getMetadata().getKeyspace(keyspace).get().getTable(table).get().getPartitionKey();
        List<DataType> colTypes = new ArrayList<>();
        for(ColumnMetadata base : ace)
        {
            colTypes.add(base.getType());
        }
        return colTypes;
    }
    /**
     * Gets the list of Partition Keys within a specified table. To get number of partitions, use the .size() method of the List Class
     * @param keyspace The keyspace from which the Partition List is to be retrieved.
     * @param table The table from which the Partition List is to be retrieved.
     * @return The list of all Partition Keys within the specified table.
     */
    public List<String> getPartitionList(String keyspace, String table)
    {
        List<String> colNames = getPartitionColList(keyspace, table);
        List<DataType> colTypes = getPartitionColTypeList(keyspace, table);
        keyspace = keyspace.toLowerCase();
        table = table.toLowerCase();
        Select select = QueryBuilder.selectFrom(keyspace, table).columns(colNames);
        ResultSet y = session.execute(select.build());
        Set<String> diffStrings = new TreeSet<>();
        y.forEach(x -> {StringBuilder sb = new StringBuilder();
                        sb.append('(');
                        for(int i=0; i < colNames.size(); i++)
                        {
                            sb.append(stringConv(colTypes.get(i), x, colNames.get(i)) + ", ");
                        }
                        sb.delete(sb.length()-2, sb.length());
                        sb.append(')');
                        diffStrings.add(sb.toString());
                        });
        List<String> parts = new ArrayList<>();
        for(String x: diffStrings)
        {
            parts.add(x);
        }
        return parts;
    }
    /**
     * Gets a map of Partition Keys within a specified table mapped to the number of Rows that they contain within them.
     * @param keyspace The keyspace from which the PartitionToRows Map is to be retrieved.
     * @param table The table from which the PartitionToRows Map is to be retrieved.
     * @return The map of all Partition Keys to the Number of Rows they house within the specified table.
     */
    public Map<String, Integer> getNumRowsPerPart(String keyspace, String table)
    {
        List<String> colNames = getPartitionColList(keyspace, table);
        List<DataType> colTypes = getPartitionColTypeList(keyspace, table);
        keyspace = keyspace.toLowerCase();
        table = table.toLowerCase();
        Select select = QueryBuilder.selectFrom(keyspace, table).columns(colNames);
        ResultSet y = session.execute(select.build());
        Map<String, Integer> partitionKeysTONumInPartition = new TreeMap<>();
        y.forEach(x -> {StringBuilder sb = new StringBuilder();
                        sb.append('(');
                        for(int i=0; i < colNames.size(); i++)
                        {
                            sb.append(stringConv(colTypes.get(i), x, colNames.get(i)) + ", ");
                        }
                        sb.delete(sb.length()-2, sb.length());
                        sb.append(')');
                        if(partitionKeysTONumInPartition.containsKey(sb.toString()))
                        {
                            partitionKeysTONumInPartition.put(sb.toString(), partitionKeysTONumInPartition.get(sb.toString())+1);
                        }
            else
            {
                partitionKeysTONumInPartition.put(sb.toString(), 1);
            }
        });
        return partitionKeysTONumInPartition;
    }
    /*public double getClusterSize()
    {
        Optional<Metrics> a = session.getMetrics();
        if(a.isPresent())
        {
            a.get().getSessionMetric()
        }
    }

     */
    private String stringConv(DataType a, Row b, String col)
    {
        if(a.equals(DataTypes.ASCII)) {
            return b.getString(col);
        }
        else if(a.equals(DataTypes.BIGINT)) {
            return String.valueOf(b.getLong(col));
        }
        else if(a.equals(DataTypes.BLOB)) {
            return "String.valueOf(b.getLong(col))";// TO BE IMPLEMENTED
        }
        else if(a.equals(DataTypes.BOOLEAN)) {
            return String.valueOf(b.getBoolean(col));
        }
        else if(a.equals(DataTypes.COUNTER)) {
            return "String.valueOf(b.getLong(col))";// TO BE IMPLEMENTED
        }
        else if(a.equals(DataTypes.DATE)) {
            return "String.valueOf(b.getString(col))"; // TO BE IMPLEMENTED
        }
        else if(a.equals(DataTypes.DECIMAL)) {
            return b.getBigDecimal(col).toString();
        }
        else if(a.equals(DataTypes.DOUBLE)) {
            return String.valueOf(b.getDouble(col));
        }
        else if(a.equals(DataTypes.FLOAT)) {
            return String.valueOf(b.getFloat(col));
        }
        else if(a.equals(DataTypes.INET)) {
            return b.getInetAddress(col).toString();
        }
        else if(a.equals(DataTypes.INT)) {
            return String.valueOf(b.getInt(col));
        }
        else if(a.equals(DataTypes.TIMESTAMP)) {
            return b.getInstant(col).toString();
        }
        else if(a.equals(DataTypes.TEXT)) {
            return b.getString(col);
        }
        else if(a.equals(DataTypes.UUID)) {
            return  b.getUuid(col).toString();
        }
        // SOME TYPES YET TO BE IMPLEMENTED
        return "";
    }
    private int intConvert(DataType a)
    {
        if(a.equals(DataTypes.ASCII))
        {
            return 2;
        }
        else if(a.equals(DataTypes.BIGINT))
        {
            return 80; //implement more flexible value
        }
        else if(a.equals(DataTypes.BLOB))
        {
            return 65535; //implement more flexible value
        }
        else if(a.equals(DataTypes.BOOLEAN))
        {
            return 1;
        }
        else if(a.equals(DataTypes.COUNTER))
        {
            return 64;
        }
        else if(a.equals(DataTypes.DATE))
        {
            return 32;
        }
        else if(a.equals(DataTypes.DECIMAL))
        {
            return 5;
        }
        else if(a.equals(DataTypes.DOUBLE))
        {
            return 8;
        }
        else if(a.equals(DataTypes.FLOAT))
        {
            return 4;
        }
        else if(a.equals(DataTypes.INET))
        {
            return 19;
        }
        else if(a.equals(DataTypes.INT))
        {
            return 4;
        }
        else if(a.equals(DataTypes.TIMESTAMP))
        {
            return 13;
        }
        else if(a.equals(DataTypes.TEXT))
        {
            return 10;
        }
        // SOME TYPES YET TO BE IMPLEMENTED
        return 0;
    }
}
