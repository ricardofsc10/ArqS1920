package GUI;

import Business.TradeEssApp;
import Business.Trader;
import Business.User;
import Data.UserDAO;

import java.sql.SQLException;

public class GUISaldo extends GUI {

    private User user;

    /**
     * Método que imprime no ecrã o menu de gestão do Saldo de um Apostador
     */
    protected static void gerirSaldoScene() throws SQLException, Exception {
        System.out.println("--- Gerir Saldo ---");
        System.out.println("1 - Ver saldo");
        System.out.println("2 - Depositar Dinheiro");
        System.out.println("0 - Sair");
        System.out.println("-------------------");
    }

    /**
     * Método que recebe e trata os inputs relativos ao menu de gestão de saldo
     */
    protected static void gerirSaldoHandler() throws SQLException, Exception {
        while (true) {
            String opcao = readLine();
            if (opcao.equals("1")) {
                System.out.println(checkSaldo(getUser()));
                continuar();
                gerirSaldoScene();
            }
            else if (opcao.equals("2")) {
                depositarDinheiro();
                continuar();
                gerirSaldoScene();
            }
            else if (opcao.equals("0")) {
                break;
            } else {
                System.out.println("Input não reconhecido!");
                continuar();
                gerirSaldoScene();
            }
        }
    }

    public static float checkSaldo(User u) throws SQLException, Exception{
        return tradeEssApp.checkSaldo(u);
    }

    /**
     * Método que permite ao trader depositar dinheiro no sistema
     */
    private static void depositarDinheiro() throws SQLException, Exception {
        Trader atual = (Trader) getUser();
        System.out.println("Qual a quantia que quer depositar?");
        int quantia = readLineInt();
        atual.addMoney(quantia);
        System.out.println(atual.getSaldoConta());
        ((Trader) atual).setSaldoConta(atual.getSaldoConta());
    }
}
