/*
 * Copyright (c) 2019, IPD Reussner. All rights reserved.
 */

package edu.kit.informatik;

/**
 * This class provides functionality for Strings like stated in the assignment.
 * This class is a utility class.
 *
 * @author Thomas Weber
 * @version 1.0
 */
public final class StringUtility {

	/**
	 * Capitalizes the given word.
	 *
	 * @param word the word we want to capitalize
	 * @return the capitalized word
	 */
	public static String capitalize(String word) {
		return word.substring(0, 1).toUpperCase() + word.substring(1);
	}

	/**
	 * Counts the occurrences of the given character in the given word.
	 *
	 * @param word      string where we want to count the occurrences of the given
	 *                  character
	 * @param character the character we are counting
	 * @return the occurrence count
	 */
	public static int countCharacter(String word, char character) {
		int count = 0;

		// for is used here for demonstration purposes only
		// foreach is clearly preferred !
		for (int i = 0; i < word.length(); i++) {
			// remember that we compare data of type char here, not objects (equals) !
			if (word.charAt(i) == character) {
				count += 1;
			}
		}

		return count;
	}

	/**
	 * Checks if the two given words are anagrams. The two words are anagrams if
	 * they can be converted into each other by reordering the characters.
	 *
	 * @param word1 the first word
	 * @param word2 the second word
	 * @return returns a boolean signaling if the two words are anagrams
	 */
	public static boolean isAnagram(String word1, String word2) {
		// if the words have a different lengths, they cannot be anagrams
		if (word1.length() != word2.length()) {
			return false;
		}

		String anagram = word2;

		// we do not need the indices so we use foreach
		for (final char character : word1.toCharArray()) {
			final int index = anagram.indexOf(character);
			if (index == -1) {
				return false;
			}
			anagram = removeCharacter(anagram, index);
		}

		return anagram.isEmpty();
	}

	/**
	 * Checks whether or not the given string is a palindrome.
	 *
	 * @param word a string which may be a palindrome
	 * @return whether or not the given string is a palindrome
	 */
	public static boolean isPalindrome(String word) {
		return word.equals(reverse(word));
	}

	/**
	 * Removes a character at the given index from the given word if possible. If
	 * not the word is returned unchanged.
	 *
	 * @param word  a string we want to remove a character from
	 * @param index the index of the character we want to remove
	 * @return the word without that character if the index can be found in the
	 *         given word
	 */
	public static String removeCharacter(String word, int index) {
		if ((index < 0) || (index >= word.length())) {
			return word;
		}

		return word.substring(0, index) + word.substring(index + 1);
	}

	/**
	 * Reverses the given String and returns it as a new String instance.
	 *
	 * @param word the word that will be reversed
	 * @return the reversed word in a new String object
	 */
	public static String reverse(String word) {
		String reverse = "";

		// we need access to the indices of the characters, so we have to use for instead of foreach
        for (int i = word.length() - 1; i >= 0; i--) {
			reverse += word.charAt(i);
		}

		return reverse;
	}

	/**
	 *  Do not use this constructor. Utility classes shall not be instantiated.
	 */
	private StringUtility() throws AssertionError {
        throw new AssertionError("No StringUtility instances for you!");
	}
}
