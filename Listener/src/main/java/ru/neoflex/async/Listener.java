package ru.neoflex.async;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class Listener extends AbstractVerticle {
    private static final Logger logger = LoggerFactory.getLogger(Listener.class);
    HttpServer httpServer;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        int port = config().getInteger("http.port", 8080);
        httpServer = vertx.createHttpServer();
        httpServer
                .requestHandler(this::handleRequest)
                .listen(port)
                .onSuccess(event -> {
                    logger.info("Server listen on " + event.actualPort());
                    startPromise.complete();
                })
                .onFailure(event -> {
                    logger.error("Failed to listen", event);
                });
    }

    @Override
    public void stop() throws Exception {
        logger.info("Server stop");
    }

    private void handleRequest(HttpServerRequest request) {
        HttpServerResponse response = request.response();
        request.handler(event -> {
            response.write(event);
        }).endHandler(event -> {
            response.end();
        });
    }
}
