package Data;

import Business.Stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

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

        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Stock get(Object key) {
        return null;
    }

    @Override
    public Stock put(Integer key, Stock value) {
        return null;
    }

    @Override
    public Stock remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Stock> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<Stock> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, Stock>> entrySet() {
        return null;
    }
}
