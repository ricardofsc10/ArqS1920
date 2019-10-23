package GUI;
import Business.Stock;
import Data.StockDAO;

import java.sql.SQLException;
import java.time.format.DateTimeParseException;

public class GUIAdmin extends GUI {

    private static StockDAO stockDAO;

    protected static boolean adminLoggedIn()throws SQLException, Exception{
        String opcao = readLine();
        if (opcao.equals("1")) {
            registarNewStock();
            continuar();
            MainMenu.loggedIn();
        } else if (opcao.equals("2")) {
            removerStocks();
            continuar();
            MainMenu.loggedIn();
        } else if (opcao.equals("0")) {
            terminarSessao(getUser());
            return false;
        } else {
            System.out.println("Input não reconhecido. ");
            MainMenu.loggedIn();
        }
        return true;
    }

    private static void removerStocks() {

    }

    private static void registarNewStock() {
        System.out.println("---- Registar Novo Stock/Ativo ----");
        System.out.println("Escolha um nome:");
        String nome = readLine();
        System.out.println("Indique a que empresa pertence:");
        String empresa = readLine();
        System.out.println("Indique o preço de compra:");
        float compra = readLineFloat();
        System.out.println("Indique o preço de venda:");
        float venda = readLineFloat();

        tryRegistarStock(nome, empresa, compra, venda);
    }

    private static void tryRegistarStock(String nome, String empresa, float precoCompra, float precoVenda) {
        try {
            Business.Stock s = new Stock();
            s.setName(nome);
            s.setOwner(empresa);
            s.setCfdBuy(precoCompra);
            s.setCfdSale(precoVenda);
            registarStock(s);
            System.out.println("Stock Registado!");
        } catch (Exception e) {
            System.out.println("Registo Cancelado.");
            return;
        }
    }
}
