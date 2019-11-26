package Business;

import Data.CfdDAO;

import java.io.Serializable;

public class Cfd implements Serializable {

    private Integer id;
    private Integer idAsset;
    private String type;
    private String user;
    Double lowerlimit;
    private Double upperlimit;
    private Double units;
    private Double start_value;
    private Boolean active;

    public Cfd() {
        this.id = 0;
        this.idAsset = 0;
        this.type = "N/A";
        this.user = "N/A";
        this.lowerlimit = 0.0;
        this.upperlimit = 0.0;
        this.units = 0.0;
        this.start_value = 0.0;
        this.active = true;
    }
    public Cfd(Integer id, Integer asset, String type, String user, Double lowerlimit, Double upperlimit, Double units, Double start_value, Boolean active) {
        this.id = id;
        this.idAsset = asset;
        this.type = type;
        this.user = user;
        this.lowerlimit = lowerlimit;
        this.upperlimit = upperlimit;
        this.units = units;
        this.start_value = start_value;
        this.active = active;
    }

    public Cfd(Integer asset, String type, String user, Double lowerlimit, Double upperlimit, Double units, Double start_value, Boolean active){
        this.idAsset = asset;
        this.type = type;
        this.user = user;
        this.lowerlimit = lowerlimit;
        this.upperlimit = upperlimit;
        this.units = units;
        this.start_value = start_value;
        this.active = active;
    }

    public Cfd (Cfd cfd) {

        this.id = cfd.getId();
        this.idAsset = cfd.getidAsset();
        this.type = cfd.getType();
        this.user = cfd.getUser();
        this.lowerlimit = cfd.getLowerlimit();
        this.upperlimit = cfd.getUpperlimit();
        this.units = cfd.getUnits();
        this.start_value = cfd.getStart_value();
        this.active = cfd.getActive();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setIdAsset(Integer id){
        this.idAsset = id;
    }

    public String getAsset(Integer id){return new Asset().getName();}

    public void setType(String type) {
        this.type = type;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setLowerlimit(Double lowerlimit) {
        this.lowerlimit = lowerlimit;
    }

    public void setUpperlimit(Double upperlimit) {
        this.upperlimit = upperlimit;
    }

    public void setUnits(Double units) {
        this.units = units;
    }

    public void setStart_value(Double start_value) {
        this.start_value = start_value;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public Double getStart_value() {
        return start_value;
    }

    public Double getUpperlimit() {
        return upperlimit;
    }

    public Double getLowerlimit() {
        return lowerlimit;
    }

    public String getUser() {
        return user;
    }

    public String getType() {
        return type;
    }

    public Integer getidAsset() {
        return idAsset;
    }

    public Integer getId() {
        return id;
    }

    public Double getUnits() {
        return units;
    }

    public void setUnActive() {
        this.active = false ;
        CfdDAO.update(this.id, this);
    }


    public Cfd clone() {
        return new Cfd(this);
    }

    public boolean equals(Object o) {

        if(this == o) {
            return true;
        }

        if((o == null) || (this.getClass() != o.getClass())) return false;
        else {
            Cfd a = (Cfd) o;

            return(this.id.equals(a.getId())
                    && this.idAsset.equals(a.getidAsset())
                    && this.type.equals(a.getType())
                    && this.user.equals(a.getUser())
                    && this.lowerlimit.equals(a.getLowerlimit())
                    && this.upperlimit.equals(a.getUpperlimit())
                    && this.start_value.equals(a.getStart_value())
                    && this.active.equals(a.getActive()));
        }
    }

    public String toString() {

        StringBuffer sb = new StringBuffer();
        sb.append("[CFD ID] ");
        sb.append(this.id+"\n");
        sb.append("[ASSET] ");
        sb.append(this.idAsset+"\n");
        sb.append("[TYPE] ");
        sb.append(this.type+"\n");
        sb.append("[OWNER] ");
        sb.append(this.user+"\n");
        sb.append("[UNITS BOUGHT] ");
        sb.append(this.units+"\n");
        sb.append("[STOP LOSS LIMIT] ");
        sb.append(this.lowerlimit+"\n");
        sb.append("[TAKE PROFIT LIMIT] ");
        sb.append(this.upperlimit+"\n");
        sb.append("[STOCK START VALUE] ");
        sb.append(this.start_value+"\n");
        sb.append("[STATUS] ");
        if(this.active == true) {
            sb.append("ACTIVE\n");
        } else sb.append("TERMINATED\n");

        return sb.toString();

    }

}
