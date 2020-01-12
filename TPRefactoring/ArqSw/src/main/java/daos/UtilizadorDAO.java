package daos;

import servidor.Estado;
import servidor.Pedido;
import servidor.Utilizador;
import cliente.App;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class UtilizadorDAO implements Map<Integer, Utilizador> {

    private Connection conn;
    Logger log = Logger.getLogger(App.class.getName());


    @Override
    public synchronized int size() {
        int i = 0;
        Statement stm = null;
        ResultSet rs = null;
        try {
            conn = Connect.connect();
            stm = conn.createStatement();
            rs = stm.executeQuery("SELECT * FROM Utilizador");
            for (; rs.next(); i++) ;

        } catch (SQLException e) {
            throw new NullPointerException(e.getMessage());
        } finally {
            try {
                stm.close();
                rs.close();
                Connect.close(conn);
            } catch (SQLException e) {
                log.info(e.getMessage());
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
    public synchronized Utilizador get(Object key) {

        Utilizador u = new Utilizador();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = Connect.connect();
            ps = conn.prepareStatement("SELECT * FROM Utilizador WHERE idUtilizador= ?");
            ps.setString(1, Integer.toString((Integer) key));
            rs = ps.executeQuery();
            if (rs.next()) {
                u.setId((Integer)key);
                u.setUsername(rs.getString(2));
                u.setPassword(rs.getString(3));
                u.setPlafom(rs.getLong("plafom"));
            }
            else u = null;
        } catch (SQLException e) {
            log.info(e.getMessage());
        } finally {
            try {
                ps.close();
                rs.close();
                Connect.close(conn);
            } catch (SQLException e) {
                log.info(e.getMessage());
            }
        }
        return u;

    }

    @Override
    public synchronized   Utilizador put(Integer key, Utilizador utilizador) {
        PreparedStatement ps = null;
        try {
            conn = Connect.connect();
            ps = conn.prepareStatement("DELETE FROM Utilizador WHERE idUtilizador = ?");
            ps.setString(1,Integer.toString((Integer) key));
            ps.executeUpdate();
            ps = conn.prepareStatement("INSERT INTO Utilizador (IdUtilizador,username,password,plafom) VALUES (?,?,?,?)");
            ps.setString(1, Integer.toString(key));
            ps.setString(2, utilizador.getUsername());
            ps.setString(3, utilizador.getPassword());
            ps.setString(4, Float.toString(utilizador.getPlafom()));
            ps.executeUpdate();
        } catch (SQLException e) {
            log.info(e.getMessage());
        } finally {
            try {
                ps.close();
                Connect.close(conn);

            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
        return utilizador;

    }

    @Override
    public Utilizador remove(Object o) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Utilizador> map) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<Integer> keySet() {
        return Collections.emptySet();
    }

    @Override
    public synchronized Collection<Utilizador> values() {
        Collection<Utilizador> col = new HashSet<Utilizador>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = Connect.connect();
            stm = conn.prepareStatement("SELECT * FROM Utilizador");
             rs = stm.executeQuery();
            while (rs.next()) {
                Utilizador u = new Utilizador(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getLong(4));
                List<Pedido.Memento> mementos = mementosUtilizador(rs.getInt(1));
              u.setPedidosSave(mementos);

                col.add(u);
            }

        } catch (SQLException e) {
            log.info("nao fez a query");
            throw new NullPointerException("nao fez a query");
        } finally {
            try {
                stm.close();
                rs.close();
                Connect.close(conn);
            } catch (SQLException e) {
                log.info(e.getMessage());
            }

        }


        return col;
    }

    @Override
    public Set<Entry<Integer, Utilizador>> entrySet() {
        return Collections.emptySet();
    }


    public  List<Pedido.Memento> mementosUtilizador(int id){
        List<Pedido.Memento> mementos = new ArrayList<>();
        PreparedStatement ps =null;
        ResultSet rs = null;
        try {
            conn = Connect.connect();

            ps = conn.prepareStatement("SELECT * FROM Pedido WHERE idUtilizador= ? AND estado= 0" );
            ps.setString(1, Integer.toString( id));
            rs = ps.executeQuery();
            Pedido p = new Pedido();
            while (rs.next())  {
                String descricao = rs.getString("descricao");
                int estado = rs.getInt("estado");
                int idPedido = rs.getInt("idPedido");
                int idUtilizador = rs.getInt("idUtilizador");
                Estado e = null;
                if (estado == 0)
                    e  =  new Estado(descricao,false,idPedido,idUtilizador);

                else
                    e  =  new Estado(descricao,true,idPedido,idUtilizador);
                p.set(e);
                mementos.add(p.saveToMemento());

            }
        } catch (SQLException e) {
            log.info(e.getMessage());
        } finally {
            try {
                ps.close();
                rs.close();
                Connect.close(conn);
            } catch (SQLException e) {
                log.info(e.getMessage());
            }
        }
        return mementos;
    }
 /******************************* NOVO REQUISITO ****************/
    public void putNotificacao(Integer key,String notificacao){
        PreparedStatement ps = null;
        try {
            conn = Connect.connect();
            ps = conn.prepareStatement("INSERT INTO Notificacao (idUtilizador,descricao,enviado) VALUES (?,?,?)");
            ps.setString(1, Integer.toString(key));
            ps.setString(2, notificacao);
            ps.setString(3, Integer.toString(0));
            ps.executeUpdate();
        } catch (SQLException e) {
            log.info(e.getMessage());
        } finally {
            try {
                ps.close();
                Connect.close(conn);

            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }

    }

    public synchronized List<String> getNotificacoes(int id) {
        List<String> notificacoes =  new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            conn = Connect.connect();
            stm = conn.prepareStatement("SELECT * FROM Notificacao where idUtilizador = ? AND enviado = 0");
            stm.setString(1, Integer.toString((Integer)id));
            rs = stm.executeQuery();
            while (rs.next()) {
                notificacoes.add(rs.getString(3));
            }
            stm = conn.prepareStatement("DELETE FROM Notificacao WHERE idUtilizador = ?");
            stm.setString(1,Integer.toString((Integer) id));
            stm.executeUpdate();



        } catch (SQLException e) {
            log.info("nao fez a query");
            throw new NullPointerException("nao fez a query");
        } finally {
            try {
                stm.close();
                rs.close();
                Connect.close(conn);
            } catch (SQLException e) {
                log.info(e.getMessage());
            }

        }

        return notificacoes;
    }

}

