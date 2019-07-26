/**
 * Modelliert das n-Damenproblem. Bei diesem Problem soll eine Anzahl von
 * <tt>n</tt> Damen auf einem <tt>n</tt>x<tt>n</tt> grossem Schachbrett so
 * platziert werden, dass sie sich nicht gegenseitig bedrohen k&ouml;nnen.
 * 
 * @author anonymous
 *
 */
public class QueensProblem {

    /**
     * Anzahl der Damen und Seitenl&auml;nge des Schachbretts.
     */
    private final int n;

    /**
     * Erstellt eine Instanz des n-Damenproblems f&uuml;r eingegebenes 
     * <tt>n</tt>.
     * @param n Anzahl der Damen und Seitenl&auml;nge des Schachbretts.
     * Muss > 0 sein.
     */
    public QueensProblem(final int n) {
	// Koennte auch als IllegalArgumentException realisiert sein.
	assert (n > 0) : "Das n-Damenproblem ergibt nur mit n > 0 Sinn.";

	this.n = n;
    }

    /**
     * Versucht das n-Damenproblem zu l&ouml;sen und gibt bei Erfolg eine
     * g&uuml;ltige Belegung des Schachbretts zur&uuml;ck. Wenn das Problem 
     * nicht l&ouml;sbar ist, wird ein leeres Brett zur&uuml;ckgegeben.
     * @return Schachbrett mit einer g&uuml;ltigen Belegung oder ein leeres
     * Brett, wenn das Problem nicht l&ouml;sbar ist.
     */
    public Board solve() {
	return extend(new Board(n));
    }

    /*
     * Erweitert ein uebergebenes konfliktfreies n x m Schachbrett mit m Damen 
     * so, dass ein konfliktfreies n x n Brett mit n Damen entsteht. Dabei muss 
     * m >=0 und <= n sein. Wenn kein konfliktfreies n x n Brett gefunden werden
     * kann, dann wird eine leeres n x 0 Brett zurueckgegeben.
     */
    private Board extend(Board oldBoard) {
	assert (oldBoard.getNumberOfColumns() <= n) : "Das Brett ist zu gross.";
	assert (!oldBoard.hasConflict()) : "Das Brett hat einen Konflikt.";
	
	Board solution;
	
	if (oldBoard.getNumberOfColumns() == n) {
	    solution = oldBoard;
	} else {
	    solution = new Board(n);

	    for (int i = 0; solution.isEmpty() && i < n; i++) {
		final Board board = oldBoard.newColumn(i);
        
		if (!board.hasConflict()) {
		    solution = extend(board);
		}	
	    }
	}
	
	return solution;
    }

}
