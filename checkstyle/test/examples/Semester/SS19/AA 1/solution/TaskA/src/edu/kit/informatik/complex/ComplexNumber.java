/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.complex;

/**
 * Represents an complex number.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public final class ComplexNumber implements Expression {
    private final int real;
    private final int imaginary;

    /**
     * Creates a new complex number with the given real part and imaginary part.
     * ComplexNumber is an immutable type.
     * 
     * @param real - the real part
     * @param imaginary - the imaginary part
     */
    public ComplexNumber(final int real, final int imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    /**
     * Adds the other complex number to this one and returns the result.
     * 
     * @param other - the number to be added
     * @return the sum of this and the other number
     */
    public ComplexNumber add(final ComplexNumber other) {
        return new ComplexNumber(this.real + other.real, this.imaginary + other.imaginary);
    }

    /**
     * Subtracts the other complex number from this one and returns the result.
     * 
     * @param other - the number to be subtracted
     * @return the difference of this and the other number
     */
    public ComplexNumber subtract(final ComplexNumber other) {
        return new ComplexNumber(this.real - other.real, this.imaginary - other.imaginary);
    }

    /**
     * Multiplies the other complex number to this one and returns the result.
     * 
     * @param other - the number to be multiplied
     * @return the product of this and the other number
     */
    public ComplexNumber multiply(final ComplexNumber other) {
        final int realResult = this.real * other.real - this.imaginary * other.imaginary;
        final int imaginaryResult = this.real * other.imaginary + this.imaginary * other.real;
        return new ComplexNumber(realResult, imaginaryResult);
    }
    
    /**
     * Divides this number by the other complex number and returns the result.
     * 
     * @param other - the divisor
     * @return the division result of this complex number divided by the other number
     * @throws FormatException 
     */
    public ComplexNumber divide(final ComplexNumber other) throws FormatException {
        final int denominator = other.real * other.real + other.imaginary * other.imaginary;
        if (denominator == 0) {
            throw new FormatException("division by zero");
        }
        final int numeratorReal = this.real * other.real + this.imaginary * other.imaginary;
        final int numeratorImaginary = this.imaginary * other.real - this.real * other.imaginary;
        return new ComplexNumber(numeratorReal / denominator, numeratorImaginary / denominator);
    }
    
    @Override
    public String toString() {
        return "(" + this.real + " + " + this.imaginary + "i)";
    }

    @Override
    public ComplexNumber evaluate() {
        return this;
    }
}
