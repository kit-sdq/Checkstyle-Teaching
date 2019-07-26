package edu.kit.informatik;

public class Main {
    public static void main(String[] args) {
        if (args[0].equals("public")) {
            System.out.println("Creating account...");
            Account account = new Account(5, 10);
            System.out.println("Initial balance: " + account.getBalance());
            System.out.println("Creating bank...");
            Bank bank = new Bank(5);
            System.out.println("Done.");
        } else if (args[0].equals("deposit")) {
            Account account = new Account(5, 10);
            deposit(account, 25);
            deposit(account, 13);
        } else if (args[0].equals("withdraw")) {
            Account account = new Account(5, 10);
            deposit(account, 25);
            withdraw(account, 11);
            withdraw(account, 14);
        } else if (args[0].equals("withdrawtoomuch")) {
            Account account = new Account(5, 10);
            withdraw(account, 5);
            deposit(account, 2);
            withdraw(account, 10);
            withdraw(account, 3);
        } else if (args[0].equals("length")) {
            Bank bank = new Bank (151);
            length(bank);
            create(bank, 1);
            length(bank);
        } else if (args[0].equals("create")) {
            Bank bank = new Bank (151);
            create(bank, 1);
            create(bank, 170);
            create(bank, 5);
        } else if (args[0].equals("size")) {
            Bank bank = new Bank (151);
            size(bank);
            create(bank, 15);
            size(bank);
            create(bank, 2);
            create(bank, 3);
            size(bank);
        } else if (args[0].equals("createinc")) {
            Bank bank = new Bank (151);
            length(bank);
            size(bank);
            create(bank, 1);
            length(bank);
            size(bank);
            create(bank, 2);
            create(bank, 3);
            create(bank, 4);
            create(bank, 5);
            create(bank, 6);
            create(bank, 7);
            create(bank, 8);
            length(bank);
            size(bank);
            create(bank, 9);
            length(bank);
            size(bank);
        } else if (args[0].equals("get")) {
            Bank bank = new Bank (151);
            create(bank, 1);
            create(bank, 170);
            create(bank, 5);
            get(bank, 0);
            get(bank, 1);
            get(bank, 2);
        } else if (args[0].equals("getempty")) {
            Bank bank = new Bank (151);
            get(bank, 0);
            create(bank, 1);
            create(bank, 170);
            create(bank, 5);
            get(bank, 2);
            get(bank, 3);
        } else if (args[0].equals("remove")) {
            Bank bank = new Bank (151);
            create(bank, 1);
            create(bank, 170);
            create(bank, 5);
            get(bank, 0);
            get(bank, 1);
            get(bank, 2);
            remove(bank, 1);
            get(bank, 0);
            get(bank, 1);
            get(bank, 2);
            remove(bank, 5);
            get(bank, 0);
            get(bank, 1);
            create(bank, 35);
            create(bank, 36);
            remove(bank, 35);
            get(bank, 0);
            get(bank, 1);
            get(bank, 2);
        } else if (args[0].equals("removenonexisting")) {
            Bank bank = new Bank (151);
            remove(bank, 1);
            create(bank, 1);
            create(bank, 170);
            create(bank, 5);
            remove(bank, 200);
        } else if (args[0].equals("removedec")) {
            Bank bank = new Bank (151);
            create(bank, 1);
            create(bank, 2);
            create(bank, 3);
            create(bank, 4);
            create(bank, 5);
            create(bank, 6);
            create(bank, 7);
            create(bank, 8);
            create(bank, 9);
            length(bank);
            size(bank);
            remove(bank, 9);
            remove(bank, 8);
            remove(bank, 7);
            remove(bank, 6);
            remove(bank, 5);
            remove(bank, 4);
            remove(bank, 3);
            length(bank);
            size(bank);
            remove(bank, 2);
            length(bank);
            size(bank);
            remove(bank, 1);
            length(bank);
            size(bank);
        } else if (args[0].equals("contains")) {
            Bank bank = new Bank (151);
            contains(bank, 1);
            create(bank, 1);
            create(bank, 170);
            create(bank, 5);
            contains(bank, 170);
            contains(bank, 1);
            contains(bank, 5);
            contains(bank, 25);
        } else if (args[0].equals("transfer")) {
            Bank bank = new Bank (151);
            create(bank, 1);
            create(bank, 2);
            deposit(bank.getAccount(0), 10);
            transfer(bank, 1, 2, 5);
            balance(bank, 0);
            balance(bank, 1);
            transfer(bank, 2, 1, 2);
            balance(bank, 0);
            balance(bank, 1);
        } else if (args[0].equals("transfernosource")) {
            Bank bank = new Bank (151);
            create(bank, 2);
            deposit(bank.getAccount(0), 10);
            transfer(bank, 1, 2, 5);
        } else if (args[0].equals("transfernotarget")) {
            Bank bank = new Bank (151);
            create(bank, 1);
            deposit(bank.getAccount(0), 10);
            transfer(bank, 1, 2, 5);
        } else if (args[0].equals("transfernomoney")) {
            Bank bank = new Bank (151);
            create(bank, 1);
            create(bank, 2);
            transfer(bank, 1, 2, 5);
            deposit(bank.getAccount(0), 2);
            transfer(bank, 1, 2, 3);
        }
    }
    
    private static void deposit(Account account, int amount) {
        System.out.print("Deposited " + amount);
        account.deposit(amount);
        System.out.println(", new balance: " + account.getBalance());
    }
    
    private static void withdraw(Account account, int amount) {
        boolean result = account.withdraw(amount);
        if (result) {
            System.out.println("Withdrawal of " + amount + " successful, new balance: " + account.getBalance());
        } else {
            System.out.println("Withdrawel of " + amount + " failed");
        }
    }
    
    private static void create(Bank bank, int accountNumber) {
        System.out.println("Created account at " + bank.createAccount(accountNumber));
    }
    
    private static void remove(Bank bank, int accountNumber) {
        boolean result = bank.removeAccount(accountNumber);
        if (result) {
            System.out.println("Removal of account " + accountNumber + " successful");
        } else {
            System.out.println("Removal of account " + accountNumber + " failed");
        }
    }
    
    private static void contains(Bank bank, int accountNumber) {
        boolean result = bank.containsAccount(accountNumber);
        if (result) {
            System.out.println("Bank contains account with number " + accountNumber);
        } else {
            System.out.println("Bank doesn't contain account with number " + accountNumber);
        }
    }
    
    private static void transfer(Bank bank, int from, int to, int amount) {
        boolean result = bank.internalBankTransfer(from, to, amount);
        if (result) {
            System.out.println("Transfer of " + amount + " succeeded");
        } else {
            System.out.println("Transfer of " + amount + " failed");
        }
    }
    
    private static void balance(Bank bank, int index) {
        System.out.println("Account at index " + index + " has a balance of " + bank.getAccount(index).getBalance());
    }
    
    private static void length(Bank bank) {
        System.out.println("Number of account slots: " + bank.length());
    }
    
    private static void size(Bank bank) {
        System.out.println("Number of accounts: " + bank.size());
    }
    
    private static void get(Bank bank, int index) {
        Account account = bank.getAccount(index);
        if (account == null) {
            System.out.println("No account at " + index);
        } else {
            System.out.println("Account at " + index + " has the number " + bank.getAccount(index).getAccountNumber()
                    + " and a balance of " + bank.getAccount(index).getBalance());
        }
    }
}