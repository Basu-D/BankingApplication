package org.example.banking.controller;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.example.banking.service.AccountService;
import org.example.banking.util.ResponseUtil;
import org.example.banking.util.ValidationUtil;

public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public void createAccount(RoutingContext context) {
        //validate request
        ValidationUtil.validateAccountCreationRequest(context);
        //extract request as json
        JsonObject body = context.body().asJsonObject();
        String name = body.getString("name");
        double initialBalance = body.getDouble("initialBalance", 0.0);

        JsonObject response = accountService.createAccount(name, initialBalance);
        ResponseUtil.sendSuccessResponse(context, 201, response);
    }

    public void readBalance(RoutingContext context) {
        ValidationUtil.validateAccountIdFromPathParam(context);
        String accountId = context.pathParam("id");

        JsonObject response = accountService.getBalance(accountId);
        ResponseUtil.sendSuccessResponse(context, 200, response);
    }

    public void depositAmount(RoutingContext context) {
        ValidationUtil.validateAmountDepositOrWithDrawRequest(context);
        String accountId = context.pathParam("id");
        double amount = context.body().asJsonObject().getDouble("amount");
        JsonObject response = accountService.addBalance(accountId, amount);
        ResponseUtil.sendSuccessResponse(context, 200, response);
    }

    public void withdrawAmount(RoutingContext context) {
        ValidationUtil.validateAmountDepositOrWithDrawRequest(context);
        String accountId = context.pathParam("id");
        double amount = context.body().asJsonObject().getDouble("amount");
        JsonObject response = accountService.subtractBalance(accountId, amount);
        ResponseUtil.sendSuccessResponse(context, 200, response);
    }
}
