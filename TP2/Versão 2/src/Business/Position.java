package Business;

public class Position implements Comparable<Position>, ObserverPosition{
    //------------------------------------ INSTANCE VARIABLES -----------------------------------------------
    private App app;
    private int idPosition;
    private String type;
    private int idUser;
    private int idMarketStock;
    private int amount;
    private int units_remaining;
    private float stop_loss;
    private float take_profit;
    private String status;
    private float deal_value;
    private String deal_date;
    //--------------------------------------------------------------------------------------------------------

    //------------------------------------ CONSTRUCTORS -----------------------------------------------------

    public Position(App app){
        this.app = app;
    }

    public Position(int idPosition, String type, int idUser, int idMarketStock, int amount, int units_remaining, float stop_loss, float take_profit, String status, float deal_value, String deal_date){
        this.idPosition = idPosition;
        this.type = type;
        this.idUser = idUser;
        this.idMarketStock = idMarketStock;
        this.amount = amount;
        this.units_remaining = units_remaining;
        this.stop_loss = stop_loss;
        this.take_profit = take_profit;
        this.status = status;
        this.deal_value = deal_value;
        this.deal_date = deal_date;
    }


    public Position(Position pst){
        this.idPosition = pst.getIdPosition();
        this.type = pst.getType();
        this.idUser = pst. getidUser();
        this.idMarketStock = pst.getMarketstock_id();
        this.amount = pst.getAmount();
        this.units_remaining = pst.getUnits_remaining();
        this.stop_loss = pst.getStop_loss();
        this.take_profit = pst.getTake_profit();
        this.status = pst.getStatus();
        this.deal_value = pst.getDeal_value();
        this.deal_date = pst.getDeal_date();
    }

    public Position(){
        this.idPosition = -1;
        this.type = "";
        this.idUser = -1;
        this.idMarketStock = -1;
        this.amount = 0;
        this.units_remaining = 0;
        this.stop_loss = 0;
        this.take_profit = 0;
        this.status = "";
        this.deal_value = 0;
        this.deal_date = "00-00-0000";
    }
    //---------------------------------------------------------------------------------------------------------


    //---------------------------------------- SETTERS & GETTERS ----------------------------------------------

    public int getIdPosition() {
        return idPosition;
    }

    public void setIdPosition(int idPosition) {
        this.idPosition = idPosition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getidUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getMarketstock_id() {
        return idMarketStock;
    }

    public void setMarketstock_id(int idMarketStock) {
        this.idMarketStock = idMarketStock;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getUnits_remaining() {
        return units_remaining;
    }

    public void setUnits_remaining(int units_remaining) {
        this.units_remaining = units_remaining;
    }

    public float getStop_loss() {
        return stop_loss;
    }

    public void setStop_loss(float stop_loss) {
        this.stop_loss = stop_loss;
    }

    public float getTake_profit() {
        return take_profit;
    }

    public void setTake_profit(float take_profit) {
        this.take_profit = take_profit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getDeal_value() {
        return deal_value;
    }

    public void setDeal_value(float deal_value) {
        this.deal_value = deal_value;
    }

    public String getDeal_date() {
        return deal_date;
    }

    public void setDeal_date(String deal_date) {
        this.deal_date = deal_date;
    }
    //------------------------------------------------------------------------------------------------

    /**
     * Método clone
     * @return Position
     */

    public Position clone(){return new Position(this);}


    /**
     * Método equals
     * @param o
     * @return boolean
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (idPosition != position.idPosition) return false;
        if (idUser != position.idUser) return false;
        if (idMarketStock != position.idMarketStock) return false;
        if (amount != position.amount) return false;
        if (units_remaining != position.units_remaining) return false;
        if (Float.compare(position.stop_loss, stop_loss) != 0) return false;
        if (Float.compare(position.take_profit, take_profit) != 0) return false;
        if (Float.compare(position.deal_value, deal_value) != 0) return false;
        if (!type.equals(position.type)) return false;
        if (!status.equals(position.status)) return false;
        return deal_date.equals(position.deal_date);
    }

    /**
     * Método hashCode
     * @return int
     */
    public int hashCode() {
        int result = idPosition;
        result = 31 * result + type.hashCode();
        result = 31 * result + idUser;
        result = 31 * result + idMarketStock;
        result = 31 * result + amount;
        result = 31 * result + units_remaining;
        result = 31 * result + (stop_loss != +0.0f ? Float.floatToIntBits(stop_loss) : 0);
        result = 31 * result + (take_profit != +0.0f ? Float.floatToIntBits(take_profit) : 0);
        result = 31 * result + status.hashCode();
        result = 31 * result + (deal_value != +0.0f ? Float.floatToIntBits(deal_value) : 0);
        result = 31 * result + deal_date.hashCode();
        return result;
    }

    /**
     * Método toString
     * @return String
     */
    public String toString() {
        return "Position{\n" +
                "    Type:           '" + type + '\'' +"\n"+
                "    User Id:         " + idUser + "\n"+
                "    Marketstock Id:  " +  idMarketStock +"\n"+
                "    Amount:          " + amount +"\n"+
                "    Units Remaining: " + units_remaining +"\n"+
                "    StopLoss:        " + stop_loss +"\n"+
                "    TakeProfit:      " + take_profit +"\n"+
                "    Status:          " + status + "\n"+
                "    Deal Value:      " + deal_value +"\n"+
                "    Deal Date:      " + deal_date +"\n"+
                '}'+"\n";
    }


    /**
     * Método compareTo
     * @param position
     * @return int
     */
    public int compareTo(Position position) {
        return idPosition - position.idPosition;
    }

    /**
     * Método que atualiza o estado de uma posição de compra
     * @param mstk
     */
    public void updateBuy(MarketStock mstk) {

        app.updateBuyPositionWaiting(mstk);
    }

    /**
     * Método que atualiza o estado de uma posição de venda
     */
    public void updateSale(MarketStock mstk) {

        app.updateSalePositionWaiting(mstk);
    }
}
