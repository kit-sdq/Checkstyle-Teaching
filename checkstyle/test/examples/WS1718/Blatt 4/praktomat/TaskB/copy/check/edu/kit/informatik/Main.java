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

public class Main {
    public static void main(String[] args) {
        if (args[0].equals("methodcheck")) {
            Map<String, Pair> mlMethods = new LinkedHashMap<>();
            mlMethods.put("void edu.kit.informatik.MinimalList.add(edu.kit.informatik.konto.Account)", new Pair("void add(Account)", false));
            mlMethods.put("void edu.kit.informatik.MinimalList.add(edu.kit.informatik.konto.Account,int)", new Pair("void add(Account, int)", false));
            mlMethods.put("boolean edu.kit.informatik.MinimalList.remove(int)", new Pair("boolean remove(int)", false));
            mlMethods.put("edu.kit.informatik.konto.Account edu.kit.informatik.MinimalList.getFirst()", new Pair("Account getFirst()", false));
            mlMethods.put("edu.kit.informatik.konto.Account edu.kit.informatik.MinimalList.getLast()", new Pair("Account getLast()", false));
            mlMethods.put("edu.kit.informatik.konto.Account edu.kit.informatik.MinimalList.get(int)", new Pair("Account get(int)", false));
            mlMethods.put("boolean edu.kit.informatik.MinimalList.contains(edu.kit.informatik.konto.Account)", new Pair("boolean contains(Account)", false));
            mlMethods.put("int edu.kit.informatik.MinimalList.size()", new Pair("int size()", false));
            check("MinimalList", "methods", MinimalList.class.getDeclaredMethods(), mlMethods);

            System.out.println();

            Map<String, Pair> bankMethods = new LinkedHashMap<>();
            bankMethods.put("int edu.kit.informatik.bank.Bank.createAccount(int)", new Pair("int createAccount(int)", false));
            bankMethods.put("boolean edu.kit.informatik.bank.Bank.removeAccount(int)", new Pair("boolean removeAccount(int)", false));
            bankMethods.put("boolean edu.kit.informatik.bank.Bank.containsAccount(int)", new Pair("boolean containsAccount(int)", false));
            bankMethods.put("boolean edu.kit.informatik.bank.Bank.transfer(int,int,int)", new Pair("boolean transfer(int,int,int)", false));
            bankMethods.put("int edu.kit.informatik.bank.Bank.length()", new Pair("int length()", false));
            bankMethods.put("edu.kit.informatik.konto.Account edu.kit.informatik.bank.Bank.getAccount(int)", new Pair("Account getAccount(int)", false));
            bankMethods.put("int edu.kit.informatik.bank.Bank.getBankCode()", new Pair("int getBankCode()", false));
            check("Bank", "methods", Bank.class.getDeclaredMethods(), bankMethods);

            System.out.println();

            Map<String, Pair> accountMethods = new LinkedHashMap<>();
            accountMethods.put("boolean edu.kit.informatik.konto.Account.withdraw(int)", new Pair("boolean withdraw(int)", false));
            accountMethods.put("void edu.kit.informatik.konto.Account.deposit(int)", new Pair("void deposit(int)", false));
            accountMethods.put("int edu.kit.informatik.konto.Account.getBalance()", new Pair("int getBalance()", false));
            accountMethods.put("int edu.kit.informatik.konto.Account.getAccountNumber()", new Pair("int getAccountNumber()", false));
            accountMethods.put("int edu.kit.informatik.konto.Account.getBankCode()", new Pair("int getBankAccount()", false));
            check("Account", "methods", Account.class.getDeclaredMethods(), accountMethods);
        } else if (args[0].equals("constructorcheck")) {
            check("Bank", "contructors", "Bank(int)", Bank.class.getDeclaredConstructors(), "edu.kit.informatik.bank.Bank(int)");
            System.out.println();
            check("Account", "contructors", "Account(int,int)", Account.class.getDeclaredConstructors(), "edu.kit.informatik.konto.Account(int,int)");
            System.out.println();
            check("PrivateAcount", "contructors", "PrivateAccount(int,int)", PrivateAccount.class.getDeclaredConstructors(), "edu.kit.informatik.konto.PrivateAccount(int,int)");
            System.out.println();
            check("BusinessAccount", "contructors", "BusinessAccount(int,int)", BusinessAccount.class.getDeclaredConstructors(), "edu.kit.informatik.konto.BusinessAccount(int,int)");
        } else if (args[0].equals("creation")) {
            Bank bank = new Bank(5);
            System.out.println("Created bank with bankcode " + bank.getBankCode() + ".");
            Account account = new Account(5, 10);
            System.out.println("Created account with bankcode " + account.getBankCode() + " and account number " + account.getAccountNumber() + ".");
            PrivateAccount pa = new PrivateAccount(6, 12);
            System.out.println("Created private account with bankcode " + pa.getBankCode() + " and account number " + pa.getAccountNumber() + ".");
            BusinessAccount ba = new BusinessAccount(7, 14);
            System.out.println("Created business account with bankcode " + ba.getBankCode() + " and account number " + ba.getAccountNumber() + ".");
        }
    }
    
    private static <T> void check(String name, String type, T[] actual, Map<String, Pair> expected) {
        for (T current : actual) {
            for (Map.Entry<String, Pair> entry : expected.entrySet()) {
                if (current.toString().contains(entry.getKey())) {
                    entry.getValue().setValue(true);
                }
            }
        }

        System.out.println("Checking " + type + " of " + name + "...");
        for (Pair current : expected.values()) {
            System.out.println(current.getKey() + ": " + (current.getValue() ? "found" : "missing"));
        }
    }

    private static <T> void check(String name, String type, String description, T[] actual, String expected) {
        System.out.println("Checking " + type + " of " + name + "...");
        boolean found = false;
        for (T current : actual) {
            if (current.toString().contains(expected)) {
                found = true;
            }
        }


        System.out.println(description + ": " + (found ? "found" : "missing"));
    }
    
    private static class Pair {
        String key;
        boolean value;
        
        private Pair(String key, boolean value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public boolean getValue() {
            return value;
        }
        
        public void setValue(boolean newValue) {
            value = newValue;
        }
    }
}