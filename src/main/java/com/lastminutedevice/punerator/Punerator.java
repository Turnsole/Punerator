package com.lastminutedevice.punerator;

import java.util.ArrayList;
import java.util.List;

public class Punerator {

    /**
     * Provided a pre-trained model and input token, generate a list of pun suggestions.
     *
     * @param model a PuneratorModel to employ
     * @param input an input token to generate suggestions for
     * @return list of strings, empty if no puns found
     */
    public static List<String> getPuns(final PuneratorModel model, final String input) {
        List<String> superstrings = model.getCandidates(input);
        List<String> results = new ArrayList<>();

        for (String candidate : superstrings) {
            String pun = Soundex.findMatch(input.toLowerCase(), candidate);
            if (pun != null) {
                results.add(String.format("%s (%s)", pun, candidate));
            }
        }

        return results;
    }
}