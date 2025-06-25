package org.example.banking.util;

import io.vertx.core.json.JsonObject;

public class AccountHelper {
    public static void updateBalance(JsonObject account, double newBalance) {
        account.put("balance", newBalance);
    }
}
