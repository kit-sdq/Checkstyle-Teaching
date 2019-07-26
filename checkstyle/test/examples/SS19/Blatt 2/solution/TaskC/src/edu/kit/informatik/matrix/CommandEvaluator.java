package edu.kit.informatik.matrix;

import edu.kit.informatik.InputException;

/**
 * Evaluates an input command.
 * 
 * @author Jonathan Schenkenberger
 * @version 1.0
 */
public class CommandEvaluator {
    private static final String SEPERATOR_COMMAND = " ";
    private static final String SEPERATOR_META = ":";
    private static final String SEPERATOR_ELEMENTS = ",";

    private static final String META_LIST_ELEMENT = "\\d+:";
    private static final String ELEMENT = "(-?(\\d+))";
    private static final String ELEMENT_LIST = "(" + ELEMENT + ",)*" + ELEMENT;

    private static final String ADD = "matrix-addition" + SEPERATOR_COMMAND + "(" + META_LIST_ELEMENT + "){2}"
            + ELEMENT_LIST;
    private static final String MULTIPLY = "matrix-multiplication" + SEPERATOR_COMMAND + "(" + META_LIST_ELEMENT
            + "){3}" + ELEMENT_LIST;
    private static final String SCALAR_MULTIPLY = "scalar-multiplication" + SEPERATOR_COMMAND + "(" + META_LIST_ELEMENT
            + "){3}" + ELEMENT_LIST;
    private static final String TRANSPOSITION = "transposition" + SEPERATOR_COMMAND + "(" + META_LIST_ELEMENT + "){2}"
            + ELEMENT_LIST;
    private static final String MAIN_DIAGONAL = "main-diagonal" + SEPERATOR_COMMAND + META_LIST_ELEMENT + ELEMENT_LIST;

    private String command;
    private String argument;
    private boolean twoMatrices;
    private int[] metaData;
    private Matrix matrixOne;
    private Matrix matrixTwo;

    /**
     * Creates a new command evaluator for the given input.
     * 
     * @param input
     *            - the given input
     * @throws IllegalArgumentException
     *             - if the command is unknown
     */
    public CommandEvaluator(final String input) throws IllegalArgumentException {
        this.twoMatrices = false;
        if (input.matches(ADD)) {
            this.command = ADD;
            this.twoMatrices = true;
        } else if (input.matches(MULTIPLY)) {
            this.command = MULTIPLY;
            this.twoMatrices = true;
        } else if (input.matches(SCALAR_MULTIPLY)) {
            this.command = SCALAR_MULTIPLY;
        } else if (input.matches(TRANSPOSITION)) {
            this.command = TRANSPOSITION;
        } else if (input.matches(MAIN_DIAGONAL)) {
            this.command = MAIN_DIAGONAL;
        } else {
            throw new IllegalArgumentException("unknown command!");
        }

        this.argument = input.split(SEPERATOR_COMMAND)[1];
        setMetaData();
    }

    /**
     * Evaluates a command.
     * 
     * @return the result of the executed command
     * @throws InputException
     *             - if the arguments of the command are semantically incorrect
     */
    public String evaluate() throws InputException {
        final String[] allElements = getAllElements();
        if (this.twoMatrices) {
            final int rowNumFirst = this.metaData[0];
            final int colNumFirst = this.metaData[1];

            if (allElements.length <= rowNumFirst * colNumFirst) {
                // InputException is used when parsing is not possible
                // IllegalArgumentException is used when a passed argument is incorrect
                throw new InputException("element count not correct!");
            }

            final int lengthOfFirst = rowNumFirst * colNumFirst;
            final int lengthOfSecond = allElements.length - lengthOfFirst;
            final String[] elementsOfFirstMatrix = new String[lengthOfFirst];
            final String[] elementsOfSecondMatrix = new String[lengthOfSecond];
            System.arraycopy(allElements, 0, elementsOfFirstMatrix, 0, lengthOfFirst);
            System.arraycopy(allElements, lengthOfFirst, elementsOfSecondMatrix, 0, lengthOfSecond);

            switch (this.command) {
                case ADD:
                    this.matrixOne = createMatrix(rowNumFirst, colNumFirst, elementsOfFirstMatrix);
                    this.matrixTwo = createMatrix(rowNumFirst, colNumFirst, elementsOfSecondMatrix);
                    return this.matrixOne.add(this.matrixTwo).toString();
                case MULTIPLY:
                    this.matrixOne = createMatrix(rowNumFirst, colNumFirst, elementsOfFirstMatrix);
                    this.matrixTwo = createMatrix(colNumFirst, this.metaData[2], elementsOfSecondMatrix);
                    return this.matrixOne.multiply(this.matrixTwo).toString();
                default: assert false; // should not happen
            }
        } else {
            switch (this.command) {
                case SCALAR_MULTIPLY:
                    this.matrixOne = createMatrix(this.metaData[1], this.metaData[2], allElements);
                    return this.matrixOne.scalarMultiplication(this.metaData[0]).toString();
                case TRANSPOSITION:
                    this.matrixOne = createMatrix(this.metaData[0], this.metaData[1], allElements);
                    return this.matrixOne.transpose().toString();
                case MAIN_DIAGONAL:
                    this.matrixOne = createMatrix(this.metaData[0], this.metaData[0], allElements);
                    try {
                        final int[] mainDiagonal = this.matrixOne.mainDiagonal();
                        return new Matrix(new int[][] {mainDiagonal}).toString();
                    } catch (IllegalStateException e) {
                        throw new InputException(e.getMessage());
                    }
                default: assert false; // should not happen
            }
        }

        return null; // should not happen
    }

    private void setMetaData() throws IllegalArgumentException {
        final String[] splitRes = this.argument.split(SEPERATOR_META);
        final int[] result = new int[splitRes.length - 1];
        for (int i = 0; i < result.length; i++) {
            try {
                result[i] = Integer.parseInt(splitRes[i]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Number format not correct!");
            }
        }
        this.metaData = result;
    }

    private String[] getAllElements() {
        final String[] metaSplitRes = this.argument.split(SEPERATOR_META);
        final String elementsStr = metaSplitRes[metaSplitRes.length - 1];
        return elementsStr.split(SEPERATOR_ELEMENTS);
    }

    private static Matrix createMatrix(final int rowNum, final int colNum, final String[] elements)
            throws InputException {
        if (elements.length != rowNum * colNum) {
            throw new InputException("element count not correct!");
        }

        final int[][] matrix = new int[rowNum][colNum];

        for (int i = 0; i < elements.length; i++) {
            try {
                matrix[i / colNum][i % colNum] = Integer.parseInt(elements[i]);
            } catch (NumberFormatException e) {
                throw new InputException(e.getMessage());
            }
        }

        return new Matrix(matrix);
    }
}
