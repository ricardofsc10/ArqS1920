package servidor;

public class AuxContrato {

    private AuxContrato(){
        throw new IllegalMonitorStateException("Utility class");
    }

    public static boolean contratoAux(int value){
        boolean result = false;
        if(value==0) result = true;
        return result;
    }
}
