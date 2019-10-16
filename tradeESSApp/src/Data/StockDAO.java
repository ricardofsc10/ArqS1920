package Data;

import Business.Stock;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class StockDAO implements Map<Integer, Stock> {
    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
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
