package GUI;

import Business.*;
import Exception.*;
import Data.*;
import java.sql.SQLException;

public class GUITrader extends GUI {

    private static TradeEssApp tradeEssApp;

    protected static boolean traderLoggedIn() throws SQLException, Exception{
        int amount;
        float stop_loss, take_profit, money, saldoConta;
        String stockName, closeBuy, closeSale;

        String opcao = readLine();
        switch (opcao){
            case "1": // consultar saldo
                GUISaldo.gerirSaldoScene();
                GUISaldo.gerirSaldoHandler();
                continuar();
                MainMenu.loggedIn();

            case "2": //comprar posição
                System.out.println(tradeEssApp.listaStocks());
                System.out.println("Insira o nome do stock que quer comprar: ");
                stockName = readLine();
                System.out.println("Quanto € quer dar?");
                amount = readLineInt();
                System.out.println("Defina o Stop Loss: ");
                stop_loss = readLineInt();
                System.out.println("Defina o Take Profit: ");
                take_profit = readLineInt();
                saldoConta = tradeEssApp.checkSaldo();

                if(tradeEssApp.listaStocks().contains(tradeEssApp.idStockGivenName(stockName))){
                    if(tradeEssApp.isAbleToBuy(stockName, amount, saldoConta)){
                        if(tradeEssApp.profitBuyAvailable(stockName, stop_loss, take_profit)){
                            tradeEssApp.buyPosition(stockName, amount, stop_loss, take_profit, "Sim");
                        }
                        else {
                            System.out.println("Esta compra não é rentável para si, neste momento... Pretende continuar, mesmo assim? (Sim|Não)");
                            closeBuy = readLine();
                            tradeEssApp.buyPosition(stockName, amount, stop_loss, take_profit, closeBuy);
                        }
                    }
                    else System.out.println("Sem saldo suficiente! :(");
                }
                else {
                    System.out.println("Não existe!\n");
                }
                continuar();
                traderLoggedIn();
                break;

            case "3": //vender posição
                System.out.println(tradeEssApp.listStocksSale());
                System.out.println("Insira o nome do stock que quer vender: ");
                stockName = readLine();
                amount = tradeEssApp.amount(stockName);
                System.out.println("Defina o Stop Loss: ");
                stop_loss = readLineInt();
                System.out.println("Defina o Take Profit: ");
                take_profit = readLineInt();

                if(tradeEssApp.listStocksSale().contains(tradeEssApp.idStockGivenName(stockName))){
                    if(tradeEssApp.profitSaleAvailable(stockName, stop_loss, take_profit)){
                        System.out.println("Venda rentável!!");
                        tradeEssApp.sellPosition(stockName, amount, stop_loss, take_profit, "Sim");
                        System.out.println("Venda efetuada com sucesso!!");
                    }
                    else{
                        System.out.println("Esta venda não é rentável para si, neste momento... Quer continuar, mesmo assim? (Sim | Não)");
                        closeSale = readLine();
                        tradeEssApp.sellPosition(stockName, amount, stop_loss, take_profit, closeSale);
                    }
                }
                else {
                    System.out.println("Não existe!\n");
                }
                continuar();
                traderLoggedIn();
                break;


            case "4": //consultar portefólio
                System.out.println("O seu portefólio: " + tradeEssApp.checkPortfolio());
                continuar();
                break;

            case "5": //apagar conta
                try{
                    tradeEssApp.deleteAccount();
                }
                catch(UtilizadorInexistenteException e){
                    e.printStackTrace();
                    traderLoggedIn();
                }
                continuar();
                traderLoggedIn();
                break;
            case "6": //terminar sessão
                tradeEssApp.terminarSessao();
                MainMenu.showMenu();
                return false;

            default:
                System.out.println("Input não reconhecido...");
                continuar();
                MainMenu.showMenu();
                break;

        }

        return true;
    }
}
