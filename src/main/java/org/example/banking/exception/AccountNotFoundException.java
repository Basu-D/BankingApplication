package org.example.banking.exception;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(String accountId) {
        super("Account Not Found for accountId: " + accountId);
    }
}
