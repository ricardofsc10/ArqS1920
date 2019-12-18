package Business;

import Data.*;
import Exceptions.PasswordInvalidException;
import Exceptions.StockNotExistsException;
import Exceptions.UserExistsException;
import Exceptions.UsernameInvalidException;
import GUI.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class App {
    //------------------------------------ INSTANCE VARIABLES -----------------------------------------------
    private User user;
    private UserDAO userDAO;
    public MarketStockDAO stockDAO; //assets -> ativos
    private PositionDAO positionDAO; //cfds -> contratos
    public NotificationDAO notificationDAO; //notificações por causa do observer pattern
    //--------------------------------------------------------------------------------------------------------

    //------------------------------------ CONSTRUCTORS -----------------------------------------------------

    public App(){
        user = null;
        userDAO = new UserDAO();
        stockDAO = new MarketStockDAO();
        positionDAO = new PositionDAO();
        notificationDAO = new NotificationDAO();
    }
    //---------------------------------------------------------------------------------------------------------

    /**
     * Popular base de dados com os stocks que queremos
     */
    public void loadStocks() {
        System.out.println("\n> Starting to populate the Market:");
        createStock("INTC");
        createStock("NVDA");
        createStock("FB");
        createStock("NOK");
        createStock("MSFT");
        createStock("GC=F");
        createStock("GOOG");
        createStock("NFLX");
        createStock("KO");
        createStock("NKE");
        createStock("EA");
        createStock("AAPL");
        createStock("SI=F");
        createStock("TSLA");
        System.out.println("\n> Database populated! \n\n");
    }

    /**
     * Método responsável pelo início de sessão de um utilizador
     * @param username
     * @param password
     * @throws UsernameInvalidException
     * @throws PasswordInvalidException
     */
    public void startSession(String username, String password) throws UsernameInvalidException, PasswordInvalidException {
        try {
            this.user = this.checkUser(username, password);
            if (!(this.user == null))
                System.out.println("\n\n                                       * * * * * * * * * * * * * * * * * * * * *WELCOME: " + user.getName() + "!* * * * * * * * * * * * * * * * * * * * *\n");
        } catch (Exception e) {
            throw new UsernameInvalidException(e.getMessage());
        }
    }


    /**
     * Método auxiliar ao método de início de sessão
     * @param username
     * @param password
     * @return
     * @throws UsernameInvalidException
     * @throws PasswordInvalidException
     */
    private User checkUser(String username, String password) throws UsernameInvalidException, PasswordInvalidException {
        if (this.userDAO.containsKey(idGivenUsername(username))) {
            user = this.userDAO.get(idGivenUsername(username));
            if (user.getPassword().equals(password))
                return user;

            else throw new PasswordInvalidException("Password is wrong!\n");
        } else throw new UsernameInvalidException("This username doesn't exists!\n");
    }

    /**
     * Método responsável pelo registo de um utilizador
     * @param username
     * @param name
     * @param email
     * @param password
     * @param account_balance
     * @throws UserExistsException
     */
    public void registerUser(String username, String name, String email, String password, float account_balance) throws UserExistsException {
        int id = userDAO.size() + 1;
        user = new User(id, username, name, email, password, account_balance);

        if (userDAO.get(idGivenUsername(username)).getUsername() == null) {
            userDAO.put(id, user);
        } else throw new UserExistsException("This username already exists!\n");
    }

    /**
     * Método que nos dá a informação de um utilizador
     * @return
     */
    public String getUserInfo() {
        return (user.toString());
    }

    /**
     * Método que apaga um utilizador
     * @throws UsernameInvalidException
     */
    public void deleteUser() throws UsernameInvalidException {
        if (this.userDAO.containsKey(user.getIdUser())) {
            this.userDAO.remove(idGivenUsername(user.getUsername()));

            do {
                this.positionDAO.remove(positionGivenId(user.getIdUser()));

            } while (this.positionDAO.containsKey(positionGivenId(user.getIdUser())));

            System.out.println("All user positions opened where removed!");

            do {
                this.notificationDAO.remove(notificationGivenId(user.getIdUser()));

            } while (this.notificationDAO.containsKey(notificationGivenId(user.getIdUser())));

            System.out.println("All user notifications where removed!");
        } else throw new UsernameInvalidException("There is no user with that username!");
    }

    /**
     * Fornece o id de uma posição dado o id de um utilizador
     * @param user_id
     * @return
     */
    private int positionGivenId(Integer user_id) {
        int id = -1;

        for (Position pst : positionDAO.values()) {
            if (pst.getidUser() == user_id) {
                id = pst.getIdPosition();
            }
        }
        return id;
    }

    /**
     * Fornece o id de uma notificação dado o id de um utilizador
     * @param user_id
     * @return
     */
    private int notificationGivenId(Integer user_id) {
        int id = -1;

        for (Notification ntf : notificationDAO.values()) {
            if (ntf.getNotific_user_id() == user_id) {
                id = ntf.getId_notification();
            }
        }
        return id;
    }

    /**
     * Fornece o id do utilizador dado o seu username
     * @param username
     * @return
     */
    private int idGivenUsername(String username) {
        int id = -1;

        for (User user : userDAO.values()) {
            if (user.getUsername().equals(username)) {
                id = user.getIdUser();
            }
        }
        return id;
    }

    /**
     * Fornece o nome do utilizador dado o seu id
     * @param id
     * @return
     */
    private String usernameGivenId(int id) {
        String rs = "";

        for (User user : userDAO.values()) {
            if (user.getIdUser() == id) {
                rs = user.getName();
            }
        }
        return rs;
    }

    /**
     * Fornece o id do stock/ativo dado o seu nome
     * @param stockname
     * @return
     */
    private int stock_id(String stockname) {
        int id = -1;

        for (MarketStock mstk : stockDAO.values()) {
            if (mstk.getName().equals(stockname)) {
                id = mstk.getId_stock();
            }
        }
        return id;
    }

    /**
     * Fornece o nome do ativo dado o seu id
     * @param id
     * @return
     */
    private String stock_name(int id) {
        String stknm = null;

        for (MarketStock mstk : stockDAO.values()) {
            if (mstk.getId_stock() == id) {
                stknm = mstk.getName();
            }
        }
        return stknm;
    }

    /**
     * Fornece o ativo dando o seu nome
     * @param name
     * @return
     */
    public MarketStock Mstock_name(String name) {
        MarketStock mstkr = null;

        for (MarketStock mstk : stockDAO.values()) {
            if (mstk.getName().equals(name)) {
                mstkr = mstk;
            }
        }
        return mstkr;
    }

    /**
     * Fornece o ativo dando o seu id
     * @param id
     * @return
     */
    private MarketStock Mstock_id(int id) {
        MarketStock mstkr = null;

        for (MarketStock mstk : stockDAO.values()) {
            if (mstk.getId_stock() == id) {
                mstkr = mstk;
            }
        }
        return mstkr;
    }

    /**
     * Apresenta a conta de um utilizador
     * @return
     */
    public float checkAccount() {
        return user.getAccount_balance();
    }

    /**
     * Indica quantas unidades de um dado ativo ainda há para venda
     * @param id_stock
     * @return
     */
    public int unitsRemaining(int id_stock) {
        int units = -1;

        for (Position pst : positionDAO.values()) {
            if ((pst.getidUser() == (user.getIdUser())) && pst.getMarketstock_id() == id_stock && (pst.getType().equals("Buy") && (pst.getStatus().equals("Dealt"))))
                units = pst.getUnits_remaining();
        }
        return units;
    }

    /**
     * Estando uma posição de venda aberta, atualiza a quantidade de unidades que ainda há de um dado ativo na base de dados
     * @param id_stock
     * @param amount
     */
    private void updateUnitsRemaining(int id_stock, int amount) {
        for (Position pst : positionDAO.values()) {
            if ((pst.getidUser() == (user.getIdUser())) && pst.getMarketstock_id() == id_stock && (pst.getType().equals("Buy") && (pst.getStatus().equals("Dealt")))) {
                pst.setUnits_remaining(pst.getUnits_remaining()-amount);
                positionDAO.put(pst.getIdPosition(), pst);
            }
        }
    }

    /**
     * Formulário de adição de fundos à conta de um utilizador
     * @param value
     */
    public void addFund(float value) {
        System.out.println("You Had:  " + user.getAccount_balance());
        user.addFunds(value);
        System.out.println("Funds Added!");
        System.out.println("Now You Currently Have:  " + user.getAccount_balance());
        userDAO.put(user.getIdUser(), user);
    }

    /**
     * Atualiza conta do utilizador depois de ter feito uma compra
     * @param value
     */
    private void debitFund(float value) {
        System.out.println("You Had:  " + user.getAccount_balance());
        user.debitFunds(value);
        System.out.println("Purchase Completed!");
        System.out.println("Now You Currently Have:  " + user.getAccount_balance());
        userDAO.put(user.getIdUser(), user);
    }

    /**
     * Fornece todos os ativos disponíveis no mercado
     * @return
     */
    public List<MarketStock> listMarketStocks() {
        List<MarketStock> lmstk = new ArrayList<>();

        lmstk.addAll(stockDAO.values());
        return lmstk;
    }

    /**
     * Apresenta os ativos numa lista
     */
    public void prettyStockList(){
        ArrayList <ArrayList<String>> list = new ArrayList<> ();
        ArrayList<String> header = new ArrayList<>();
        header.add("ID STOCK");
        header.add("NAME");
        header.add("OWNER");
        header.add("CFD BUY");
        header.add("CFD SALE");
        header.add("PRICE");
        list.add(header);
        int i = 0;
        MarketStock cur;

        for(Integer cod : stockDAO.keySet()){
            ArrayList<String> line = new ArrayList <>();
            line.add(Integer.toString(++i));
            line.add(stockDAO.get(cod).getName());
            line.add(stockDAO.get(cod).getOwner());
            if(stockDAO.get(cod).getCfd_Buy() == 0.0) {
                line.add("CLOSED");
                line.add("CLOSED");
            } else {
                line.add(""+stockDAO.get(cod).getCfd_Buy());
                line.add(""+stockDAO.get(cod).getCfd_Sale());
            }
            line.add(""+stockDAO.get(cod).getPrice());
            list.add(line);
        }
        Table table = new Table(list);
        table.print();
    }

    /**
     * Apresenta a lista de notificações de um utilizador
     * @return
     */
    public List<Notification> checkUserNotifications() {
        List<Notification> lntf = new ArrayList<>();

        for (Notification ntf : notificationDAO.values()) {
            if (ntf.getNotific_user_id() == (user.getIdUser()))
                lntf.add(ntf);
        }
        return lntf;
    }

    /**
     * Apresenta o portfólio de um utilizador numa tabela
     */
    public void prettyPortfolio(){
        ArrayList <ArrayList<String>> list = new ArrayList<> ();
        ArrayList<String> header = new ArrayList<>();
        Position cur = null;

        header.add("ID POSITION");
        header.add("TYPE");
        header.add("ID USER");
        header.add("ID STOCK");
        header.add("AMOUNT");
        header.add("STOP LOSS");
        header.add("TAKE PROFIT");
        header.add("STATUS");
        header.add("DEAL VALUE");
        list.add(header);

        for(Integer id : positionDAO.keySet()) {
            ArrayList<String> line = new ArrayList <>();
            line.add(""+cur.getIdPosition());
            line.add(""+cur.getType());
            line.add(""+cur.getidUser());
            line.add(""+cur.getMarketstock_id());
            line.add(""+cur.getAmount());
            line.add(""+cur.getStop_loss());
            line.add(""+cur.getTake_profit());
            line.add(""+cur.getStatus());
            line.add(""+cur.getDeal_value());

            list.add(line);
        }
        Table table = new Table(list);
        table.print();
    }

    /**
     * Apresenta todos os ativos disponíveis para venda
     * @return
     */
    public List<MarketStock> stocksOnSale() {
        List<MarketStock> lstksl = new ArrayList<>();

        for (Position pst : positionDAO.values()) {
            if ((pst.getidUser() == (user.getIdUser())) && (pst.getType().equals("Buy") && (pst.getStatus().equals("Dealt"))) && (pst.getUnits_remaining() > 0))
                lstksl.add(Mstock_id(pst.getMarketstock_id()));
        }
        return lstksl;
    }

    /**
     * Apresenta os ativos que um dado utilizador tem em espera
     * @return
     */
    public List<MarketStock> stocksOnWaiting() {
        List<MarketStock> lstksl = new ArrayList<>();

        for (Position pst : positionDAO.values()) {
            if ((pst.getidUser() == (user.getIdUser())) && (pst.getType().equals("Buy") && (pst.getStatus().equals("Waiting"))) && (pst.getUnits_remaining() > 0))
                lstksl.add(Mstock_id(pst.getMarketstock_id()));
        }
        return lstksl;
    }

    /**
     * Apresenta todas as posições que o utilizador possui
     * @return
     */
    public List<String> myDeals() {
        List<String> lstpst = new ArrayList<>();

        for (Position pst : positionDAO.values()) {
            if (pst.getidUser() == (user.getIdUser()))
                lstpst.add(myDealsString(pst));
        }
        return lstpst;
    }

    /**
     * Método equivalente ao toString
     * @param pst
     * @return
     */
    private String myDealsString(Position pst) {
        return  "                          POSITION " + pst.getIdPosition() + "\n" +
                "    Type:           '" + pst.getType() + '\'' + "\n" +
                "    User:            " + usernameGivenId(pst.getidUser()) + "\n" +
                "    Marketstock:     " + stock_name(pst.getMarketstock_id()) + "\n" +
                "    Amount:          " + pst.getAmount() + "\n" +
                "    Units Remaining: " + pst.getUnits_remaining() + "\n" +
                "    StopLoss:        " + pst.getStop_loss() + "\n" +
                "    TakeProfit:      " + pst.getTake_profit() + "\n" +
                "    Status:          " + pst.getStatus() + "\n" +
                "    Deal Value:      " + pst.getDeal_value() + "\n" +
                "    Deal Date:       " + pst.getDeal_date() + "\n";
    }

    /**
     * Confirma se um utilizador pode comprar um dado ativo
     * @param account_balance
     * @param stockname
     * @param amount
     * @return
     */
    public boolean isAbleToBuy(float account_balance, String stockname, int amount) {
        return (account_balance > amount * stockDAO.get(stock_id(stockname)).getCfd_Buy());
    }

    /**
     * Verifica se existe profit num market buy
     * @param stockname
     * @param stop_loss
     * @param take_profit
     * @return
     */
    public boolean existsProfitOnBuy(String stockname, float stop_loss, float take_profit) {
        return ((stockDAO.get(stock_id(stockname)).getCfd_Buy()) >= take_profit || (stockDAO.get(stock_id(stockname)).getCfd_Buy()) <= stop_loss);
    }

    /**
     * Verifica se existe profit num market sale
     * @param stockname
     * @param stop_loss
     * @param take_profit
     * @return
     */
    public boolean existsProfitOnSale(String stockname, float stop_loss, float take_profit) {
        return ((stockDAO.get(stock_id(stockname)).getCfd_Sale()) >= take_profit || (stockDAO.get(stock_id(stockname)).getCfd_Sale()) <= stop_loss);
    }

    /**
     * Comprar um ativo
     * @param stockname
     * @param amount
     * @param stop_loss
     * @param take_profit
     */
    public void openBuyPositionDealt(String stockname, int amount, float stop_loss, float take_profit) {
        int sz = positionDAO.size() + 1;
        float dv;
        String date, day, month, year;

        Position buy_pst = new Position();
        buy_pst.setIdPosition(sz);
        buy_pst.setType("Buy");
        buy_pst.setIdUser(user.getIdUser());
        buy_pst.setMarketstock_id(stock_id(stockname));
        buy_pst.setAmount(amount);
        buy_pst.setUnits_remaining(amount);
        buy_pst.setStop_loss(stop_loss);
        buy_pst.setTake_profit(take_profit);
        buy_pst.setStatus("Dealt");
        dv = amount * (stockDAO.get(stock_id(stockname)).getCfd_Buy());
        buy_pst.setDeal_value(dv);

        day = String.valueOf(LocalDateTime.now().getDayOfMonth());
        month = String.valueOf(LocalDateTime.now().getMonthValue());
        year = String.valueOf(LocalDateTime.now().getYear());
        date = day + "-" + month + "-" + year;
        buy_pst.setDeal_date(date);

        positionDAO.put(buy_pst.getIdPosition(), buy_pst);
        debitFund(dv);
    }

    /**
     * Esperar que a compra seja profitable para se poder realizar
     * @param stockname
     * @param amount
     * @param stop_loss
     * @param take_profit
     */
    public void openBuyPositionWaiting(String stockname, int amount, float stop_loss, float take_profit) {
        int sz = positionDAO.size() + 1;

        Position buy_pst = new Position();
        buy_pst.setIdPosition(sz);
        buy_pst.setType("Buy");
        buy_pst.setIdUser(user.getIdUser());
        buy_pst.setMarketstock_id(stock_id(stockname));
        buy_pst.setAmount(amount);
        buy_pst.setUnits_remaining(amount);
        buy_pst.setStop_loss(stop_loss);
        buy_pst.setTake_profit(take_profit);
        buy_pst.setStatus("Waiting");
        buy_pst.setDeal_value(0);
        buy_pst.setDeal_date("00-00-0000");

        positionDAO.put(buy_pst.getIdPosition(), buy_pst);
        stockDAO.get(stock_id(stockname)).addBuyPositionObservingStock(buy_pst);
        System.out.println("Since you want a profitable buy, you will get a message in your notifications once this deal is done!");

    }

    /**
     * Quando a compra se torna profitable, a compra realiza-se
     * @param mstk
     */
    public void updateBuyPositionWaiting(MarketStock mstk) {
        float dv;
        String date, day, month, year;
        int nsz = notificationDAO.size() + 1;

        for (Position pst : positionDAO.values()) {
            if ((pst.getMarketstock_id() == mstk.getId_stock() && (pst.getType().equals("Buy") && (pst.getStatus().equals("Waiting"))))) {
                if (isAbleToBuy(checkAccount(), mstk.getName(), pst.getAmount())) {
                    if (existsProfitOnBuy(mstk.getName(), pst.getStop_loss(), pst.getTake_profit())) {

                        pst.setStatus("Dealt");

                        dv = pst.getAmount() * (mstk.getCfd_Buy());
                        pst.setDeal_value(dv);

                        day = String.valueOf(LocalDateTime.now().getDayOfMonth());
                        month = String.valueOf(LocalDateTime.now().getMonthValue());
                        year = String.valueOf(LocalDateTime.now().getYear());
                        date = day + "-" + month + "-" + year;
                        pst.setDeal_date(date);

                        positionDAO.put(pst.getIdPosition(), pst);
                        debitFund(dv);

                        Notification ntf = new Notification();
                        ntf.setId_notification(nsz);
                        ntf.setNotific_user_id(user.getIdUser());
                        ntf.setInfo(prettyPositionNotification(pst));
                        notificationDAO.put(ntf.getId_notification(), ntf);

                        stockDAO.get(mstk.getId_stock()).removeBuyPositionObservingStock(pst);
                    }
                }
            }
        }
    }


    /**
     * Vender ativo
     * @param stockname
     * @param amount
     * @param stop_loss
     * @param take_profit
     */
    public void openSalePositionDealt(String stockname, int amount, float stop_loss, float take_profit) {
        int sz = positionDAO.size() + 1;
        float dv;
        String date, day, month, year;

        Position sale_pst = new Position();
        sale_pst.setIdPosition(sz);
        sale_pst.setType("Sale");
        sale_pst.setIdUser(user.getIdUser());
        sale_pst.setMarketstock_id(stock_id(stockname));
        sale_pst.setAmount(amount);
        sale_pst.setUnits_remaining(0);
        sale_pst.setStop_loss(stop_loss);
        sale_pst.setTake_profit(take_profit);
        sale_pst.setStatus("Dealt");
        dv = amount * (stockDAO.get(stock_id(stockname)).getCfd_Sale());
        sale_pst.setDeal_value(dv);
        day = String.valueOf(LocalDateTime.now().getDayOfMonth());
        month = String.valueOf(LocalDateTime.now().getMonthValue());
        year = String.valueOf(LocalDateTime.now().getYear());
        date = day + "-" + month + "-" + year;
        sale_pst.setDeal_date(date);

        updateUnitsRemaining(stock_id(stockname), amount);
        positionDAO.put(sale_pst.getIdPosition(), sale_pst);
        addFund(dv);
    }

    /**
     * Esperar que a venda seja profitable para se poder realizar
     * @param stockname
     * @param amount
     * @param stop_loss
     * @param take_profit
     */
    public void openSalePositionWaiting(String stockname, int amount, float stop_loss, float take_profit) {
        int sz = positionDAO.size() + 1;

        Position sale_pst = new Position();
        sale_pst.setIdPosition(sz);
        sale_pst.setType("Sale");
        sale_pst.setIdUser(user.getIdUser());
        sale_pst.setMarketstock_id(stock_id(stockname));
        sale_pst.setAmount(amount);
        sale_pst.setUnits_remaining(0);
        sale_pst.setStop_loss(stop_loss);
        sale_pst.setTake_profit(take_profit);
        sale_pst.setStatus("Waiting");
        sale_pst.setDeal_value(0);
        sale_pst.setDeal_date("00-00-0000");

        updateUnitsRemaining(stock_id(stockname), amount);
        positionDAO.put(sale_pst.getIdPosition(), sale_pst);
        stockDAO.get(stock_id(stockname)).addSalePositionObservingStock(sale_pst);
        System.out.println("Since aou want a profitable sale, you will get a message in your notifications once this deal is done!");

    }

    /**
     * Quando a venda se torna profitable, a venda realiza-se
     * @param mstk
     */
    public void updateSalePositionWaiting(MarketStock mstk) {
        float dv;
        String date, day, month, year;
        int nsz = notificationDAO.size() + 1;

        for (Position pst : positionDAO.values()) {
            if ((pst.getMarketstock_id() == mstk.getId_stock() && (pst.getType().equals("Sale") && (pst.getStatus().equals("Waiting"))))) {
                if (existsProfitOnSale(mstk.getName(), pst.getStop_loss(), pst.getTake_profit())) {

                    pst.setStatus("Dealt");

                    dv = pst.getAmount() * (mstk.getCfd_Buy());
                    pst.setDeal_value(dv);

                    day = String.valueOf(LocalDateTime.now().getDayOfMonth());
                    month = String.valueOf(LocalDateTime.now().getMonthValue());
                    year = String.valueOf(LocalDateTime.now().getYear());
                    date = day + "-" + month + "-" + year;
                    pst.setDeal_date(date);

                    positionDAO.put(pst.getIdPosition(), pst);
                    addFund(dv);

                    Notification ntf = new Notification();
                    ntf.setId_notification(nsz);
                    ntf.setNotific_user_id(user.getIdUser());
                    ntf.setInfo(prettyPositionNotification(pst));
                    notificationDAO.put(ntf.getId_notification(), ntf);

                    stockDAO.get(mstk.getId_stock()).removeSalePositionObservingStock(pst);
                }
            }
        }
    }

    /**
     * Método que cria um ativos e povoa a base de dados com os ativos sempre que a aplicação é iniciada
     * @param inptsym
     */
    private void createStock(String inptsym) {
        int sz = stockDAO.size() + 1;
        MarketStock mstk = new MarketStock();

        if (inptsym != null && API.getStockName(inptsym) != null && !stockExists(inptsym)) {
            try {
                System.out.println("\n");
                mstk.setId_stock(sz);
                mstk.setName(API.getStockName(inptsym));
                System.out.println("MarketStock Symbol:    " + mstk.getName());
                mstk.setOwner(API.getStockCompany(inptsym));
                System.out.println("MarketStock Company:   " + mstk.getOwner());
                mstk.setCfd_buy(API.getStockCfdBuy(inptsym));
                mstk.setCfd_sale(API.getStockCfdSale(inptsym));
                mstk.setPrice(API.getPrice(inptsym));
                stockDAO.put(sz, mstk);

            } catch (NullPointerException e) {
                System.out.println("Couldn't retrieve some info about the stock! Not added, sorry!");
            }
        } else {
            if (inptsym == null)
                System.out.println("No input symbols");

            if (stockExists(inptsym))
                System.out.println("\nThe " + inptsym + " stock is already on the market database...");

        }
    }

    /**
     * Método parecido ao anteiror, mas que envia notificações aos utilizadores sempre um novo ativo é adicionado ao mercado
     * @param inptsym
     */
    public void addStock(String inptsym) {
        int sz = stockDAO.size() + 1;
        int nsz = notificationDAO.size()+1;
        MarketStock mstk = new MarketStock();

        if (inptsym != null && API.getStockName(inptsym) != null && !stockExists(inptsym)) {
            try {
                mstk.setId_stock(sz);
                mstk.setName(API.getStockName(inptsym));
                System.out.println("MarketStock Symbol:    " + mstk.getName());
                mstk.setOwner(API.getStockCompany(inptsym));
                System.out.println("MarketStock Company:   " + mstk.getOwner());
                mstk.setCfd_buy(API.getStockCfdBuy(inptsym));
                mstk.setCfd_sale(API.getStockCfdSale(inptsym));
                mstk.setPrice(API.getPrice(inptsym));
                stockDAO.put(sz, mstk);
                Notification nts = new Notification();
                for (User user : userDAO.values()) {
                    nts.setId_notification(nsz);
                    nsz++;
                    nts.setNotific_user_id(user.getIdUser());
                    nts.setInfo("The market of stocks grew bigger, since the "+mstk.getName()+ " stock from "+mstk.getOwner()+" was added!");
                    notificationDAO.put(nts.getId_notification(), nts);
                }

            } catch (NullPointerException e) {
                System.out.println("Couldn't retrieve some info about the stock! Not added, sorry!");
            }
        } else {
            if (inptsym == null)
                System.out.println("No input symbols...");

            if (stockExists(inptsym))
                System.out.println("The " + inptsym + " stock is already on the market database...");

        }
    }

    /**
     * Verifica se um dado ativo existe
     * @param mstk
     * @return
     */
    private boolean stockExists(String mstk) {
        for (MarketStock m : stockDAO.values()) {
            if (m.getName().equals(mstk))
                return true;
        }

        return false;
    }

    /**
     * Remove ativo
     * @param namestocktoremove
     * @throws StockNotExistsException
     */
    public void removeStock(String namestocktoremove) throws StockNotExistsException {
        if (stockExists(namestocktoremove)) {
            this.stockDAO.remove(stock_id(namestocktoremove));
        } else throw new StockNotExistsException();
    }

    /**
     * Método de término de sessão
     */
    public void logOut() {
        this.user = null;
        System.out.println("> Session Ended!\n");
    }

    /**
     * Mensagem de notificação
     * @param pst
     * @return
     */
    public String prettyPositionNotification(Position pst) {
        String st;

        if (pst.getType().equals("Buy")) {
            st = "The " + pst.getAmount() + " " + stock_name(pst.getMarketstock_id()) + " stocks you wanted to buy, with a Stop Loss of " + pst.getStop_loss() + " and a Take Profit of " + pst.getTake_profit() + ", where bought by " + pst.getDeal_value() + " on " + pst.getDeal_date() + ". Check \"MY DEALS\" On The Menu For More Info";
        } else {
            st = "The " + pst.getAmount() + " " + stock_name(pst.getMarketstock_id()) + " stocks you wanted to sale, with a Stop Loss of " + pst.getStop_loss() + " and a Take Profit of " + pst.getTake_profit() + ", where sold by " + pst.getDeal_value() + " on " + pst.getDeal_date() + ". Check \"MY DEALS\" On The Menu For More Info";
        }
        return st;
    }

    /**
     * Atualiza a lista de observadores de compra. É atualizada sempre que a aplicação é iniciada
     */
    public void updateBuyObservers() {
        String stockname;

        for (Position pst : positionDAO.values()) {
            if (pst.getType().equals("Buy") && pst.getStatus().equals("Waiting")) {
                stockname = stock_name(pst.getMarketstock_id());
                stockDAO.get(stock_id(stockname)).addBuyPositionObservingStock(pst);
            }
        }
    }

    /**
     * Atualiza a lista de observadores de venda. É atualizada sempre que a aplicação é iniciada
     */
    public void updateSaleObservers() {
        String stockname;

        for (Position pst : positionDAO.values()) {
            if (pst.getType().equals("Sale") && pst.getStatus().equals("Waiting")) {
                stockname = stock_name(pst.getMarketstock_id());
                stockDAO.get(stock_id(stockname)).addBuyPositionObservingStock(pst);
            }
        }
    }
}
