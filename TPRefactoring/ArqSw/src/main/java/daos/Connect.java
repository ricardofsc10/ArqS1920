package daos;

import java.util.logging.Logger;

import java.sql.Connection;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {


     private static Logger log = Logger.getLogger(Connect.class.getName());


    private Connect() {
        throw new IllegalStateException("Utility class");

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
            log.info(e.getMessage());
        }
    }
}
