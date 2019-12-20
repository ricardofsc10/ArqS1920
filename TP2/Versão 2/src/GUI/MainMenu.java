package GUI;

import Business.App;
import Exceptions.UsernameInvalidException;

import java.util.Scanner;

public class MainMenu extends GUI{

    public static void showMenu(App app) {
        System.out.println("1 - USER INFO");
        System.out.println("2 - OPEN BUY POSITION");
        System.out.println("3 - OPEN SALE POSITION");
        System.out.println("4 - ADD FUNDS");
        System.out.println("5 - CHECK PORTFOLIO");
        System.out.println("6 - MY DEALS");
        System.out.println("7 - NOTIFICATIONS");
        System.out.println("8 - DELETE ACCOUNT");
        System.out.println("9 - END SESSION");


        String option = readLine();
        if (option.equals("1")) {
            System.out.println(app.getUserInfo());
            continuar();
            showMenu(app);

        } else if (option.equals("2")) {
            System.out.println(app.listMarketStocks());
            openBuyPositionMenu();
            continuar();
            showMenu(app);
        } else if (option.equals("3")) {
            System.out.println(app.stocksOnSale());
            if(!app.stocksOnSale().isEmpty()) {
                openSalePositionMenu();
                continuar();
                showMenu(app);
            }
            else{
                System.out.println("You don't have nothing to sell at the moment! :(");
                continuar();
                showMenu(app);
            }

        } else if (option.equals("4")) {
            addFundsMenu();
            continuar();
            showMenu(app);

        } else if(option.equals("5")){
            app.prettyPortfolio();
            continuar();

        } else if(option.equals("6")){
            System.out.println(app.myDeals());
            continuar();
        } else if(option.equals("7")){
            System.out.println(app.checkUserNotifications());
            continuar();
        } else if(option.equals("8")){
            try {
                app.deleteUser();
                System.out.println("Account Deleted!\n");
                continuar();
            } catch (UsernameInvalidException e) {
                e.printStackTrace();
                showMenu(app);
            }
            showMenu(app);
        } else if(option.equals("9")){
            app.logOut();
            continuar();
            GUI.showMenu(app);
        } else if(option.equals("0")){
            System.out.println(app.listMarketStocks());
            continuar();
            showMenu(app);
        }
        else {
            System.out.println("Invalid input!");
        }
    }
}
