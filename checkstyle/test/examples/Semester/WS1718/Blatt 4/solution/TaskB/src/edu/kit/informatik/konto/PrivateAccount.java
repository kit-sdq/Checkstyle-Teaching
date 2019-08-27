package edu.kit.informatik.konto;

public class PrivateAccount extends Account {
    /**
     * Creates a new private account.
     *
     * @param bankCode The bankcode of the bank the account belongs to.
     * @param accountNumber The number of the account.
     */
    public PrivateAccount(int bankCode, int accountNumber) {
        super(bankCode, accountNumber);
    }
}
