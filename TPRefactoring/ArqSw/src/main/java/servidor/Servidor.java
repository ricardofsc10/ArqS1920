package servidor;


import java.io.IOException;
import java.net.InetSocketAddress;

import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Servidor {
    private static final int PORT =4545;

    public static void main(String[] args) throws IOException, InterruptedException {
        ESSLtd ess = new ESSLtd();
        RealTime rt = new RealTime(ess);
        rt.update();
        AsynchronousChannelGroup g = AsynchronousChannelGroup.withFixedThreadPool(5, Executors.defaultThreadFactory());
        try (AsynchronousServerSocketChannel ssc = AsynchronousServerSocketChannel.open(g)) {

            ssc.bind(new InetSocketAddress(PORT));

            ssc.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {
                @Override
                public void completed(final AsynchronousSocketChannel sc, Object o) {
                    new ThreadCliente(sc, ess);
                    ssc.accept(null, this);
                }

                @Override
                public void failed(Throwable throwable, Object o) {
                    /**
                     * este método está vazio porque não é necessário, embora seja obrigatório ele estar presente
                     * nesta classe devido ao "implements Map<....>"
                     */
                }
            });
        }

        g.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
    }
}
