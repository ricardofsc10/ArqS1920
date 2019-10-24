package Exception;

public class TraderRegistadoException extends Exception {
    String email;

    public String getEmail(){
        return this.email;
    }

    public TraderRegistadoException(String email){
        this.email = email;
    }
}
