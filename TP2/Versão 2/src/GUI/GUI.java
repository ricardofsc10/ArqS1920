package GUI;

import Business.App;
import Business.Checker;
import Exceptions.PasswordInvalidException;
import Exceptions.StockNotExistsException;
import Exceptions.UserExistsException;
import Exceptions.UsernameInvalidException;

import java.util.Scanner;

public class GUI {
    private static App ngplt;


    public static void main(String[] args) {
        ngplt = new App();
        try {
            ngplt.loadStocks();

            Checker check = new Checker(ngplt);
            check.start();

            ngplt.updateBuyObservers();
            ngplt.updateSaleObservers();
        }
        catch (NullPointerException e){
            System.out.println("\n\nUps! There's no data due to API problem...\n\n");
        }

        showMenu(ngplt);
    }

    /**
     * Método que apresenta o menu principal ao utilizador
     * @param ngplt
     */
    public static void showMenu(App ngplt){
        System.out.println("----- ESS TRADING APP-----");

        System.out.println("1 - LOGIN");
        System.out.println("2 - SIGN UP ");
        System.out.println("3 - ADD STOCK TO MARKET");
        System.out.println("4 - REMOVE STOCK FROM MARKET");
        System.out.println("5 - LIST STOCKS CURRENTLY ON THE MARKET");
        System.out.println("-------------------");

        boolean handler = true;
        while (handler) {
            handler = menuHandler();
        }
    }

    /**
     * Método auxiliar para a apresentação do menu principal, escolhendo a opção desejada
     * @return
     */
    private static boolean menuHandler(){
        String option = readLine();
        if (option.equals("1")) {
            loginMenu();
            MainMenu.showMenu(ngplt);
        } else if (option.equals("2")) {
            signMenu();
        } else if (option.equals("3")) {
            addStockMenu();
            continuar();
            showMenu(ngplt);
        } else if (option.equals("4")) {
            removeStockMenu();
            continuar();
            showMenu(ngplt);
        } else if(option.equals("5")){
            try {
                ngplt.prettyStockList();
            }
            catch (NullPointerException e){
                System.out.println("\n\nUps! There's no data due to API problem...\n\n");
            }
            continuar();
            showMenu(ngplt);
        } else {
            System.out.println("Input não reconhecido.");
        }
        return true;
    }

    /**
     * Método que lê uma linha do teclado (System.in)
     * @return Linha lida do teclado
     */
    protected static String readLine(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * Método que lê um inteiro do teclado
     * @return Inteiro lido do teclado
     */
    protected static int readLineInt(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    /**
     * Método que lê um float do teclado
     * @return Float lido do teclado
     */
    protected static float readLineFloat(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextFloat();
    }

    /**
     * Método que formulário de logIn do utilizador
     */
    private static void loginMenu() {

        System.out.println("---- LOGIN ----");
        System.out.println("Username:");
        String username = readLine();
        System.out.println("Password:");
        String password = readLine();
        try {
            ngplt.startSession(username, password);

        } catch (PasswordInvalidException | UsernameInvalidException e) {
            System.out.println(e.getMessage());
            MainMenu.showMenu(ngplt);
        }
    }

    /**
     * Método que apresenta o formulário de registo do utilizador
     */
    private static void signMenu() {

        System.out.println("---- SIGN UP ----");
        System.out.println("Enter your username:");
        String username = readLine();
        System.out.println("Enter your name:");
        String name = readLine();
        System.out.println("Enter your password:");
        String password = readLine();
        System.out.println("Enter your email:");
        String email = readLine();
        System.out.println("How much money your account starts with?");
        float account_balance = readLineFloat();

        try {
            ngplt.registerUser(username, name, email, password, account_balance);
            System.out.println("Registered with success! Yay");
            continuar();
            showMenu(ngplt);
        } catch (UserExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Método que adiciona um stock ao mercado
     */
    private static void addStockMenu() {
        System.out.println("---- ADD STOCK TO MARKETPLACE ----");
        System.out.println("Enter the Stock's name you want to add to the Market:");
        String name = readLine();

        ngplt.addStock(name);
    }

    /**
     * Método que remove um stock do mercado
     */
    private static void removeStockMenu() {
        System.out.println("---- REMOVE STOCK FROM MARKETPLACE ----");
        System.out.println("Enter the Stock's name you want to remove from the Market:");
        String name = readLine();

        try {
            ngplt.removeStock(name);
        } catch (StockNotExistsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método que apresenta o formulário para abertura de uma posição de compra
     */
    static void openBuyPositionMenu() {
        Integer amount, id_stk, units_remaining;
        float account_balance, stop_loss, take_profit;
        String close_buy, stockname;

        System.out.println("---- OPEN BUY POSITION ----");
        System.out.println("Name of the Stock you want to buy:");
        stockname = readLine();
        while(ngplt.stocksOnSale().contains(ngplt.Mstock_name(stockname)) || ngplt.stocksOnWaiting().contains(ngplt.Mstock_name(stockname))) {
            id_stk = ngplt.Mstock_name(stockname).getId_stock();
            units_remaining = ngplt.unitsRemaining(id_stk);
            if (units_remaining == -1) {
                System.out.println("Ups, you have a pending buy position opened for the " + ngplt.Mstock_name(stockname).getName() + " stock! Wait for that position to get concluded before opening a new position\n");
                System.out.println("Name of the Stock you want to buy:");
                stockname = readLine();
            }
            else {
                System.out.println("Ups, you still have " + units_remaining.toString() + " units of this stock! Open sale positions for them first\n");
                System.out.println("Name of the Stock you want to buy:");
                stockname = readLine();            }
        }

        System.out.println("How much you want to buy?");
        amount = readLineInt();
        System.out.println("What's the Stop Loss you are Willing To Define?");
        stop_loss = readLineFloat();
        System.out.println("What's the Take Profit You Are Willing To Define?");
        take_profit = readLineFloat();
        account_balance = ngplt.checkAccount();

        if ((ngplt.isAbleToBuy(account_balance, stockname, amount))) {

            if ((ngplt.existsProfitOnBuy(stockname, stop_loss, take_profit))) {
                System.out.println("Profitable Purchase");
                ngplt.openBuyPositionDealt(stockname, amount, stop_loss, take_profit);
                System.out.println("Purchase Done! Check \"MY DEALS\" On The Menu For More Info");

            }
            else {
                System.out.println("This Buy Is Not Profitable For You At This Moment. You Wish To End It Now Anyway ? (yes|no)\n");
                close_buy = readLine();

                while ((!close_buy.equals("yes") && !close_buy.equals("YES") && !close_buy.equals("Y") && !close_buy.equals("Yes") && !close_buy.equals("y")) && (!close_buy.equals("no") && !close_buy.equals("NO") && !close_buy.equals("N") && !close_buy.equals("No") && !close_buy.equals("n"))) {
                    System.out.println("> Input Value Is Not Valid!\n This Buy Is Not Profitable For You At This Moment. You Wish To End It Now Anyway ? (yes|no)\n");
                    close_buy = readLine();
                }
                if(close_buy.equals("yes") || close_buy.equals("YES") || close_buy.equals("Y") || close_buy.equals("Yes") || close_buy.equals("y")) {
                    ngplt.openBuyPositionDealt(stockname, amount, stop_loss, take_profit);
                }
                else{
                    ngplt.openBuyPositionWaiting(stockname, amount, stop_loss, take_profit);
                }
            }
        }
        else {
            System.out.println("Not enough funds!\n");
        }
    }

    /**
     * Método que apresenta o formulário para abertura de uma posição de venda
     */
    static void openSalePositionMenu() {
        String stockname, close_sale;
        float stop_loss, take_profit;
        Integer amount,id_stk, units_remaining;

        System.out.println("---- OPEN SALE POSITION ----");
        System.out.println("Name of the Stock you want to sell:");
        stockname = readLine();
        while(!ngplt.stocksOnSale().contains(ngplt.Mstock_name(stockname))){
            System.out.println("> Input Value Is Not Valid!\n");
            System.out.println("Name of the Stock you want to sell:");
            stockname = readLine();
        }
        System.out.println("> Value Inserted!\n");

        id_stk = ngplt.Mstock_name(stockname).getId_stock();
        units_remaining = ngplt.unitsRemaining(id_stk);
        System.out.println("You May Sell "+units_remaining.toString()+" Units Of Your "+(ngplt.Mstock_name(stockname)).getName()+" Stock\n");
        System.out.println("How Much You Want To Sell?");
        amount = readLineInt();

        while(amount<0 || amount==0 || amount>units_remaining ){
            System.out.println("> Input Value Is Not Valid!\n");
            System.out.println("How Much You Want To Sell?");
            amount = readLineInt();
        }
        System.out.println("What's the Stop Loss You Are Willing To Define?");
        stop_loss = readLineFloat();
        System.out.println("What's the Take Profit You Are Willing To Define?");
        take_profit = readLineFloat();

        if ((ngplt.existsProfitOnSale(stockname, stop_loss, take_profit))) {
            System.out.println("Profitable Sale");
            ngplt.openSalePositionDealt(stockname, amount, stop_loss, take_profit);
            System.out.println("Sale Done! Check \"MY DEALS\" On The Menu For More Info");
        }
        else {
            System.out.println("This Sale Is Not Profitable For You At This Moment. You Wish To End It Now Anyway? (yes|no)\n");
            close_sale = readLine();
            while ((!close_sale.equals("yes") && !close_sale.equals("YES") && !close_sale.equals("Y") && !close_sale.equals("Yes") && !close_sale.equals("y")) && (!close_sale.equals("no") && !close_sale.equals("NO") && !close_sale.equals("N") && !close_sale.equals("No") && !close_sale.equals("n"))) {
                System.out.println("> Input Value Is Not Valid!\n This Sale Is Not Profitable For You At This Moment. You Wish To End It Now Anyway ? (yes|no)\n");
                close_sale = readLine();
            }
            if(close_sale.equals("yes") || close_sale.equals("YES") || close_sale.equals("Y") || close_sale.equals("Yes") || close_sale.equals("y")) {
                ngplt.openSalePositionDealt(stockname, amount, stop_loss, take_profit);
            }
            else{
                ngplt.openSalePositionWaiting(stockname, amount, stop_loss, take_profit);
            }


        }
    }

    /**
     * Método que apresenta o formulário de adição de fundos
     */
    static void addFundsMenu() {
        float value;

        System.out.println("ADD FUNDS TO ACCOUNT");
        System.out.println("How Much Funds You Would Like To Add?");
        value = readLineFloat();

        ngplt.addFund(value);
    }



    //ESTE METODO, EMBORA NÃO IMPLEMENTADO NA TOTALIDADE, TERIA COMO OBJETIVO A INTERAÇÃO DA APLICAÇÃO COM O UTILIZADOR ACERCA DO PREÇO DO ATIVO A SEGUIR,
    //E DE SEGUIDA TRATAVA DE VERIFICAR SE O PREÇO DO ATIVO JA ESTA ACIMA DO PREÇO INSERIDO, E DECIDIR SE NOTIFICAVA O UTILIZADOR OU ADICIONAVA-O À LISTA
    //DOS OBSERVERS (USER) QUE ESTÃO A SEGUIR O PREÇO DO ATIVO
    /*private static void openTrackStockMenu() {
        String stockname;
        float start_price;

        System.out.println(                           "                                       * * * * * * * * * * * * * * * * * * * * * TRACK STOCK * * * * * * * * * * * * * * * * * * ");
        System.out.println(                           "                                       * *                                                                                   * *");
        stockname = startMenu.readString(        "                                       * *       Name Of The Stock You Want To Track:                                        * *");
        while(ngplt.listMarketStocks().contains(ngplt.Mstock_name(stockname))) {
            stockname = startMenu.readString("                                       * *       Name Of The Stock You Want To Track:                                        * *");
        }
        System.out.println("> Value Inserted!\n");
        start_price = startMenu.readFloat(       "                                       * *       From what price do you want to be notified?                                 * *");
        System.out.println("> Value Inserted!\n");
        System.out.println(                           "                                       * *                                                                                   * *");
        System.out.println(                           "                                       * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println(                           "                                                   * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *              \n");


    }*/

    /**
     * Método que define que, para continuar, o utilizador deve carregar no "Enter"
     */
    static void continuar(){
        System.out.println("\nPress Enter to continue");
        while (!readLine().equals("")) {
            System.out.println("Input value is not valid! \nPlease, press Enter to continue");
        }
    }

}
