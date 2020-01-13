package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;

public class App {

    private boolean logado;
    private Menu menu;
    private ThreadClienteEscreve escreve;
    private ThreadClienteLe le;
    BufferedReader teclado;
    private Socket cliente;
    private String string = "Id do ativo";
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
            String pedido = teclado.readLine();
            int op = Integer.parseInt(pedido);
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
                case 6: fecharContrato();
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
          String pedido=  teclado.readLine();
                int op = Integer.parseInt(pedido);
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

    public void registoInput() throws IOException {
        StringBuilder sb = new StringBuilder();
        String pedido = "REGISTAR";
        sb=sb.append(pedido);
        sb.append(" ");
        log.info("Username");
        String username= teclado.readLine();
        sb.append(username);
        sb.append(" ");
        log.info("Password");
        String password= teclado.readLine();
        sb.append(password);
        sb.append(" ");
        log.info("Saldo inicial");
        String saldo= teclado.readLine();
        sb.append(saldo);
        escreve.addPedido(sb.toString());
        String resposta= le.leResposta();
        log.info(resposta);




    }

    public void login () throws IOException {
        StringBuilder sb = new StringBuilder();
        String pedido = "SESSAO";
        sb=sb.append(pedido);
        sb.append(" ");
        log.info("Username");
        String username= teclado.readLine();
        sb.append(username);
        sb.append(" ");
        log.info("Password");
        String password= teclado.readLine();
        sb.append(password);
        sb.append(" ");
        escreve.addPedido(sb.toString());
      String resposta= le.leResposta();
        log.info(resposta);
      if(resposta.equals("SUCESSO")) {
          this.logado = true;
          Thread t = new Thread(le);
        t.start();
      }

    }

    public void fecharApp() throws IOException {
        logado=false;
        cliente.close();

    }

    public void  terminaSessao() throws InterruptedException {
        logado=false;
        StringBuilder sb = new StringBuilder();
        String pedido = "TERMINAR";
        sb=sb.append(pedido);
        escreve.addPedido(sb.toString());


    }

    public void consultarSaldo() throws InterruptedException {
        StringBuilder sb = new StringBuilder();
        String pedido = "SALDO";
        sb=sb.append(pedido);
        escreve.addPedido(sb.toString());

    }

    public void listaAtivos(){
        StringBuilder sb = new StringBuilder();
        String pedido = "LISTARATIVOS";
        sb=sb.append(pedido);
        escreve.addPedido(sb.toString());

    }

    public void venderContrato() throws IOException {
        StringBuilder sb = new StringBuilder();
        String pedido = "CONTRATOVENDA";
        sb=sb.append(pedido);
        sb.append(" ");
        log.info(string);
        String idAtivo= teclado.readLine();
        sb.append(idAtivo);
        sb.append(" ");
        log.info("Take Profit");
        String takeprofit= teclado.readLine();
        sb.append(takeprofit);
        sb.append(" ");
        log.info("StopLoss");
        String stopLoss= teclado.readLine();
        sb.append(stopLoss);
        sb.append(" ");
        log.info("Quantidade a adquirir");
        String quantidade= teclado.readLine();
        sb.append(quantidade);
        sb.append(" ");
        escreve.addPedido(sb.toString());
    }

    public void comprarContrato() throws IOException {
        StringBuilder sb = new StringBuilder();
        String pedido = "CONTRATOCOMPRA";
        sb=sb.append(pedido);
        sb.append(" ");
        log.info(string);
        String idAtivo= teclado.readLine();
        sb.append(idAtivo);
        sb.append(" ");
        log.info("Take Profit");
        String takeprofit= teclado.readLine();
        sb.append(takeprofit);
        sb.append(" ");
        log.info("StopLoss");
        String stopLoss= teclado.readLine();
        sb.append(stopLoss);
        sb.append(" ");
        log.info("Quantidade a adquirir");
        String quantidade= teclado.readLine();
        sb.append(quantidade);
        sb.append(" ");
        escreve.addPedido(sb.toString());


    }

    public void verPortefolio()  {
        StringBuilder sb = new StringBuilder();
        String pedido = "PORTEFOLIO";
        sb = sb.append(pedido);
        escreve.addPedido(sb.toString());
    }

    public void fecharContrato() throws IOException {
        StringBuilder sb = new StringBuilder();
        String pedido = "FECHARCONTRATO";
        sb=sb.append(pedido);
        sb.append(" ");
        log.info("Id do Contrato");
        String idContrato= teclado.readLine();
        sb.append(idContrato);
        sb.append(" ");
        escreve.addPedido(sb.toString());

    }
    public void verRegistos() {
        StringBuilder sb = new StringBuilder();
        String pedido = "REGISTOS";
        sb = sb.append(pedido);
        escreve.addPedido(sb.toString());
    }


/***************** NOVO REQUISITO **************/

public void seguirAtivo() throws IOException {
    StringBuilder sb = new StringBuilder();
    String pedido = "SEGUIRATIVO";
    sb=sb.append(pedido);
    sb.append(" ");
    log.info("Id do ativo");
    String idAtivo= teclado.readLine();
    sb.append(idAtivo);
    sb.append(" ");
    escreve.addPedido(sb.toString());
}


}
