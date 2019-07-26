/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik.matrix;

import java.util.StringJoiner;

/**
 * Represents a mathematical matrix.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class Matrix {
    private static final String SEPERATOR = ",";

    private final int[][] matrix;

    /**
     * Creates a new matrix.
     * 
     * @param matrix
     *            - the elements of the matrix
     * @throws IllegalArgumentException
     *             - if argument is not valid
     */
    public Matrix(final int[][] matrix) throws IllegalArgumentException {
        this.matrix = matrix;
        validateMatrix();
    }

    private void validateMatrix() {
        final int columnNumber = this.matrix[0].length;
        for (final int[] row : this.matrix) {
            if (row.length != columnNumber) {
                throw new IllegalArgumentException("illegal dimensions!");
            }
        }
    }

    /**
     * Adds this and the other matrix.
     * 
     * @param other
     *            - the other matrix
     * @return the resulting matrix
     * @throws IllegalArgumentException
     *             - if argument is not valid
     */
    public Matrix add(final Matrix other) throws IllegalArgumentException {
        if (this.getRowNumber() != other.getRowNumber() || this.getColumnNumber() != other.getColumnNumber()) {
            throw new IllegalArgumentException("illegal dimensions!");
        }

        final int[][] result = new int[getRowNumber()][getColumnNumber()];

        for (int i = 0; i < getRowNumber(); i++) {
            for (int j = 0; j < getColumnNumber(); j++) {
                result[i][j] = this.matrix[i][j] + other.matrix[i][j];
            }
        }

        return new Matrix(result);
    }

    /**
     * Multiplies this and the other matrix.
     * 
     * @param other
     *            - the other matrix
     * @return the resulting matrix
     * @throws IllegalArgumentException
     *             - if argument is not valid
     */
    public Matrix multiply(final Matrix other) throws IllegalArgumentException {
        if (this.getColumnNumber() != other.getRowNumber()) {
            throw new IllegalArgumentException("illegal dimensions!");
        }

        final int height = getRowNumber();
        final int width = other.getColumnNumber();
        final int[][] result = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int resultEntry = 0;
                for (int k = 0; k < getColumnNumber(); k++) {
                    resultEntry += this.matrix[i][k] * other.matrix[k][j];
                }
                result[i][j] = resultEntry;
            }
        }

        return new Matrix(result);
    }

    /**
     * Multiplies this matrix with a scalar.
     * 
     * @param scalar
     *            - the scalar to be multiplied
     * @return the resulting matrix
     * @throws IllegalArgumentException
     *             - if argument is not valid
     */
    public Matrix scalarMultiplication(final int scalar) throws IllegalArgumentException {
        final int[][] result = new int[getRowNumber()][getColumnNumber()];

        for (int i = 0; i < getRowNumber(); i++) {
            for (int j = 0; j < getColumnNumber(); j++) {
                result[i][j] = scalar * this.matrix[i][j];
            }
        }

        return new Matrix(result);
    }

    /**
     * Creates a transposition of this matrix.
     * 
     * @return a transposition of this matrix
     * @throws IllegalArgumentException
     *             - if argument is not valid
     */
    public Matrix transpose() throws IllegalArgumentException {
        final int[][] result = new int[getColumnNumber()][getRowNumber()];

        for (int i = 0; i < getColumnNumber(); i++) {
            for (int j = 0; j < getRowNumber(); j++) {
                result[i][j] = this.matrix[j][i];
            }
        }

        return new Matrix(result);
    }

    /**
     * Calculates the main diagonal of this matrix.
     * 
     * @return the main diagonal
     * @throws IllegalStateException
     *             - if matrix is not quadratic
     */
    public int[] mainDiagonal() throws IllegalStateException {
        if (getRowNumber() != getColumnNumber()) {
            throw new IllegalStateException("matrix not quadratic!");
        }

        final int[] result = new int[getRowNumber()];

        for (int i = 0; i < getRowNumber(); i++) {
            result[i] = this.matrix[i][i];
        }

        return result;
    }

    private int getRowNumber() {
        return matrix.length;
    }

    private int getColumnNumber() {
        return matrix[0].length;
    }

    @Override
    public String toString() {
        final StringJoiner result = new StringJoiner(SEPERATOR);
        for (final int[] row : this.matrix) {
            for (final int value : row) {
                result.add(String.valueOf(value));
            }
        }
        return result.toString();
    }
}
