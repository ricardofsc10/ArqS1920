package Data;

import java.sql.*;

public class Connect {
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234";
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/tradeessapp";

public static Connection connect() {
    Connection conn = null;
    try {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(CONN_STRING,USERNAME,PASSWORD);
        System.out.println("Connected");
        return conn;
    }
    catch (SQLException e){
    System.err.println(e);
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
    return null;
}

public static void close(Connection connection) { 
        try { 
            connection.close(); 
        } catch (Exception e) {} 
    } 
}
