package edu.kit.informatik;

public class Account {
    private int accountNumber;
    private int bankCode;
    private int balance;

    public Account(int bankCode, int accountNumber) {
        this.bankCode = bankCode;
        this.balance = 0;
        this.accountNumber = accountNumber;
    }

    public boolean withdraw(int amount) {
        if (balance - amount >= 0) {
            balance -= amount;
            return true;
        }

        return false;
    }

    public void deposit(int amount) {
        balance += amount;
    }

    //Needed as the account number is important info that should be retrievable for the customer to use.
    public int getAccountNumber() {
        return accountNumber;
    }

    //No setter as the account number should not change as long as the account exists.

    //Needed as the bank code is important info that should be retrievable for the customer to use.
    public int getBankCode() {
        return bankCode;
    }

    //No setter as the bank code is bound to the bank and shouldn't change.

    //Needed as the customer wants to obviously know his balance.
    public int getBalance() {
        return balance;
    }

    //Not setter as there is withdraw and deposit to set it safely.
}