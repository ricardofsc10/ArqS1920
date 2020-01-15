package cliente;

import java.util.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ThreadClienteLe implements Runnable {
    private Socket cliente;
    private BufferedReader in;
    Logger log = Logger.getLogger(ThreadClienteLe.class.getName());

    public ThreadClienteLe(Socket s)  throws IOException {
        this.cliente = s;
        this.in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
    }

    public String leResposta() throws IOException {
        return in.readLine();
    }

    public void run() {
            try {
                String resposta = in.readLine();
                while (resposta != null){
                    log.info(resposta);
                    if(resposta.equals("TERMINADA"))
                        break;
                }
            } catch (IOException e) {
                log.info(e.getMessage());
            }
    }
}

