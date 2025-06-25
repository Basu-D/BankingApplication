package org.example.banking.service;

import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceTest {

    @Test
    void shouldCreateAccountAndRetrieveBalance() {
        AccountService service = new AccountService();

        JsonObject created = service.createAccount("John", 500);
        String accountId = created.getString("accountId");

        JsonObject balance = service.getBalance(accountId);
        assertEquals(500, balance.getDouble("balance"));
    }

    @Test
    void shouldAddBalance() {
        AccountService service = new AccountService();
        String accountId = service.createAccount("Jane", 100).getString("accountId");

        service.addBalance(accountId, 200);
        double updated = service.getBalance(accountId).getDouble("balance");

        assertEquals(300, updated);
    }

    @Test
    void shouldSubtractBalance() {
        AccountService service = new AccountService();
        String accountId = service.createAccount("Jane", 300).getString("accountId");

        service.subtractBalance(accountId, 100);
        double updated = service.getBalance(accountId).getDouble("balance");

        assertEquals(200, updated);
    }
}
