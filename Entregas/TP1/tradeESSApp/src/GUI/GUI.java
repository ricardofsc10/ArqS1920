package GUI;

import Business.*;
import Exception.*;
import com.jimmoores.quandl.*;
import com.jimmoores.quandl.classic.ClassicQuandlSession;

import java.sql.SQLException;
import java.util.*;
import java.io.*;

public class GUI {

    public static TradeEssApp tradeEssApp = new TradeEssApp();

    public static void main(String[] args) throws SQLException,Exception {
        MainMenu.showMenu();
    }


    /**
     * Método que lê uma linha do teclado (System.in)
     * @return Linha lida do teclado
     */
    protected static String readLine(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    protected static int readLineInt(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    protected static float readLineFloat(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextFloat();
    }

    /**
     * Método que tentar iniciar sessão com os parâmetros que lhe são passados.
     * Caso o utilizado não seja registado, ou a password seja incorreta, é enviada uma Exceção.
     * @param username
     * @param password
     * @throws UtilizadorInexistenteException
     * @throws PasswordIncorretaException
     */
    protected static void iniciarSessao(String username, String password) throws Exception {
        tradeEssApp.iniciarSessao(username,password);
    }
    /**
     * Método que regista um novo trader no sistema.
     * Lança uma exceção caso o email utilizado já esteja registado no sistema.
     * @param email
     * @param password
     * @param username
     * @throws TraderRegistadoException
     */
    protected static void registarTrader(String email, String username, String password, String morada, int idade, int contacto, float saldoConta) throws TraderRegistadoException{
        tradeEssApp.registerTrader(email, username, password, morada, idade, contacto, 0);
    }

    /**
     * Método que termina a sessão no sistema
     */
    protected static void terminarSessao(User u){
        tradeEssApp.terminarSessao(u);
    }

    /**
     * Método que retorna o utilizador que está, atualmente, a usar o sistema
     * @return Utilizador que está a usar o sistema
     */
    protected static User getUser(){
        return tradeEssApp.getUser();
    }


    /**
     * Método que dá stop na execução do programa, até ser premida qualquer tecla no teclado.
     */
    protected static void continuar() {
        System.out.println("Prima qualquer tecla para continuar");
        readLine();
    }

    protected static void showStocks(){
        List<Stock> stock = getStock();
        System.out.println("---- Lista Stocks----");
        for(Stock s : stock)
            System.out.println(s);
    }

    protected static List<Stock> getStock(){
        return tradeEssApp.listaStocks();
    }

    protected static void registarStock(Stock s) throws StockExistenteException {
        tradeEssApp.registarStock(s);
    }


}
