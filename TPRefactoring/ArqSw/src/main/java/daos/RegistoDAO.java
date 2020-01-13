package daos;

import servidor.Registo;

import java.util.*;
import java.util.logging.Logger;
import java.sql.*;

public class RegistoDAO implements Map<Integer, Registo> {
    private Connection conn;
    private Statement stm;
    private ResultSet rs;
    private PreparedStatement ps;
    Logger log = Logger.getLogger(RegistoDAO.class.getName());
    @Override
    public synchronized int size() {
        int i = 0;
        try {
            conn = Connect.connect();
            stm = conn.createStatement();
            rs = stm.executeQuery("SELECT * FROM Registo");
            for (;rs.next();i++);

        }catch(SQLException e){
            throw new NullPointerException(e.getMessage());
        }finally {
            try {
                Connect.close(conn);
                stm.close();
                rs.close();
            } catch (SQLException e) {
                log.info("SQLException");
            }

        }
        return i;

    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object o) {
        return false;
    }

    @Override
    public boolean containsValue(Object o) {
        return false;
    }

    @Override
    public Registo get(Object o) {
        return null;
    }

    @Override
    public synchronized Registo put(Integer key, Registo registo) {
        try{
            conn = Connect.connect();
            ps = conn.prepareStatement("DELETE FROM Registo WHERE idRegisto = ?");
            ps.setString(1,Integer.toString((Integer) key));
            ps.executeUpdate();

            ps = conn.prepareStatement("INSERT INTO Registo (idRegisto,idUtilizador,idAtivo,lucro,quantidade) VALUES (?,?,?,?,?)");
            ps.setString(1,Integer.toString(key));
            ps.setString(2,Integer.toString(registo.getIdUtilizador()));
            ps.setString(3,Integer.toString(registo.getIdAtivo()));
            ps.setString(4,Float.toString(registo.getLucro()));
            ps.setString(5, Integer.toString(registo.getQuantidade()));
            ps.executeUpdate();
        }
        catch(SQLException e){
            log.info(e.getMessage());
        }
        finally{
            try{
                Connect.close(conn);
                ps.close();

            }
            catch(Exception e){
                log.info(e.getMessage());
            }
        }
        return registo;
    }


    @Override
    public Registo remove(Object o) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Registo> map) {
        /**
         * este método está vazio porque não é necessário, embora seja obrigatório ele estar presente
         * nesta classe devido ao "implements Map<....>"
         */
    }

    @Override
    public void clear() {
        /**
         * este método está vazio porque não é necessário, embora seja obrigatório ele estar presente
         * nesta classe devido ao "implements Map<....>"
         */
    }

    @Override
    public Set<Integer> keySet() {
        return Collections.emptySet();
    }

    @Override
    public synchronized Collection<Registo> values() {
        Collection<Registo> col = new HashSet<>();
        try {
            conn = Connect.connect();
            stm = conn.createStatement();
            rs = stm.executeQuery("SELECT * FROM Registo");
            while (rs.next()) {
                col.add(
                        new Registo(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getFloat(4),rs.getInt(5))
                );
            }

        }catch(SQLException e){
            throw new NullPointerException(e.getMessage());
        }finally {
            try {
                Connect.close(conn);
                stm.close();
                rs.close();
            } catch (SQLException e) {
                log.info("SQLException");
            }
        }



        return col;
    }

    @Override
    public Set<Entry<Integer, Registo>> entrySet() {
        return Collections.emptySet();
    }
}
