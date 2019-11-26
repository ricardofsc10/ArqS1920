package Business;

import java.io.Serializable;

public class Watchlist implements Serializable {

    private Integer id;
    private Double limit;
    private String code;
    private String user;
    private Integer upordown;

    public Watchlist() {
        this.id = -1;
        this.code = "N/A";
        this.user = "N/A";
        this.limit = 0.0;
        this.upordown = 0;
    }

    public Watchlist(Integer id, String code, String user, Double limit, Integer upordown) {
        this.id = id;
        this.code = code;
        this.user = user;
        this.limit = limit;
        this.upordown  = upordown;
    }

    public Watchlist(String code, String user, Double limit, Integer upordown) {
        this.code = code;
        this.user = user;
        this.limit = limit;
        this.upordown  = upordown;
    }

    public Watchlist(Watchlist a) {
        this.code = a.getCode();
        this.user = a.getUser();
        this.limit = a.getLimit();
        this.upordown = a.getUpordown();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUpordown() {
        return this.upordown;
    }

    public Double getLimit() {
        return this.limit;
    }

    public String getCode() {
        return this.code;
    }

    public String getUser() {
        return this.user;
    }

    public void setCode(String code) {
        this.code = code;
    }
    public void setLimit(Double limit) {
        this.limit = limit;
    }
    public void setUpordown(Integer upordown) {
        this.upordown = upordown;
    }
    public void setUser(String user) {
        this.user = user;
    }

    public boolean equals(Object o) {

        if(this == o) return true;

        if((o == null) || (this.getClass() != o.getClass())) return false;
        else {
            Watchlist a = (Watchlist) o;

            return(this.code.equals(a.getCode())
                    && this.user.equals(a.getUser())
                    && this.limit.equals(a.getLimit())
                    && this.upordown.equals(a.getUpordown())
            );
        }

    }

    public String toString() {

        StringBuffer sb = new StringBuffer();
        sb.append("["+this.getCode()+"]"+"\n");
        sb.append("["+this.getUser()+"]"+"\n");
        sb.append("[LIMIT] "+this.getLimit()+"\n");
        sb.append("[UP OR DOWN] "+this.getUpordown()+"\n");

        return sb.toString();
    }
}
