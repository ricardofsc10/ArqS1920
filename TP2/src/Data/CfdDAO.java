package Data;

import Business.Cfd;
import Business.User;

import java.sql.*;
import java.util.*;

public class CfdDAO implements Map<Integer, Cfd> {

    Connection connection;

    public synchronized static void update(Integer key, Cfd value){
        Cfd cfd;
        PreparedStatement preparedStatement = null;
        Connection conn = null;

        try{
            conn = Data.connect();

            preparedStatement = conn.prepareStatement("UPDATE cfd SET active = ?;");
            preparedStatement.setBoolean(1, value.getActive());

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
    public synchronized int size() {
        int size = -1;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = Data.connect();
            preparedStatement = connection.prepareStatement("SELECT COUNT(*) from cfd");
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
    public synchronized boolean isEmpty() {
        return this.size()==0;
    }

    @Override
    public synchronized boolean containsKey(Object key) {
        boolean res = false;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = Data.connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM cfd WHERE id = ?");
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
    public synchronized boolean containsValue(Object value) {
        boolean res = false;
        if(value.getClass().getName().equals("ESSTrading.Business.Cfd")){
            Cfd cfd = (Cfd) value;
            int id = cfd.getId();
            Cfd thisCfd = this.get(id);
            if(thisCfd.equals(cfd)) res = true;
        }
        return res;
    }

    @Override
    public synchronized Cfd get(Object key) {
        Cfd cfd = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = Data.connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM cfd WHERE id =?");
            preparedStatement.setString(1, Integer.toString((Integer) key));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                cfd = new Cfd();
                cfd.setId(resultSet.getInt("id"));
                cfd.setIdAsset(resultSet.getInt("idAsset"));
                cfd.setType(resultSet.getString("type"));
                cfd.setUser(resultSet.getString("user"));
                cfd.setLowerlimit(resultSet.getDouble("lowerlimit"));
                cfd.setUpperlimit(resultSet.getDouble("upperlimit"));
                cfd.setUnits(resultSet.getDouble("units"));
                cfd.setStart_value(resultSet.getDouble("start_value"));
                cfd.setActive(resultSet.getBoolean("active"));
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
        return cfd;
    }

    @Override
    public synchronized Cfd put(Integer key, Cfd value) {
        Cfd cfd;
        PreparedStatement preparedStatement = null;

        if(this.containsKey(key)){
            cfd = this.get(key);
        }
        else cfd = value;

        try{
            connection = Data.connect();
            preparedStatement = connection.prepareStatement("DELETE from cfd WHERE id=?");
            preparedStatement.setString(1, Integer.toString((Integer) key));
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("INSERT INTO cfd(id, idAsset, type, user, lowerlimit, upperlimit, units, start_value, active) VALUES (?,?,?,?,?,?,?,?,?);");
            preparedStatement.setString(1, Integer.toString((Integer) key));
            preparedStatement.setInt(2, value.getidAsset());
            preparedStatement.setString(3, value.getType());
            preparedStatement.setString(4, value.getUser());
            preparedStatement.setDouble(5, value.getLowerlimit());
            preparedStatement.setDouble(6, value.getUpperlimit());
            preparedStatement.setDouble(7, value.getUnits());
            preparedStatement.setDouble(8, value.getStart_value());
            preparedStatement.setBoolean(9, value.getActive());

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
        return cfd;
    }

    @Override
    public synchronized Cfd remove(Object key) {
        Cfd cfd = this.get((Integer) key);
        PreparedStatement ppstt = null;
        try {
            connection = Data.connect();
            ppstt = connection.prepareStatement("DELETE from cfd WHERE id = ?");
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
        return cfd;
    }

    @Override
    public synchronized void putAll(Map<? extends Integer, ? extends Cfd> m) {
        for(Cfd cfd : m.values()){
            m.entrySet().stream().forEach(e->this.put(e.getKey(), e.getValue()));
        }

    }

    @Override
    public synchronized void clear() {
        Statement stt = null;
        try{
            connection = Data.connect();
            stt = connection.createStatement();
            stt.executeUpdate("DELETE from cfd");
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
    public synchronized Set<Integer> keySet() {
        Set<Integer> set = null;
        PreparedStatement ppstt = null;
        ResultSet rs = null;

        try {
            connection = Data.connect();
            set = new TreeSet<>();
            ppstt = connection.prepareStatement("SELECT * FROM cfd");
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
    public synchronized Collection<Cfd> values() {
        Collection<Cfd> collection = new TreeSet<>();
        PreparedStatement ppstt = null;
        ResultSet rs = null;
        try {
            connection = Data.connect();
            ppstt = connection.prepareStatement("SELECT * From cfd");
            rs = ppstt.executeQuery();
            while(rs.next()){
                Cfd cfd = new Cfd();
                cfd.setId(rs.getInt("id"));
                cfd.setIdAsset(rs.getInt("idAsset"));
                cfd.setType(rs.getString("type"));
                cfd.setUser(rs.getString("user"));
                cfd.setLowerlimit(rs.getDouble("lowerlimit"));
                cfd.setUpperlimit(rs.getDouble("upperlimit"));
                cfd.setUnits(rs.getDouble("units"));
                cfd.setStart_value(rs.getDouble("start_value"));
                cfd.setActive(rs.getBoolean("active"));

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
    public synchronized Set<Entry<Integer, Cfd>> entrySet() {
        Set<Integer> set = new TreeSet<>(this.keySet());

        TreeMap<Integer, Cfd> map = new TreeMap<>();
        set.stream().forEach(e->map.put(e,this.get(e)));

        return map.entrySet();
    }
}
