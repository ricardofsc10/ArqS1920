package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

public class Operacoes{
    protected boolean logado;
    protected ThreadClienteEscreve escreve;
    protected ThreadClienteLe le;
    protected Socket cliente;
    BufferedReader teclado;
    String string = "Id do ativo";

    Logger log = Logger.getLogger(Operacoes.class.getName());

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
    public void login() throws IOException {
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
        this.logado=false;
        cliente.close();

    }

    public void  terminaSessao() {
        this.logado=false;
        StringBuilder sb = new StringBuilder();
        String pedido = "TERMINAR";
        sb=sb.append(pedido);
        escreve.addPedido(sb.toString());
    }

    public void consultarSaldo(){
        StringBuilder sb = new StringBuilder();
        String pedido = "SALDO";
        sb=sb.append(pedido);
        escreve.addPedido(sb.toString());
    }

    public void listaAtivos(){
        StringBuilder sb = new StringBuilder();
        String pedido = "LISTAR ATIVOS";
        sb=sb.append(pedido);
        escreve.addPedido(sb.toString());
    }

    public void venderContrato() throws IOException {
        StringBuilder sb = new StringBuilder();
        String pedido = "CONTRATO VENDA";
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
        String pedido = "SEGUIR ATIVO";
        sb=sb.append(pedido);
        sb.append(" ");
        log.info(string);
        String idAtivo= teclado.readLine();
        sb.append(idAtivo);
        sb.append(" ");
        escreve.addPedido(sb.toString());
    }
}
