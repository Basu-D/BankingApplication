package org.example.banking.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.example.banking.controller.AccountController;
import org.example.banking.controller.SafeHandler;
import org.example.banking.router.AccountRouter;
import org.example.banking.service.AccountService;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        AccountService accountService = new AccountService();
        AccountController accountController = new AccountController(accountService);

        Router router = AccountRouter.create(vertx, accountController);
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8888, http -> {
                    if (http.succeeded()) {
                        System.out.println("HTTP Server started on port 8888");
                        startPromise.complete();
                    } else {
                        System.out.println("HTTP Server failed to start : " + http.cause());
                        startPromise.fail(http.cause());
                    }
                });
    }
}