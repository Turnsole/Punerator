package com.lastminutedevice.punerator;

public class Soundex {

    /**
     * Encodes the input string in a modified American Soundex notation. It is modified from true Soundex
     * in that it is not padded or truncated to meet the four-character standard, and soft sounds are preserved as 0.
     *
     * @param input a given English word
     * @return a simplified pronunciation key
     */
    public static String encode(String input) {
        input = input.toLowerCase();

        StringBuilder builder = new StringBuilder();
        if (input.length() > 0) {
            builder.append(input.charAt(0));
        }

        if (input.length() > 1) {
            Character newCharacter = null;
            for (int i = 1; i < input.length(); i++) {
                newCharacter = encodeChar(input.charAt(i));
                if (newCharacter != null && builder.charAt(builder.length() - 1) != newCharacter) { // again, char array?
                    builder.append(newCharacter);
                }
            }
        }
        return builder.toString();
    }

    public static String formatMatch(String input, String candidate) {
        for (int i = 0; i < candidate.length(); i++) {
            if(candidate.charAt(i) == input.charAt(0)) {
                if (matchesRest(input, candidate.substring(i)) ) {
                    String result = i > 0 ? candidate.substring(0, i) : "";
                    result += input.toUpperCase();
                    if (result.length() < candidate.length()) {
                        result += candidate.substring(result.length());
                    }
                    return result;
                }
            }
        }

        return null;
    }

    /**
     * @param input a string to look for
     * @param candidate a string to look in
     * @return whether candidate contains either an exact or Soundex match for input
     */
    private static boolean matchesRest(String input, String candidate) {
        if (candidate.length() >= input.length()) {
            // Skipping through any number of vowels or repeated symbols, match on the consonants.
            int inputIndex = 0;
            int candidateIndex = 0;
            while (candidateIndex < candidate.length() && inputIndex < input.length()) {
                Character candidateChar = candidate.charAt(candidateIndex);
                while (candidateIndex < candidate.length() - 1 && encodeChar(candidateChar) == null) {
                    candidateIndex++;
                    candidateChar = candidate.charAt(candidateIndex);
                }

                Character inputChar = input.charAt(inputIndex);
                while (inputIndex < input.length() - 1 && encodeChar(inputChar) == null) {
                    inputIndex++;
                    inputChar = input.charAt(inputIndex);
                }

                if (encodeChar(candidateChar) != encodeChar(inputChar)) {
                    return false;
                }
                candidateIndex++;
                inputIndex++;
            }
            return true;
        }
        return false;
    }

    /**
     * @param input a character to encode
     * @return the Soundex representation of that character
     */
    private static Character encodeChar(char input) {
        // Not the prettiest to look at, but much faster than regex and slightly faster than Set operations.
        switch (input) {
            case 'b':
            case 'f':
            case 'p':
            case 'v':
                return '1';
            case 'c':
            case 'g':
            case 'j':
            case 'k':
            case 'q':
            case 's':
            case 'x':
            case 'z':
                return'2';
            case 'd':
            case 't':
                return '3';
            case 'l':
                return '4';
            case 'm':
            case 'n':
                return '5';
            case 'r':
                return '6';
            default:
                return null;
        }
    }
}
