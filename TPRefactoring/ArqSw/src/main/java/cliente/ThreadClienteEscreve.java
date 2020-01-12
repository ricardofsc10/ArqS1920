package cliente;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadClienteEscreve {
    private Socket cliente;
    private PrintWriter out;

    public ThreadClienteEscreve(Socket socket) throws IOException {
        this.cliente = socket;
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void addPedido(String pedido) {
        this.out.println(pedido);
    }

}

