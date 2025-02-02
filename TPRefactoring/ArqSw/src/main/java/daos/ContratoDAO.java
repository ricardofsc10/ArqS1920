package daos;

import servidor.AuxContrato;
import servidor.Contrato;

import java.util.*;
import java.util.logging.Logger;


import java.sql.*;

public class ContratoDAO implements Map<Integer, Contrato> {

    private Connection conn;
    Logger log = Logger.getLogger(ContratoDAO.class.getName());
    private static final String ID_CONTRATO = "idContrato";
    private static final String ID_ATIVO = "idAtivo";
    private static final String ID_UTILIZADOR = "idUtilizador";
    private static final String PRECO = "preco";
    private static final String TAKE_PROFIT = "takeprofit";
    private static final String STOP_LOSS = "stoploss";
    private static final String QUANTIDADE = "quantidade";
    private static final String COMPRA = "compra";
    private static final String ENCERRADO = "encerrado";
    Statement stm;
    ResultSet rs;
    PreparedStatement ps;
    boolean comprar;
    boolean fechar;


    @Override
    public synchronized int size() {
        int i = 0;
        try {
            conn = Connect.connect();
            stm = conn.createStatement();
            rs = stm.executeQuery("SELECT * FROM Contrato");
            for (;rs.next();i++);

        }catch(SQLException e){
            throw new NullPointerException(e.getMessage());
        }finally {
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
    public  synchronized Contrato get(Object key) {

        Contrato c = new Contrato();
        try{
            conn = Connect.connect();
            ps = conn.prepareStatement("SELECT * FROM Contrato WHERE idContrato= ?");
            ps.setString(1,Integer.toString((Integer) key));
            rs = ps.executeQuery();
            if(rs.next()){
                c.setId(rs.getInt(ID_CONTRATO));
                c.setIdAtivo(rs.getInt(ID_ATIVO));
                c.setIdUtilizador(rs.getInt(ID_UTILIZADOR));
                c.setPreco(rs.getInt(PRECO));
                c.setTakeProfit(rs.getLong(TAKE_PROFIT));
                c.setStopLoss(rs.getLong(STOP_LOSS));
                c.setQuantidade(rs.getInt(QUANTIDADE));
                int compra = rs.getInt(COMPRA);
                comprar = AuxContrato.contratoAux(compra);
                c.setCompra(comprar);

                int encerrado = rs.getInt(ENCERRADO);
                fechar = AuxContrato.contratoAux(encerrado);
                c.setCompra(fechar);
            }
            else c = null;
        }
        catch(SQLException e){
            log.info(e.getMessage());
        }
        finally{
            try{
                ps.close();
                rs.close();
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
            ps = conn.prepareStatement("DELETE FROM Contrato WHERE idContrato = ?");
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
    public synchronized Collection<Contrato> values() {
        Collection<Contrato> col = new HashSet<>();
        try {
            conn = Connect.connect();
            stm = conn.createStatement();
            rs = stm.executeQuery("SELECT * FROM Contrato where encerrado = 0");
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
                comprar = AuxContrato.contratoAux(compra);
                c.setCompra(comprar);

                int encerrado = rs.getInt(ENCERRADO);
                fechar = AuxContrato.contratoAux(encerrado);
                c.setCompra(fechar);
                col.add(c);
            }
        }
        catch(SQLException e){
            log.info(e.getMessage());
        }
        finally{
            try{
                stm.close();
                rs.close();
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
            ps = conn.prepareStatement("SELECT * FROM Contrato WHERE idContrato= ? AND idUtilizador= ?");
            ps.setString(1,Integer.toString((Integer) key));
            ps.setString(2,Integer.toString((Integer) idUtilizador));
            rs = ps.executeQuery();
            if(rs.next()){
                c.setId(rs.getInt(ID_CONTRATO));
                c.setIdAtivo(rs.getInt(ID_ATIVO));
                c.setIdUtilizador(rs.getInt(ID_UTILIZADOR));
                c.setPreco(rs.getInt(PRECO));
                c.setTakeProfit(rs.getLong(TAKE_PROFIT));
                c.setStopLoss(rs.getLong(STOP_LOSS));
                c.setQuantidade(rs.getInt(QUANTIDADE));
                int compra = rs.getInt(COMPRA);
                comprar = AuxContrato.contratoAux(compra);
                c.setCompra(comprar);

                int encerrado = rs.getInt(ENCERRADO);
                fechar = AuxContrato.contratoAux(encerrado);
                c.setCompra(fechar);

            }
            else c = null;
        }
        catch(SQLException e){
            log.info(e.getMessage());
        }
        finally{
            try{
                rs.close();
                ps.close();
                Connect.close(conn);
            }
            catch(Exception e){
                log.info(e.getMessage());
            }
        }
        return c;

    }

}
