package com.lastminutedevice.punerator;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: tests
 */
public class Punerator {

    public static List<String> getPuns(String input) {
        List<String> results = new ArrayList<String>();
        for (String homophone : getHomophones(input)) {
            results.addAll(getSuperStrings(homophone));
        }
        return results;
    }

    /**
     * Takes an input string, and returns a list of strings with similar phonemes.
     *
     * @param input the String to make homophones of
     * @return a list of homophonic strings to the input
     */
    private static List<String> getHomophones(String input) {
        List<String> results = new ArrayList<String>();
        // TODO
        results.add(input);
        return results;
    }

    /**
     * Searches some database for superstrings given a substring.
     *
     * @param input a String to find puns of
     * @return a list of
     */
    private static List<String> getSuperStrings(String input) {
        List<String> results = new ArrayList<String>();

        return results;
    }
}