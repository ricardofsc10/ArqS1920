package Business;

import Data.*;
import Exceptions.PasswordIncorretaException;
import Exceptions.UserAlreadyExistsException;
import Exceptions.UtilizadorInexistenteException;
import GUI.GUI;
import GUI.Menu;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Trader extends User implements Runnable, DataSubject {

    private static User atual;
    private static Data data;
    private static Thread thread;
    private static Integer stop = 0;

    private static Map<String,MyObserver> observers;
    public static UserDAO userDAO;
    public static AssetDAO assets;
    public static CfdDAO cfds;
    public static HashMap <Integer, String> not = new HashMap <Integer, String> ();

    public User getUser(){
        return atual;
    }

    public String getUsernamee(){
        return atual.getUsername();
    }


    @Override
    public synchronized void run() {
        try{
            CfdDAO cfds = new CfdDAO();

            cfds = data.getAllCfd();
            ConcurrentHashMap <Integer, Watchlist> watchlist = new ConcurrentHashMap <Integer, Watchlist> ();
            watchlist = data.getWatchlist();

            Double ask;
            Double bid;
            Stock st;
            Stock sl;
            Double value;

            while(stop==0) {

                for(Cfd c : cfds.values()) {

                    int idAsset = c.getidAsset();

                    st = YahooFinance.get(assets.get(idAsset).getCode());
                    ask = st.getQuote().getAsk().doubleValue();
                    bid = st.getQuote().getBid().doubleValue();

                    if(c.getType().equals("BUY") && c.getActive().equals(true)) {
                        if(ask >= c.getUpperlimit() || ask <= c.getLowerlimit()) {
                            autoCloseCfd(c, ask);
                            observers.get(atual.getUsername()).update("CFD CLOSED");
                            avisa("CFD CLOSED");
                        }
                    }
                    if(c.getType().equals("SELL") && c.getActive().equals(true)) {
                        if(bid >= c.getUpperlimit() || bid <= c.getLowerlimit()) {
                            autoCloseCfd(c, bid);
                            observers.get(atual.getEmail()).update("CFD CLOSED");
                            avisa("CFD CLOSED");
                        }
                    }
                }

                for(Watchlist w: watchlist.values()) {

                    sl = YahooFinance.get(w.getCode());
                    value = sl.getQuote().getPrice().doubleValue();
                    if(w.getUpordown().equals(1)) {
                        //UPPER THAN
                        String isup = "UP";
                        if(value >= w.getLimit()) avisa(isup);
                    }
                    if(w.getUpordown().equals(0)) {
                        //LOWER THAN
                        String isdown = "DOWN";
                        if(value < w.getLimit()) avisa(isdown);
                    }

                }
            }
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {}
        System.out.println(Thread.currentThread().getName());
    }

    private synchronized static void avisa(String mensagem) {
        data.getUser(atual.getUsername()).addNotification(mensagem);
    }


    public synchronized static void main(String[] args) throws Exception {
        Locale.setDefault(new Locale("en", "US"));
        data = new Data();
        atual = new User();
        userDAO = new UserDAO();
        assets = new AssetDAO();
        cfds = new CfdDAO();
        observers = new HashMap<String, MyObserver>();
        Trader t = new Trader();
        thread = new Thread(t);
        thread.start();
        Menu.showMenu();
    }

    public synchronized void logIn(String username, String password) throws SQLException, UtilizadorInexistenteException, PasswordIncorretaException {
        if(this.userDAO.containsKey(data.idUserGivenUsername(username))){
            atual = this.userDAO.get(data.idUserGivenUsername(username));
            if(!atual.getPassword().equals(password)) throw new PasswordIncorretaException("Password incorreta!");
        }
        else throw new UtilizadorInexistenteException("Utilizador inexistente!");

        System.out.println("Sessão iniciada com sucesso.");
    }


    public synchronized void signUp(String email, String username, String name, String password, int age, double plafond) throws UserAlreadyExistsException {
        data.addUser(email, username, name, password, age, 0.0, plafond);
    }

    public static void loadAssets() throws IOException {
        if(assets.size() == 0){
            assets.put(assets.size()+1, new Asset(YahooFinance.get("FB")));
            assets.put(assets.size()+1, new Asset(YahooFinance.get("AAPL")));
            assets.put(assets.size()+1, new Asset(YahooFinance.get("TSLA")));
            assets.put(assets.size()+1, new Asset(YahooFinance.get("DE")));
            assets.put(assets.size()+1, new Asset(YahooFinance.get("GOOG")));
            assets.put(assets.size()+1, new Asset(YahooFinance.get("ORCL")));
            assets.put(assets.size()+1, new Asset(YahooFinance.get("PFE")));
            assets.put(assets.size()+1, new Asset(YahooFinance.get("IBM")));
            assets.put(assets.size()+1, new Asset(YahooFinance.get("PBR")));
            assets.put(assets.size()+1, new Asset(YahooFinance.get("OI")));
            assets.put(assets.size()+1, new Asset(YahooFinance.get("GDDY")));
            assets.put(assets.size()+1, new Asset(YahooFinance.get("VZ")));
            assets.put(assets.size()+1, new Asset(YahooFinance.get("ATC.AS")));
            assets.put(assets.size()+1, new Asset(YahooFinance.get("AIR.PA")));
        }
    }


    public static synchronized void listCfds() {
        String list_users;
        Cfd cur;

        System.out.println("[ACTIVE CFDS]\n");

        System.out.println(atual.getEmail());
        System.out.println(data.getUser(atual.getUsername()).getUsername());

        list_users = data.getUser(atual.getUsername()).getUsername();

        ArrayList <ArrayList<String>> list = new ArrayList<> ();
        ArrayList<String> header = new ArrayList<>();
        header.add("CFD NR");
        header.add("USER");
        header.add("ASSET");
        header.add("TYPE");
        header.add("UNIT");
        header.add("INITIAL VALUE [$]");
        header.add("TAKE PROFIT [$]");
        header.add("STOP LOSS [$]");
        header.add("STATUS");
        list.add(header);

        for(Integer id : cfds.keySet()) {
            System.out.println("HEREEEEE: " + (cfds.get(id).getUser()));
            if((cfds.get(id).getUser()).equals(list_users)) {

                cur = cfds.get(id);
                ArrayList<String> line = new ArrayList <>();

                line.add(""+cur.getId());
                line.add(""+data.giveuser(list_users).getName());
                line.add(""+cur.getidAsset());
                line.add(""+cur.getType());
                line.add(""+cur.getUnits());
                line.add(""+cur.getStart_value());
                line.add(""+cur.getUpperlimit());
                line.add(""+cur.getLowerlimit());
                if(cur.getActive()) {
                    line.add("ACTIVE");
                } else line.add("CLOSED");

                list.add(line);
            }
        }

        Table tablecfd = new Table(list);
        tablecfd.print();
    }

    public synchronized static void checkProfit() {

        Double pf;
        String list;

        list = atual.getUsername();
        System.out.println("[PROFIT]");
        pf = data.getUser(list).getProfit();
        if(pf >= 0.0) {
            System.out.println("GAINING "+pf+"\n");
        } else {
            System.out.println("[[[LOSING]]] "+pf+"\n");
        }
    }

    public static synchronized void closeCfd() throws IOException{
        Integer nr;
        String username;
        String type;
        Double balance;
        Double current_value;
        Double start_value;
        Double units;
        String asset;
        Double ratio;
        Double invested;
        Double gain;
        Cfd closing;
        Asset hold;
        Double profit;

        username = atual.getUsername();

        listCfds();

        System.out.println("\n [INSERT CFD TO CLOSE]:");

        nr = GUI.readLineInt();
        closing = data.getCfd(nr);
        start_value = closing.getStart_value();
        units = closing.getUnits();
        type = closing.getType();
        Integer aux_hold = closing.getidAsset();
        hold = assets.get(aux_hold);

        if(type.equals("BUY")){
            current_value = YahooFinance.get(hold.getCode()).getQuote().getAsk().doubleValue();
            if(current_value == 0.0) current_value = YahooFinance.get(hold.getCode()).getQuote().getPreviousClose().doubleValue();
            ratio = (current_value/start_value)/100;
            invested = start_value * units;
            balance = invested * ratio;
            gain = invested + balance;
        }
        else {
            current_value = YahooFinance.get(hold.getCode()).getQuote().getBid().doubleValue();
            if(current_value == 0.0) current_value = YahooFinance.get(hold.getCode()).getQuote().getPreviousClose().doubleValue();
            ratio = (current_value/start_value)/100;
            invested = start_value * units;
            balance = invested * ratio;
            gain = invested + balance;
        }

        data.getCfd(nr).setUnActive();

        profit = data.getUser(username).getProfit();
        profit = profit + balance;
        data.getUser(username).setProfit(profit);
        data.getUser(atual.getUsername()).addNotification("CFD " + nr + " CLOSED!");

        System.out.println("[CFD CLOSED]");
        System.out.println("[GAIN] $"+ balance +"\n");
        data.getUser(username).setProfit(profit);

        System.out.println(":))))): " + data.getUser(username).getProfit());
    }

    public static synchronized void autoCloseCfd(Cfd c, Double value) throws IOException{
        Integer nr;
        String username;
        Double balance;
        Double current_value;
        Double start_value;
        Double units;
        Double ratio;
        Double invested;
        Double gain;
        Cfd closing;
        Asset hold;
        Double profit;

        username = atual.getUsername();

        nr = c.getId();
        start_value = c.getStart_value();
        units = c.getUnits();
        hold = assets.get(c.getidAsset());


        current_value = value;
        if(current_value == 0.0) current_value = YahooFinance.get(hold.getCode()).getQuote().getPreviousClose().doubleValue();
        ratio = (current_value / start_value) / 100;
        invested = start_value * units;
        balance = invested * ratio;
        gain = invested+balance;

        data.getCfd(nr).setUnActive();
        profit = data.getUser(username).getProfit();

        profit = profit + balance;
        data.getUser(username).setProfit(profit);
    }

    public static synchronized void createCfd() throws IOException{
        String cfdUsername;
        Integer cfdAsset;
        String cfdType;
        Double cfdUpper;
        Double cfdLower;
        Double cfdStartValue;
        Double cfdUnits;
        Asset asset;

        System.out.println("[START A NEW CFD]");
        cfdUsername = atual.getUsername();

        System.out.print("[INSERT THE ASSET CODE] ");
        cfdAsset = GUI.readLineInt();

        System.out.print("[CHOOSE THE TYPE | SHORT OR BUY] ");
        cfdType = GUI.readLine();

        System.out.print("[AMOUNT OF UNITS] ");
        cfdUnits = GUI.readLineDouble();

        System.out.print("[SET \"TAKE PROFIT\" $] ");
        cfdUpper = GUI.readLineDouble();

        System.out.print("[SET \"STOP LOSS\" $] ");
        cfdLower = GUI.readLineDouble();

        if(cfdType.equals("BUY")) {
            cfdStartValue = assets.get(cfdAsset).getBid();
            if(cfdStartValue == 0.0) cfdStartValue = assets.get(cfdAsset).getPrevious();

        } else {
            cfdStartValue = assets.get(cfdAsset).getAsk();
            if(cfdStartValue == 0.0) cfdStartValue = assets.get(cfdAsset).getPrevious();
        }

        asset = data.getAsset(cfdAsset);
        Integer idAsset = asset.getId();

        Integer id = cfds.size()+1;

        Cfd novo = new Cfd(idAsset, cfdType, cfdUsername, cfdLower, cfdUpper, cfdUnits, cfdStartValue, true);
        cfds.put(id, novo);
        addObserver(atual.getUsername(), (MyObserver) atual);

        System.out.println("[CFD REGISTADO COM SUCESSO]");
        GUI.continuar();
    }

    public static synchronized void addtoWatchlist(){

        String user = atual.getUsername();
        System.out.println("INSERT ASSET CODE TO WATCH");
        String code = GUI.readLine();

        System.out.println("INSERT LIMIT WARNING");
        Double limit = GUI.readLineDouble();

        System.out.println("UP OR DOWN LIMIT? 1 - UP | 0 - DOWN");
        Integer upOrDown = GUI.readLineInt();

        Watchlist watch = new Watchlist(code, user, limit, upOrDown);
        data.addtoWatchlist(watch);
        System.out.println("DATA: " + data.getWatchlist());
    }

    public static void showAssets(){
        ArrayList <ArrayList<String>> list = new ArrayList<> ();
        ArrayList<String> header = new ArrayList<>();
        header.add("NR");
        header.add("CODE");
        header.add("NAME");
        header.add("ASK [$]");
        header.add("BID [$]");
        header.add("CLOSED PRICE [$]");
        list.add(header);
        int i = 0;
        for(int cod: assets.keySet()) {

            ArrayList<String> line = new ArrayList <>();
            line.add(Integer.toString(++i));
            line.add(assets.get(cod).getCode());
            line.add(assets.get(cod).getName());
            if(assets.get(cod).getAsk().equals(0.0)) {
                line.add("CLOSED");
                line.add("CLOSED");
            } else {
                line.add(""+assets.get(cod).getAsk());
                line.add(""+assets.get(cod).getBid());
            }
            line.add(""+assets.get(cod).getPrevious());
            list.add(line);
        }
        Table t = new Table(list);
        t.print();
    }

    public static void showWatchlist() throws IOException {
        ConcurrentHashMap<Integer, Watchlist> userlist = new ConcurrentHashMap <Integer, Watchlist> ();
        Watchlist cur = new Watchlist();

        ArrayList <ArrayList<String>> list = new ArrayList<> ();
        ArrayList<String> header = new ArrayList<>();
        header.add("ASSET CODE");
        header.add("USER");
        header.add("LIMIT");
        header.add("ACTUAL VALUE");
        header.add("UP(1) OR DOWN(0)");
        list.add(header);

        userlist = data.getWatchlist();

        for(Integer id : userlist.keySet()) {
            ArrayList<String> line = new ArrayList <>();
            cur = userlist.get(id);
            line.add(""+cur.getCode());
            line.add(""+cur.getUser());
            line.add(""+cur.getLimit());
            line.add(""+YahooFinance.get(cur.getCode()).getQuote().getPreviousClose());
            line.add(""+cur.getUpordown());

            list.add(line);
        }

        Table tablecfd = new Table(list);
        tablecfd.print();
    }

    public static void showNotifications() {
        ArrayList <ArrayList<String>> list = new ArrayList<> ();
        ArrayList<String> header = new ArrayList<>();
        not = data.getUser(atual.getUsername()).getNotifications();
        header.add("NR");
        header.add("MESSAGE");
        list.add(header);

        if(not == null) System.out.println("Não há notificações!");
        else{

            System.out.println("NOTI: " + not);
            for(Integer id: not.keySet()) {
                ArrayList <String> line = new ArrayList <> ();
                line.add(""+id);
                line.add(not.get(id));

                list.add(line);
            }
        }

        Table t = new Table(list);
        t.print();
    }


    public static void addObserver(String e, MyObserver observer){
        observers.put(e, observer);
    }

    public void removeObserver(MyObserver observer) {
        observers.remove(atual.getUsername());
    }
}
