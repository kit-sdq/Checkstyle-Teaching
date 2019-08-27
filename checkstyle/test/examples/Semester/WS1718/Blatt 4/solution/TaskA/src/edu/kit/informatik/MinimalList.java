package edu.kit.informatik;

public class MinimalList<T> {
    private Element head;
    private int size;

    /**
     * Adds a new value to the end of the list.
     * 
     * @param value The value to add to the list.
     */
    public void add(T value) {
        if (head == null) {
            head = new Element(value, null);
        } else {
            getLastElement().next = new Element(value, null);
        }

        size++;
    }

    /**
     * Adds a new value to the given index in the list. If the index doesn't exist or is invalid, it adds to the end.
     * 
     * @param value The value to add to the list.
     * @param index The index to add the value at.
     */
    public void add(T value, int index) {
        if (head == null || index >= size || index < 0) {
            add(value);
        } else if (index == 0) {
            //Special case if added at the beginning
            head = new Element(value, head);
            size++;
        } else {
            //Get element in front of desired index 
            Element current = getPreviousElement(index);
            current.next = new Element(value, current.next);
            size++;
        }
    }

    /**
     * Removes the value at the given index from the list.
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
     * Removes a given value from the list. Only removes the first occurence of the value.
     * 
     * @param value The value to remove.
     * @return true, if the value exists and got removed, false otherwise.
     */
    public boolean remove(T value) {
        if (head == null) {
            return false;
        } else if (head.value.equals(value)) {
            head = head.next;
            size--;
        } else {
            Element previous = getPreviousElement(value);
            if (previous == null) {
                return false;
            }
            
            previous.next = previous.next.next;
            size--;
        }
        
        return true;
    }

    /**
     * Gets the first value of the list (the head).
     * 
     * @return The first value or null if the list is empty.
     */
    public T getFirst() {
        if (head == null) {
            return null;
        }

        return head.value;
    }

    /**
     * Gets the last value of the list (at index n - 1).
     * 
     * @return The last value or null if the list is empty.
     */
    public T getLast() {
        Element last = getLastElement();
        if (last == null) {
            return null;
        }
        return last.value;
    }

    /**
     * Gets the value at a specific index.
     * 
     * @param index The index to get the the value from.
     * @return The value at the index or null if the index doesn't exist.
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        } else {
            Element current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current.value;
        }
    }
    
    /**
     * Gets the specific instance of a value from the list.
     * 
     * @param value The value to get the instance of.
     * @return The instance matching the value or null if it doesn't exist. 
     */
    public T get(T value) {
        Element current = head;
        while (current != null) {
            if (current.value.equals(value)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Checks if a value exists in the list.
     * 
     * @param value The value to check for existance.
     * @return true, if the value exists, false otherwise.
     */
    public boolean contains(T value) {
        Element current = head;
        while (current != null) {
            if (current.value.equals(value)) {
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
    
    //Returns null if there is no previous element (i.e. the head matches or value doesn't exist)
    private Element getPreviousElement(T value) {
        Element previous = null;
        Element current = head;
        while (current != null) {
            if (current.value.equals(value)) {
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
        private T value;
        private Element next;

        private Element(T value, Element next) {
            this.value = value;
            this.next = next;
        }
    }
}