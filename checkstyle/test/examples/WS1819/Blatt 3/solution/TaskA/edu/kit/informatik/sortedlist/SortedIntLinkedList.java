/*
 * Copyright (c) 2018, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.sortedlist;

/**
 * The SortedIntLinkedList is an implementation of the sorted-int-list-interface requested by the assignment.
 * It is implemented using an implementation of a doubly-linked-list.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class SortedIntLinkedList {
    private static final int NOT_FOUND = -1;
    
    private Cell head;
    private Cell last;
    private int size;
    
    /**
     * Creates a new empty SortedIntLinkedList.
     */
    public SortedIntLinkedList() {
        this.head = null;
        this.last = null;
        this.size = 0;
    }
    
    /**
     * Adds the given number to the list at the correct position (in order).
     * 
     * @param number - number to be added
     */
    public void add(final int number) {
        final Cell toBeInserted = new Cell(number);
        final Iterator iter = new Iterator(true);
        if (this.head == null) {
            //list is empty, so we can insert at the beginning
            this.head = toBeInserted;
            this.last = this.head;
        } else {
            boolean inserted = false;
            while (iter.hasNext()) {
                final int next = iter.getNext();
                if (next >= number) { //list is sorted, so if next is >= number we can insert
                    //inserting the cell after the predecessor cell and before the next cell
                    final Cell nextCell = iter.cell;
                    final Cell predCell = nextCell.pred;
                    nextCell.pred = toBeInserted;
                    if (predCell != null) {
                        predCell.next = toBeInserted;
                    }
                    
                    //setting the predecessor and next fields of the cell to be inserted
                    toBeInserted.pred = predCell;
                    toBeInserted.next = nextCell;
                    
                    if (nextCell == this.head) {
                        //setting head to the cell to be inserted if its next cell is the former head
                        this.head = toBeInserted;
                    }
                    
                    inserted = true;
                    break;
                }
                iter.next();
            }
            
            if (!inserted) {
                //element is larger than all other elements, so we can insert at the end
                final Cell lastBefore = this.last;
                this.last = toBeInserted;
                this.last.pred = lastBefore;
                lastBefore.next = this.last;
            }
        }
        
        this.size++;
    }

    /**
     * Gets the size of the list.
     * 
     * @return the size of the list
     */
    public int size() {
        return this.size;
    }

    /**
     * Determines whether the list is empty.
     * 
     * @return whether the list is empty
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Clears the list, i.e. removes all elements from it.
     */
    public void clear() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Gets the number at the given index of the sorted list.
     * 
     * @param index - the given index
     * @return the number at the given index of the sorted list
     */
    public int get(final int index) {
        return getCell(index).number;
    }
    
    /**
     * Gets the cell at the given index.
     * 
     * @param index - the given index
     * @return the respective cell
     */
    private Cell getCell(final int index) {
        final int size = this.size;
        final int mid = size / 2;
        
        final Iterator iter;
        final int loopIndex;
        if (index < mid) {
            iter = new Iterator(true);
            loopIndex = index;
        } else {
            iter = new Iterator(false);
            loopIndex = (size - 1) - index;
        }
        
        for (int i = 0; i < loopIndex; i++) {
            iter.next();
        }
        
        return iter.cell;
    }

    /**
     * Gets the first index of occurrence of the given number.
     * 
     * @param number - the given number
     * @return the first index of occurrence or -1 when list does not contain number
     */
    public int indexOf(final int number) {
        return indexOf(number, true);
    }

    /**
     * Gets the last index of occurrence of the given number.
     * 
     * @param number - the given number
     * @return the last index of occurrence or -1 when list does not contain number
     */
    public int lastIndexOf(final int number) {
        return indexOf(number, false);
    }
    
    /**
     * Gets the index of the given number.
     * 
     * @param number - the given number
     * @param forward - whether the list should be traversed forwards
     * @return the respective index or -1 if not found
     */
    private int indexOf(final int number, final boolean forward) {
        final Iterator iter = forward ? new Iterator(true) : new Iterator(false);
        for (int i = 0; i < this.size; i++) {
            final int element = iter.next();
            if (number == element) {
                return forward ? i : ((this.size - 1) - i);
            }
        }
        
        return NOT_FOUND;
    }

    /**
     * Removes the first occurrence of the given number from the list.
     * 
     * @param number - the given number
     * @return whether the number was contained before removal
     */
    public boolean remove(final int number) {
        final int index = indexOf(number);
        if (index != -1) {
            final Cell toBeRemoved = getCell(index);
            if (toBeRemoved != this.head && toBeRemoved != this.last) {
                toBeRemoved.pred.next = toBeRemoved.next;
                toBeRemoved.next.pred = toBeRemoved.pred; 
            } else if (toBeRemoved == this.head && toBeRemoved == this.last) {
                //list has only one element, so removing it is equivalent to clearing the list
                clear();
                return true;
            } else if (toBeRemoved == this.head) {
                //toBeRemoved is the head of the list, so the next cell becomes the new head
                this.head = toBeRemoved.next;
            } else {
                //toBeRemoved is the last of the list, so the pred cell becomes the new last
                this.last = toBeRemoved.pred;
            }
            this.size--;
            return true;
        }
        
        return false;
    }

    /**
     * Determines whether the list contains the given number.
     * 
     * @param number - the given number
     * @return whether the list contains the given number
     */
    public boolean contains(final int number) {
        return indexOf(number) != NOT_FOUND;
    }

    /**
     * Gets the requested string representation.
     * 
     * @return the requested string representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        
        final Iterator iter = new Iterator(true);
        while (iter.hasNext()) {
            final int next = iter.next();
            sb.append(next);
            sb.append(",");
        }
        
        if (this.size > 0) {
            sb.deleteCharAt(sb.length() - 1); 
        }
        
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * Represents a list cell of this doubly-linked list.
     */
    private class Cell {
        private int number;
        private Cell pred;
        private Cell next;
        
        public Cell(final int number) {
            this.number = number;
            this.pred = null;
            this.next = null;
        }
    }
    
    /**
     * Internal helper iterator for easier traversation of the list.
     */
    private class Iterator {
        private Cell cell;
        private boolean forward;
        
        public Iterator(final boolean forward) {
            this.cell = forward ? head : last;
            this.forward = forward;
        }
        
        public boolean hasNext() {
            return cell != null;
        }
        
        public int next() {
            final int ret = cell.number;
            cell = forward ? cell.next : cell.pred;
            return ret;
        }
        
        public int getNext() {
            return cell.number;
        }
    }

}
