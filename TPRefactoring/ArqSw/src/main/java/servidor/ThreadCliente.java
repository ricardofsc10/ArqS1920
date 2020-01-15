package servidor;

import java.nio.channels.AsynchronousSocketChannel;
import java.util.List;
import java.util.logging.Logger;


public class ThreadCliente {
    private Utilizador user;
    private ESSLtd ess;
    private Comunicacao comunicacao;
    private static String terminar = "TERMINAR";
    Logger log = Logger.getLogger(ThreadCliente.class.getName());

    public ThreadCliente(AsynchronousSocketChannel s, ESSLtd ess){
        this.user = null;
        this.ess= ess;
        this.comunicacao = new Comunicacao(s,this);
    }

    public boolean confirmar(String pedido){
        String[] campos = pedido.split(" ");
        boolean result = false;
        if (!(campos[0].equals("SESSAO") || campos[0].equals("REGISTAR") || campos[0].equals(terminar)))
            result = true;
        return result;
    }

    public void enviarNotificacao(List<String> not){
            comunicacao.adicionaQueue("**********NOTIFICAÇÃO*******");
            for (String s : not)
                comunicacao.adicionaQueue(s);
            comunicacao.send();
    }

    public void enviarPedidosPendentes(){
        int tamanho = user.getPedidosSave().size();
        int i = 0;
        if (tamanho > 0) {
            comunicacao.adicionaQueue("O servidor foi abaixo mas o seus pedidos foram salvos.");
            comunicacao.send();
            while (i < tamanho) {
                proximoPedido();
                i++;
            }
        }
    }



    public void processaPedido(String pedido) {
        if (confirmar(pedido)){
            int size = ess.sizePedidos() + 1;
            Pedido p = new Pedido();
            p.set(pedido, false, size, user.getId());
            ess.updateEstadoPedido(p);
            user.setPedido(p);
            proximoPedido();
            /**********************NOVO REQUISITO***********/
            List<String> not = ess.veNotificacoes(user);
            if(not.isEmpty()) {
                enviarNotificacao(not);
            }
            /**********************************************/
        } else {
            String resposta = interpretaPedido(pedido);
            comunicacao.adicionaQueue(resposta);
            comunicacao.send();
            if (user != null) {
                enviarPedidosPendentes();
            }
        }
    }


    public String interpretaPedido(String pedido)  {
        String [] campos;
        campos = pedido.split(" ");
        switch (campos[0]) {
            case "SESSAO":
                return iniciarSessao(campos[1], campos[2]);
            case "REGISTAR":
                return registarUtilizador(campos[1], campos[2], campos[3]);
            case "SALDO":
                return saldoUtilizador(user);
            case "LISTARATIVOS":
                return listaAtivos();
            case "CONTRATOVENDA":
                return criarContratoVenda(campos[1],campos[2],campos[3],campos[4]);
            case "CONTRATOCOMPRA":
                return criarContratoCompra(campos[1],campos[2],campos[3],campos[4]);
            case "PORTEFOLIO":
                return verPortefolio();
            case "FECHARCONTRATO":
                return fecharContrato(campos[1]);
            case "REGISTOS":
                return verRegistos();
            case "TERMINAR":
                return terminarSessao();

/****************** NOVO REQUISITO*************/
            case "SEGUIRATIVO":
                return seguirAtivo(campos[1]);
            default:
                return pedido + " NAO É UM COMANDO VÁLIDO\n";
        }
    }



    public String iniciarSessao(String username,String password ){
        boolean sucess = false;
        String result;
        try {
            user= ess.iniciarSessao(username,password);
            sucess=true;

        } catch (UtilizadorInvalidoException e) {
            log.info(e.getMessage());
        }
        finally {
            if(sucess)
                result = "SUCESSO";
            else
                result = "Login não efetuado";
        }
        return result;

    }

    public String registarUtilizador(String username,String password,String saldo ) {
        boolean sucess=false;
        String result;
        try {
            int plafom = Integer.parseInt(saldo);
            ess.registarUtilizador(username,password,plafom);
            sucess=true;

        } catch (UsernameInvalidoException e) {
            log.info(e.getMessage());

        }
        finally {
            if(sucess)
                result = "UTILIZADOR REGISTADO COM SUCESSO";
            else
                result= "Username ou saldo invalido";
        }
        return result;
    }

    public String saldoUtilizador(Utilizador u){
        float saldo= ess.saldo(u);
        String plafom = Float.toString(saldo);
        return  "O seu saldo : "+ plafom;
    }
    public String listaAtivos(){
        List<Ativo> ativos = ess.listarAtivos();
        StringBuilder sb = new StringBuilder();
        sb.append("  Idenficação  | Nome  | preco de compra  | preco de um venda\n");
        for(Ativo a : ativos) {
            sb.append(a);
            sb.append(" ");
        }
        return sb.toString();
    }

    public String criarContratoVenda(String idAtivo,String tkp,String stl,String quant){
        boolean sucess = false;
        String result = null;
        try {
            int idativo = Integer.parseInt(idAtivo);
            float tk = Float.parseFloat(tkp);
            float sl = Float.parseFloat(stl);
            int quantidade = Integer.parseInt(quant);

            ess.criarContratoVenda(this.user, idativo, tk, sl, quantidade);
            sucess = true;

        } catch (AtivoInvalidoException | SaldoInsuficienteException e) {
            log.info(e.getMessage());
        } finally {
            if (sucess)
                result = "Contrato criado com sucesso";
            else
                result = "Dados Inválidos ou saldo insuficiente";

        }
        return result;
    }


    public String criarContratoCompra(String idAtivo,String tkp,String stl,String quant){
        boolean sucess = false;
        String result = null;
        try {
            int idativo = Integer.parseInt(idAtivo);
            float tk = Float.parseFloat(tkp);
            float sl = Float.parseFloat(stl);
            int quantidade = Integer.parseInt(quant);

            ess.criarContratoCompra(this.user, idativo, tk, sl, quantidade);
            sucess = true;

        } catch (AtivoInvalidoException | SaldoInsuficienteException e) {
            log.info(e.getMessage());
        } finally {
            if (sucess)
                result = "Contrato criado com sucesso";
            else
                result="Dados Inválidos ou saldo insuficiente";

        }
        return result;
    }


    public String verPortefolio(){
        List<Contrato> contratos= ess.listarPortefolio(this.user);
        StringBuilder sb = new StringBuilder();
        sb.append("         PORTEFOLIO         \n");
        for(Contrato c : contratos) {
            sb.append(c);
            sb.append("\n");
        }
        return sb.toString();
    }


    public String fecharContrato(String idContrato){
        boolean sucess= false;
        String result = null;
        try{
            int idcontrato = Integer.parseInt(idContrato);
            ess.fecharContrato(this.user,idcontrato);
            sucess =true;
        } catch (ContratoInvalidoException e) {
            log.info(e.getMessage());
        } finally {
            if (sucess)
                result ="Contrato fechado com sucesso";
            else
                result= "Id invalido ";
        }
        return result;
    }

    public String verRegistos(){
        List<Registo> registos = ess.listaRegistos(this.user);
        StringBuilder sb = new StringBuilder();
        sb.append("         REGISTOS         \n");
        for(Registo r : registos) {
            sb.append(r);
            sb.append("\n");
        }
        return sb.toString();
    }

    public String terminarSessao(){
        this.user=null;
        return "TERMINADA";
    }


    public void proximoPedido() {
        if (user != null) {
            Pedido p = user.getPedidoAtual();
            if (p != null) {
                String req = p.getEstado().getPedido();
                String resposta = interpretaPedido(req);
                comunicacao.adicionaQueue(resposta);
                comunicacao.send();
                p.getEstado().setEstado(true);
                ess.updateEstadoPedido(p);
                //out.println(resposta);//VERSAO SINCRONA
                if (!req.equals(terminar)) {
                    user.pedidoFinalizado();
                }
            }
        }
    }

/******************** NOVO REQUISITO ****************/
    private String seguirAtivo(String idAtivo) {
        boolean sucess = false;
        String result = null;
        try {
            int idativo = Integer.parseInt(idAtivo);
            ess.seguirAtivo(user,idativo);
            sucess= true;
        }finally {
            if (sucess)
                result= "Ativo seguido com sucesso";
            else
                result= "Id invalido ";
        }
        return result;
    }

}




