package Business;


import Data.*;
import Exception.*;

import java.util.*;

public class TradeEssApp {

    private User user;
    private UserDAO userDAO;
    private StockDAO stockDAO;
    private PositionDAO positionDAO;

    public TradeEssApp() {
        this.user = null;
        this.userDAO = new UserDAO();
        this.stockDAO = new StockDAO();
        this.positionDAO = new PositionDAO();
    }

    public void registarTrader(String email, String username, String pass, String morada, int age, int contacto, float saldoConta) throws TraderRegistadoException {
        int id = userDAO.size()+1;
        if(!(user instanceof Admin)){
            user = new Trader(id, email, username, pass, morada, age, contacto, saldoConta);
        }

        if(userDAO.get(idUserGivenUsername(username)).getUsername() == null){
            userDAO.put(id, user);
        }
        else throw new TraderRegistadoException("Este negociante já se encontra registado com o seguinte e-mail: " +email);
    }

    /**
     * Método que tenta autentica um utilizador no sistema.
     * Lança uma exceção caso a o utilizador não exista no sistema, ou caso a password esteja errada.
     * @param username
     * @param password
     * @throws PasswordIncorretaException
     * @throws UtilizadorInexistenteException
     */
    public void iniciarSessao(String username, String password) throws PasswordIncorretaException, UtilizadorInexistenteException {

        if(this.userDAO.containsKey(idUserGivenUsername(username))){
            user = this.userDAO.get(idUserGivenUsername(username));
            if(!user.getPassword().equals(password)) throw new PasswordIncorretaException("Password incorreta!");
        }
        else throw new UtilizadorInexistenteException("Utilizador inexistente!");

        System.out.println("-------- BEM-VINDO! -------");
    }

    //return idUser, dado o username do user
    public int idUserGivenUsername(String username){
        int id = -1;
        for(User user : userDAO.values()){
            if(user.getUsername().equals(username)){
                id = user.getId();
            }
        }
        return id;
    }

    /**
     * Método que termina a sessão do utilizador atual.
     */
    public void terminarSessao(){
        user = null;
        System.out.println("Sessão terminada com sucesso!");
    }

    public User getUser(){
        return user;
    }

    public float checkSaldo(){
        User trader = new Trader();
        return ((Trader) trader).getSaldoConta();
    }

    //lista dos ativos que o user tem para vender
    public List<Stock> listStocksSale(){
        List<Stock> stocks = new ArrayList<>();
        for(Position p : positionDAO.values()){
            if((p.getIdUser() == (user.getId())) && (p.getStatus().equals("Em espera") && (p.getType().equals("Para compra"))))
                stocks.add(stockNameGivenId(p.getIdStock()));
        }
        return stocks;
    }

    //dado um idStock, retorna o stock
    private Stock stockNameGivenId(int idStock) {
        Stock stock = null;
        for(Stock s : stockDAO.values()){
            if(stock.getIdStock() == idStock)
                stock = s;
        }
        return stock;
    }

    //lista ativos para compra
    public List<Stock> listaStocks() {
        List<Stock> stocks = new ArrayList<>();
        stocks.addAll(stockDAO.values());
        return stocks;
    }

    //checkar se o user tem poder de compra, ou seja, se tem dinheiro para fazer compras
    public boolean isAbleToBuy(String stockname, int value, float accountBalance){
        return (accountBalance > value * stockDAO.get(idStockGivenName(stockname)).getCfdBuy());
    }

    //checkar se existe profit pra comprar no "mercado"
    public boolean profitBuyAvailable(String stockName, float stop_loss, float take_profit){
        return ((stockDAO.get(idStockGivenName(stockName)).getCfdBuy() >= take_profit
                || (stockDAO.get(idStockGivenName(stockName)).getCfdBuy()) <= stop_loss));
    }

    //checkar se existe profit pra venda no "mercado"
    public boolean profitSaleAvailable(String stockName, float stop_loss, float take_profit){
        return ((stockDAO.get(idStockGivenName(stockName)).getCfdSale() >= take_profit
                || (stockDAO.get(idStockGivenName(stockName)).getCfdSale()) <= stop_loss));
    }

    public void buyPosition(String stockName, int valor, float stop_loss, float take_profit, String fecharNegocio){
        int size = positionDAO.size()+1;
        float value;

        Position position = new Position();
        position.setIdPosition(size);
        position.setStatus("Para compra");
        position.setIdUser(user.getId());
        position.setIdStock(idStockGivenName(stockName));
        position.setAmount(valor);
        position.setStop_loss(stop_loss);
        position.setTake_profit(take_profit);

        if(fecharNegocio.equalsIgnoreCase("yes") || fecharNegocio.equalsIgnoreCase("y") || fecharNegocio.equalsIgnoreCase("sim")){
            position.setStatus("Negócio fechado");
            value = valor * (stockDAO.get(idStockGivenName(stockName)).getCfdBuy());
            position.setDealValue(value);
            positionDAO.put(position.getIdPosition(), position);
            removeMoney(value);
        }
        else {
            position.setStatus("Em espera");
            position.setDealValue(0);
        }
    }

    //adicionar money na conta (quando o user adicionar ou quando fizer uma venda)
    public void addMoney(float valor){
        User u = new Trader();
        System.out.println(u.getUsername() + " tem: " + ((Trader) u).getSaldoConta());
        ((Trader) u).addMoney(valor);
        System.out.println("O dinheiro foi adicionado na sua conta com sucesso!");
        System.out.println(u.getUsername() + " tem agora: " + ((Trader) u).getSaldoConta());
        userDAO.put(u.getId(), u);
    }

    public void sellPosition(String stockName, int valor, float stop_loss, float take_profit, String fecharNegocio){
        int size = positionDAO.size()+1;
        float value;

        Position salePosition = new Position();
        salePosition.setIdPosition(size);
        salePosition.setStatus("Para venda");
        salePosition.setIdUser(user.getId());
        salePosition.setIdStock(idStockGivenName(stockName));
        salePosition.setAmount(valor);
        salePosition.setStop_loss(stop_loss);
        salePosition.setTake_profit(take_profit);

        if(fecharNegocio.equalsIgnoreCase("yes") || fecharNegocio.equalsIgnoreCase("y")){
            salePosition.setStatus("Negócio fechado");
            value = valor * (stockDAO.get(idStockGivenName(stockName)).getCfdSale());
            salePosition.setDealValue(value);
            positionDAO.put(salePosition.getIdPosition(), salePosition);
            addMoney(value);
        }
        else {
            salePosition.setStatus("Em espera");
            salePosition.setDealValue(0);
        }
    }

    //retirar money da conta (quando o user retirar ou quando fizer uma compra)
    public void removeMoney(float valor){
        User u = new Trader();
        System.out.println(u.getUsername() + " tem: " + ((Trader) u).getSaldoConta());
        ((Trader) u).removeMoney(valor);
        System.out.println("Foi retirado dinheiro da sua conta! :(");
        System.out.println(u.getUsername() + " tem agora: " + ((Trader) u).getSaldoConta());
        userDAO.put(u.getId(), u);
    }

    //return idStock, dado o nome do stock
    public int idStockGivenName(String stockName){
        int id = -1;
        for(Stock stock:stockDAO.values()){
            if(stock.getName().equals(stockName))
                id = stock.getIdStock();
        }
        return id;
    }

    //quantidade total para venda
    public int amount(String stockName) {
        int a = 0;
        for(Position p : positionDAO.values()){
            if((p.getIdUser() == user.getId()) && (p.getIdStock() == idStockGivenName(stockName) && p.getType().equals("Para compra") && p.getStatus().equals("Negócio Fechado")))
                a = p.getAmount();
        }
        return a;
    }

    //abrir portefólio
    public List<Position> checkPortfolio(){
        List<Position> positions = new ArrayList<>();
        for(Position p : positionDAO.values()){
            if(p.getIdUser()==(user.getId()) && p.getStatus().equals("Em espera"))
                positions.add(p);
        }
        return positions;
    }

    //apagar conta de um user
    public void deleteAccount() throws UtilizadorInexistenteException{
        if(this.userDAO.containsKey(user.getId())){
            this.userDAO.remove(idUserGivenUsername(user.getUsername()));
        }
        else throw new UtilizadorInexistenteException("Ação abortada! Este utilizador não existe...");
    }

    //remover stock
    public void removeStock(String stockName) throws NoStockAvailableException{
        if(stockAvailable(stockName)){
            this.stockDAO.remove(idStockGivenName(stockName));
        }
        else
            throw new NoStockAvailableException();

    }

    //verificar se existe stock de cenas
    public boolean stockAvailable(String stockName){
        for(Stock s : stockDAO.values()){
            if(s.getName().equals(stockName))
                return true;
        }
        return false;
    }
}
