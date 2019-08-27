public class AccountHolder {
	Account account;
	String firstName;
	String lastName;
	int accountHolderNumber;
	
	public AccountHolder(Account account, String firstName, String lastName, int accountHolderNumber) {
		this.account = account;
		this.firstName = firstName;
		this.lastName = lastName;
		this.accountHolderNumber = accountHolderNumber;
	}
	
	//Needed to do transfers by just knowing the AccountHolder.
	public Account getAccount() {
		return account;
	}
	
	//Needed because the customer can change account types etc.
	public void setAccount(Account account) {
		this.account = account;
	}
	
	//Needed to get the customers name for invoices etc.
	public String getFirstName() {
		return firstName;
	}
	
	//No setter as the first name shouldn't change throughout life.
	
	//Needed to get the customers name for invoices etc.
	public String getLastName() {
		return lastName;
	}
	
	//Needed in case the customer is changing its last name because of marriage.
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	//needed to get the account holder number for invoices etc.
	public int accountHolderNumber() {
		return accountHolderNumber;
	}
}