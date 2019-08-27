public class Account {
	private static int accountCounter;
	
	private int accountNumber;
	private int bankCode;
	private int balance;	
	
	public Account(int bankCode) {
		this.bankCode = bankCode;
		this.balance = 0;
		this.accountNumber = accountCounter++;
	}
	
	public boolean withdraw(int amount) {
		if (balance - amount >= 0) {
			balance -= amount;
			return true;
		}
		
		return false;
	}
	
	public void deposit(int amount) {
		balance += amount;
	}
	
	public void transfer(Account account, int amount) {
		withdraw(amount);
		account.deposit(amount);
	}
	
	//Needed as the account number is important info that should be retrievable for the customer to use.
	public int getAccountNumber() {
		return accountNumber;
	}
	
	//No setter as the account number is determined by the internal counter.
	
	//Needed as the bank code is important info that should be retrievable for the customer to use.
	public int getBankCode() {
		return bankCode;
	}
	
	//No setter as the bank code is bound to the bank and shouldn't change.
	
	//Needed as the customer wants to obviously know his balance.
	public int getBalance() {
		return balance;
	}
	
	//Not setter as there is withdraw and deposit to set it safely.
	
	//No getter or setter for accountCounter as it is 
	//a) static and
	//b) an internal counter that noone should have access to.
}