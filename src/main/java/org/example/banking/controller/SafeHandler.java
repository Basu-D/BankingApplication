package org.example.banking.controller;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.example.banking.exception.AccountNotFoundException;
import org.example.banking.exception.InsufficientBalanceException;
import org.example.banking.util.ResponseUtil;

public class SafeHandler {
    public static Handler<RoutingContext> wrap(RoutingHandler handler) {
        return context -> {
            try {
                handler.handle(context);
            } catch (IllegalArgumentException | InsufficientBalanceException e) {
                ResponseUtil.sendErrorResponse(context, 400, sanitizeMessage(e.getMessage()));
            } catch (AccountNotFoundException e) {
                ResponseUtil.sendErrorResponse(context, 404, sanitizeMessage(e.getMessage()));
            } catch (Exception e) {
                // log error
                System.out.println(e.getMessage());
                ResponseUtil.sendErrorResponse(context, 500, "Something went wrong.");
            }
        };
    }

    // to prevent exposing stack traces or internal messages in prod
    private static String sanitizeMessage(String message) {
        if (message == null || message.toLowerCase().contains("exception")) {
            return "Something went wrong.";
        }
        return message;
    }
}

