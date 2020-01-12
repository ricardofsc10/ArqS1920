package daos;

import servidor.Contrato;
import cliente.App;
import org.slf4j.Logger;


import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ContratoDAO implements Map<Integer, Contrato> {

    private Connection conn;
    Logger log = Logger.getLogger(App.class.getName());
    private static final String ID_CONTRATO = "idContrato";
    private static final String ID_ATIVO = "idAtivo";
    private static final String ID_UTILIZADOR = "idUtilizador";
    private static final String PRECO = "preco";
    private static final String TAKE_PROFIT = "takeprofit";
    private static final String STOP_LOSS = "stoploss";
    private static final String QUANTIDADE = "quantidade";
    private static final String COMPRA = "compra";
    private static final String ENCERRADO = "encerrado";


    @Override
    public synchronized int size() {
        int i = 0;
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Contrato");
            for (;rs.next();i++);

        }catch(SQLException e){
            throw new NullPointerException(e.getMessage());
        }finally {
            Connect.close(conn);
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
    public  synchronized Contrato get(Object key) {

        Contrato c = new Contrato();
        try{
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Contrato WHERE idContrato= ?");
            ps.setString(1,Integer.toString((Integer) key));
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                c.setId(rs.getInt(ID_CONTRATO));
                c.setIdAtivo(rs.getInt(ID_ATIVO));
                c.setIdUtilizador(rs.getInt(ID_UTILIZADOR));
                c.setPreco(rs.getInt(PRECO));
                c.setTakeProfit(rs.getLong(TAKE_PROFIT));
                c.setStopLoss(rs.getLong(STOP_LOSS));
                c.setQuantidade(rs.getInt(QUANTIDADE));
                int compra = rs.getInt(COMPRA);
                if(compra==0)
                    c.setCompra(false);
                else
                    c.setCompra(true);
                int encerrado = rs.getInt(ENCERRADO);
                if(encerrado==0)
                    c.setEncerrado(false);
                else
                    c.setEncerrado(true);

            }
            else c = null;
        }
        catch(SQLException e){
            log.info(e.getMessage());
        }
        finally{
            try{
                Connect.close(conn);
            }
            catch(Exception e){
                log.info(e.getMessage());
            }
        }
        return c;

    }

    @Override
    public synchronized Contrato put(Integer key, Contrato contrato) {
        try{
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM Contrato WHERE idContrato = ?");
            ps.setString(1,Integer.toString((Integer) key));
            ps.executeUpdate();

            ps = conn.prepareStatement(
                    "INSERT INTO Contrato (idContrato,idAtivo,idUtilizador,preco,takeprofit,stoploss,quantidade,compra,encerrado) " +
                            "VALUES (?,?,?,?,?,?,?,?,?)");
            ps.setString(1,Integer.toString(key));
            ps.setString(2, Integer.toString(contrato.getIdAtivo()));
            ps.setString(3, Integer.toString(contrato.getIdUtilizador()));
            ps.setString(4, Float.toString(contrato.getPreco()));
            ps.setString(5, Float.toString(contrato.getTakeProfit()));
            ps.setString(6, Float.toString(contrato.getStopLoss()));
            ps.setString(7, Integer.toString(contrato.getQuantidade()));
            if(contrato.isCompra())
                ps.setString(8, Integer.toString(1));
            else
                ps.setString(8, Integer.toString(0));
            if(contrato.isEncerrado())
                ps.setString(9, Integer.toString(1));
            else
                ps.setString(9, Integer.toString(0));

            ps.executeUpdate();
        }
        catch(SQLException e){
            log.info(e.getMessage());
        }
        finally{
            try{
                Connect.close(conn);

            }
            catch(Exception e){
                log.info(e.getMessage());
            }
        }
        return contrato;
    }

    @Override
    public Contrato remove(Object o) {
        return null;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Contrato> map) {
        throw new UnsupportedOperationException();

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<Integer> keySet() {
        return Collections.emptySet();
    }

    @Override
    public synchronized Collection<Contrato> values() {
        Collection<Contrato> col = new HashSet<>();
        try {
            conn = Connect.connect();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT * FROM Contrato where encerrado = 0");
            while (rs.next()) {
                Contrato c = new Contrato();
                c.setId(rs.getInt(ID_CONTRATO));
                c.setIdAtivo(rs.getInt(ID_ATIVO));
                c.setIdUtilizador(rs.getInt(ID_UTILIZADOR));
                c.setPreco(rs.getInt(PRECO));
                c.setTakeProfit(rs.getLong(TAKE_PROFIT));
                c.setStopLoss(rs.getLong(STOP_LOSS));
                c.setQuantidade(rs.getInt(QUANTIDADE));
                int compra = rs.getInt(COMPRA);
                if(compra==0)
                    c.setCompra(false);
                else
                    c.setCompra(true);
                int encerrado = rs.getInt(ENCERRADO);
                if(encerrado==0)
                    c.setEncerrado(false);
                else
                    c.setEncerrado(true);
                col.add(c);
            }
        }
        catch(SQLException e){
            log.info(e.getMessage());
        }
        finally{
            try{
                Connect.close(conn);

            }
            catch(Exception e){
                log.info(e.getMessage());
            }
        }



        return col;
    }

    @Override
    public Set<Entry<Integer, Contrato>> entrySet() {
        return Collections.emptySet();
    }

    public  synchronized Contrato get(Object key, Object idUtilizador) {

        Contrato c = new Contrato();
        try{
            conn = Connect.connect();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM Contrato WHERE idContrato= ? AND idUtilizador= ?");
            ps.setString(1,Integer.toString((Integer) key));
            ps.setString(2,Integer.toString((Integer) idUtilizador));
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                c.setId(rs.getInt(ID_CONTRATO));
                c.setIdAtivo(rs.getInt(ID_ATIVO));
                c.setIdUtilizador(rs.getInt(ID_UTILIZADOR));
                c.setPreco(rs.getInt(PRECO));
                c.setTakeProfit(rs.getLong(TAKE_PROFIT));
                c.setStopLoss(rs.getLong(STOP_LOSS));
                c.setQuantidade(rs.getInt(QUANTIDADE));
                int compra = rs.getInt(COMPRA);
                if(compra==0)
                    c.setCompra(false);
                else
                    c.setCompra(true);
                int encerrado = rs.getInt(ENCERRADO);
                if(encerrado==0)
                    c.setEncerrado(false);
                else
                    c.setEncerrado(true);

            }
            else c = null;
        }
        catch(SQLException e){
            log.info(e.getMessage());
        }
        finally{
            try{
                Connect.close(conn);
            }
            catch(Exception e){
                log.info(e.getMessage());
            }
        }
        return c;

    }

}
