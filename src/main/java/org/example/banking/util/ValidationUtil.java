package org.example.banking.util;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class ValidationUtil {
    private static final String REQUEST_BODY_NULL = "Request body should not be null or empty";
    private static final String ACCOUNT_ID_NULL = "The 'accountId' path parameter must not be null or blank";
    private static final String AMOUNT_NULL = "amount should not be null";

    public static void validateAccountCreationRequest(RoutingContext context) {
        JsonObject request = extractAndValidateRequestBody(context);

        String name = request.getString("name");
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name should not be null or blank");
        }

        double initialBalance = request.getDouble("initialBalance", 0.0);
        if (initialBalance < 0) {
            throw new IllegalArgumentException("initialBalance cannot be negative");
        }
    }

    public static void validateAccountIdFromPathParam(RoutingContext context) {
        String accountId = context.pathParam("id");
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException(ACCOUNT_ID_NULL);
        }
    }

    public static void validateAmountDepositOrWithDrawRequest(RoutingContext context) {
        validateAccountIdFromPathParam(context);
        JsonObject request = extractAndValidateRequestBody(context);

        Double amount = request.getDouble("amount");
        if (amount == null) {
            throw new IllegalArgumentException(AMOUNT_NULL);
        }

        validateAmount(amount);
    }

    public static void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
    }

    private static JsonObject extractAndValidateRequestBody(RoutingContext context) {
        JsonObject request = context.body().asJsonObject();
        if (request == null || request.isEmpty()) {
            throw new IllegalArgumentException(REQUEST_BODY_NULL);
        }
        return request;
    }
}
