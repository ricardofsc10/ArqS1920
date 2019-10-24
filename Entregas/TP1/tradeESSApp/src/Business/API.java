package Business;

import yahoofinance.YahooFinance;
import yahoofinance.Stock;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class API extends Thread{

    private TradeEssApp tradeEssApp;

    public API(TradeEssApp tradeEssApp){
        this.tradeEssApp = tradeEssApp;
    }

    public static float getStkCfdBuy(String stock){
        float cfb = -1;
        try {
            Stock s = YahooFinance.get(stock);
            cfb = s.getQuote().getAsk().floatValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cfb;
    }

    public static float getStkCfdSale(String stock){
        float cfs = -1;
        try {
            Stock s = YahooFinance.get(stock);
            cfs = (s.getQuote().getBid()).floatValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cfs;
    }


    public static String getStkCompany(String stock){
        String stkowner = null;
        try{
            Stock s = YahooFinance.get(stock);
            stkowner = s.getName();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return stkowner;
    }


    public static String getStkName(String stock){
        String stkname = null;
        try{
            Stock s = YahooFinance.get(stock);
            stkname = s.getSymbol();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return stkname;
    }

    public void creation(){
        tradeEssApp.createStock("NVDA");
        tradeEssApp.createStock("APPL");
    }

    public void run(){
        creation();

        while(true){
            List<Business.Stock> stocks = tradeEssApp.listaStocks();
            for(Business.Stock s : stocks){
                float cfdBuy = getStkCfdBuy(s.getName());
                float cfdSale = getStkCfdSale(s.getName());
                Set<Position> positionBuy = tradeEssApp.listarPositionBuy(s.getIdStock());
                Set<Position> positionSale = tradeEssApp.listarPositionSale(s.getIdStock());

                if(s.getCfdBuy() != cfdBuy){
                    for(Position p : positionBuy){
                        try{
                            tradeEssApp.buyPosition(s.getName(), p.getAmount(), p.getStop_loss(), p.getTake_profit(), p.getStatus());
                        } catch (Exception e) {
                            System.out.println("Sem dinheiro suficiente :(");
                        }
                    }
                    if(s.getCfdSale() != cfdSale){
                        for(Position p : positionSale){
                            tradeEssApp.sellPosition(s.getName(), p.getAmount(), p.getStop_loss(), p.getTake_profit(), p.getStatus());
                        }
                    }
                    s.setCfdBuy(cfdBuy);
                    s.setCfdSale(cfdSale);
                    tradeEssApp.getStocks().put(s.getIdStock(), s);
                }
            }
            try{
                sleep(300000);
            }
            catch (InterruptedException e){
                e.getMessage();
            }
        }
    }

}
