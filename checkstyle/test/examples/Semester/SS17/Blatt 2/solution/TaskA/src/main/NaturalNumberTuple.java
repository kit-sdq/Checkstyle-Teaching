/**
 * The {@code NaturalNumberTuple} class encapsulates one or more natural numbers (excluding 0) organized as a tuple.
 * This means:
 * <ul>
 * <li>encapsulated numbers are ordered, and</li>
 * <li>the same number may appear more than once.</li>
 * </ul>
 * 
 * @version 1.1
 */
public class NaturalNumberTuple {
    private int[] tuple;

    /**
     * Constructs a new tuple comprising the given {@code numbers} in the same order.
     * <p>
     * All values in {@code numbers} that are smaller or equal to 0 are ignored.
     *
     * @param numbers
     *            the numbers to initialize this tuple
     */
    public NaturalNumberTuple(int[] numbers) {
        //Count all valid values for exact array size
        int count = 0;
        for (int number : numbers) {
            if (number > 0) {
                count++;
            }
        }

        this.tuple = new int[count];

        //Copy all valid values to array
        count = 0;
        for (int i = 0; i < numbers.length; i++) {
            if (numbers[i] > 0) {
                this.tuple[count++] = numbers[i];
            }
        }
    }

    /**
     * @return the smallest number stored in this tuple; -1 if this tuple is empty.
     */
    public int min() {
        int min = Integer.MAX_VALUE;
        for (int number : tuple) {
            if (min > number) {
                min = number;
            }
        }

        return (min == Integer.MAX_VALUE) ? -1 : min;
    }

    /**
     * 
     * @return the largest number stored in this tuple; -1 if this tuple is empty.
     */
    public int max() {
        int max = -1;
        for (int number : tuple) {
            if (max < number) {
                max = number;
            }
        }

        return max;
    }

    /**
     * Inserts the specified {@code number} at the end of this tuple. If {@code number} is smaller or equal to 0, this
     * method has no effect.
     * 
     * @param number
     *            the number to be inserted
     * @return the tuple resulting from inserting the specified {@code number}. If {@code number} is smaller or equal to
     *         0, this tuple is returned without any modifications.
     */
    public NaturalNumberTuple insert(int number) {
        if (number <= 0) {
            return this;
        }

        int[] newTuple = new int[tuple.length + 1];
        System.arraycopy(tuple, 0, newTuple, 0, tuple.length);
        newTuple[newTuple.length - 1] = number;

        return new NaturalNumberTuple(newTuple);
    }

    /**
     * Removes any occurrence of the specified {@code number} in this tuple. If {@code number} is not contained in this
     * tuple, this method has no effect.
     * 
     * @param number
     *            the number to be removed
     * @return the tuple resulting from removing the specified {@code number}. If {@code number} is not contained in
     *         this tuple, this tuple is returned without any modifications.
     */
    public NaturalNumberTuple remove(int number) {
        if (number <= 0) {
            return this;
        }

        int[] newTuple = new int[tuple.length];
        System.arraycopy(tuple, 0, newTuple, 0, tuple.length);

        //Set every value to remove to zero, creating a new NaturalNumberTuple will remove these zeros.
        for (int i = 0; i < tuple.length; i++) {
            if (tuple[i] == number) {
                newTuple[i] = 0;
            }
        }

        return new NaturalNumberTuple(newTuple);
    }

    /**
     * @param number
     *            the number whose index is to be returned. The first number in this tuple has index 0.
     * @return the index of the first occurrence of the specified {@code number} in this tuple; -1 if this tuple does
     *         not contain the {@code number}.
     */
    public int indexOf(int number) {
        for (int i = 0; i < tuple.length; i++) {
            if (tuple[i] == number) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Counts the occurrences of {@code number} in this tuple.
     * 
     * @param number
     *            the number to count
     * @return how often the specified {@code number} is contained in this tuple
     */
    public int countNumbers(int number) {
        int count = 0;
        for (int current : tuple) {
            if (current == number) {
                count++;
            }
        }

        return count;
    }

    /**
     * Swaps the numbers stored at positions {@code firstPosition} and {@code secondPosition}.
     * <p>
     * Notice that the first number in this tuple has index 0.
     * 
     * @param firstPosition
     *            the first index
     * @param lastPosition
     *            the second index
     * @return the tuple resulting from swapping the numbers stored at the specified positions. If either
     *         {@code firstPosition} or {@code secondPosition} is an invalid index, this method return this tuple
     *         without any modifications.
     */
    public NaturalNumberTuple swap(int firstPosition, int lastPosition) {
        if (!validateIndex(firstPosition) || !validateIndex(lastPosition)) {
            return this;
        }

        int[] newTuple = new int[tuple.length];
        System.arraycopy(tuple, 0, newTuple, 0, tuple.length);
        swap(firstPosition, lastPosition, newTuple);

        return new NaturalNumberTuple(newTuple);
    }

    //Swaps the position of two values in an integer array.
    private void swap(int firstPosition, int lastPosition, int[] array) {
        int temp = array[firstPosition];
        array[firstPosition] = array[lastPosition];
        array[lastPosition] = temp;
    }

    //Checks if an index is valid for this tuple
    private boolean validateIndex(int index) {
        return index >= 0 && index < tuple.length;
    }

    /**
     * Returns a <i>copy</i> of this tuple that contains every number stored in this tuple exactly once. This method has
     * no effect on this tuple.
     * 
     * @return a copy of this tuple without any repeated occurrence of any number
     */
    public NaturalNumberTuple toSet() {
        int[] newTuple = new int[tuple.length];
        System.arraycopy(tuple, 0, newTuple, 0, tuple.length);

        //Sort tuple before conversion to set to make the conversion easier.
        //The resulting tuple will be sorted aswell.
        sort(newTuple);

        int currentUniqueValue = 0;
        for (int i = 0; i < newTuple.length; i++) {
            if (currentUniqueValue != newTuple[i]) {
                currentUniqueValue = newTuple[i];
            } else {
                newTuple[i] = 0;
            }
        }
        return new NaturalNumberTuple(newTuple);
    }

    //Sorts a given array in-place.
    public void sort(int[] array) {
        quicksort(0, array.length - 1, array);
    }

    // Sorts an array with the Quicksort sorting algorithm. (https://de.wikipedia.org/wiki/Quicksort)
    private void quicksort(int lowerIndex, int upperIndex, int array[]) {
        if (lowerIndex < upperIndex) {
            int i = lowerIndex;
            int j = upperIndex - 1;
            int pivot = array[upperIndex];

            do {
                //Search for an element greater than the pivot starting from the lower limit
                while (array[i] <= pivot && i < upperIndex) {
                    i++;
                }
                //Search for an element smaller than the pivot starting from the upper limit
                while (array[j] >= pivot && j > lowerIndex) {
                    j--;
                }

                //Swap the found values
                if (i < j) {
                    swap(i, j, array);
                }
            } while (i < j);

            //Swap pivot to its correct place
            if (array[i] > pivot) {
                swap(i, upperIndex, array);
            }

            //The pivot is now at its correct place at index i, run quicksort with left and right remainder.
            quicksort(lowerIndex, i - 1, array);
            quicksort(i + 1, upperIndex, array);
        }
    }

    /**
     * Indicates if this tuple is equal to the specified {@code tuple}. Two tuples are considered equal if they contain
     * the same sequence of numbers.
     * 
     * @param tuple
     *            the other tuple with which to compare
     * @return {@code true} if this tuple is the same as the specified {@code tuple}; {@code false} otherwise
     */
    public boolean equals(NaturalNumberTuple tuple) {
        if (this.tuple.length != tuple.tuple.length) {
            return false;
        }

        for (int i = 0; i < this.tuple.length; i++) {
            if (this.tuple[i] != tuple.tuple[i]) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tuple.length; i++) {
            sb.append(tuple[i]);
            if (i != tuple.length - 1) {
                sb.append(",");
            }
        }

        return sb.toString();
    }

    /**
     * Prints this tuple to the console. Therefore this method outputs a single line of numbers representing this tuple.
     * Numbers are separated by a comma, without any whitespace before or after the comma.
     * 
     * @since 1.1
     */
    public void print() {
        System.out.println(toString());
    }
}
