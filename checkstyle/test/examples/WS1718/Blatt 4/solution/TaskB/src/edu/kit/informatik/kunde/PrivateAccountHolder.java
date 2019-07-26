package edu.kit.informatik.kunde;

import edu.kit.informatik.konto.PrivateAccount;

public class PrivateAccountHolder extends AccountHolder {
    /**
     * Creates a new account holder that specifically is a private customer and therefore has a private account.
     *
     * @param account The private account that belongs to the account holder.
     * @param accountHolderNumber The ID of the account holder.
     */
    public PrivateAccountHolder(PrivateAccount account, int accountHolderNumber) {
        super(account, accountHolderNumber);
    }
}
