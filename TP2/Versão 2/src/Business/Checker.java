package Business;

import java.util.List;

public class Checker extends Thread {

    private App app;

    public Checker(App app){this.app = app;}

    public void run(){


        do {
            System.out.println("\n> Wait While Stock Prices Are Being Updated...");
            List<MarketStock> lmstk = app.listMarketStocks();

            for (MarketStock mstk : lmstk) {

                float cfd_buy = API.getStockCfdBuy(mstk.getName());
                float cfd_sale = API.getStockCfdSale(mstk.getName());


                if (mstk.getCfd_Buy() != cfd_buy && mstk.getCfd_Sale() == cfd_sale ) {
                    mstk.notifyBuyPositionObservingStock();
                    mstk.setCfd_buy(cfd_buy);
                    app.stockDAO.put(mstk.getId_stock(), mstk);
                }

                if (mstk.getCfd_Sale() != cfd_sale && mstk.getCfd_Buy() == cfd_buy) {
                    mstk.notifySalePositionObservingStock();
                    mstk.setCfd_sale(cfd_sale);
                    app.stockDAO.put(mstk.getId_stock(), mstk);
                }

                if (mstk.getCfd_Sale() != cfd_sale && mstk.getCfd_Buy() != cfd_buy) {
                    mstk.notifyBuyPositionObservingStock();
                    mstk.notifySalePositionObservingStock();
                    mstk.setCfd_buy(cfd_buy);
                    mstk.setCfd_sale(cfd_sale);
                    app.stockDAO.put(mstk.getId_stock(), mstk);
                }

            }
            try {
                System.out.println("> Stock prices updated!\n");
                sleep(1 * 60 * 1000); // update after 2min
            } catch (InterruptedException e) {
                System.out.println("Couldn't update the stock prices... :(");
            }

        } while (true);
    }
}
