package Data;

import Business.Trader;
import Business.User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class UserDAO implements Map<Integer, User> {


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
    public User get(Object key) {
        return null;
    }

    @Override
    public User put(Integer key, User value) {
        return null;
    }

    @Override
    public User remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends User> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<Integer> keySet() {
        return null;
    }

    @Override
    public Collection<User> values() {
        return null;
    }

    @Override
    public Set<Entry<Integer, User>> entrySet() {
        return null;
    }
}
