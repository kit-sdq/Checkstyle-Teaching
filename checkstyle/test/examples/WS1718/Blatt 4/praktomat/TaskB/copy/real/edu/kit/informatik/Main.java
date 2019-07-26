package edu.kit.informatik;

import edu.kit.informatik.bank.Bank;
import edu.kit.informatik.konto.Account;
import edu.kit.informatik.konto.BusinessAccount;
import edu.kit.informatik.konto.PrivateAccount;
import edu.kit.informatik.kunde.AccountHolder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;

public class Main {
    public static void main(String[] args) {
        switch (args[0]) {
            //Account
            case "deposit": {
                Account account = new Account(5, 10);
                System.out.println("Created account with state " + stringifyAccount(account));
                deposit(account, 25);
                deposit(account, 13);
                break;
            }
            case "withdraw": {
                Account account = new Account(5, 10);
                System.out.println("Created account with state " + stringifyAccount(account));
                deposit(account, 25);
                withdraw(account, 11);
                withdraw(account, 14);
                break;
            }
            case "withdrawtoomuch": {
                Account account = new Account(5, 10);
                System.out.println("Created account with state " + stringifyAccount(account));
                withdraw(account, 5);
                deposit(account, 2);
                withdraw(account, 10);
                withdraw(account, 3);
                break;
            }
            
            //MinimalList
            
            case "add": {
                MinimalList ml = new MinimalList();
                add(ml, new Account(5, 10));
                add(ml, new Account(6, 12));
                add(ml, new Account(7, 14));
                break;
            }
            case "size": {
                MinimalList ml = new MinimalList();
                size(ml);
                add(ml, new Account(5, 10));
                size(ml);
                add(ml, new Account(6, 12));
                size(ml);
                add(ml, new Account(7, 14));
                size(ml);
                break;
            }
            case "getML": {
                MinimalList ml = new MinimalList();
                get(ml, 0);
                add(ml, new Account(5, 10));
                get(ml, 0);
                get(ml, 1);
                add(ml, new Account(6, 12));
                add(ml, new Account(7, 14));
                get(ml, 0);
                get(ml, 1);
                get(ml, 2);
                get(ml, 3);
                break;
            }
            case "getFirst": {
                MinimalList ml = new MinimalList();
                getFirst(ml);
                add(ml, new Account(5, 10));
                getFirst(ml);
                add(ml, new Account(6, 12));
                add(ml, new Account(7, 14));
                getFirst(ml);
                break;
            }
            case "getLast": {
                MinimalList ml = new MinimalList();
                getLast(ml);
                add(ml, new Account(5, 10));
                getLast(ml);
                add(ml, new Account(6, 12));
                add(ml, new Account(7, 14));
                getLast(ml);
                break;
            }
            case "addindex": {
                MinimalList ml = new MinimalList();
                add(ml, new Account(5, 10));
                add(ml, new Account(6, 12));
                add(ml, new Account(7, 14));
                add(ml, new Account(8, 16), 1);
                printState(ml);
                add(ml, new Account(9, 18), 3);
                printState(ml);
                break;
            }
            case "addindexfirst": {
                MinimalList ml = new MinimalList();
                add(ml, new Account(5, 10));
                add(ml, new Account(6, 12), 0);
                printState(ml);
                add(ml, new Account(7, 14), 0);
                printState(ml);
                break;
            }
            case "addindexlast": {
                MinimalList ml = new MinimalList();
                add(ml, new Account(5, 10));
                add(ml, new Account(6, 12), 1);
                printState(ml);
                add(ml, new Account(7, 14), 2);
                printState(ml);
                break;
            }
            case "addindexinvalid": {
                MinimalList ml = new MinimalList();
                add(ml, new Account(5, 10));
                add(ml, new Account(6, 12), -1);
                printState(ml);
                add(ml, new Account(7, 14), 10);
                printState(ml);
                break;
            }
            case "removeML": {
                MinimalList ml = new MinimalList();
                add(ml, new Account(5, 10));
                add(ml, new Account(6, 12));
                add(ml, new Account(7, 14));
                add(ml, new Account(8, 16));
                remove(ml, 2);
                printState(ml);
                remove(ml, 1);
                printState(ml);
                break;
            }
            case "removefirst": {
                MinimalList ml = new MinimalList();
                add(ml, new Account(5, 10));
                add(ml, new Account(6, 12));
                add(ml, new Account(7, 14));
                remove(ml, 0);
                printState(ml);
                break;
            }
            case "removeend": {
                MinimalList ml = new MinimalList();
                add(ml, new Account(5, 10));
                add(ml, new Account(6, 12));
                add(ml, new Account(7, 14));
                remove(ml, 2);
                printState(ml);
                break;
            }
            case "removelast": {
                MinimalList ml = new MinimalList();
                add(ml, new Account(5, 10));
                remove(ml, 0);
                printState(ml);
                break;
            }
            case "removeinvalid": {
                MinimalList ml = new MinimalList();
                remove(ml, 0);
                add(ml, new Account(5, 10));
                add(ml, new Account(6, 12));
                add(ml, new Account(7, 14));
                remove(ml, -1);
                remove(ml, 9);
                printState(ml);
                break;
            }
            case "containsML": {
                MinimalList ml = new MinimalList();
                add(ml, new Account(5, 10));
                add(ml, new Account(6, 12));
                add(ml, new Account(7, 14));
                contains(ml, new Account(5, 10));
                contains(ml, new Account(6, 12));
                contains(ml, new Account(7, 14));
                contains(ml, new Account(8, 14));
                contains(ml, new Account(7, 16));
                contains(ml, new Account(8, 16));
                break;
            }
            
            //Bank
                
            case "create": {
                Bank bank = new Bank(151);
                create(bank, 1);
                break;
            }
            case "get": {
                Bank bank = new Bank(151);
                create(bank, 1);
                create(bank, 2);
                create(bank, 3);
                get(bank, 0);
                get(bank, 1);
                get(bank, 2);
                break;
            }
            case "getempty": {
                Bank bank = new Bank(151);
                get(bank, 0);
                create(bank, 1);
                get(bank, 1);
                get(bank, 10);
                break;
            }
            case "createmultiple": {
                Bank bank = new Bank(151);
                create(bank, 1);
                create(bank, 2);
                create(bank, 3);
                create(bank, 4);
                create(bank, 5);
                create(bank, 6);
                create(bank, 7);
                create(bank, 8);
                create(bank, 9);
                printState(bank);
                break;
            }
            case "length": {
                Bank bank = new Bank(151);
                length(bank);
                create(bank, 1);
                length(bank);
                break;
            }
            case "createunordered": {
                Bank bank = new Bank(151);
                create(bank, 2);
                create(bank, 7);
                create(bank, 4);
                create(bank, 5);
                create(bank, 3);
                create(bank, 8);
                create(bank, 1);
                create(bank, 9);
                create(bank, 6);
                printState(bank);
                break;
            }
            case "remove": {
                Bank bank = new Bank(151);
                create(bank, 1);
                create(bank, 2);
                create(bank, 3);
                printState(bank);
                remove(bank, 1);
                printState(bank);
                remove(bank, 3);
                printState(bank);
                create(bank, 35);
                create(bank, 36);
                remove(bank, 35);
                printState(bank);
                break;
            }
            case "removenonexisting": {
                Bank bank = new Bank(151);
                remove(bank, 1);
                create(bank, 1);
                create(bank, 2);
                create(bank, 3);
                remove(bank, 200);
                printState(bank);
                break;
            }
            case "contains": {
                Bank bank = new Bank(151);
                contains(bank, 1);
                create(bank, 1);
                create(bank, 2);
                create(bank, 3);
                contains(bank, 2);
                contains(bank, 1);
                contains(bank, 3);
                contains(bank, 25);
                break;
            }
            case "transfer": {
                Bank bank = new Bank(151);
                create(bank, 1);
                create(bank, 2);
                deposit(bank.getAccount(0), 10);
                transfer(bank, 1, 2, 5);
                printState(bank);
                transfer(bank, 2, 1, 2);
                printState(bank);
                break;
            }
            case "transfernosource": {
                Bank bank = new Bank(151);
                create(bank, 2);
                deposit(bank.getAccount(0), 10);
                transfer(bank, 1, 2, 5);
                printState(bank);
                break;
            }
            case "transfernotarget": {
                Bank bank = new Bank(151);
                create(bank, 1);
                deposit(bank.getAccount(0), 10);
                transfer(bank, 1, 2, 5);
                printState(bank);
                break;
            }
            case "transfernomoney": {
                Bank bank = new Bank(151);
                create(bank, 1);
                create(bank, 2);
                transfer(bank, 1, 2, 5);
                deposit(bank.getAccount(0), 2);
                transfer(bank, 1, 2, 3);
                printState(bank);
                break;
            }
        }
    }
    
    private static String stringifyAccount(Account account) {
        return "(" + account.getBankCode() + "," + account.getAccountNumber() + "," + account.getBalance() + ")";
    }

    //Account
    private static void deposit(Account account, int amount) {
        System.out.print("Deposited " + amount);
        account.deposit(amount);
        System.out.println(", new account state: " + stringifyAccount(account));
    }

    private static void withdraw(Account account, int amount) {
        boolean result = account.withdraw(amount);
        if (result) {
            System.out.println("Withdrawal of " + amount + " successful, new account state: " + stringifyAccount(account));
        } else {
            System.out.println("Withdrawal of " + amount + " failed, new account state: " + stringifyAccount(account));
        }
    }
    
    //MinimalList
    private static void size(MinimalList ml) {
        System.out.println("Size: " + ml.size());    
    }
    
    private static void add(MinimalList ml, Account account) {
        ml.add(account);
        System.out.println("Added account with state " + stringifyAccount(account));
    }

    private static void add(MinimalList ml, Account account, int index) {
        ml.add(account, index);
        System.out.println("Added account with state " + stringifyAccount(account));
    }
    
    private static void get(MinimalList ml, int index) {
        Account result = ml.get(index);
        if (result == null) {
            System.out.println("No account at index " + index);
        } else {
            System.out.println("Account state at index " + index + ": " + stringifyAccount(result));
        }
    }
    
    private static void getFirst(MinimalList ml) {
        Account result = ml.getFirst();
        if (result == null) {
            System.out.println("No account at head of list");
        } else {
            System.out.println("Account state at head of list: " + stringifyAccount(result));
        }
    }
    
    private static void getLast(MinimalList ml) {
        Account result = ml.getLast();
        if (result == null) {
            System.out.println("No account at tail of list");
        } else {
            System.out.println("Account state at tail of list: " + stringifyAccount(result));
        }
    }

    private static void remove(MinimalList ml, int index) {
        boolean result = ml.remove(index);
        if (result) {
            System.out.println("Removal of account at index " + index + " successful");
        } else {
            System.out.println("Removal of account at index " + index + " failed");
        }
    }
    
    private static void contains(MinimalList ml, Account account) {
        boolean result = ml.contains(account);
        if (result) {
            System.out.println("Account with state " + stringifyAccount(account) + " exists");
        } else {
            System.out.println("Account with state " + stringifyAccount(account) + " doesn't exist");
        }
    }
    
    private static void printState(MinimalList ml) {
        if (ml.size() == 0) {
            System.out.println("List is empty");
        }
        
        for (int i = 0; i < ml.size(); i++) {
            System.out.println("Account state at index " + i + ": " + stringifyAccount(ml.get(i)));
        }
    }

    //Bank
    private static void create(Bank bank, int accountNumber) {
        System.out.println("Created account at " + bank.createAccount(accountNumber));
    }

    private static void remove(Bank bank, int accountNumber) {
        boolean result = bank.removeAccount(accountNumber);
        if (result) {
            System.out.println("Removal of account with number " + accountNumber + " successful");
        } else {
            System.out.println("Removal of account with number " + accountNumber + " failed");
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
        boolean result = bank.transfer(from, to, amount);
        if (result) {
            System.out.println("Transfer of " + amount + " succeeded");
        } else {
            System.out.println("Transfer of " + amount + " failed");
        }
    }

    private static void balance(Bank bank, int index) {
        System.out.println("Account state at index " + index + ": " + stringifyAccount(bank.getAccount(index)));
    }

    private static void length(Bank bank) {
        System.out.println("Number of accounts: " + bank.length());
    }

    private static void get(Bank bank, int index) {
        Account account = bank.getAccount(index);
        if (account == null) {
            System.out.println("No account at " + index);
        } else {
            System.out.println("Account state at " + index + ": " + stringifyAccount(account));
        }
    }

    private static void printState(Bank bank) {
        if (bank.length() == 0) {
            System.out.println("List is empty");
        }

        for (int i = 0; i < bank.length(); i++) {
            System.out.println("Account state at index " + i + ": " + stringifyAccount(bank.getAccount(i)));
        }
    }
}