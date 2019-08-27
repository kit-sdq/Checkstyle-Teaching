package edu.kit.informatik;

public class Bank {
    private int bankCode;
    private Account[] accounts;
    private int currentIndex;

    public Bank(int bankCode) {
        this.bankCode = bankCode;
        this.accounts = new Account[8];
        this.currentIndex = 0;
    }

    public int createAccount(int accountNumber) {
        //Double size if full
        if (currentIndex == accounts.length) {
            Account[] moreAccounts = new Account[accounts.length * 2];
            for (int i = 0; i < accounts.length; i++) {
                moreAccounts[i] = accounts[i];
            }
            accounts = moreAccounts;
        }

        accounts[currentIndex] = new Account(bankCode, accountNumber);
        return currentIndex++;
    }

    public boolean removeAccount(int accountNumber) {
        int indexToRemove = -1;
        for (int i = 0; i < currentIndex; i++) {
            if (accounts[i].getAccountNumber() == accountNumber) {
                indexToRemove = i;
                currentIndex--;
                break;
            }
        }

        if (indexToRemove == -1) {
            return false;
        }

        //If number of accounts is too low, shrink array while moving accounts to the left
        if (accounts.length != 8 && currentIndex < accounts.length / 8) {
            /*
             * Offset used to determine if the loop is currently in front of the hole or after it.
             * If it is in front of the hole, just copy the current account to the new array.
             * Else, copy the next account to move all accounts to the left.
             */
            int offset = 0;
            Account[] lessAccounts = new Account[accounts.length / 2];
            for (int j = 0; j < currentIndex; j++) {
                if (indexToRemove == j) {
                    offset = 1;
                }

                lessAccounts[j] = accounts[j + offset];
            }

            accounts = lessAccounts;
        } else {
            for (int j = indexToRemove; j < currentIndex; j++) {
                accounts[j] = accounts[j + 1];
            }
        }

        return true;
    }

    public boolean containsAccount(int accountNumber) {
        for (Account account : accounts) {
            if (account != null && account.getAccountNumber() == accountNumber) {
                return true;
            }
        }

        return false;
    }

    public boolean internalBankTransfer(int fromAccountNumber, int toAccountNumber, int amount) {
        Account fromAccount = getAccountByNumber(fromAccountNumber);
        Account toAccount = getAccountByNumber(toAccountNumber);

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

    public int length() {
        return accounts.length;
    }

    public int size() {
        return currentIndex;
    }

    public Account getAccount(int index) {
        if (index >= currentIndex) {
            return null;
        }

        return accounts[index];
    }

    //Returns the account with the given number, if it exists, else it returns null;
    private Account getAccountByNumber(int accountNumber) {
        for (Account account : accounts) {
            if (account != null && account.getAccountNumber() == accountNumber) {
                return account;
            }
        }

        return null;
    }
}
