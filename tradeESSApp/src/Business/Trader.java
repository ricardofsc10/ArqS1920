package Business;


public class Trader extends User {
    private float saldoConta;

    public Trader(){
        this.saldoConta = 0;
    }

    public Trader(int id, String email, String username, String password, String morada, int idade, int contacto) {
        super(id, email, username, password, morada, idade, contacto);
        this.setSaldoConta(saldoConta);
    }

    public Trader(int id, String email, String username, String password, String morada, int idade, int contacto, float saldoConta){
        super(id, email, username, password, morada, idade, contacto);
        this.saldoConta = saldoConta;
    }

    public float getSaldoConta() {
        return saldoConta;
    }

    public void setSaldoConta(float saldoConta) {
        this.saldoConta = saldoConta;
    }

    //adicionar dinheiro na conta
    public void addMoney(float valor){
        this.saldoConta +=valor;
    }

    //retirar dinheiro da conta
    public void removeMoney(float valor){
        this.saldoConta -=valor;
    }
}
