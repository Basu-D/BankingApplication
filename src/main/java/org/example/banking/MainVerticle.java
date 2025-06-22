package org.example.banking;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route("/health").handler(context -> context.response().end("UP"));

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8888, http -> {
                    if(http.succeeded()) {
                        System.out.println("HTTP Server started on port 8888");
                    } else {
                        System.out.println("HTTP Server failed to start");
                    }
                });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());

    }
}