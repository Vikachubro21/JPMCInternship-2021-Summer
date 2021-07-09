import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.ColumnDefinitions;

public class R4_NumColumnsAndColumnDefinitions {

    public static int getNumberOfColumnsFromTable(String keyspaceName0, String tableName0, CqlSession session0) {

        ResultSet resultSet1 = session0.execute("SELECT * FROM " + keyspaceName0 + "." + tableName0);
        ColumnDefinitions colDefinitions1 = resultSet1.getColumnDefinitions();
        int numberOfColumns = colDefinitions1.size();

        return numberOfColumns;
    }

    public static ColumnDefinitions getObjColumnDefinitionsFromTable(String keyspaceName0, String tableName0, com.datastax.oss.driver.api.core.CqlSession session0) {

        ResultSet resultSet1 = session0.execute("SELECT * FROM " + keyspaceName0 + "." + tableName0);
        ColumnDefinitions colDefinitions1 = resultSet1.getColumnDefinitions();

        return colDefinitions1;
    }

}
