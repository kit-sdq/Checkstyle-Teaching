package edu.kit.informatik.kunde;

import edu.kit.informatik.konto.BusinessAccount;

public class BusinessAccountHolder extends AccountHolder {
    /**
     * Creates a new account holder that specifically is a business customer and therefore has a business account.
     * 
     * @param account The business account that belongs to the account holder.
     * @param accountHolderNumber The ID of the account holder.
     */
    public BusinessAccountHolder(BusinessAccount account, int accountHolderNumber) {
        super(account, accountHolderNumber);
    }
}
