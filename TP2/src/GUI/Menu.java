package GUI;

import Business.Trader;
import Business.User;
import Data.UserDAO;
import Exceptions.PasswordIncorretaException;
import Exceptions.UserAlreadyExistsException;
import Exceptions.UtilizadorInexistenteException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import Data.*;

public class Menu extends GUI {

    private static void apagaecra(){
        System.out.println("\f");
    }

    /**
     * Método que imprime no ecrã o menu principal
     */
    public static void showMenu() throws Exception {

        System.out.println("----- ESSTrading App -----");

        System.out.println("1 - Login");
        System.out.println("2 - Sign up");
        System.out.println("0 - Sair");
        System.out.println("-------------------");

        boolean handler = true;
        while (handler) {
            handler = menuHandler();
        }
    }

    private static boolean menuHandler() throws Exception {
        String option = readLine();
        if (option.equals("1")){
            logInForm();
        }
        else if(option.equals("2")){
            registerForm();
        }
        else if(option.equals("0")){
            logOut();
            return false;
        }
        else {
            System.out.println("Please, enter a valid number...");
        }
        return true;
    }

    private static void logInForm() throws Exception {
        apagaecra();
        System.out.println("---- LOGIN ----");
        boolean actual = true;

        while(actual){
            System.out.println("Username:");
            String username = readLine();
            System.out.println("Password:");
            String password = readLine();
            tryLogIn(username, password);
        }
    }

    private static void registerForm() throws Exception {
        System.out.println("---- Efetuar Registo ----");
        System.out.println("Enter your e-mail:");
        String email = readLine();
        System.out.println("Enter your username:");
        String username = readLine();
        System.out.println("Enter your Name:");
        String name = readLine();
        System.out.println("Enter your password:");
        String password = readLine();
        System.out.println("Enter your age:");
        int age = readLineInt();

        if(age >=18){
            System.out.println("Valid age!");
        }
        else{
            System.out.println("Invalid age!");
            logOut();
        }
        System.out.println("Enter your plafond:");
        Double plafond = readLineDouble();

        trySignUp(email, username, name, password, age, plafond);
        System.out.println("Deseja iniciar sessão? (Sim | Não)");
        String resposta = readLine();
        if(resposta.equalsIgnoreCase("sim")){
            try{
                trader.logIn(username, password);
            }
            catch (UtilizadorInexistenteException | PasswordIncorretaException | SQLException e){
                e.printStackTrace();
            }
        }
        else showMenu();

    }


    private static void trySignUp(String email, String username, String name, String password, int age, double plafond){
        try{
            signUp(email, username, name, password, age, plafond);
            System.out.println("Utilizador registado com sucesso!");
            continuar();
        }
        catch (UserAlreadyExistsException u){
            u.printStackTrace();
        }
    }

    public static void logOut(){
        System.out.println("See ya, " + trader.getUsernamee());
    }

    private static void tryLogIn(String username, String password) throws Exception {
        try {
            trader.logIn(username, password);
            continuar();
            loggedIn();
        } catch (PasswordIncorretaException e) {
            System.out.println("Password Incorreta.");
            continuar();
        } catch (UtilizadorInexistenteException e) {
            System.out.println("Utilizador Inexistente.");
            continuar();
        }
    }

    protected static void loggedIn() throws Exception {
        MenuUser.loggedInGUI();
        boolean isLoggedIn = true;
        while (isLoggedIn) {
            isLoggedIn = MenuUser.loggedIn();
        }
    }
}
