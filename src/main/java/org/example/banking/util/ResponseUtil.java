package org.example.banking.util;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class ResponseUtil {

    public static void sendErrorResponse(RoutingContext context, int statusCode, String errorMessage) {
        JsonObject errorResponse = new JsonObject()
                .put("status", statusCode)
                .put("error", errorMessage);
        context.response()
                .setStatusCode(statusCode)
                .putHeader("Content-Type", "application/json")
                .end(errorResponse.encode());
    }

    public static void sendSuccessResponse(RoutingContext context, int statusCode, JsonObject responseBody) {
        context.response()
                .setStatusCode(statusCode)
                .putHeader("Content-Type", "application/json")
                .end(responseBody.encode());
    }
}

