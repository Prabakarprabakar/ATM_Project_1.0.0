package atm;
import java.sql.*;

public class DataBase {
    private static final String JDBC_URL="jdbc:mysql://localhost:3306/ATM_SYSTEM_DataBase";
    private static final String USERNAME="root";
    private static final String PASSWORD="Prabakar@2828";
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println(e);
        }
        return connection;
    }

}
