package Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import Business.*;
import GUI.*;
import Exceptions.UserAlreadyExistsException;
import Exceptions.UtilizadorInexistenteException;

public class Data {

    private static UserDAO userDAO;
    private CfdDAO cfdDAO;
    private AssetDAO assetDAO;
    public ConcurrentHashMap <Integer, Watchlist> watchlist;

    public static Connection connect(){
        Connection connect = null;
        try{
            String url = "jdbc:sqlite:/Users/ines/Desktop/ESSTrading/database.db";
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

    public Data(){
        this.userDAO = new UserDAO();
        this.cfdDAO = new CfdDAO();
        this.assetDAO = new AssetDAO();
        this.watchlist = new ConcurrentHashMap<Integer, Watchlist>();
    }

    public Data(UserDAO userDAO, CfdDAO cfdDAO, AssetDAO assetDAO, ConcurrentHashMap <Integer, Watchlist> watchlist){
        this.userDAO = userDAO;
        this.cfdDAO = cfdDAO;
        this.assetDAO = assetDAO;
        this.watchlist = watchlist;
    }

    public void clearAll(){
        cfdDAO.clear();
        assetDAO.clear();
    }

    //User Operations

    public User getUser(String username){
        return this.userDAO.get(idUserGivenUsername(username));
    }

    /**
     *
     * @param username
     * @return idUser
     */
    public int idUserGivenUsername(String username){
        int id = -1;
        for(User user : userDAO.values()){
            if(user.getUsername().equals(username)){
                id = user.getId();
            }
        }
        return id;
    }

    public void addUser(String email, String username, String name, String pass, int idade, Double profit, Double plafond) throws UserAlreadyExistsException {
        int id = userDAO.size()+1;

        User user = new User(id, email, username, name, pass, idade, profit, plafond);

        if((this.userDAO.get(idUserGivenUsername(username))) == null) this.userDAO.put(id, user);
        else throw new UserAlreadyExistsException("User already exists!");
    }

    public void removeUser(User atual) throws UtilizadorInexistenteException {
        if(this.userDAO.containsKey(atual.getId())){
            this.userDAO.remove(idUserGivenUsername(atual.getUsername()));
            Menu.logOut();
        }
        else throw new UtilizadorInexistenteException("Ação abortada! Este utilizador não existe...");
    }

    public User giveuser(String username) {
        User user = new User();

        int id = idUserGivenUsername(username);

        if(this.userDAO.containsKey(id)) user = this.userDAO.get(id);
        return user;
    }


    //CFD Operations
    public Cfd getCfd(Integer id) {
        return this.cfdDAO.get(id);
    }

    public CfdDAO getAllCfd(){
        return this.cfdDAO;
    }

    //Asset Operations
    public Asset getAsset(Integer code){
        return this.assetDAO.get(code);
    }

    //Watchlist Operations
    public ConcurrentHashMap <Integer, Watchlist> getWatchlist() {
        return watchlist;
    }

    public void addtoWatchlist (Watchlist c) {
        Integer size = watchlist.size()+1;
        watchlist.put(size, c);
    }
}
