package Data;

import Business.User;

import java.util.*;
import java.sql.*;

public class UserDAO implements Map<Integer, User> {

    Connection connection;

    public User getUser(int idUser) throws SQLException, Exception{
        Connection c = Data.connect();
        User res;
        PreparedStatement ps = null;
        ResultSet rs = null;
        if(c!=null) {
            ps = c.prepareStatement("SELECT * FROM user WHERE id = ?");
            ps.setInt(1, idUser);
            rs = ps.executeQuery();
            if(rs.next()) {
                res = new User();
                res.setId(rs.getInt("id"));
                res.setEmail(rs.getString("email"));
                res.setUsername(rs.getString("username"));
                res.setName(rs.getString("name"));
                res.setPassword(rs.getString("password"));
                res.setIdade(rs.getInt("idade"));
                res.setProfit(rs.getDouble("profit"));
                res.setPlafond(rs.getDouble("plafond"));
                return res;
            }
            else{
                throw new Exception("User not found.");
            }
        }
        else{
            throw new Exception("Unable to establish connection.");
        }
    }

    public static void update(Integer key, User value){
        User user;
        PreparedStatement preparedStatement = null;
        Connection conn = null;

        try{
            conn = Data.connect();

            preparedStatement = conn.prepareStatement("UPDATE user SET profit = ?;");
            preparedStatement.setDouble(1, value.getProfit());

            preparedStatement.executeUpdate();

            System.out.println("ai mãe");
        }
        catch (SQLException e){
            e.getMessage();
        }
        finally {
            try{
                preparedStatement.close();
                Data.close(conn);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

    }

    @Override
    public int size() {
        int size = -1;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = Data.connect();
            preparedStatement = connection.prepareStatement("SELECT COUNT(*) from USER");
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                size = resultSet.getInt(1);
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            try{
                preparedStatement.close();
                resultSet.close();
                Data.close(connection);
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
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = Data.connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE id = ?");
            preparedStatement.setString(1, Integer.toString((Integer) key));
            resultSet = preparedStatement.executeQuery();
            res = resultSet.next();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            try{
                preparedStatement.close();
                resultSet.close();
                Data.close(connection);
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
        if(value.getClass().getName().equals("ESSTrading.Business.User")){
            User user = (User) value;
            int id = user.getId();
            User thisUser = this.get(id);
            if(thisUser.equals(user)) res = true;
        }
        return res;
    }

    @Override
    public synchronized User get(Object key) {
        User user = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = Data.connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM User WHERE id =?");
            preparedStatement.setString(1, Integer.toString((Integer) key));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setUsername(resultSet.getString("username"));
                user.setName(resultSet.getString("name"));
                user.setPassword(resultSet.getString("password"));
                user.setIdade(resultSet.getInt("idade"));
                user.setProfit(resultSet.getDouble("profit"));
                user.setPlafond(resultSet.getDouble("plafond"));
            }
        }
        catch (SQLException e){
            e.getMessage();
        }
        finally {
            try{
                preparedStatement.close();
                resultSet.close();
                Data.close(connection);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return user;
    }

    @Override
    public synchronized User put(Integer key, User value) {
        User user;
        PreparedStatement preparedStatement = null;

        if(this.containsKey(key)){
            user = this.get(key);
        }
        else user = value;

        try{
            connection = Data.connect();
            preparedStatement = connection.prepareStatement("DELETE from user WHERE id=?");
            preparedStatement.setString(1, Integer.toString((Integer) key));
            preparedStatement.executeUpdate();

            System.out.println("base de dados :(((");

            preparedStatement = connection.prepareStatement("INSERT INTO user(id, email, username, name, password, idade, profit, plafond) VALUES (?,?,?,?,?,?,?,?);");
            preparedStatement.setString(1, Integer.toString(key));
            preparedStatement.setString(2, value.getEmail());
            preparedStatement.setString(3, value.getUsername());
            preparedStatement.setString(4, value.getName());
            preparedStatement.setString(5, value.getPassword());
            preparedStatement.setInt(6, value.getIdade());
            preparedStatement.setDouble(7, value.getProfit());
            preparedStatement.setDouble(8, value.getPlafond());

            preparedStatement.executeUpdate();

            System.out.println("ai mãe");
        }
        catch (SQLException e){
            e.getMessage();
        }
        finally {
            try{
                preparedStatement.close();
                Data.close(connection);
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
        PreparedStatement ppstt = null;
        try {
            connection = Data.connect();
            ppstt = connection.prepareStatement("DELETE from User WHERE id = ?");
            ppstt.setString(1,Integer.toString((Integer) key));
            ppstt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                ppstt.close();
                Data.close(connection);
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
        Statement stt = null;
        try{
            connection = Data.connect();
            stt = connection.createStatement();
            stt.executeUpdate("DELETE from User");
        }catch (Exception e){
            throw new NullPointerException(e.getMessage());
        }
        finally{
            try {
                stt.close();
                Data.close(connection);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> set = null;
        PreparedStatement ppstt = null;
        ResultSet rs = null;

        try {
            connection = Data.connect();
            set = new TreeSet<>();
            ppstt = connection.prepareStatement("SELECT * from User");
            rs = ppstt.executeQuery();
            while(rs.next()){
                set.add(rs.getInt("id"));
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            try {
                ppstt.close();
                rs.close();
                Data.close(connection);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        return set;
    }

    @Override
    public Collection<User> values() {
        Collection<User> collection = new TreeSet<>();
        PreparedStatement ppstt = null;
        ResultSet rs = null;
        try {
            connection = Data.connect();
            ppstt = connection.prepareStatement("SELECT * From User");
            rs = ppstt.executeQuery();
            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setIdade(rs.getInt("idade"));
                user.setProfit(rs.getDouble("profit"));
                user.setPlafond(rs.getDouble("plafond"));

                collection.add(user);

            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        finally {
            try {
                ppstt.close();
                rs.close();
                Data.close(connection);
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
