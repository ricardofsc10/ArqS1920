package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionDB {


    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String URL = "localhost";
    private static final String SCHEMA = "database";

    //abrir connection
    public static Connection startConnection(){
        Connection connection = null;

        try{
            String url = "jdbc:mysql://" + URL + "/" + SCHEMA + "?allowPublicKeyRetrieval=true&useSSL=false";
            String driver = "org.sqlite.JBDC"; //
            Class.forName(driver);

            connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
            System.out.println("Connected!");
        }
        catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    //fechar connection (se ela estiver aberta)
    public static void closeConnection(Connection connection){
        try{
            if(connection != null && !connection.isClosed()) connection.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }
}
