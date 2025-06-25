package org.example.banking.router;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.example.banking.controller.AccountController;
import org.example.banking.controller.SafeHandler;

public class AccountRouter {
    public static Router create(Vertx vertx, AccountController accountController) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.get("/health").handler(ctx -> ctx.response().end("UP"));
        router.post("/api/account/create").handler(SafeHandler.wrap(accountController::createAccount));
        router.get("/api/account/:id/balance").handler(SafeHandler.wrap(accountController::readBalance));
        router.post("/api/account/:id/deposit").handler(SafeHandler.wrap(accountController::depositAmount));
        router.post("/api/account/:id/withdraw").handler(SafeHandler.wrap(accountController::withdrawAmount));

        return router;
    }
}
