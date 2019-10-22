package Data;

import Business.Trader;
import Business.User;

import Data.Connect;

import java.sql.*;
import java.util.*;


public class UserDAO implements Map<Integer, User> {

    Connection connection;

    public User getUser(int idUser) throws SQLException, Exception{
        Connection c = Connect.connect();
        User res;
        if(c!=null) {
            PreparedStatement ps = c.prepareStatement("SELECT * FROM user WHERE id = ?");
            ps.setInt(1, idUser);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) { 
                res = new User(); 
                res.setId(rs.getInt("id")); 
                res.setEmail(rs.getString("email"));
                res.setUsername(rs.getString("username"));
                res.setPassword(rs.getString("password"));
                res.setMorada(rs.getString("morada"));
                res.setIdade(rs.getInt("idade"));
                res.setContacto(rs.getInt("contacto"));
                ((Trader) res).setSaldoConta(rs.getFloat("saldo"));
                return res;
            }
            else{
                throw new Exception("User não encontrado.");
            }
        }
        else{
            throw new Exception("Unable to establish connection");
        }
}


/*public void addUser( String email,String username, String password, String morada,int idade, int contacto, int saldo) throws SQLException, Exception{
        Connection c = Connect.connect();
        if(c!=null) { 
            PreparedStatement ps = c.prepareStatement("INSERT INTO user VALUES (?,?,?,?,?,?,?,?);");
            ps.setInt(1, size()+1);
            ps.setString(2, email);
            ps.setString(3, username);
            ps.setString(4, password);
            ps.setString(5, morada);
            ps.setInt(6, idade);
            ps.setInt(7,contacto);
            ps.setInt(8, saldo);
            ps.executeUpdate();         
            Connect.close(c);
            }
        else{
            throw new Exception("Unable to establish connection");
        }
    }*/
    
    @Override
    public int size() {

        int size = -1;
        try{
            connection = Connect.connect();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) from USER");
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                size = resultSet.getInt(1);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            try{
                Connect.close(connection);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return size;
    }

    @Override
    public boolean isEmpty() {
        return this.size()==0;
    }

    @Override
    public boolean containsKey(Object key) {
        boolean res = false;
        try{
            connection = Connect.connect();
            System.out.println("Sucesso na conexão!!");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE id = ?");
            preparedStatement.setString(1, Integer.toString((Integer) key));
            ResultSet resultSet = preparedStatement.executeQuery();
            res = resultSet.next();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            try{
                Connect.close(connection);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return res;
    }

    @Override
    public boolean containsValue(Object value) {
        boolean res = false;
        if(value.getClass().getName().equals("tradeESSApp.Business.User")){
            User user = (User) value;
            int id = user.getId();
            User thisUser = this.get(id);
            if(thisUser.equals(user)) res = true;
        }
        return res;
    }

    @Override
    public User get(Object key) {
        User user = new User();

        try{
            connection = Connect.connect();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM User WHERE id =?");
            preparedStatement.setString(1, Integer.toString((Integer) key));
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setUsername(resultSet.getString("username"));
                user.setContacto(resultSet.getInt("contacto"));
                user.setIdade(resultSet.getInt("idade"));
                user.setMorada(resultSet.getString("morada"));
                user.setPassword(resultSet.getString("password"));
                ((Trader) user).setSaldoConta(resultSet.getFloat("saldo"));
            }
        }
        catch (SQLException e){
            e.getMessage();
        }
        finally {
            try{
                Connect.close(connection);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return user;
    }

    @Override
    public User put(Integer key, User value) {
        User user;

        if(this.containsKey(key)){
            user = this.get(key);
        }
        else user = value;

        try{
            connection = Connect.connect();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE from user WHERE id=?");
            preparedStatement.setString(1, Integer.toString((Integer) key));
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("INSERT INTO user VALUES (?,?,?,?,?,?,?,?);");
            preparedStatement.setString(1, Integer.toString((Integer) key));
            preparedStatement.setString(2, value.getEmail());
            preparedStatement.setString(3, value.getUsername());
            preparedStatement.setString(4, value.getPassword());
            preparedStatement.setString(5, value.getMorada());
            preparedStatement.setInt(6, value.getIdade());
            preparedStatement.setInt(7, value.getContacto());
            preparedStatement.setFloat(8, ((Trader) value).getSaldoConta());

            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.getMessage();
        }
        finally {
            try{
                Connect.close(connection);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return user;
    }

    @Override
    public User remove(Object key) {
        User user = this.get((Integer) key);
        try {
            connection = Connect.connect();
            PreparedStatement ppstt = connection.prepareStatement("DELETE from User WHERE id = ?");
            ppstt.setString(1,Integer.toString((Integer) key));
            ppstt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                Connect.close(connection);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return user;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends User> m) {
        for(User user : m.values()){
            m.entrySet().stream().forEach(e->this.put(e.getKey(), e.getValue()));
        }
    }

    @Override
    public void clear() {
        try{
            connection = Connect.connect();
            Statement stt = connection.createStatement();
            stt.executeUpdate("DELETE from User");
        }catch (Exception e){
            throw new NullPointerException(e.getMessage());
        }
        finally{
            try {
                Connect.close(connection);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> set = null;

        try {
            connection = Connect.connect();
            set = new TreeSet<>();
            PreparedStatement ppstt = connection.prepareStatement("SELECT * from User");
            ResultSet rs = ppstt.executeQuery();
            while(rs.next()){
                set.add(rs.getInt("id"));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            try {
                Connect.close(connection);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return set;
    }

    @Override
    public Collection<User> values() {
        Collection<User> collection = new TreeSet<>();
        try {
            connection = Connect.connect();
            PreparedStatement ppstt = connection.prepareStatement("SELECT * From User");
            ResultSet rs = ppstt.executeQuery();
            while(rs.next()){
                User user = new Trader();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setMorada(rs.getString("morada"));
                user.setIdade(rs.getInt("idade"));
                user.setContacto(rs.getInt("contacto"));
                ((Trader) user).setSaldoConta(rs.getFloat("saldo"));

                collection.add(user);

            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            try {
                Connect.close(connection);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return collection;
    }

    @Override
    public Set<Entry<Integer, User>> entrySet() {
        Set<Integer> set = new TreeSet<>(this.keySet());

        TreeMap<Integer,User> map = new TreeMap<>();
        set.stream().forEach(e->map.put(e,this.get(e)));

        return map.entrySet();
    }
}
