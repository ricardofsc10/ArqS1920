package Business;

import yahoofinance.YahooFinance;
import yahoofinance.Stock;

import java.io.IOException;

public class API {

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
}
