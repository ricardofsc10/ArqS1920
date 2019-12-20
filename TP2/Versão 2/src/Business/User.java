package Business;

public class User implements Comparable<User>{
    //------------------------------------ INSTANCE VARIABLES -----------------------------------------------
    private App app;
    private int idUser;
    private String username;
    private String name;
    private String email;
    private String password;
    private float account_balance;
    //--------------------------------------------------------------------------------------------------------

    //------------------------------------ CONSTRUCTORS -----------------------------------------------------

    public User(int idUser,String username, String name, String email, String password, float account_balance) {
        this.idUser = idUser;
        this.username = name;
        this.name = username;
        this.email = email;
        this.password = password;
        this.account_balance = account_balance;
    }

    public User(User usr) {
        this.idUser = usr.getIdUser();
        this.username = usr.getUsername();
        this.name = usr.getName();
        this.email = usr.getEmail();
        this.password = usr.getPassword();
        this.account_balance = usr.getAccount_balance();
    }

    public User() {
        this.idUser = 0;
        this.username = null;
        this.name = null;
        this.email = null;
        this.password = null;
        this.account_balance = 0;
    }

    //---------------------------------------------------------------------------------------------------------


    //---------------------------------------- SETTERS & GETTERS ----------------------------------------------
    public int getIdUser() {return idUser;}
    public void setIdUser(int idUser){this.idUser = idUser;}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(float account_balance) {
        this.account_balance = account_balance;
    }
    //------------------------------------------------------------------------------------------------

    /**
     * Método clone
     * @return user
     */
    public User clone() {
        return new User(this);
    }



    /**
     * Método equals
     * @param o
     * @return boolean
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (getIdUser() != user.getIdUser()) return false;
        if (Float.compare(user.getAccount_balance(), getAccount_balance()) != 0) return false;
        if (!getUsername().equals(user.getUsername())) return false;
        if (!getName().equals(user.getName())) return false;
        if (!getEmail().equals(user.getEmail())) return false;
        return getPassword().equals(user.getPassword());
    }


    /**
     * Método hashCode
     * @return
     */
    public int hashCode() {
        int result = getIdUser();
        result = 31 * result + getUsername().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getEmail().hashCode();
        result = 31 * result + getPassword().hashCode();
        result = 31 * result + (getAccount_balance() != +0.0f ? Float.floatToIntBits(getAccount_balance()) : 0);
        return result;
    }

    /**
     * Método toString
     * @return string
     */
    public String toString() {
        return  "    Username:          " + username +"\n"+
                "    Name:              " + name +"\n"+
                "    Email:             " + email +"\n"+
                "    Password:          " + password +"\n"+
                "    Account Balance:   " + account_balance +"\n";
    }

    /**
     * Adiciona fundos na conta do utilizador
     * @param value
     */
    public void addFunds(float value) {
        this.account_balance += value;
    }



    /**
     * Debita fundos na conta do utilizador
     * @param value
     */
    public void debitFunds(float value) {
        this.account_balance -= value;
    }


    /**
     * Método compareTo
     * @param user
     * @return int
     */
    public int compareTo(User user) {
        return idUser - user.idUser;
    }
}
