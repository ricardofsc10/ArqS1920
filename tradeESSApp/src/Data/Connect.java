package Data;

import java.awt.*;
import java.sql.*;

public class Connect {
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Ineslucasalves02";
    private static final String CONN_STRING = "jdbc:mysql://127.0.0.1:3306/tradeessapp"; // /Users/ines/Desktop/ArqS1920/tradeESSApp

public static Connection connect() {
    Connection connect = null;
    try{
        String url = "jdbc:sqlite:/Users/ines//Desktop/database.db";
        Class.forName("org.sqlite.JDBC");
        connect = DriverManager.getConnection(url);
    } catch(SQLException | ClassNotFoundException e){
        System.out.println(e.getMessage());
    }

    return connect;
}

public static void close(Connection connection) { 
        try { 
            connection.close(); 
        } catch (Exception e) {} 
    } 
}
