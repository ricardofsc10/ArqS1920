package Data;

import Business.User;

import java.util.*;
import java.sql.*;

public class UserDAO implements Map<Integer, User> {
    Connection connection;

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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE idUser = ?");
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
        if(value.getClass().getName().equals("ESS.src.Business.User")){
            User user = (User) value;
            int id = user.getIdUser();
            User thisUser = this.get(id);
            if(thisUser.equals(user)) res = true;
        }
        return res;
    }

    @Override
    public User get(Object key) {
        User usr = new User();

        try {
            connection = Connect.connect();
            PreparedStatement ppstt = connection.prepareStatement("SELECT * FROM User WHERE idUser = ?");
            ppstt.setString(1,Integer.toString((Integer) key));
            ResultSet rs = ppstt.executeQuery();
            if(rs.next()){
                usr.setIdUser(rs.getInt("idUser"));
                usr.setUsername(rs.getString("username"));
                usr.setName(rs.getString("name"));
                usr.setEmail(rs.getString("email"));
                usr.setPassword(rs.getString("password"));
                usr.setAccount_balance(rs.getFloat("account_balance"));

            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        finally{
            try {
                Connect.close(connection);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

        }
        return usr;
    }

    @Override
    public User put(Integer key, User value) {
        User usr;

        if (this.containsKey(key)){
            usr = this.get(key);
        }
        else usr = value;
        try {
            connection = Connect.connect();
            PreparedStatement ppstt = connection.prepareStatement("DELETE FROM User WHERE idUser = ?");
            ppstt.setString(1,Integer.toString((Integer) key));
            ppstt.executeUpdate();

            ppstt = connection.prepareStatement("INSERT INTO User(idUser,username,name,email,password,account_balance) VALUES (?,?,?,?,?,?)");
            ppstt.setString(1,Integer.toString((Integer) key));
            ppstt.setString(2,value.getUsername());
            ppstt.setString(3,value.getName());
            ppstt.setString(4,value.getEmail());
            ppstt.setString(5,value.getPassword());
            ppstt.setString(6,Float.toString(value.getAccount_balance()));
            ppstt.executeUpdate();


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
        return usr;
    }

    @Override
    public User remove(Object key) {
        User usr = this.get((Integer) key);
        try {
            connection = Connect.connect();
            PreparedStatement ppstt = connection.prepareStatement("DELETE FROM User WHERE idUser = ?");
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
        return usr;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends User> user) {
        for(User usr : user.values()){
            user.entrySet().stream().forEach(e->this.put(e.getKey(),e.getValue()));
        }
    }

    @Override
    public void clear() {
        try{
            connection = Connect.connect();
            Statement stt = connection.createStatement();
            stt.executeUpdate("DELETE FROM User");
        }catch (Exception e){
            throw new NullPointerException(e.getMessage());
        }
        finally{
            Connect.close(connection);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> st = null;

        try {
            connection = Connect.connect();
            st = new TreeSet<>();
            PreparedStatement ppstt = connection.prepareStatement("SELECT * FROM User");
            ResultSet rs = ppstt.executeQuery();
            while(rs.next()){
                st.add(rs.getInt("idUser"));
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
        return st;
    }

    @Override
    public Collection<User> values() {
        Collection<User> collect = new TreeSet<>();
        try {
            connection = Connect.connect();
            PreparedStatement ppstt = connection.prepareStatement("SELECT * FROM User");
            ResultSet rs = ppstt.executeQuery();
            while(rs.next()){
                User usr = new User();
                usr.setIdUser(rs.getInt("idUser"));
                usr.setUsername(rs.getString("username"));
                usr.setName(rs.getString("name"));
                usr.setEmail(rs.getString("email"));
                usr.setPassword(rs.getString("password"));
                usr.setAccount_balance(rs.getFloat("account_balance"));


                collect.add(usr);

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
        return collect;
    }

    @Override
    public Set<Entry<Integer, User>> entrySet() {
        Set<Integer> keys = new TreeSet<>(this.keySet());

        TreeMap<Integer,User> map = new TreeMap<>();
        keys.stream().forEach(e->map.put(e,this.get(e)));
        return map.entrySet();
    }
}
