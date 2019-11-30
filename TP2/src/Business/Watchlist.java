package Business;

import java.io.Serializable;

public class Watchlist implements Serializable {

    private Integer id;
    private String code;
    private String user;

    public Watchlist() {
        this.id = -1;
        this.code = "N/A";
        this.user = "N/A";
    }

    public Watchlist(Integer id, String code, String user, Double limit, Integer upordown) {
        this.id = id;
        this.code = code;
        this.user = user;
    }

    public Watchlist(String code, String user) {
        this.code = code;
        this.user = user;
    }

    public Watchlist(Watchlist a) {
        this.code = a.getCode();
        this.user = a.getUser();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
            );
        }

    }

    public String toString() {

        StringBuffer sb = new StringBuffer();
        sb.append("["+this.getCode()+"]"+"\n");
        sb.append("["+this.getUser()+"]"+"\n");


        return sb.toString();
    }
}
