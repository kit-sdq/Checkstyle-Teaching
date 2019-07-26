package edu.kit.informatik.kunde;

import edu.kit.informatik.konto.Account;

public class AccountHolder {
    private Account account;
    private int accountHolderNumber;

    /**
     * Creates a new account holder. Each account holder has exactly one account. 
     * 
     * @param account The account that belongs to the account holder.
     * @param accountHolderNumber The ID of the account holder.
     */
    public AccountHolder(Account account, int accountHolderNumber) {
        this.account = account;
        this.accountHolderNumber = accountHolderNumber;
    }

    /**
     * Gets the account that belongs to the account holder.
     * 
     * @return The account that belongs to the account holder.
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Gets the account holder ID.
     * 
     * @return The ID of the account holder.
     */
    public int getAccountHolderNumber() {
        return accountHolderNumber;
    }
}