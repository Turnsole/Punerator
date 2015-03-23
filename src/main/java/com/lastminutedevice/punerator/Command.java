package com.lastminutedevice.punerator;

import java.util.List;

/**
 * CLI for com.lastminutedevice.punerator.Punerator
 */
public class Command {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        PuneratorModel model = new PuneratorModel();
        model.trainFromFile("src/main/resources/short_word_list.txt");   // todo: gzip for distribution
        System.out.println(String.format("Trained on %s words in %d ms.", model.size(), System.currentTimeMillis() - startTime));

        for (String input : args) {
            List<String> results = Punerator.getPuns(model, input);
            System.out.println(String.format("Found: %s puns for %s.", results.size(), input));
            for (String pun : results) {
                System.out.println("\t" + pun);
            }
        }
    }
}
