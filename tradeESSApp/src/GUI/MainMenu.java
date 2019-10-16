package GUI;

import Business.*;

import Exception.*;

public class MainMenu extends GUI{

    /**
     * Método que imprime no ecrã o menu principal
     */
    protected static void showMenu() {
        System.out.println("----- Trade ESS -----");

        System.out.println("1 - Efetuar Registo");
        System.out.println("2 - Iniciar Sessão");
        System.out.println("3 - Consultar Mercado");
        System.out.println("0 - Sair");
        System.out.println("-------------------");

        boolean handler = true;
        while (handler) {
            handler = menuHandler();
        }
    }

    /**
     * Handler do menu principal que recebe e trata os inputs do utilizador.
     * Retorna false quando o utilizador pretende sair do programa, parando o ciclo e a execução do programa
     * @return Boolean
     */
    private static boolean menuHandler() {
        String option = readLine();
        if (option.equals("1")) {
            registerForm();
            showMenu();
        } else if (option.equals("2")) {
            logInForm();
            showMenu();
        } else if (option.equals("3")) {
            //aqui tem que mostrar o stock das cenas
            showMenu();
        } else if (option.equals("0")) {
            terminarSessao();
            return false;
        } else {
            System.out.println("Input não reconhecido.");
        }
        return true;
    }

    private static void logInForm(){
        System.out.println("---- Iniciar Sessão ----");
        System.out.println("Email:");
        String email = readLine();
        System.out.println("Password:");
        String password = readLine();
        tryLogIn(email, password);
    }

    private static void tryLogIn(String email, String password) {
        try {
            iniciarSessao(email, password);
            System.out.println("Sessão iniciada com sucesso.");
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

    protected static void loggedIn() {
        logInUser();
        User utilizador = getUser();
        boolean isLoggedIn = true;
        if(utilizador instanceof Admin){
            while (isLoggedIn){
                isLoggedIn = GUIAdmin.adminLoggedIn();
            }
        }
        else if(utilizador instanceof Trader){
            while (isLoggedIn) {
                isLoggedIn = GUITrader.traderLoggedIn();
            }
        }
    }

    private static void traderLoggedIn(Trader t) {
        System.out.println("---- Bem-vindo, negociante! ----");
        System.out.println("Nome: " + t.getUsername());
        System.out.println("Saldo:" +t.getSaldoConta());
        System.out.println("--------------------");
        System.out.println("1 - Consultar e gerir saldo");
        System.out.println("2 - Comprar posição");
        System.out.println("3 - Vender posição");
        System.out.println("4 - Consultar Portefólio");
        System.out.println("5 - Apagar Conta");
        System.out.println("6 - Terminar sessão");
        System.out.println("0 - Sair");
        System.out.println("--------------------");
    }

    private static void adminLoggedIn() {
        System.out.println("---- Bem-vindo, Administrador -----");
        System.out.println("1 - Consultar Stocks");
        System.out.println("2 - Expulsar Utilizador");
        System.out.println("0 - Sair");
        System.out.println("-------------------------");
    }

    private static void logInUser() {
        User utilizador = getUser();
        if(utilizador instanceof Admin){
            adminLoggedIn();
        }
        else if(utilizador instanceof Trader){
            traderLoggedIn((Trader) utilizador);
        }
    }

    /**
     * Método que imprime no ecrã o formulário de registo
     */
    private static void registerForm() {
        System.out.println("---- Efetuar Registo ----");
        System.out.println("Insira o seu email:");
        String email = readLine();
        System.out.println("Insira o seu username:");
        String username = readLine();
        System.out.println("Insira a sua password:");
        String password = readLine();
        System.out.println("Insira a sua morada:");
        String morada = readLine();
        System.out.println("Insira a sua idade:");
        int idade = readLineInt();

        if(idade >=18){
            System.out.println("Idade válida!");
        }
        else{
            System.out.println("Idade inválida!");
            terminarSessao();
        }

        System.out.println("Insira o seu contacto telefónico:");
        int contacto = readLineInt();
        int length = String.valueOf(contacto).length();
        if(length != 9){
            System.out.println("Número de contacto inválido!");
            terminarSessao();
        }
        tryRegistarTrader(email, username, password, morada, idade, contacto, 0);
        System.out.println("Deseja iniciar sessão? (Sim | Não)");
        String resposta = readLine();
        if(resposta.equalsIgnoreCase("sim")){
            try{
                iniciarSessao(username, password);
            }
            catch (UtilizadorInexistenteException | PasswordIncorretaException e){
                e.printStackTrace();
            }
        }
        else showMenu();
    }

    private static void tryRegistarTrader(String email, String username, String password, String morada, int idade, int contacto, float saldoConta) {
        try {
            registarTrader(email, username, password, morada, idade, contacto, saldoConta);
            System.out.println("Utilizador registado com sucesso!");
            continuar();
        } catch (TraderRegistadoException e) {
            System.out.println("Utilizador já se encontra registado!");
            continuar();
        }
    }
}
