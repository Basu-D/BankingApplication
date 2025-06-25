package org.example.banking.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(double balance) {
        super("Insufficient balance. available balance is " + balance);
    }
}
