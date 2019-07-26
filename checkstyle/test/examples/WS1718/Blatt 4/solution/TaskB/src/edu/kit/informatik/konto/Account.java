package edu.kit.informatik.konto;

public class Account {
    private int accountNumber;
    private int bankCode;
    private int balance;

    /**
     * Creates a new account with no money in it.
     * 
     * @param bankCode The bank code of the bank the account belongs to.
     * @param accountNumber The number of the account to create.
     */
    public Account(int bankCode, int accountNumber) {
        this.bankCode = bankCode;
        this.balance = 0;
        this.accountNumber = accountNumber;
    }

    /**
     * Withdraws an amount of money from the account if the balance is at least that amount.
     * 
     * @param amount The amount of money to withdraw.
     * @return true, if the withdrawal was successful, false otherwise.
     */
    public boolean withdraw(int amount) {
        if (balance - amount >= 0) {
            balance -= amount;
            return true;
        }

        return false;
    }

    /**
     * Deposits an amount of money in the account.
     * 
     * @param amount The amount of money to deposit.
     */
    public void deposit(int amount) {
        balance += amount;
    }


    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Account account = (Account) other;
        return accountNumber == account.accountNumber 
                && bankCode == account.bankCode;
    }

    @Override
    public int hashCode() {
        return accountNumber * bankCode / 2 + 7;
    }

    /**
     * Gets the number of the account.
     * 
     * @return The number of the account.
     */
    public int getAccountNumber() {
        return accountNumber;
    }

    /**
     * Gets the bankcode of the bank the account belongs to.
     * 
     * @return The bankcode.
     */
    public int getBankCode() {
        return bankCode;
    }

    /**
     * Gets the current balance of the account.
     * 
     * @return The balance of the account.
     */
    public int getBalance() {
        return balance;
    }
}