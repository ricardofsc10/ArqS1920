package Data;

import java.sql.*;

public class Connect {
    private static final String URL = "localhost:3306"; 
    private static final String SCHEMA = "tradeessapp"; 
    private static final String USERNAME = "root"; 
    private static final String PASSWORD = "arqs19"; 
    
    public static Connection connect() { 
        try { 
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            Connection cn = DriverManager.getConnection("jdbc:mysql://"+URL+"/"+SCHEMA+"?user="+USERNAME+"&password="+PASSWORD); 
            return cn; 
        } catch (Exception e) {
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