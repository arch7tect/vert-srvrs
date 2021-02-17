package ru.neoflex.async;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            vertx.close();
        }));
        ConfigRetriever retriever = ConfigRetriever.create(vertx);
        retriever.getConfig()
                .onSuccess(event -> {
                    vertx.deployVerticle("ru.neoflex.async.Listener",
                            new DeploymentOptions().setConfig(event));
                })
                .onFailure(event -> {
                    logger.error("ConfigRetriever", event);
                });
    }
}
