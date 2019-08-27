package edu.kit.informatik.konto;

public class BusinessAccount extends Account {
    /**
     * Creates a new business account.
     * 
     * @param bankCode The bankcode of the bank the account belongs to.
     * @param accountNumber The number of the account.
     */
    public BusinessAccount(int bankCode, int accountNumber) {
        super(bankCode, accountNumber);
    }
}
