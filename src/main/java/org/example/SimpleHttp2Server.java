package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.http2.server.HTTP2CServerConnectionFactory;
import org.eclipse.jetty.server.handler.AbstractHandler;

import java.io.IOException;

public class SimpleHttp2Server {
    public static void main(String[] args) throws Exception {
        Server server = new Server();

        HttpConfiguration httpConfig = new HttpConfiguration();

        // Configurar un conector para HTTP/2 sin TLS (h2c)
        ServerConnector connector = new ServerConnector(server,
                new HttpConnectionFactory(),
                new HTTP2CServerConnectionFactory(httpConfig));
        connector.setPort(8080);
        server.addConnector(connector);

        // Establecer un manejador que responda solo a la ruta "/"
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
                // Validar si la ruta es "/"
                if ("/".equals(target)) {
                    response.setContentType("text/plain;charset=utf-8");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().println("Hello HTTP/2 (h2c) World!");
                    baseRequest.setHandled(true);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        });

        server.start();
        server.join();
    }
}
