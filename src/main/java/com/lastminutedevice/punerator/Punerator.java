package com.lastminutedevice.punerator;

import net.ricecode.similarity.DiceCoefficientStrategy;

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
    public static List<Candidate> getPuns(final PuneratorModel model, final String input) {
        List<String> candidates = model.getCandidates(input);

        List<Candidate> results = new ArrayList<>();
        for (String candidate : candidates) {
            String pun = Soundex.findMatch(input.toLowerCase(), candidate);
            if (pun != null) {
                // TODO: experimentally determine what level of string similarity best provokes mirth.
                // TODO: implement a version of this strategy that takes character class (probably Soundex) into account.
                double diceScore = DiceCoefficientStrategy.score(input, candidate);

                results.add(new Candidate(candidate, pun, diceScore));
            }
        }

        return results;
    }
}