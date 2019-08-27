public class Main {
	public static void main(String[] args) {
		Account account = new Account(252627);
		AccountHolder accountHolder = new AccountHolder(account, "John", "Doe", 42);
		System.out.println("Account number: " + account.getAccountNumber());
		System.out.println("Bank code: " + account.getBankCode());
		System.out.println("Balance: " + account.getBalance());
		System.out.println("First name: " + accountHolder.getFirstName());
		System.out.println("Last name: " + accountHolder.getLastName());
		
		System.out.println("\nDeposit 5 to John Doe...");
		account.deposit(5);
		System.out.println("Account number: " + account.getAccountNumber());
		System.out.println("Bank code: " + account.getBankCode());
		System.out.println("Balance: " + account.getBalance());
		System.out.println("First name: " + accountHolder.getFirstName());
		System.out.println("Last name: " + accountHolder.getLastName());
		
		System.out.println("\nWithdraw 10 from John Doe...");
		System.out.println(account.withdraw(10));
		System.out.println("Account number: " + account.getAccountNumber());
		System.out.println("Bank code: " + account.getBankCode());
		System.out.println("Balance: " + account.getBalance());
		System.out.println("First name: " + accountHolder.getFirstName());
		System.out.println("Last name: " + accountHolder.getLastName());
		
		System.out.println("\nWithdraw 4 from John Doe...");
		System.out.println(account.withdraw(4));
		System.out.println("Account number: " + account.getAccountNumber());
		System.out.println("Bank code: " + account.getBankCode());
		System.out.println("Balance: " + account.getBalance());
		System.out.println("First name: " + accountHolder.getFirstName());
		System.out.println("Last name: " + accountHolder.getLastName());
		
		Account accountTarget = new Account(123456);
		AccountHolder accountHolderTarget = new AccountHolder(accountTarget, "Max", "Mustermann", 11);
		
		System.out.println("\nTransfer 1 from John Doe to Max Mustermann...");
		account.transfer(accountTarget, 1);
		System.out.println("Account number: " + account.getAccountNumber());
		System.out.println("Bank code: " + account.getBankCode());
		System.out.println("Balance: " + account.getBalance());
		System.out.println("First name: " + accountHolder.getFirstName());
		System.out.println("Last name: " + accountHolder.getLastName());
		System.out.println("Account number: " + accountTarget.getAccountNumber());
		System.out.println("Bank code: " + accountTarget.getBankCode());
		System.out.println("Balance: " + accountTarget.getBalance());
		System.out.println("First name: " + accountHolderTarget.getFirstName());
		System.out.println("Last name: " + accountHolderTarget.getLastName());
	}
}