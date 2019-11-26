package GUI;

import Business.*;
import Exceptions.UserAlreadyExistsException;

import java.sql.SQLException;
import java.util.Scanner;

public class GUI {

    public static Trader trader = new Trader();

    public static void showMenu() throws SQLException, UserAlreadyExistsException, Exception {
        Menu.showMenu();
    }

    /**
     * Método que dá stop na execução do programa, até ser premida qualquer tecla no teclado.
     */
    public static void continuar() {
        System.out.println("Prima qualquer tecla para continuar");
        readLine();
    }

    /**
     * Método que lê uma linha do teclado (System.in)
     * @return Linha lida do teclado
     */
    public static String readLine(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static int readLineInt(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    protected static float readLineFloat(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextFloat();
    }
    public static Double readLineDouble(){
        Scanner scanner = new Scanner(System.in);
        return scanner.nextDouble();
    }

    protected static void signUp(String email, String username, String name, String password, int age, double plafond) throws UserAlreadyExistsException{
        trader.signUp(email, username, name, password, age, plafond);
    }

    protected static User getUser(){
        return trader.getUser();
    }
}
