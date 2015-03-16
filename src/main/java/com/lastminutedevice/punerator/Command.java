package com.lastminutedevice.punerator;

import java.util.List;

/**
 * CLI for the com.lastminutedevice.punerator.Punerator
 */
public class Command {
    public static void main(String[] args) {
        Trainer trainer = new Trainer("src/main/resources/word_list.txt");  // todo: gzip
        trainer.train();

        for (String input : args) {
            List<String> results = Punerator.getPuns(input);
            System.out.println(String.format("Found: %s puns for %s.", results.size(), input));
            for (String pun : results) {
                System.out.println("\t" + pun);
            }
        }
    }
}
