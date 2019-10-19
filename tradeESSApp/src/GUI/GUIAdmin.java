package GUI;
import java.sql.SQLException;

public class GUIAdmin extends GUI {

    protected static boolean adminLoggedIn()throws SQLException, Exception{
        //temos que definir o que é o que admin é capaz de fazer
        return true;
    }
}
