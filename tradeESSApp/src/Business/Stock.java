package Business;

public class Stock implements Comparable<Stock>{

    private int idStock;
    private String name;
    private String owner;
    private float cfdBuy;
    private float cfdSale;

    public Stock(int idStock, String name, String owner, float cfdBuy, float cfdSale) {
        this.idStock = idStock;
        this.name = name;
        this.owner = owner;
        this.cfdBuy = cfdBuy;
        this.cfdSale = cfdSale;
    }

    public Stock(Stock s){
        this.idStock = s.getIdStock();
        this.name = s.getName();
        this.owner = s.getOwner();
        this.cfdBuy = s.getCfdBuy();
        this.cfdSale = s.getCfdSale();
    }

    public Stock(){
        this.idStock = -1;
        this.name = "";
        this.owner = "";
        this.cfdBuy = 0;
        this.cfdSale = 0;
    }

    public int getIdStock() {
        return idStock;
    }

    public void setIdStock(int idStock) {
        this.idStock = idStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public float getCfdBuy() {
        return cfdBuy;
    }

    public void setCfdBuy(float cfdBuy) {
        this.cfdBuy = cfdBuy;
    }

    public float getCfdSale() {
        return cfdSale;
    }

    public void setCfdSale(float cfdSale) {
        this.cfdSale = cfdSale;
    }

    @Override
    public int compareTo(Stock s) {
        return idStock - s.idStock;
    }

    //HASHCODE
    public int hashCode() {
        int res = getIdStock();
        res = 59 * res + getName().hashCode();
        res = 59 * res + getOwner().hashCode();
        res = 59 * res + (cfdBuy != +0.0f ? Float.floatToIntBits(cfdBuy) : 0);
        res = 59 * res + (cfdSale != +0.0f ? Float.floatToIntBits(cfdSale) : 0);
        return res;
    }
}
