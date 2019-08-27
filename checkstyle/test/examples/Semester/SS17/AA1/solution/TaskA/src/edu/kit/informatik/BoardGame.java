package edu.kit.informatik;

import java.util.ArrayList;
import java.util.List;

public class BoardGame {
    private Cell[][] board;
    private BoardType boardType;
    private List<Player> players;
    private boolean isRunning;

    public BoardGame(BoardType boardType, int boardSize, int playerCount) {
        this.boardType = boardType;
        this.players = new ArrayList<>();
        this.isRunning = true;
        
        createBoard(boardSize);
        
        for (int i = 1; i <= playerCount; i++) {
            players.add(new Player(i, getCellByCarthesian(0, 0)));
        }
    }
    
    public int getBoardSize() {
        return board.length;
    }
    
    public List<Player> getPlayers() {
        return players;
    }

    public Cell getCellByCarthesian(int x, int y) {
        Coordinates coords = carthesianToMatrix(x, y);
        return board[coords.getX()][coords.getY()];
    }
    
    public Player getOccupier(Cell cell) {
        for (Player player : players) {
            if (player.getCurrentCell().equals(cell)) {
                return player;
            }
        }
        
        return null;
    }

    public boolean isFirstCell(Cell cell) {
        return cell.getOwnCoordinates().getX() == 0 && cell.getOwnCoordinates().getY() == 0;
    }

    public boolean isLastCell(Cell cell) {
        return cell.getOwnCoordinates().getX() == getBoardEdge() && cell.getOwnCoordinates().getY() == getBoardEdge();
    }

    public void setCell(Cell cell) {
        Coordinates matrixCoordinates = carthesianToMatrix(cell.getOwnCoordinates().getX(), cell.getOwnCoordinates().getY());
        board[matrixCoordinates.getX()][matrixCoordinates.getY()] = cell;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public int move(int dieRoll) throws GameException {
        int stepsLeft = dieRoll;
        Player currentPlayer = players.remove(0);
        Coordinates coordinates = currentPlayer.getCurrentCell().getOwnCoordinates();
        int x = coordinates.getX();
        int y = coordinates.getY();
        
        while (stepsLeft > 0) {
            int direction = (y % 2 == 0) ? 1 : ((y == getBoardEdge()) ? 1 : -1);

            int stepsToEdge = 0;
            if (direction == 1) {
                stepsToEdge = getBoardEdge() - x;
            } else {
                stepsToEdge = x;
            }
            
            if (y == getBoardEdge() && stepsToEdge < stepsLeft) {
                switch (boardType) {
                    case STANDARD:
                        players.add(currentPlayer);
                        throw new GameException("number of steps exceed last cell.");
                    case TORUS:
                        x = direction * ((stepsLeft - stepsToEdge) % getBoardSize());
                        stepsLeft = 0;
                        break;
                }
            } else if (stepsToEdge >= stepsLeft) {
                x += direction * stepsLeft;
                stepsLeft = 0;
            } else {
                x += direction * stepsToEdge;
                y++;
                if (y == getBoardEdge()) {
                    x = 0;
                }
                stepsLeft -= (stepsToEdge + 1);
            }
        }
        
        Cell newCell = getCellByCarthesian(x, y);
        if (newCell.isReferencing()) {
            newCell = newCell.getReferencedCell();
        }
        
        Player occupier = getOccupier(newCell);
        if (occupier != null) {
            occupier.setCurrentCell(getCellByCarthesian(0, 0));
        }
        
        currentPlayer.setCurrentCell(newCell);
        
        players.add(currentPlayer);

        if (isLastCell(newCell)) {
            isRunning = false;
            return currentPlayer.getNumber();
        }
        
        return 0;
    }
    
    private void createBoard(int boardSize) {
        board = new Cell[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = new Cell(matrixToCarthesian(i, j));
            }
        }
    }
    
    private Coordinates matrixToCarthesian(int row, int column) {
        return new Coordinates(column, getBoardEdge() - row);
    }
    
    private Coordinates carthesianToMatrix(int x, int y) {
        return new Coordinates(getBoardEdge() - y, x);
    }

    private int getBoardEdge() {
        return board.length - 1;
    }
    
}