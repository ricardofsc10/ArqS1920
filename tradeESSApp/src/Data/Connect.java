package Data;

import java.awt.*;
import java.sql.*;

public class Connect {
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Ineslucasalves02";
    private static final String CONN_STRING = "jdbc:mysql://127.0.0.1:3306/tradeessapp"; // /Users/ines/Desktop/ArqS1920/tradeESSApp

public static Connection connect() {
    Connection conn = null;
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(CONN_STRING,USERNAME,PASSWORD);
        System.out.println("Connecteddddd");
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
