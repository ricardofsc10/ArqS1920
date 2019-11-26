package Data;

import Business.Asset;
import Business.User;

import java.sql.*;
import java.util.*;

public class AssetDAO implements Map<Integer, Asset> {

    private Connection connection;

    @Override
    public synchronized int size() {
        int size = -1;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = Data.connect();
            preparedStatement = connection.prepareStatement("SELECT COUNT(*) from asset");
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
            preparedStatement = connection.prepareStatement("SELECT * FROM asset WHERE id = ?");
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
        if(value.getClass().getName().equals("ESSTrading.Business.Asset")){
            Asset asset = (Asset) value;
            int id = asset.getId();
            Asset thisAsset = this.get(id);
            if(thisAsset.equals(asset)) res = true;
        }
        return res;
    }

    @Override
    public synchronized Asset get(Object key) {
        Asset asset = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = Data.connect();
            preparedStatement = connection.prepareStatement("SELECT * FROM asset WHERE id =?");
            preparedStatement.setString(1, Integer.toString((Integer) key));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                asset = new Asset();
                asset.setId(resultSet.getInt("id"));
                asset.setCode(resultSet.getString("code"));
                asset.setName(resultSet.getString("name"));
                asset.setAsk(resultSet.getDouble("ask"));
                asset.setBid(resultSet.getDouble("bid"));
                asset.setPrev(resultSet.getDouble("prev"));
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
        return asset;
    }

    @Override
    public synchronized Asset put(Integer key, Asset value) {
        Asset asset;
        PreparedStatement preparedStatement = null;

        if(this.containsKey(key)){
            asset = this.get(key);
        }
        else asset = value;

        try{
            connection = Data.connect();
            preparedStatement = connection.prepareStatement("DELETE from asset WHERE id=?");
            preparedStatement.setString(1, Integer.toString((Integer) key));
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement("INSERT INTO asset(id, code, name, ask, bid, prev) VALUES (?,?,?,?,?,?);");
            preparedStatement.setString(1, Integer.toString(key));
            preparedStatement.setString(2, value.getCode());
            preparedStatement.setString(3, value.getName());
            preparedStatement.setDouble(4, value.getAsk());
            preparedStatement.setDouble(5, value.getBid());;
            preparedStatement.setDouble(6, value.getPrevious());

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
        return asset;
    }

    @Override
    public synchronized Asset remove(Object key) {
        Asset asset = this.get((Integer) key);
        PreparedStatement ppstt = null;
        try {
            connection = Data.connect();
            ppstt = connection.prepareStatement("DELETE from asset WHERE id = ?");
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
        return asset;
    }

    @Override
    public synchronized void putAll(Map<? extends Integer, ? extends Asset> m) {
        for(Asset asset : m.values()){
            m.entrySet().stream().forEach(e->this.put(e.getKey(), e.getValue()));
        }

    }

    @Override
    public synchronized void clear() {
        Statement stt = null;
        try{
            connection = Data.connect();
            stt = connection.createStatement();
            stt.executeUpdate("DELETE from asset");
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
        PreparedStatement ppstt= null;
        ResultSet rs = null;

        try {
            connection = Data.connect();
            set = new TreeSet<>();
            ppstt = connection.prepareStatement("SELECT * FROM asset");
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
    public synchronized Collection<Asset> values() {
        Collection<Asset> collection = new TreeSet<>();
        PreparedStatement ppstt = null;
        ResultSet rs = null;
        try {
            connection = Data.connect();
            ppstt = connection.prepareStatement("SELECT * From User");
            rs = ppstt.executeQuery();
            while(rs.next()){
                Asset asset = new Asset();
                asset.setId(rs.getInt("id"));
                asset.setCode(rs.getString("code"));
                asset.setName(rs.getString("name"));
                asset.setAsk(rs.getDouble("ask"));
                asset.setBid(rs.getDouble("bid"));
                asset.setPrev(rs.getDouble("prev"));

                collection.add(asset);

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
    public synchronized Set<Entry<Integer, Asset>> entrySet() {
        Set<Integer> set = new TreeSet<>(this.keySet());

        TreeMap<Integer,Asset> map = new TreeMap<>();
        set.stream().forEach(e->map.put(e,this.get(e)));

        return map.entrySet();
    }
}
