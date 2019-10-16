package Exception;

public class NoStockAvailableException extends Exception {
    public NoStockAvailableException(){
        super("Sem stock no sistema!");
    }
}
