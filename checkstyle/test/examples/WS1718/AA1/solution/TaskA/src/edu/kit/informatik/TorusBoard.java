package edu.kit.informatik;

public class TorusBoard extends Board {
    public TorusBoard(int size) {
        super(size);
    }

    @Override
    public Cell getCell(int i, int j) {
        int n = getBoardSize();
        int row = ((i % n) + n) % n;
        int col = ((j % n) + n) % n;
        return board[row][col];
    }
}
