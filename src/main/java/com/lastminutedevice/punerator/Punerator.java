package com.lastminutedevice.punerator;

import eu.crydee.syllablecounter.SyllableCounter;

import java.util.ArrayList;
import java.util.List;

public class Punerator {

    public static List<String> getPuns(final PuneratorModel model, final String input) {
        List<String> results = new ArrayList<>();
        results.addAll(model.getSuperStrings(input));

        // If there were no exact superstrings, break it down.
        if (results.size() == 0) {
            SyllableCounter counter = new SyllableCounter();
            for(String syllable : counter.tokenize(input)) {
                results.addAll(model.getSuperStrings(syllable));
            }
        }

        return results;
    }
}