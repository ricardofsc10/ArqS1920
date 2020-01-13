package servidor;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Logger;

class UpdateAtivo extends Thread{

    private ESSLtd ess ;
    private String nomeAtivo;
    Logger log = Logger.getLogger(UpdateAtivo.class.getName());

    public UpdateAtivo(ESSLtd ess,String nomeAtivo) {
        this.ess = ess;
        this.nomeAtivo=nomeAtivo;
    }
    @Override
        public void run(){
        Ativo ativo;
        BigDecimal  zero= new BigDecimal("0.0");
            try {
        while(true) {
            ativo=ess.getAtivo(nomeAtivo);
            float compra;
            float venda;
            Stock stock = null;
            stock = YahooFinance.get(nomeAtivo);
            BigDecimal precoVenda = stock.getQuote().getBid();
            BigDecimal precoCompra = stock.getQuote().getAsk();
            if(precoCompra!=null && precoCompra.compareTo(zero)!=0)
                compra = precoCompra.floatValue();
            else
                compra = ativo.getPrecoCompra();
            if(precoVenda!=null &&  precoVenda.compareTo(zero)!= 0)
                venda = precoVenda.floatValue();
            else
                venda=ativo.getPrecoVenda();

            ativo.setValores(venda,compra);
            ess.getAtivos().put(ativo.getId(),ativo);
            sleep(2000);

        }
        } catch (IOException | InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info(e.getMessage()); }

    }

}

public class RealTime {

    private ESSLtd ess;

    public RealTime(ESSLtd ees) {
        this.ess = ees;
    }
    public void update() throws IOException {
       ess.criarAtivo("INTC",1);
       ess.criarAtivo("TSLA",2);
       ess.criarAtivo("BABA",3);
       ess.criarAtivo("GOOG",4);
       ess.criarAtivo("NVDA",5);

       UpdateAtivo up1 = new UpdateAtivo(ess,"INTC");
       UpdateAtivo up2 = new UpdateAtivo(ess,"TSLA");
       UpdateAtivo up3 = new UpdateAtivo(ess,"BABA");
       UpdateAtivo up4 = new UpdateAtivo(ess,"GOOG");
       UpdateAtivo up5 = new UpdateAtivo(ess,"NVDA");
       up1.start();
       up2.start();
       up3.start();
       up4.start();
       up5.start();

    }


// Alteracao no metodo UpdateAtivo porque temos que instanciar ESS_ltd no contrato e estes sao instanciados como Observers no ativo
    // logo é preciso ir ao DAO para associar ESS_ltd aos contratos


    }

