package abs.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
    private static String host = "";
    private static String username = "";
    private static String db = "";
    private static String pass = "";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost/abs?user=admin&password=pass");
    }
}
