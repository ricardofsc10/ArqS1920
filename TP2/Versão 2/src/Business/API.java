package Business;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;

public class API {
    public static float getPrice(String marketstock){
        float prc = -1;
        try {
            Stock s = YahooFinance.get(marketstock);
            prc = s.getQuote().getPrice().floatValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prc;
    }

    public static float getStockCfdBuy(String marketstock){
        float cfb = -1;
        try {
            Stock s = YahooFinance.get(marketstock);
            cfb = s.getQuote().getAsk().floatValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cfb;
    }

    public static float getStockCfdSale(String marketstock){
        float cfs = -1;
        try {
            Stock s = YahooFinance.get(marketstock);
            cfs = (s.getQuote().getBid()).floatValue();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cfs;
    }


    public static String getStockCompany(String marketstock){
        String stkowner = null;
        try{
            Stock s = YahooFinance.get(marketstock);
            stkowner = s.getName();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return stkowner;
    }


    public static String getStockName(String marketstock){
        String stkname = null;
        try{
            Stock s = YahooFinance.get(marketstock);
            stkname = s.getSymbol();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return stkname;
    }
}
