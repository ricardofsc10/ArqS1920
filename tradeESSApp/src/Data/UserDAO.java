package Data;

import Business.Trader;
import Business.User;

import Data.Connect;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class UserDAO implements Map<Integer, User> {

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


public void addUser( String email,String username, String password, String morada,int idade, int contacto) throws SQLException, Exception{
        Connection c = Connect.connect();
        if(c!=null) { 
            PreparedStatement ps = c.prepareStatement("INSERT INTO user VALUES (?,?,?,?,?,?,?);");
            ps.setInt(1, size()+1);
            ps.setString(2, email);
            ps.setString(3, username);
            ps.setString(4, password);
            ps.setString(5, morada);
            ps.setInt(6, idade);
            ps.setInt(7,contacto);
            ps.executeUpdate();         
            Connect.close(c);
            }
        else{
            throw new Exception("Unable to establish connection");
        }
    }
    
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
