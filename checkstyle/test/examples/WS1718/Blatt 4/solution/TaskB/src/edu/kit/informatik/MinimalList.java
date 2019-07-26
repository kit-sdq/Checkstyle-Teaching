package edu.kit.informatik;

import edu.kit.informatik.konto.Account;

public class MinimalList {
    private Element head;
    private int size;

    /**
     * Adds a new account to the end of the list.
     *
     * @param account The account to add to the list.
     */
    public void add(Account account) {
        if (head == null) {
            head = new Element(account, null);
        } else {
            getLastElement().next = new Element(account, null);
        }

        size++;
    }

    /**
     * Adds a new account to the given index in the list. If the index doesn't exist or is invalid, it adds to the end.
     *
     * @param account The account to add to the list.
     * @param index The index to add the value at.
     */
    public void add(Account account, int index) {
        if (head == null || index >= size || index < 0) {
            add(account);
        } else if (index == 0) {
            //Special case if added at the beginning
            head = new Element(account, head);
            size++;
        } else {
            //Get element in front of desired index 
            Element current = getPreviousElement(index);
            current.next = new Element(account, current.next);
            size++;
        }
    }

    /**
     * Removes the account at the given index from the list.
     *
     * @param index The index of the element to remove.
     * @return true, if the index existed and got removed, false otherwise.
     */
    public boolean remove(int index) {
        if (index < 0 || index >= size) {
            return false;
        } else if (index == 0) {
            head = head.next;
            size--;
        } else {
            Element previous = getPreviousElement(index);
            previous.next = previous.next.next;
            size--;
        }

        return true;
    }

    /**
     * Removes a given account from the list. Only removes the first occurence of the account.
     *
     * @param account The account to remove.
     * @return true, if the value exists and got removed, false otherwise.
     */
    public boolean remove(Account account) {
        if (head == null) {
            return false;
        } else if (head.account.equals(account)) {
            head = head.next;
            size--;
        } else {
            Element previous = getPreviousElement(account);
            if (previous == null) {
                return false;
            }
            
            previous.next = previous.next.next;
            size--;
        }
        
        return true;
    }

    /**
     * Gets the first account of the list (the head).
     *
     * @return The first account or null if the list is empty.
     */
    public Account getFirst() {
        if (head == null) {
            return null;
        }

        return head.account;
    }

    /**
     * Gets the last account of the list (at index n - 1).
     *
     * @return The last account or null if the list is empty.
     */
    public Account getLast() {
        Element last = getLastElement();
        if (last == null) {
            return null;
        }
        return last.account;
    }

    /**
     * Gets the account at a specific index.
     *
     * @param index The index to get the the account from.
     * @return The account at the index or null if the index doesn't exist.
     */
    public Account get(int index) {
        if (index < 0 || index >= size) {
            return null;
        } else {
            Element current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current.account;
        }
    }

    /**
     * Gets the specific instance of an account from the list.
     *
     * @param account The account to get the instance of.
     * @return The instance matching the account or null if it doesn't exist. 
     */
    public Account get(Account account) {
        Element current = head;
        while (current != null) {
            if (current.account.equals(account)) {
                return current.account;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Checks if a account exists in the list.
     *
     * @param account The account to check for existance.
     * @return true, if the account exists, false otherwise.
     */
    public boolean contains(Account account) {
        Element current = head;
        while (current != null) {
            if (current.account.equals(account)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    /**
     * Gets the size of the list, i.e. the amount of entries.
     *
     * @return The size of the list.
     */
    public int size() {
        return size;
    }

    private Element getPreviousElement(int index) {
        Element current = head;
        for (int i = 1; i < index; i++) {
            current = current.next;
        }
        return current;
    }
    
    //Returns null if there is no previous element (i.e. the head matches or account doesn't exist)
    private Element getPreviousElement(Account account) {
        Element previous = null;
        Element current = head;
        while (current != null) {
            if (current.account.equals(account)) {
                return previous;
            }
            previous = current;
            current = current.next;
        }
        return null;
    }

    private Element getLastElement() {
        if (head == null) {
            return null;
        }
        
        Element current = head;
        while (current.next != null) {
            current = current.next;
        }
        return current;
    }

    private final class Element {
        private Account account;
        private Element next;

        private Element(Account account, Element next) {
            this.account = account;
            this.next = next;
        }
    }
}