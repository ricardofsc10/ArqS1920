package Business;

import java.io.Serializable;

public class User implements Serializable, Comparable<User> {

    private int id;
    private String email;
    private String username;
    private String password;
    private String morada;
    private int idade;
    private int contacto;

    /**
     * Construtor vazio de um Utilizador
     */
    public User(){
        this.id = 0;
        this.email="";
        this.username="";
        this.password="";
        this.morada="";
        this.idade=0;
        this.contacto=0;
    }

    /**
     * Construtor parametrizado de um Utilizador
     * @param id
     * @param email
     * @param username
     * @param password
     * @param morada
     * @param idade
     * @param contacto
     */
    public User(int id, String email, String username, String password, String morada, int idade, int contacto) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.morada = morada;
        this.idade = idade;
        this.contacto = contacto;
    }

    /**
     * Getter do identificador do Utilizador
     * @return idUtilizador
     */
    public int getId() {
        return id;
    }

    /**
     * Getter do email do Utilizador
     * @return Email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Getter do username do Utilizador
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter da password do Utilizador
     * @return Password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setter do identificador do Utilizador
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter do email do Utilizador
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Setter do username do Utilizador
     * @return username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Setter da password do Utilizador
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public int getContacto() {
        return contacto;
    }

    public void setContacto(int contacto) {
        this.contacto = contacto;
    }

    public int compareTo(User u){
        return id - u.id;
    }
}
