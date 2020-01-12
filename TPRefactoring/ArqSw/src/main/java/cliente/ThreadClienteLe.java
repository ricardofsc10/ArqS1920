package cliente;

import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ThreadClienteLe implements Runnable {
    private Socket cliente;
    private BufferedReader in;
    Logger log = Logger.getLogger(App.class.getName());

    public ThreadClienteLe(Socket s,App app)  throws IOException {
        this.cliente = s;
        this.in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        this.aplicacao=app;
    }

    public String leResposta() throws IOException {
        String resposta = null;
        resposta = in.readLine();
        return resposta;

        return resposta;
    }

    public void run() throws IOException {
        String resposta ;

        while((resposta = in.readLine()) != null){
            log.info(resposta);
                if(resposta.equals("TERMINADA"))
                    break;

        }

    }
}

