/**
 * Repr&auml;sentiert ein Schachbrett zugeschnitten f&uuml;r die L&ouml;sung 
 * des n-Damenproblems. Das Brett kann eine Gr&ouml;sse von 
 * <tt>n</tt>x<tt>0</tt> bis <tt>n</tt>x<tt>n</tt> annehmen. Dabei ist je 
 * Spalte immer genau eine Damenfigur platziert.
 * 
 * @author anonymous
 *
 */
public class Board {

    /**
     * Speichert die Belegung des Spielbrettes. Je Spalte gibt es einen
     * Arrayeintrag. Dieser Eintrag gibt an an welcher Position/Reihe sich
     * die Damenfigur in dieser Spalte befindet. Die Eintraege sind immer
     * >= 0 und < numberOfRows.
     */
    private final int[] board;

    /** Anzahl der Reihen des Spielbretts. Muss > 0 sein.*/
    private final int numberOfRows;

    /**
     * Erstellt ein leeres Schachbrett mit <tt>numberOfRows</tt> Reihen und
     * 0 Spalten.
     * @param numberOfRows Anzahl der Reihen des Brettes. Die Anzahl muss
     * gr&ouml;sser oder gleich 1 sein.
     */
    public Board(int numberOfRows) {
	this(0, numberOfRows);
    }

    /*
     * Erstellt ein neues Schachbrett mit <tt>numberOfRows</tt> Reihen und
     * <tt>numberOfColumns</tt> Spalten. Die Anzahl der Reihen muss dabei 
     * > 0 und die Anzahl der Spalten >= 0 sein. Zudem darf die Anzahl
     * der Spalten nur maximal so gross wie die Anzahl der Reihen sein.
     * Achtung: Nach dem Aufruf dieser Methode sind noch keine Damen platziert.
     * Dies muss erst noch in einem weiteren Schritt erfolgen.
     */
    private Board(int numberOfColumns, int numberOfRows) {
	assert (numberOfColumns >= 0) : "Negative Anzahl Spalten";
	assert (numberOfRows > 0) : "Anzahl Reihen nicht > 0";
	assert (numberOfRows >= numberOfColumns) : "Zu viele Spalten (>Reihen)";

	this.board = new int[numberOfColumns];
	this.numberOfRows = numberOfRows;
    }

    /**
     * Gibt die Anzahl der Spalten des Spielbretts zur&uuml;ck.
     * @return Anzahl der Spalten des Spielbretts.
     */
    public int getNumberOfColumns() {
	return board.length;
    }

    /**
     * &Uuml;berpr&uuml;ft ob das Spielbrett leer ist.
     * @return <tt>true</tt> wenn das Spielbrett leer ist.
     */
    public boolean isEmpty() {
	/* Da man bei unserem Brett nur Spalten hinzufuegen kann, wenn man
	 * zeitgleich auch eine Dame platziert, genuegt es zu testen ob
	 * eine Spalte existiert um zu erkennen ob das Brett leer ist.
	 */
	return board.length == 0;
    }

    /**
     * Erstellt ein um eine Spalte breiteres neues Spielbrett. Dabei wird die
     * Belegung dieses Spielbretts &uuml;bernommen. Zus&auml;tzlich wird
     * auch noch eine neue Dame in die hinzugekommene Spalte gesetzt.
     * @param rowPosOfQueen Reihe in der die Damenfigur in der neuen Spalte
     * platziert werden soll. Die Position muss gr&oumlsser als 0 und kleiner
     * als die Anzahl der Reihen im Brett sein.
     * @return Gibt das neue breitere Spielbrett zur&uuml;ck.
     */
    public Board newColumn(int rowPosOfQueen) {
	// Koennte auch als IllegalArgumentException realisiert sein.
	assert (rowPosOfQueen >= 0 && rowPosOfQueen < numberOfRows)
	    : "Position der Dame ist nicht im Bereich >= 0 und < " 
		+ numberOfRows + ". Position: " + rowPosOfQueen;

	final Board extendedBoard = new Board(board.length + 1, numberOfRows);
	System.arraycopy(board, 0, extendedBoard.board, 0, board.length);
	extendedBoard.board[extendedBoard.board.length - 1] = rowPosOfQueen;

	return extendedBoard;
    }

    /**
     * &Uuml;berpr&uuml;ft ob sich die Damenfiguren auf dem Spielbrett 
     * gegenseitig bedrohen.
     * @return <tt>true</tt> falls sich die Damen bedrohen, sonst 
     * <tt>false</tt>.
     */
    public boolean hasConflict() {
	boolean conflict = false;

	/*
	 * Die letzte Spalte wird zuerst auf Konflikte ueberprueft, da in der
	 * Regel dort ein Konflikt auftreten wird und man somit
	 * schneller terminiert. Die restlichen Spalten muessen anschliessend
	 * aber trotzdem noch auf Konflikte untersucht werden, da deren 
	 * Konfliktfreiheit bei einer anderen Anwendung der Board Klasse nicht
	 * garantiert werden kann.
	 */
	for (int col = board.length - 1; !conflict && col >= 0; col--) {
	    for (int i = 0; !conflict && i < board.length; i++) {
		if (i != col) {
		    final int delta = col - i;

		    // 1. zwei damen in der selben reihe
		    // 2. dame in spalte i ist diagonal erreichbar.
		    conflict = (board[col] == board[i])
			    || (board[i] == board[col] + delta 
			    || board[i] == board[col] - delta);
		}
	    }
	}

	return conflict;
    }

    /**
     * Gibt die Belegung des Brettes als String zur&uuml;ck. Leere Felder
     * werden als "." repr&auml;sentiert, Damenfiguren sind mit "*"
     * gekennzeichnet.
     * @return Belegung des Brettes als String.
     */
    public String toString() {
	final StringBuilder result = new StringBuilder();

	for (int column = 0; column < board.length; column++) {
	    for (int row = 0; row < numberOfRows; row++) {
		result.append(board[column] == row ? "* " : ". ");
	    }

	    if (column < board.length - 1) {
		result.append(System.getProperty("line.separator"));
	    }
	}

	return result.toString();
    }

}
