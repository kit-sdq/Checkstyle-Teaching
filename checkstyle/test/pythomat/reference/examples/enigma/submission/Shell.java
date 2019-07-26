package student;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import enigma.EnigmaComponent;

public class Shell {

    private Shell() {
    }

    public static void main(String[] args) throws IOException {

	if (args.length % 3 != 0) {
	    System.out.println("Error, invalid number of arguments");
	    return;
	}
	int rotorCount = args.length / 3;
	
	/*
	int[] patchboard = convert("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray());
	int[] reflector = convert("BACDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray());
	*/

	int[] patchboard = convert("JWULCMNOHPQZYXIRADKEGVBTSF".toCharArray());
	int[] reflector = convert("IMETCGFRAYSQBZXWLHKDVUPOJN".toCharArray());

	Enigma enigma = new Enigma(patchboard, rotorCount, reflector);
	for (int i = rotorCount - 1; i >= 0; i--) {
	    if (args[3 * i].length() != 26 || !args[3 * i].matches("[A-Z]*")) {
		System.out.println("Error, invalid permutation for rotor " + i);
		return;
	    } else if (!checkPermutation(convert(args[3 * i].toCharArray()))) {
		System.out.println("Error, invalid permutation for rotor " + i);
		return;
	    } else if (!args[3 * i + 1].matches("[A-Z]")) {
		System.out.println("Error, invalid tick for rotor " + i);
		return;
	    } else if (!args[3 * i + 2].matches("[A-Z]")) {
		System.out.println("Error, invalid position for rotor " + i);
		return;
	    }
	    enigma.addRotor(convert(args[3 * i].toCharArray()),
			    convert(args[3 * i + 1].charAt(0)),
			    convert(args[3 * i + 2].charAt(0)));
	}

	BufferedReader in = 
	    new BufferedReader(new InputStreamReader(System.in));

	String input;
	do {
	    input = in.readLine();
		if (input == null) {
			continue;
		}
	    if (!input.matches("[A-Z]*")) {
		System.out.println("Error, invalid input.");
		input = "";
	    } else if (input.length() > 0) {
		for (int i = 0; i < input.length(); i++) {
		    System.out.print(convert(
		        enigma.encode(convert(input.charAt(i)))));
		}
		System.out.println("");
	    }
	} while (input != null && input.length() > 0);
    }

    private static boolean checkPermutation(int[] perm) {
	boolean[] charPresent = new boolean[perm.length];
	for (int i = 0; i < perm.length; i++) {
	    if (charPresent[perm[i]]) {
		return false;
	    } else {
		charPresent[perm[i]] = true;
	    }
	}
	return true;
    }

    private static int convert(char letter) {
	return letter - 65;
    }

    private static char convert(int letter) {
	return (char) (letter + 65);
    }

    private static int[] convert(char[] letters) {
	int[] l = new int[letters.length];
	for (int i = 0; i < letters.length; i++) {
	    l[i] = convert(letters[i]);
	}
	return l;
    }

}
