package Business;

public class Notification implements Comparable<Notification>{
    //------------------------------------ INSTANCE VARIABLES -----------------------------------------------
    private int idNotification;
    private int notificUserId;
    private String info;
    //--------------------------------------------------------------------------------------------------------

    //------------------------------------ CONSTRUCTORS -----------------------------------------------------

    public Notification(int idNotification,int notificUserId,String info){
        this.idNotification = idNotification;
        this.notificUserId = notificUserId;
        this.info = info;
    }


    public Notification(Notification notif){
        this.idNotification = notif.getId_notification();
        this.notificUserId = notif.getNotific_user_id();
        this.info = notif.getInfo();
    }


    public Notification(){
        this.idNotification = -1;
        this.notificUserId = -1;
        this.info = "";
    }

    //---------------------------------------------------------------------------------------------------------


    //---------------------------------------- SETTERS & GETTERS ----------------------------------------------

    public int getId_notification() {
        return idNotification;
    }

    public void setId_notification(int idNotification) {
        this.idNotification = idNotification;
    }

    public int getNotific_user_id() {
        return notificUserId;
    }

    public void setNotific_user_id(int notificUserId) {
        this.notificUserId = notificUserId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    //---------------------------------------------------------------------------------------------------------

    /**
     * Método clone
     * @return
     */

    public Notification clone(){return new Notification(this);}

    /**
     * Método equals
     * @param o
     * @return
     */

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification that = (Notification) o;

        if (getId_notification() != that.getId_notification()) return false;
        if (getNotific_user_id() != that.getNotific_user_id()) return false;
        return getInfo().equals(that.getInfo());
    }

    /**
     * Método hashCode
     * @return
     */
    public int hashCode() {
        int result = getId_notification();
        result = 31 * result + getNotific_user_id();
        result = 31 * result + getInfo().hashCode();
        return result;
    }

    /**
     * Método toString
     * @return
     */
    public String toString() {
        return "Notification:\n\n" +
                "    "+info+"\n\n";
    }

    /**
     * Método compareTo
     * @param notification
     * @return
     */
    public int compareTo(Notification notification){
        return idNotification - notification.idNotification;
    }
}
