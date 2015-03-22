package com.lastminutedevice.punerator;

import java.util.ArrayList;
import java.util.List;

public class Punerator {

    public static List<String> getPuns(final PuneratorModel model, final String input) {
        List <String> superstrings = model.getSuperStrings(input);
        List <String> results = new ArrayList<>();

        for(String candidate : superstrings) {
            String pun = Soundex.formatMatch(input, candidate);
            if (pun != null) {
                results.add(pun);
            } else {
                System.out.println("Could not find match for: " + candidate);
            }
        }

        return results;
    }
}