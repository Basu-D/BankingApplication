package org.example.banking.service;

import io.vertx.core.json.JsonObject;
import org.example.banking.exception.AccountNotFoundException;
import org.example.banking.exception.InsufficientBalanceException;
import org.example.banking.util.AccountHelper;
import org.example.banking.util.ValidationUtil;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AccountService {
    private final ConcurrentHashMap<String, JsonObject> accounts = new ConcurrentHashMap<>();
    // This class will contain business logic related to account management.
    // For example, methods to create an account, retrieve account details, etc.

    private String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }

    public JsonObject createAccount(String userName, double initialBalance) {
        String accountId = generateRandomUUID();
        JsonObject account = new JsonObject()
                .put("accountId", accountId)
                .put("userName", userName)
                .put("balance", initialBalance);
        accounts.put(accountId, account);
        return account;
    }

    public JsonObject getAccount(String accountId) {
        JsonObject account = accounts.get(accountId);
        if (account == null) {
            throw new AccountNotFoundException(accountId);
        }
        return account;
    }

    public JsonObject updateAccount(JsonObject account) {
        return accounts.put(account.getString("accountId"), account);
    }

    public JsonObject getBalance(String accountId) {
        JsonObject account = getAccount(accountId);

        return Optional.ofNullable(account).map(acc -> new JsonObject()
                        .put("accountId", acc.getString("accountId"))
                        .put("balance", acc.getDouble("balance")))
                .orElse(new JsonObject().put("error", "Account Not Found"));
    }

    public JsonObject addBalance(String accountId, double amount) {
        ValidationUtil.validateAmount(amount);
        JsonObject account = getAccount(accountId);
        double balance = account.getDouble("balance");
        AccountHelper.updateBalance(account, balance + amount);
        return updateAccount(account);
    }

    public JsonObject subtractBalance(String accountId, double amount) {
        ValidationUtil.validateAmount(amount);
        JsonObject account = getAccount(accountId);
        double balance = account.getDouble("balance");
        if (balance > amount) {
            AccountHelper.updateBalance(account, balance - amount);
            return updateAccount(account);
        } else {
            throw new InsufficientBalanceException(balance);
        }
    }
}
