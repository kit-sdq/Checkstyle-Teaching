package programmieren;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

/**
 * Diese Klasse stellt einige einfache Methoden zur Ein- und Ausgabe auf einem
 * Terminal zur Verf&uuml;gung.
 *
 * @author P. Pepper und Gruppe
 * @version 1.1 Modifiziert von A. Lochbihler
 * @version 1.2 Modifiziert von F. Kelbert
 * @version XS 	Reduziert von F. Merz, M. Iser
 */
public final class MyTerminal {

    /** Privater Konstruktor gegen Objekterzeugung. */
    private MyTerminal() {
    }

    /** Ein Reader-Objekt, das bei allen Lesezugriffen verwendet wird. */
    private static BufferedReader in = new BufferedReader(
                    new InputStreamReader(System.in));

    /** Allgemeine Fehlermeldung. */
    private static final String ERROR = "Error! ";

    /** Fehlermeldung fuer ungueltige int-Eingabe. */
    private static final String ERROR_INVALID_INT =
        "Ungueltige Integer-Zahl! (Nochmal eingeben) ";

    /**
     * Liest eine Zeichenkette vom Terminal.
     *
     * @return das gelesene Zeichen. Modifiziert von A. L ochbihler, damit kein
     *         Null-String mehr zurueckgegeben wird.
     */
    public static String readString() {
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    /**
     * Gibt eine Zeichenkette aus und erwartet die Eingabe einer Zeichenkette.
     *
     * @param prompt die auszugebende Zeichenkette.
     * @return die eingegebene Zeichenkette.
     */
    public static String askString(String prompt) {
        System.out.print(prompt);
        return readString();
    }

    /**
     * Liest eine ganze Zahl (32 Bit) vom Terminal.
     *
     * @return die gelesene Zahl.
     */
    public static int readInt() {
        while (true) {
            for (String token : getTokens()) {
                try {
                    return Integer.parseInt(token);
                } catch (NumberFormatException e) {
                    System.err.println(ERROR_INVALID_INT);
                }
            }
        }
    }

    /**
     * Gibt eine Zeichenkette aus und erwartet die Eingabe einer ganzen Zahl
     * (32 Bit).
     *
     * @param prompt die auszugebende Zeichenkette.
     * @return die eingegebene Zahl.
     */
    public static int askInt(String prompt) {
        System.out.print(prompt);
        return readInt();
    }

    /**
     * Liest eine Zeile von der Eingabe und spaltet Sie an whitespace-Zeichen
     * auf.
     *
     * @return Array von Strings, die die
     */
    private static String[] getTokens() {
        String line;
        try {
            line = in.readLine();
        } catch (IOException e) {
            throw new Error(e);
        }

        if (line == null) {
            System.err.println(ERROR);
            return null;
        } else {
            return line.split("\\s");
        }
    }
}
