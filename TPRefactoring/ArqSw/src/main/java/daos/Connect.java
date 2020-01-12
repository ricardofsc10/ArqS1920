package daos;

import cliente.App;
import org.slf4j.Logger;

import java.sql.Connection;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {

    Logger log = Logger.getLogger(App.class.getName());


    private Connect() {
        throw new IllegalStaeException("Utility class");

    }

    public static Connection connect(){
        Connection connect = null;
        try{
            String path = Paths.get(".").toAbsolutePath().normalize().toString();
            String url = "jdbc:sqlite:" + path +  "/teste1.db";
            Class.forName("org.sqlite.JDBC");
            connect = DriverManager.getConnection(url);
        } catch(SQLException | ClassNotFoundException e){
            log.info(e.getMessage());
        }

        return connect;
    }

    public static void close(Connection c){
        try{
            if(c != null && !c.isClosed()){
                c.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
