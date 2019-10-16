package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {

    //abrir connection
    public static Connection startConnection(){
        Connection connection = null;
        try{
            String url = "jdbc:mysqlite://localhost:3306/database"; //??? not sure
            String driver = "org.sqlite.JBDC"; // oh well :))))))
            Class.forName(driver);

            connection = DriverManager.getConnection(url);
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
