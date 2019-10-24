package Data;

import Business.Stock;

import java.sql.*;
import java.util.*;

public class StockDAO implements Map<Integer, Stock> {
    private Connection connection;

    @Override
    public int size() {
        int size = -1;
        try {
            connection = Connect.connect();
            PreparedStatement ppstt = connection.prepareStatement("SELECT COUNT(*) FROM Stock");
            ResultSet rs = ppstt.executeQuery();
            if(rs.next()){
                size = rs.getInt(1);
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE idStock = ?");
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
        if(value.getClass().getName().equals("tradeESSApp.Business.Stock")){
            Stock stock = (Stock) value;
            int idStock = stock.getIdStock();
            Stock thisStock = this.get(idStock);
            if(thisStock.equals(stock)) res = true;
        }
        return res;
    }

    @Override
    public Stock get(Object key) {

        Stock stock = new Stock();

        try {
            connection = Connect.connect();
            PreparedStatement ppstt = connection.prepareStatement("SELECT * FROM Stock WHERE idStock = ?");
            ppstt.setString(1,Integer.toString((Integer)key));
            ResultSet rs = ppstt.executeQuery();
            if(rs.next()){
                stock.setIdStock(rs.getInt("idStock"));
                stock.setName(rs.getString("name"));
                stock.setOwner(rs.getString("owner"));
                stock.setCfdBuy(rs.getFloat("cfdBuy"));
                stock.setCfdSale(rs.getFloat("cfdSale"));
            }
        }catch (SQLException e){
            System.out.printf(e.getMessage());
        }
        finally{
            try {
                Connect.close(connection);
            }catch (Exception e){
                System.out.printf(e.getMessage());
            }

        }
        return stock;
    }

    @Override
    public Stock put(Integer key, Stock value) {
        Stock stock;

        if (this.containsKey(key)){
            stock = this.get(key);
        }
        else stock = value;
        try {
            connection = Connect.connect();
            PreparedStatement ppstt = connection.prepareStatement("DELETE FROM Stock WHERE idStock = ?");
            ppstt.setString(1,Integer.toString((Integer) key));
            ppstt.executeUpdate();

            ppstt = connection.prepareStatement("INSERT INTO Stock(idStock, name, owner, cfdBuy, cfdSale) VALUES (?,?,?,?,?)");
            ppstt.setString(2,value.getName());
            ppstt.setString(3,value.getOwner());
            ppstt.setString(4,Float.toString(value.getCfdBuy()));
            ppstt.setString(5,Float.toString(value.getCfdSale()));

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
        return stock;
    }

    @Override
    public Stock remove(Object key) {
        Stock stock = this.get((Integer) key);
        try {
            connection = Connect.connect();
            PreparedStatement ppstt = connection.prepareStatement("DELETE FROM Stock WHERE idStock = ?");
            ppstt.setString(1,Integer.toString((Integer) key));

            ppstt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                Connect.close(connection);
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return stock;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Stock> m) {
        for(Stock s : m.values()){
            put(s.getIdStock(), s);
        }
    }

    @Override
    public void clear() {
        try{
            connection = Connect.connect();
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM Stock");
        }catch (Exception e){
            throw new NullPointerException(e.getMessage());
        }
        finally{
            Connect.close(connection);
        }
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> set = null;

        try {
            connection = Connect.connect();
            set = new TreeSet<>();
            PreparedStatement ppstt = connection.prepareStatement("SELECT * FROM Stock");
            ResultSet rs = ppstt.executeQuery();
            while(rs.next()){
                set.add(rs.getInt("id_market_stock"));
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
    public Collection<Stock> values() {
        Collection<Stock> collection = new TreeSet<>();
        try {
            connection = Connect.connect();
            PreparedStatement ppstt = connection.prepareStatement("SELECT * FROM Stock");
            ResultSet rs = ppstt.executeQuery();
            while(rs.next()){
                Stock s = new Stock();
                s.setIdStock(rs.getInt("idStock"));
                s.setName(rs.getString("name"));
                s.setOwner(rs.getString("owner"));
                s.setCfdBuy(rs.getFloat("cfdBuy"));
                s.setCfdSale(rs.getFloat("cfdSale"));

                collection.add(s);

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
    public Set<Entry<Integer, Stock>> entrySet() {
        Set<Integer> keys = new TreeSet<>(this.keySet());
        TreeMap<Integer, Stock> map = new TreeMap<>();
        keys.stream().forEach(e-> map.put(e,this.get(e)));
        return map.entrySet();
    }
}
