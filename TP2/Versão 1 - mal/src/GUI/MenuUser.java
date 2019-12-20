package GUI;

import java.io.IOException;

public class MenuUser extends GUI {

    public static void loggedInGUI(){
        System.out.println("---- Welcome, " +trader.getUsernamee() + "! ----");
        System.out.println("1 - CHECK ASSET LIST");
        System.out.println("2 - CHECK ACTIVE CFDS");
        System.out.println("3 - CHECK PROFIT");
        System.out.println("4 - CLOSE CFD");
        System.out.println("5 - OPEN CFD");
        System.out.println("6 - ADD ASSET TO WATCHLIST");
        System.out.println("7 - SHOW WATCHLIST");
        System.out.println("8 - SHOW NOTIFICATIONS");
        System.out.println("0 - LOGOUT");
        System.out.println("--------------------");
    }

    protected static boolean loggedIn() throws Exception {
        String opcao = readLine();
        switch (opcao){
            case "0": {
                // LOGOUT
                System.out.println("LOGGING OUT...");
                Menu.logOut();
                Menu.showMenu();
                break;
            }
            case "1": {
                // CHECK ASSET LIST
                System.out.println("REFRESHING ASSET RATES\n");
                try{
                    trader.loadAssets();
                } catch (IOException e) {}
                trader.showAssets();
                continuar();
                loggedInGUI();
                break;
            }
            case "2": {
                //CHECK ACTIVE CFDS
                trader.listCfds();
                continuar();
                loggedInGUI();
                break;
            }
            case "3": {
                //CHECK PROFIT
                trader.checkProfit();
                continuar();
                loggedInGUI();
                break;
            }
            case "4": {
                //CLOSE CFD
                trader.closeCfd();
                continuar();
                loggedInGUI();
                break;
            }
            case "5": {
                //OPEN CFD
                trader.showAssets();
                try{
                    trader.createCfd();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                continuar();
                loggedInGUI();
                break;
            }
            case "6": {
                //ADD ASSET TO WATCHLIST -> MALI
                trader.showAssets();
                try{
                    trader.addtoWatchlist();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                continuar();
                loggedInGUI();
                break;
            }
            case "7": {
                //SHOW WATCHLIST
                try {
                    trader.showWatchlist();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                continuar();
                loggedInGUI();
                break;
            }
            case "8": {
                //SHOW NOTIFICATIONS
                trader.showNotifications();
                continuar();
                loggedInGUI();
                break;
            }
        }
        return true;
    }
}

