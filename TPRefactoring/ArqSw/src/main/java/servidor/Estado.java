package servidor;

public class Estado {
    private String pedido;
    private boolean status;
    private Integer identificador;
    private Integer idUtilizador;

    public Estado(String pedido, boolean estado,int id,int idU){
        this.pedido=pedido;
        this.status=estado;
        this.identificador = id;
        this.idUtilizador= idU;
    }

    public String getPedido() {
        return pedido;
    }

    public boolean getEstado(){
        return status;
    }

    public Integer getIdentificador() {
        return identificador;
    }

    public Integer getIdUtilizador() {
        return idUtilizador;
    }

    public void setEstado(boolean estado){
        this.status= estado;
    }

}
