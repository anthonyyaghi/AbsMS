package abs.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
    private static Connection connection= null;

    public static Connection getConnection() throws SQLException {
        return connection;
    }

    public static void setConnection(Connection connection) {
        ConnectionProvider.connection = connection;
    }
}
