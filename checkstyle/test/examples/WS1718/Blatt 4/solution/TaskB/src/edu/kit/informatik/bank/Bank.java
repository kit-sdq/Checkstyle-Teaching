package edu.kit.informatik.bank;

import edu.kit.informatik.konto.Account;
import edu.kit.informatik.MinimalList;

public class Bank {
    private int bankCode;
    private MinimalList accounts;

    /**
     * Creates a new Bank with an empty list of accounts.
     * 
     * @param bankCode The bankcode of the bank.
     */
    public Bank(int bankCode) {
        this.bankCode = bankCode;
        this.accounts = new MinimalList();
    }

    /**
     * Creates a new account in the bank with the given account number.
     * The account list is kept ascendingly sorted in the process.
     * 
     * @param accountNumber The number of the new account.
     * @return The index where the account was added in the list.
     */
    public int createAccount(int accountNumber) {
        //Search for correct index to add in a sorted manner
        for (int i = 0; i < accounts.size(); i++) {
            //If the accountNumber at the current index is bigger,
            //add here (moves back the bigger account number in the process)
            if (accounts.get(i).getAccountNumber() > accountNumber) {
                accounts.add(new Account(bankCode, accountNumber), i);
                return i;
            }
        }
        
        //If it is the biggest accountNumber yet (or the list is empty), add to end
        accounts.add(new Account(bankCode, accountNumber));
        return accounts.size() - 1;
    }

    /**
     * Removes the account with the given number from the bank if it exists. 
     * 
     * @param accountNumber The account number of the account to remove.
     * @return true, if the account existed and got removed, false otherwise.
     */
    public boolean removeAccount(int accountNumber) {
        return accounts.remove(new Account(bankCode, accountNumber));
    }

    /**
     * Checks if an account with the given number exists in the bank.
     * 
     * @param accountNumber The number of the account to check.
     * @return true, if the account exists in the bank, false otherwise.
     */
    public boolean containsAccount(int accountNumber) {
        return accounts.contains(new Account(bankCode, accountNumber));
    }

    /**
     * Transfers an amount of money from the account with the number {@code fromAccountNumber} 
     * to the account with the number {@code toAccountNumber} if both accounts exist and balance of 
     * the source account is at least {@code amount}.
     * 
     * @param fromAccountNumber The number of the source account.
     * @param toAccountNumber The number of the target account.
     * @param amount The amount of money to transfer.
     * @return true, if the transfer succeeded, false otherwise.
     */
    public boolean transfer(int fromAccountNumber, int toAccountNumber, int amount) {
        Account fromAccount = accounts.get(new Account(bankCode, fromAccountNumber));
        Account toAccount = accounts.get(new Account(bankCode, toAccountNumber));

        if (fromAccount == null || toAccount == null) {
            return false;
        }

        boolean result = fromAccount.withdraw(amount);
        if (!result) {
            return false;
        }

        toAccount.deposit(amount);

        return true;
    }

    /**
     * Retrieves the number of accounts in the bank.
     * 
     * @return The number of accounts in the bank.
     */
    public int length() {
        return accounts.size();
    }

    /**
     * Gets the account at a given index in the account list.
     * 
     * @param index The index of the account.
     * @return The account at the index.
     */
    public Account getAccount(int index) {
        return accounts.get(index);
    }

    /**
     * Gets the bank code of the bank.
     * 
     * @return The bank code of the bank.
     */
    public int getBankCode() {
        return bankCode;
    }
}
