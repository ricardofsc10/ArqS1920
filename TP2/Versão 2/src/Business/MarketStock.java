package Business;

import java.util.ArrayList;

public class MarketStock implements Comparable<MarketStock>, Observable{
    //------------------------------------ INSTANCE VARIABLES -----------------------------------------------
    private int id_stock;
    private String name;
    private String owner;
    private float cfd_buy;
    private float cfd_sale;
    private float price;

    private ArrayList<ObserverPosition> buyPositionsForStock;
    private ArrayList<ObserverPosition> salePositionsForStock;
    private ArrayList<ObserverUser> usersForPriceStock;
    //--------------------------------------------------------------------------------------------------------

    //------------------------------------ CONSTRUCTORS -----------------------------------------------------
    public MarketStock(int id_stock, String name, String owner, float cfd_buy, float cfd_sale, float price) {
        this.id_stock = id_stock;
        this.name = name;
        this.owner = owner;
        this.cfd_buy = cfd_buy;
        this.cfd_sale = cfd_sale;
        this.price = price;
        this.buyPositionsForStock = new ArrayList<ObserverPosition>();
        this.salePositionsForStock = new ArrayList<ObserverPosition>();
        this.usersForPriceStock = new ArrayList<ObserverUser>();
    }

    public MarketStock(MarketStock mstk){
        this.id_stock = mstk.getId_stock();
        this.name = mstk.getName();
        this.owner = mstk.getOwner();
        this.cfd_buy = mstk.getCfd_Buy();
        this.cfd_sale = mstk.getCfd_Sale();
        this.price = mstk.getPrice();
        this.buyPositionsForStock = mstk.getBuyPositionsForStock();
        this.salePositionsForStock = mstk.getSalePositionsForStock();
        this.usersForPriceStock = mstk.getUsersForPriceStock();
    }

    public MarketStock(){
        this.id_stock = -1;
        this.name = "";
        this.owner = "";
        this.cfd_buy = 0;
        this.cfd_sale = 0;
        this.price = 0;
        this.buyPositionsForStock = new ArrayList<ObserverPosition>();
        this.salePositionsForStock = new ArrayList<ObserverPosition>();
        this.usersForPriceStock = new ArrayList<ObserverUser>();
    }
    //---------------------------------------------------------------------------------------------------------


    //---------------------------------------- SETTERS & GETTERS ----------------------------------------------

    public int getId_stock() {
        return id_stock;
    }

    public void setId_stock(int id_stock) {
        this.id_stock = id_stock;
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

    public float getCfd_Buy() {
        return cfd_buy;
    }

    public void setCfd_buy(float cfd_buy) {
        this.cfd_buy = cfd_buy;
    }

    public float getCfd_Sale() {
        return cfd_sale;
    }

    public void setCfd_sale(float cfd_sale) {
        this.cfd_sale = cfd_sale;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public ArrayList<ObserverPosition> getBuyPositionsForStock() { return buyPositionsForStock; }

    public void setBuyPositionsForStock(ArrayList<ObserverPosition> buyPositionsForStock) { this.buyPositionsForStock = buyPositionsForStock;}

    public ArrayList<ObserverPosition> getSalePositionsForStock() { return salePositionsForStock; }

    public void setSalePositionsForStock(ArrayList<ObserverPosition> salePositionsForStock) { this.salePositionsForStock = salePositionsForStock;}


    public ArrayList<ObserverUser> getUsersForPriceStock() { return usersForPriceStock; }

    public void setUsersForPriceStock(ArrayList<ObserverUser> usersForPriceStock) { this.usersForPriceStock = usersForPriceStock;}
    //---------------------------------------------------------------------------------------------------------------------------------------------

    //CLONE
    public MarketStock clone(){
        return new MarketStock(this);
    }


    //EQUALS
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MarketStock that = (MarketStock) o;

        if (getId_stock() != that.getId_stock()) return false;
        if (Float.compare(that.cfd_buy, cfd_buy) != 0) return false;
        if (Float.compare(that.cfd_sale, cfd_sale) != 0) return false;
        if (Float.compare(that.price, price) != 0) return false;
        if (!getName().equals(that.getName())) return false;
        return getOwner().equals(that.getOwner());
    }

    //HASHCODE
    public int hashCode() {
        int result = getId_stock();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getOwner().hashCode();
        result = 31 * result + (cfd_buy != +0.0f ? Float.floatToIntBits(cfd_buy) : 0);
        result = 31 * result + (cfd_sale != +0.0f ? Float.floatToIntBits(cfd_sale) : 0);
        result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
        return result;
    }


    //TO STRING
    public String toString() {
        return "MarketStock{" +"\n"+
                "    Name:      '" + name + '\'' +"\n"+
                "    Company:   '" + owner + '\'' +"\n"+
                "    CFD_Buy:    " + cfd_buy +"\n"+
                "    CFD_Sale:   " + cfd_sale +"\n"+
                "    Price:      " + price +"\n"+
                '}';
    }


    //COMPARE TO
    public int compareTo(MarketStock marketStock) {
        return id_stock - marketStock.id_stock;
    }


    /**
     * Adicionar uma nova posição de compra à lista de posições de compra que estão a observar alterações no mercado
     * @param buy_obs
     */
    public void addBuyPositionObservingStock(ObserverPosition buy_obs) {
        buyPositionsForStock.add(buy_obs);
        //testar se o observador foi adicionado
        if(!buyPositionsForStock.isEmpty()) {
            System.out.println("EMPTY");
        }

        System.out.println(buyPositionsForStock);
    }

    /**
     * Notifica todas as posições de compra que estão a observar o mercado sobre alterações nele
     */
    public void notifyBuyPositionObservingStock() {
        if (!buyPositionsForStock.isEmpty()) {
            for (ObserverPosition buy_obs : buyPositionsForStock) {
                buy_obs.updateBuy(this);
            }
        }
    }

    /**
     * Remover uma posição de compra da lista de posições de compra que estão a observar alterações no mercado
     * @param buy_obs
     */
    public void removeBuyPositionObservingStock(ObserverPosition buy_obs) {
        buyPositionsForStock.remove(buy_obs);
    }

    /**
     * Adicionar uma nova posição de venda à lista de posições de compra que estão a observar alterações no mercado
     * @param sale_obs
     */
    public void addSalePositionObservingStock(ObserverPosition sale_obs) { salePositionsForStock.add(sale_obs); }

    /**
     * Notifica todas as posições de venda que estão a observar o mercado sobre alterações nele
     */
    public void notifySalePositionObservingStock() {
        if (!salePositionsForStock.isEmpty()) {
            for (ObserverPosition sale_obs : salePositionsForStock) {
                sale_obs.updateSale(this);
            }
        }
    }

    /**
     * Remover uma posição de venda da lista de posições de compra que estão a observar alterações no mercado
     * @param sale_obs
     */
    public void removeSalePositionObservingStock(ObserverPosition sale_obs) {
        salePositionsForStock.remove(sale_obs);
    }

    /**
     * Adicionar um novo utilziador à lista de utilizadores que estão a observar alterações no preço do mercado
     * @param user_obs
     */
    public void addUserObservingStock(ObserverUser user_obs) {
        usersForPriceStock.add(user_obs);
        //testar se o observador foi adicionado
        if(usersForPriceStock.isEmpty()) {
            System.out.println("EMPTY");
        }else{

            System.out.println(usersForPriceStock);
        }
    }

    /**
     * Notifica todos os utilizadores que estão a observar o preço do mercado sobre alterações nele
     */
    public void notifyUserObservingStock() {
        if (!usersForPriceStock.isEmpty()) {
            for (ObserverUser user_obs : usersForPriceStock) {
                user_obs.updatePrice(this);
            }
        }
    }

    @Override
    public void removeUserObservingStock(ObserverUser user_obs) {
        usersForPriceStock.remove(user_obs);
    }


}
