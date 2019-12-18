package Data;

import Business.MarketStock;

import java.sql.*;
import java.util.*;

public class MarketStockDAO implements Map<Integer, MarketStock> {
    private Connection connection;
    @Override
    public int size() {
        int size = -1;
        try {
            connection = Connect.connect();
            PreparedStatement ppstt = connection.prepareStatement("SELECT COUNT(*) FROM MarketStock");
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
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM MarketStock WHERE idStock = ?");
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
        if(value.getClass().getName().equals("ESS.src.Business.MarketStock")){
            MarketStock stock = (MarketStock) value;
            int idStock = stock.getId_stock();
            MarketStock thisStock = this.get(idStock);
            if(thisStock.equals(stock)) res = true;
        }
        return res;
    }

    @Override
    public MarketStock get(Object key) {
        MarketStock stock = new MarketStock();

        try {
            connection = Connect.connect();
            PreparedStatement ppstt = connection.prepareStatement("SELECT * FROM MarketStock WHERE idStock = ?");
            ppstt.setString(1,Integer.toString((Integer)key));
            ResultSet rs = ppstt.executeQuery();
            if(rs.next()){
                stock.setId_stock(rs.getInt("idStock"));
                stock.setName(rs.getString("name"));
                stock.setOwner(rs.getString("owner"));
                stock.setCfd_buy(rs.getFloat("cfdBuy"));
                stock.setCfd_sale(rs.getFloat("cfdSale"));
                stock.setPrice(rs.getFloat("price"));
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
    public MarketStock put(Integer key, MarketStock value) {
        MarketStock stock;

        if (this.containsKey(key)){
            stock = this.get(key);
        }
        else stock = value;
        try {
            connection = Connect.connect();
            PreparedStatement ppstt = connection.prepareStatement("DELETE FROM MarketStock WHERE idStock = ?");
            ppstt.setString(1,Integer.toString((Integer) key));
            ppstt.executeUpdate();

            ppstt = connection.prepareStatement("INSERT INTO MarketStock(idStock, name, owner, cfdBuy, cfdSale, Price) VALUES (?,?,?,?,?,?)");
            ppstt.setString(2,value.getName());
            ppstt.setString(3,value.getOwner());
            ppstt.setString(4,Float.toString(value.getCfd_Buy()));
            ppstt.setString(5,Float.toString(value.getCfd_Sale()));
            ppstt.setString(6,Float.toString(value.getPrice()));

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
    public MarketStock remove(Object key) {
        MarketStock stock = this.get((Integer) key);
        try {
            connection = Connect.connect();
            PreparedStatement ppstt = connection.prepareStatement("DELETE FROM MarketStock WHERE idStock = ?");
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
    public void putAll(Map<? extends Integer, ? extends MarketStock> map) {
        for(MarketStock s : map.values()){
            put(s.getId_stock(), s);
        }
    }

    @Override
    public void clear() {
        try{
            connection = Connect.connect();
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM MarketStock");
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
            PreparedStatement ppstt = connection.prepareStatement("SELECT * FROM MarketStock");
            ResultSet rs = ppstt.executeQuery();
            while(rs.next()){
                set.add(rs.getInt("idStock"));
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
    public Collection<MarketStock> values() {
        Collection<MarketStock> collection = new TreeSet<>();
        try {
            connection = Connect.connect();
            PreparedStatement ppstt = connection.prepareStatement("SELECT * FROM MarketStock");
            ResultSet rs = ppstt.executeQuery();
            while(rs.next()){
                MarketStock s = new MarketStock();
                s.setId_stock(rs.getInt("idStock"));
                s.setName(rs.getString("name"));
                s.setOwner(rs.getString("owner"));
                s.setCfd_buy(rs.getFloat("cfdBuy"));
                s.setCfd_sale(rs.getFloat("cfdSale"));
                s.setPrice(rs.getFloat("price"));

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
    public Set<Entry<Integer, MarketStock>> entrySet() {
        Set<Integer> keys = new TreeSet<>(this.keySet());
        TreeMap<Integer, MarketStock> map = new TreeMap<>();
        keys.stream().forEach(e-> map.put(e,this.get(e)));
        return map.entrySet();
    }
}
