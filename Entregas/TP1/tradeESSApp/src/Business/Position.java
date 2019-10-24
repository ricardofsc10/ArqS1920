package Business;

public class Position {

    private int idPosition;
    private String type;
    private int idUser;
    private int idStock;
    private int amount;
    private float stop_loss;
    private float take_profit;
    private String status;
    private float dealValue;

    public Position(int idPosition, String type, int idUser, int idStock, int amount, float stop_loss, float take_profit, String status, float dealValue) {
        this.idPosition = idPosition;
        this.type = type;
        this.idUser = idUser;
        this.idStock = idStock;
        this.amount = amount;
        this.stop_loss = stop_loss;
        this.take_profit = take_profit;
        this.status = status;
        this.dealValue = dealValue;
    }

    public Position() {
        this.idPosition = -1;
        this.type = "";
        this.idUser = -1;
        this.idStock = -1;
        this.amount = 0;
        this.stop_loss = 0;
        this.take_profit = 0;
        this.status = "";
        this.dealValue = 0;
    }

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

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdStock() {
        return idStock;
    }

    public void setIdStock(int idStock) {
        this.idStock = idStock;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public float getDealValue() {
        return dealValue;
    }

    public void setDealValue(float dealValue) {
        this.dealValue = dealValue;
    }
}
