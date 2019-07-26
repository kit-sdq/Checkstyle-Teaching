/**
 * Löst das NDamen-Problem für ein als Argument gegebens N
 *
 * @author anonymous
 *
 */
public final class NQueens {

    /**
     * Wenn eine L&ouml;sung existiert, dann wird eine g&uuml;ltige Belegung des Brettes
     * ausgegeben. Ansonsten wird eine Meldung ausgegeben, dass das Problem
     * nicht l&ouml;sbar ist. Falsche Eingaben werden mit einer Fehlermeldung 
     * abgefangen. 
     * 
     * @param argv Eine Zahl N für die das N-Damen Problem gelöst werden soll
     */
    public static void main(String[] args) {
        boolean noNumber = false;
        int n = 0;

        if (args.length < 1) {
            System.out.println("Error! Keine Zahl eingegeben.");
            System.exit(1);
        }
        
        try {
            n = Integer.parseInt(args[0]);
        } catch (NumberFormatException nf) {
            noNumber = true;
        }

        if (!noNumber && n > 0) {
            final QueensProblem qp = new QueensProblem(n);
            print(qp.solve());
        } else if (!noNumber) {
            System.out.println("Error! Zahl ist <= 0.");
        } else {
            System.out.println("Error! Keine Zahl eingegeben.");
        }
    }

    private static void print(Board board) {
        if (board.isEmpty()) {
            System.out.println("Keine Belegung gefunden.");
        } else {
            System.out.println(board.toString());
        }
    }

}
