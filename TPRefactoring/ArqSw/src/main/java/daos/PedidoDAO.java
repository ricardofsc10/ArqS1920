package daos;

import servidor.Pedido;

import java.util.Collection;
import java.util.Collections;
import java.util.logging.Logger;

import java.sql.*;
import java.util.Map;
import java.util.Set;

public class PedidoDAO implements Map<Integer, Pedido> {

    private Connection conn;
    private ResultSet rs;
    private Statement stm;
    PreparedStatement ps;
    Logger log = Logger.getLogger(PedidoDAO.class.getName());

    @Override
    public synchronized int size() {
        int i = 0;
        try {
            conn = Connect.connect();
            stm = conn.createStatement();
            rs = stm.executeQuery("SELECT * FROM Pedido");
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
    public Pedido get(Object o) {
        return null;
    }

    @Override
    public synchronized Pedido put(Integer key, Pedido pedido) {
        try{
            conn =  Connect.connect();
            ps = conn.prepareStatement("DELETE FROM Pedido WHERE idPedido = ?");
            ps.setString(1,Integer.toString((Integer) key));
            ps.executeUpdate();

            ps = conn.prepareStatement("INSERT INTO Pedido (idPedido,idUtilizador,estado,descricao) VALUES (?,?,?,?)");
            ps.setString(1,Integer.toString(key));
            ps.setString(2, Integer.toString(pedido.getEstado().getIdUtilizador()));
            boolean estado = pedido.getEstado().getEstado();
            if(estado)
                ps.setString(3, Integer.toString(1));
            else
                ps.setString(3, Integer.toString(0));
            ps.setString(4, pedido.getEstado().getPedido());
            ps.executeUpdate();
        }
        catch(SQLException | NullPointerException e){
            log.info(e.getMessage());
        }
        finally{
            Connect.close(conn);
        }
        return pedido;
    }


    @Override
    public Pedido remove(Object o) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Pedido> map) {
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
    public Collection<Pedido> values() {
        return Collections.emptySet();
    }

    @Override
    public Set<Entry<Integer, Pedido>> entrySet() {
        return Collections.emptySet();
    }
}