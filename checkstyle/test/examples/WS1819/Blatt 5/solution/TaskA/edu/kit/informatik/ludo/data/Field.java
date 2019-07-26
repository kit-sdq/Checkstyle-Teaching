/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.ludo.data;

import java.util.Objects;
import java.util.Stack;

/**
 * Represents a field on the board.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class Field implements Comparable<Field> {
    private static final int NO_NUMBER = -1;
    private static final int NOT_DIRECTLY_COMPARABLE = 0xFFFFF;

    private final String fieldName;
    private final int fieldNumber;

    private final Stack<Meeple> meeplesOnThisField;

    /**
     * Creates a new Field with the given number.
     * 
     * @param fieldNumber
     *            - the given number
     */
    protected Field(final int fieldNumber) {
        this.fieldName = null;
        this.fieldNumber = fieldNumber;
        this.meeplesOnThisField = new Stack<>();
    }

    /**
     * Creates a new Field with the given name.
     * 
     * @param fieldName
     *            - the given name
     */
    protected Field(final String fieldName) {
        this.fieldName = fieldName;
        this.fieldNumber = NO_NUMBER;
        this.meeplesOnThisField = new Stack<>();
    }

    /**
     * Determines whether the given field is a numbered field.
     * 
     * @return whether the given field is a numbered field
     */
    protected boolean isNumberedField() {
        return this.fieldNumber != NO_NUMBER;
    }

    /**
     * Determines whether the given field is a named field.
     * 
     * @return whether the given field is a named field
     */
    protected boolean isNamedField() {
        return !isNumberedField();
    }

    /**
     * Determines whether the given field is a start field.
     * 
     * @return whether the given field is a start field
     */
    protected boolean isStartField() {
        return isNamedField() && this.getFieldName().startsWith("" + LudoBoard.PREFIX_NAME_START_FIELD);
    }

    /**
     * Determines whether the given field is a final field.
     * 
     * @return whether the given field is a final field
     */
    protected boolean isFinalField() {
        return isNamedField() && !isStartField();
    }

    /**
     * Determines whether the given field is empty.
     * 
     * @return whether the given field is empty
     */
    protected boolean isEmpty() {
        return this.getCountMeeplesOnThisField() == 0;
    }

    /**
     * Gets the count of meeples on this field.
     * 
     * @return the count of meeples on this field
     */
    public int getCountMeeplesOnThisField() {
        return this.meeplesOnThisField.size();
    }

    /**
     * Gets the first meeple on this field.
     * 
     * @return the first meeple on this field
     */
    protected Meeple getFirstMeepleOnThisField() {
        if (!isEmpty()) {
            return this.meeplesOnThisField.peek();
        } else {
            throw new IllegalStateException("the field is empty!");
        }
    }

    /**
     * Gets the first meeple on this field and removes it from this field.
     * 
     * @return the first meeple on this field
     */
    public Meeple popFirstMeepleOnThisField() {
        if (!isEmpty()) {
            return this.meeplesOnThisField.pop();
        } else {
            throw new IllegalStateException("the field " + this + " is empty!");
        }
    }

    /**
     * Pushes the given meeple on this field so that it is the first meeple after
     * this method returns.
     * 
     * @param meeple
     *            - the given meeple
     */
    public void pushMeepleOnThisField(final Meeple meeple) {
        this.meeplesOnThisField.push(meeple);
    }

    /**
     * Gets the name of this named field.
     * 
     * @return the name of this named field
     */
    protected String getFieldName() {
        if (!isNumberedField()) {
            return this.fieldName;
        } else {
            throw new IllegalStateException("the field " + this + " is not a named field!");
        }
    }

    /**
     * Gets the field number of this numbered field.
     * 
     * @return the field number of this numbered field
     */
    protected int getFieldNumber() {
        if (isNumberedField()) {
            return this.fieldNumber;
        } else {
            throw new IllegalStateException("the field " + this + " is not a numbered field!");
        }
    }

    @Override
    public int hashCode() {
        final boolean numField = isNumberedField();
        final String eqlStr = numField ? ("" + this.fieldNumber) : (this.fieldName);
        return Objects.hash(getClass(), numField, eqlStr);
    }

    @Override
    public boolean equals(final Object other) {
        if (getClass().equals(other.getClass())) {
            final Field otherField = (Field) other;
            if (compareTo(otherField) == 0) {
                return isNumberedField() || (isNamedField() && this.fieldName.equals(otherField.fieldName));
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return isNumberedField() ? ("" + this.fieldNumber) : (this.fieldName);
    }

    @Override
    public int compareTo(final Field other) {
        if (isNumberedField() && other.isNumberedField()) {
            return compareToNumberedFields(other);
        } else if (!isNumberedField() && !other.isNumberedField()) {
            final int ret = compareToNamedFields(other);
            if (ret != NOT_DIRECTLY_COMPARABLE) {
                return ret;
            }
        } else if (isNumberedField() && !other.isNumberedField()) {
            return compareToThisNumberedOtherNamed(other);
        }

        return (-1) * other.compareTo(this);
    }

    private int compareToNumberedFields(final Field other) {
        return this.getFieldNumber() - other.getFieldNumber();
    }

    private int compareToNamedFields(final Field other) {
        if (isStartField() && other.isStartField()) {
            // both fields are start fields
            return 0;
        } else if (isStartField()) {
            // only this field is a start field, the other one a final field
            final int indexOtherField = other.getFieldName().charAt(0) - LudoBoard.PREFIX_NAME_FINAL_FIELD;
            return (-1) * (LudoBoard.NUMBER_OF_NORMAL_FIELDS + indexOtherField);
        } else if (other.isFinalField()) {
            // both fields are final fields
            return getFieldName().charAt(0) - other.getFieldName().charAt(0);
        }
        return NOT_DIRECTLY_COMPARABLE; // this and other must be swapped first
    }

    private int compareToThisNumberedOtherNamed(final Field other) {
        if (other.isStartField()) {
            // other one is start field
            return this.getFieldNumber() + 1;
        } else {
            // other one is final field
            final int indexOtherField = other.getFieldName().charAt(0) - LudoBoard.PREFIX_NAME_FINAL_FIELD;
            return (-1) * ((LudoBoard.NUMBER_OF_NORMAL_FIELDS - this.getFieldNumber()) + indexOtherField);
        }
    }
}
