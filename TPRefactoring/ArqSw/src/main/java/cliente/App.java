package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;

public class App extends Operacoes{

    private Menu menu;

    Logger log = Logger.getLogger(App.class.getName());


    public App (Socket s) throws IOException {
        this.logado=false;
        menu=  new Menu();
        escreve=new ThreadClienteEscreve(s);
        le=new ThreadClienteLe(s);
        teclado= new BufferedReader(new InputStreamReader(System.in));
        cliente=s;
    }

    public void mostraMenu(){
        if(logado) {
            log.info(menu.getMenuPrincipal());
            handlerMenuPrincipal();
        }
        else {
            log.info(menu.getMenuIncial());
            handlerMenuInicial();
        }
    }


    public void handlerMenuPrincipal(){
        try{
            int op = Integer.parseInt(teclado.readLine());
            switch (op) {
                case 0:
                        terminaSessao();
                        break;
                case 1:
                        consultarSaldo();
                        break;
                case 2:
                        listaAtivos();
                        break;
                case 3:
                        venderContrato();
                        break;
                case 4 :
                        comprarContrato();
                        break;
                case 5 :
                        verPortefolio();
                        break;
                case 6:
                        fecharContrato();
                        break;
                case 7:
                        verRegistos();
                        break;
                case 8:
                        seguirAtivo();
                        break;
                default : log.info("Opção inválida");
            }

            Thread.sleep(2000);
            mostraMenu();
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("Exception");
        }
    }

    public void handlerMenuInicial(){
        try {
                int op = Integer.parseInt(teclado.readLine());
                switch (op) {
                    case 0:
                        fecharApp();
                        break;
                    case 1 :
                        login();
                        Thread.sleep(1000);
                        mostraMenu();
                        break;
                    case 2:
                        registoInput();
                        Thread.sleep(1000);
                        mostraMenu();
                        break;
                    default: break;
                }

        } catch (IOException | InterruptedException  | NumberFormatException e) {
            Thread.currentThread().interrupt();
            log.info("Por favor siga-se pelos números");
            mostraMenu();
        }
    }
}