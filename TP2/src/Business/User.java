package Business;

import Data.*;
import Exceptions.PasswordIncorretaException;
import Exceptions.UserAlreadyExistsException;
import Exceptions.UtilizadorInexistenteException;
import GUI.MenuUser;

import java.io.Serializable;
import java.util.*;

public class User implements Serializable, MyObserver, Comparable<User> {

    private int id;
    private String email;
    private String username;
    private String name;
    private String password;
    private int idade;
    private Double profit; //lucro
    private Double plafond;

    private CfdDAO cfdDAO;
    private static UserDAO userDAO;
    private HashMap<Integer, String> notifications = new HashMap<Integer, String>();

    public User(int id, String email, String username, String name, String password, int idade, Double profit, Double plafond) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.name = name;
        this.password = password;
        this.idade = idade;
        this.profit = profit;
        this.plafond = plafond;
    }

    /**
     * Construtor vazio de um Utilizador
     */
    public User(){
        this.id = 0;
        this.email="";
        this.username="";
        this.name="";
        this.password="";
        this.idade=0;
        this.profit=0.0;
        this.plafond=0.0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }

    public Double getPlafond() {
        return plafond;
    }

    public void setPlafond(Double plafond) {
        this.plafond = plafond;
    }

    public CfdDAO getCfdDAO() {
        return cfdDAO;
    }

    public void setCfdDAO(CfdDAO cfdDAO) {
        this.cfdDAO = cfdDAO;
    }

    public HashMap<Integer, String> getNotifications() {
        return notifications;
    }

    public void setNotifications(HashMap<Integer, String> notifications) {
        this.notifications = notifications;
    }

    public void addNotification(String m) {
        Integer size = notifications.size()+1;
        notifications.put(size, m);
    }

    @Override
    public int compareTo(User user) {
        return id - user.id;
    }

    public void update(String mensagem) {
        Integer size = notifications.size() +1;
        notifications.put(size, mensagem);
    }

    public void addCfd(Integer id, Cfd cfd){
        cfdDAO.put(id, cfd);
    }
}
