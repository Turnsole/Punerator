package com.lastminutedevice.punerator;

/**
 * Static methods for encoding or presenting tokens in American Soundex notation.
 * http://www.archives.gov/research/census/soundex.html
 */
public class Soundex {
    static char[] vowels = {'a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'};
    static char nullCharacter = '\u0000';

    /**
     * Encodes the input string in a modified American Soundex notation. It is modified from true Soundex
     * in that it is not padded or truncated to meet the four-character standard.
     *
     * @param input a given English word
     * @return a simplified pronunciation key
     */
    public static String encode(String input) {
        StringBuilder builder = new StringBuilder(input == null ? 0 : input.length());
        if (input != null && input.length() > 0) {
            builder.append(input.charAt(0));
            char newCharacter, previousCharacter = nullCharacter;
            for (int i = 1; i < input.length(); i++) {
                newCharacter = encodeChar(input.charAt(i));
                if (newCharacter != nullCharacter && newCharacter != previousCharacter) {
                    builder.append(newCharacter);
                }

                // For purposes of this encoding, h and w do not cause a consonant to repeat.
                if (newCharacter != 'h' && newCharacter != 'w') {
                    previousCharacter = newCharacter;
                }
            }
        }
        return builder.toString();
    }

    /**
     * Find the substring that fuzzy-matches the input token and capitalize it.
     *
     * @param input     a token to search for
     * @param candidate the string to search
     * @return the candidate string with the portion that resembles the input token capitalized, or null if no match found
     */
    public static String findMatch(String input, String candidate) {
        for (int i = 0; i < candidate.length(); i++) {
            if (candidate.charAt(i) == input.charAt(0) || candidate.toLowerCase().charAt(i) == input.charAt(0)) {
                int endIndex = i + findEndIndex(candidate.substring(i).toLowerCase(), encode(input));
                if (endIndex > i) {
                    StringBuilder result = new StringBuilder(candidate.substring(0, i));
                    result.append(input.toUpperCase());
                    if (endIndex < candidate.length()) {
                        // If the match ends in a vowel and the remaining substring begins with one, collapse them.
                        if (isVowel(result.charAt(result.length() - 1)) && isVowel(candidate.charAt(endIndex))) {
                            result.setLength(result.length() - 1);
                        }
                        result.append(candidate.substring(endIndex));
                    }
                    return result.toString();
                }
            }
        }

        return null;
    }

    private static boolean isVowel(char character) {
        for (char test : vowels) {
            if (character == test) return true;
        }
        return false;
    }

    /**
     * Find the index of the last character of the match.
     * <p/>
     *
     * @return the index of the last character of the match, or 0 if there is no match.
     */
    private static int findEndIndex(String candidate, String pattern) {
        int found = 0;
        char previousMatch = nullCharacter;
        for (int i = 0; i < candidate.length(); i++) {
            if (found < pattern.length()) {
                char test = (i == 0) ? candidate.charAt(0) : encodeChar(candidate.charAt(i));
                // If test equals pattern, increment found.
                if (test == pattern.charAt(found)) {
                    found++;
                    previousMatch = test;
                }
                // If test is a neither nonencoding or a repeat, the match has failed.
                else if (test != nullCharacter && test != previousMatch) {
                    break;
                }
            } else {
                return i;
            }
        }
        return 0;
    }

    /**
     * @param input a character to encode
     * @return the Soundex representation of that character
     */
    public static char encodeChar(char input) {
        // Much faster than regex and slightly faster than Set operations.
        // Since this can be called over a million times for even small dictionaries, speed matters most here.
        // Experimentally, this is the fastest order of character classes to check for.
        switch (input) {
            // Labials and labio-dentals
            case 'B':
            case 'b':
            case 'F':
            case 'f':
            case 'P':
            case 'p':
            case 'V':
            case 'v':
                return '1';
            // Gutterals and sibilants
            case 'C':
            case 'c':
            case 'G':
            case 'g':
            case 'J':
            case 'j':
            case 'K':
            case 'k':
            case 'Q':
            case 'q':
            case 'S':
            case 's':
            case 'X':
            case 'x':
            case 'Z':
            case 'z':
                return '2';
            // Dentals
            case 'D':
            case 'd':
            case 'T':
            case 't':
                return '3';
            // Palatal fricative
            case 'L':
            case 'l':
                return '4';
            // Nasals
            case 'M':
            case 'm':
            case 'N':
            case 'n':
                return '5';
            // Dental fricative
            case 'R':
            case 'r':
                return '6';
            // Skip resonants
            default:
                return nullCharacter;
        }
    }
}
