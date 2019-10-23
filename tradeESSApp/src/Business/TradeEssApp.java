package Business;


import Data.*;
import Exception.*;
import GUI.MainMenu;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Lock;

public class TradeEssApp {

    private User user;
    private UserDAO userDAO;
    private StockDAO stockDAO;
    private PositionDAO positionDAO;

    private Lock positionLock;


    public TradeEssApp() {
        this.user = null;
        this.userDAO = new UserDAO();
        this.stockDAO = new StockDAO();
        this.positionDAO = new PositionDAO();
    }

    /**
     *
     * @param email
     * @param username
     * @param pass
     * @param morada
     * @param age
     * @param contacto
     * @param saldoConta
     * @throws TraderRegistadoException
     */
    public void registerTrader(String email, String username, String pass, String morada, int age, int contacto, float saldoConta) throws TraderRegistadoException {
        int id = userDAO.size()+1;

        System.out.println(id);
        Trader trader = new Trader(id, email, username, pass, morada, age, contacto, 0);
        if((userDAO.get(idUserGivenUsername(username))) == null){
            userDAO.put(id, trader);
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
    public int iniciarSessao(String username, String password) throws Exception {

        //verificar se é admin
        if(username.equals("admin") && password.equals("admin")){
            MainMenu.adminLoggedIn();
            return 0;
        }


        if(this.userDAO.containsKey(idUserGivenUsername(username))){
            user = this.userDAO.get(idUserGivenUsername(username));
            if(!user.getPassword().equals(password)) throw new PasswordIncorretaException("Password incorreta!");
        }
        else throw new UtilizadorInexistenteException("Utilizador inexistente!");

        System.out.println("Sessão iniciada com sucesso.");
        MainMenu.traderLoggedIn();
        return 0;
    }


    //return idUser, dado o username do user

    /**
     *
     * @param username
     * @return idUser
     */
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
    public void terminarSessao(User user){
        System.out.println("Sessão terminada com sucesso! Até já, " +user);
    }

    /**
     *
     * @return User
     */
    public User getUser(){
        return user;
    }

    /**
     * Check saldo do utilizador
     * @param u
     * @return float
     */
    public float checkSaldo(User u){
        return ((Trader) u).getSaldoConta();
    }

    /**
     * Lista dos ativos que o user tem para vender
     * @return Lisa de ativos
     */
    public List<Stock> listStocksSale(){
        List<Stock> stocks = new ArrayList<>();
        for(Position p : positionDAO.values()){
            if((p.getIdUser() == (user.getId())) && (p.getStatus().equals("Em espera") && (p.getType().equals("Para compra"))))
                stocks.add(stockNameGivenId(p.getIdStock()));
        }
        return stocks;
    }

    //dado um idStock, retorna o stock

    /**
     * Vê qual o stock que tem aquele um dado id
     * @param idStock
     * @return
     */
    private Stock stockNameGivenId(int idStock) {
        Stock stock = null;
        for(Stock s : stockDAO.values()){
            if(stock.getIdStock() == idStock)
                stock = s;
        }
        return stock;
    }

    /**
     * Lista ativos de compra
     * @return stocks
     */
    public List<Stock> listaStocks() {
        List<Stock> stocks = new ArrayList<>();
        stocks.addAll(stockDAO.values());
        return stocks;
    }

    /**
     * checkar se o user tem poder de compra, ou seja, se tem dinheiro para fazer compras
     * @param stockname
     * @param value
     * @param accountBalance
     * @return
     */
    public boolean isAbleToBuy(String stockname, int value, float accountBalance){
        return (accountBalance > value * stockDAO.get(idStockGivenName(stockname)).getCfdBuy());
    }

    /**
     * checkar se existe profit pra comprar no "mercado"
     * @param stockName
     * @param stop_loss
     * @param take_profit
     * @return
     */
    public boolean profitBuyAvailable(String stockName, float stop_loss, float take_profit){
        return ((stockDAO.get(idStockGivenName(stockName)).getCfdBuy() >= take_profit
                || (stockDAO.get(idStockGivenName(stockName)).getCfdBuy()) <= stop_loss));
    }

    /**
     * checkar se existe profit pra venda no "mercado"
     * @param stockName
     * @param stop_loss
     * @param take_profit
     * @return
     */
    public boolean profitSaleAvailable(String stockName, float stop_loss, float take_profit){
        return ((stockDAO.get(idStockGivenName(stockName)).getCfdSale() >= take_profit
                || (stockDAO.get(idStockGivenName(stockName)).getCfdSale()) <= stop_loss));
    }

    /**
     * Cria posição de compra
     * @param stockName
     * @param valor
     * @param stop_loss
     * @param take_profit
     * @param fecharNegocio
     */
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

    /**
     * adicionar dinehiro na conta (quando o user adicionar ou quando fizer uma venda)
     * @param valor
     */
    public void addMoney(float valor){
        User u = new Trader();
        System.out.println(u.getUsername() + " tem: " + ((Trader) u).getSaldoConta());
        ((Trader) u).addMoney(valor);
        System.out.println("O dinheiro foi adicionado na sua conta com sucesso!");
        System.out.println(u.getUsername() + " tem agora: " + ((Trader) u).getSaldoConta());
        userDAO.put(u.getId(), u);
    }

    /**
     * Cria posição de venda
     * @param stockName
     * @param valor
     * @param stop_loss
     * @param take_profit
     * @param fecharNegocio
     */
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

    /**
     * Retira dinehiro da conta (quando o user retirar ou quando fizer uma compra)
     * @param valor
     */
    public void removeMoney(float valor){
        User u = new Trader();
        System.out.println(u.getUsername() + " tem: " + ((Trader) u).getSaldoConta());
        ((Trader) u).removeMoney(valor);
        System.out.println("Foi retirado dinheiro da sua conta! :(");
        System.out.println(u.getUsername() + " tem agora: " + ((Trader) u).getSaldoConta());
        userDAO.put(u.getId(), u);
    }

    //return idStock, dado o nome do stock

    /**
     * Dá o id do stock com um dado nome
     * @param stockName
     * @return idStock
     */
    public int idStockGivenName(String stockName){
        int id = -1;
        for(Stock stock:stockDAO.values()){
            if(stock.getName().equals(stockName))
                id = stock.getIdStock();
        }
        return id;
    }

    //quantidade total para venda

    /**
     *
     * @param stockName
     * @return
     */
    public int amount(String stockName) {
        int a = 0;
        for(Position p : positionDAO.values()){
            if((p.getIdUser() == user.getId()) && (p.getIdStock() == idStockGivenName(stockName) && p.getType().equals("Para compra") && p.getStatus().equals("Negócio Fechado")))
                a = p.getAmount();
        }
        return a;
    }

    /**
     * Check Portefólio
     * @return lista de positions
     */
    public List<Position> checkPortfolio(){
        List<Position> positions = new ArrayList<>();
        for(Position p : positionDAO.values()){
            if(p.getIdUser()==(user.getId()) && p.getStatus().equals("Em espera"))
                positions.add(p);
        }
        return positions;
    }

    /**
     * Apagar conta de um utilizador
     * @throws UtilizadorInexistenteException
     */
    public void deleteAccount(User atual) throws UtilizadorInexistenteException{
        if(this.userDAO.containsKey(atual.getId())){
            this.userDAO.remove(idUserGivenUsername(atual.getUsername()));
            terminarSessao(atual);
        }
        else throw new UtilizadorInexistenteException("Ação abortada! Este utilizador não existe...");
    }

    /**
     * Remover stock
     * @param stockName
     * @throws NoStockAvailableException
     */
    public void removeStock(String stockName) throws NoStockAvailableException{
        if(stockAvailable(stockName)){
            this.stockDAO.remove(idStockGivenName(stockName));
        }
        else
            throw new NoStockAvailableException();

    }

    /**
     * verificar se stock  existe
     * @param stockName
     * @return
     */
    public boolean stockAvailable(String stockName){
        for(Stock s : stockDAO.values()){
            if(s.getName().equals(stockName))
                return true;
        }
        return false;
    }

    /**
     * Lista os utilizadores existentes
     * @return lista de utilizadores
     */
    public Collection<User> getUsers(){
        return userDAO.values();
    }

    /**
     * Criar Stock
     * @param stockName
     */
    public void createStock(String stockName) {
        int size = stockDAO.size() +1;

        BigDecimal value;
        float fvalue;
        Stock stock = new Stock();
        yahoofinance.Stock s = null;
        try{
            s = YahooFinance.get(stockName);
            if(!this.stockAvailable(stockName) && s!= null){
                stock.setIdStock(size);
                value = s.getQuote().getBid();
                fvalue = (value == null) ? 0 : value.floatValue();
                stock.setCfdSale(fvalue);
                value = s.getQuote().getAsk();
                fvalue = (value == null) ? 0 : value.floatValue();
                stock.setCfdBuy(fvalue);
                this.stockDAO.put(size, stock);
            }
        }
        catch (IOException e) {
        e.printStackTrace();
        }
    }

    public Set<Position> listarPositionSale(int id) {
        Set<Position> res = new HashSet<>();
        positionLock.lock();
        try {
            for (Position p : positionDAO.values())
                if (p.getIdPosition() == id && p.getType().equalsIgnoreCase("Para venda")) res.add(p);
        }
        finally {
            positionLock.unlock();
        }
        return res;
    }

    public Set<Position> listarPositionBuy(int id) {
        Set<Position> res = new HashSet<>();
        positionLock.lock();
        try {
            for (Position p : positionDAO.values())
                if (p.getIdPosition() == id && p.getType().equalsIgnoreCase("Para compra")) res.add(p);
        }
        finally {
            positionLock.unlock();
        }
        return res;
    }

    public StockDAO getStocks(){
        return stockDAO;
    }

    public void registarStock(Stock s) throws StockExistenteException {
        int id = stockDAO.size()+1;
        if(!stockDAO.containsValue(stockNameGivenId(id)))
            stockDAO.put(id, s);
        else throw new StockExistenteException("Este stock já existe...");
    }
}
