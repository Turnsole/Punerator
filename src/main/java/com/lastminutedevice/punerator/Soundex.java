package com.lastminutedevice.punerator;

public class Soundex {
    static char nullCharacter = '\u0000';

    /**
     * Encodes the input string in a modified American Soundex notation. It is modified from true Soundex
     * in that it is not padded or truncated to meet the four-character standard.
     *
     * @param input       a given English word
     * @return a simplified pronunciation key
     */
    public static String encode(String input) {

        StringBuilder builder = new StringBuilder();
        if (input != null && input.length() > 0) {
            input = input.toLowerCase();
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
                    String result = i > 0 ? candidate.substring(0, i) : "";
                    result += input.toUpperCase();
                    if (endIndex < candidate.length()) {
                        result += candidate.substring(endIndex);
                    }
                    return result;
                }
            }
        }

        return null;
    }

    /**
     * Find the index of the last character of the match.
     * <p/>
     * Return 0 if there is no match.
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
        switch (input) {
            // Labials and labio-dentals
            case 'b':
            case 'f':
            case 'p':
            case 'v':
                return '1';
            // Gutterals and sibilants
            case 'c':
            case 'g':
            case 'j':
            case 'k':
            case 'q':
            case 's':
            case 'x':
            case 'z':
                return '2';
            // Dentals
            case 'd':
            case 't':
                return '3';
            // Palatal fricative
            case 'l':
                return '4';
            // Nasals
            case 'm':
            case 'n':
                return '5';
            // Dental fricative
            case 'r':
                return '6';
            // Skip resonants
            default:
                return nullCharacter;
        }
    }
}
