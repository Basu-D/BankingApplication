package org.example.banking.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import org.example.banking.controller.AccountController;
import org.example.banking.router.AccountRouter;
import org.example.banking.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) {
        AccountService accountService = new AccountService();
        AccountController accountController = new AccountController(accountService);

        Router router = AccountRouter.create(vertx, accountController);
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8888, http -> {
                    if (http.succeeded()) {
                        logger.info("HTTP Server started on port 8888");
                        startPromise.complete();
                    } else {
                        logger.error("HTTP Server failed to start : " + http.cause());
                        startPromise.fail(http.cause());
                    }
                });
    }
}
