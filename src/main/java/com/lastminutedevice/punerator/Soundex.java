package com.lastminutedevice.punerator;

public class Soundex {

    /**
     * Encodes the input string in a modified American Soundex notation. It is modified from true Soundex
     * in that it is not padded or truncated to meet the four-character standard, and the first character may or
     * may not be encoded as specified by encodeStart.
     *
     * @param input       a given English word
     * @param encodeStart whether to encode the start character as per true Soundex notation or encode the whole string
     * @return a simplified pronunciation key
     */
    public static String encode(String input, boolean encodeStart) {
        input = input.toLowerCase();

        StringBuilder builder = new StringBuilder();
        if (input.length() > 0) {
            builder.append(encodeStart ? Soundex.encodeChar(input.charAt(0)) : input.charAt(0));
        }

        if (input.length() > 1) {
            Character newCharacter;
            for (int i = 1; i < input.length(); i++) {
                newCharacter = encodeChar(input.charAt(i));
                if (newCharacter != null && builder.charAt(builder.length() - 1) != newCharacter) { // again, char array?
                    builder.append(newCharacter);
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
                int endIndex = i + findEndIndex(input, candidate.substring(i).toLowerCase());
                if (endIndex > 0) {
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
     * From here, we know that the first two characters match.
     * We want the index of the last character of the match.
     * <p/>
     * Return 0 if there is no match.
     */
    private static int findEndIndex(String substring, String superstring) {
        int subIndex = 0;
        int superIndex = 0;
        Character subChar = null;
        Character superChar = null;
        Character prevMatchChar = null;

        // Fuzzy-match the remaining substring to the superstring.
        while (subIndex < substring.length()) {

            // Skip sonorants in substring.
            while (subIndex < substring.length() && (subChar == null || subChar == prevMatchChar)) {
                subChar = encodeChar(substring.charAt(subIndex));
                subIndex++;
            }

            // Skip sonorants in superstring.
            while (superIndex < superstring.length() && (superChar == null || superChar == prevMatchChar)) {
                superChar = encodeChar(superstring.charAt(superIndex));
                superIndex++;
            }

            if (superChar != subChar) {
                return 0;
            }

            prevMatchChar = superChar;
        }

        return superIndex;
    }

    /**
     * @param input a character to encode
     * @return the Soundex representation of that character
     */
    private static Character encodeChar(char input) {
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
                return null;
        }
    }
}
