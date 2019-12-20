package Business;

public interface Observable {
    void addBuyPositionObservingStock(ObserverPosition buy_obs);
    void notifyBuyPositionObservingStock();
    void removeBuyPositionObservingStock(ObserverPosition buy_obs);

    void addUserObservingStock(ObserverUser user_obs);
    void notifyUserObservingStock();
    void removeUserObservingStock(ObserverUser user_obs);

    void addSalePositionObservingStock(ObserverPosition sale_obs);
    void notifySalePositionObservingStock();
    void removeSalePositionObservingStock(ObserverPosition sale_obs);
}
