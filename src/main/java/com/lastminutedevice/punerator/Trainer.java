package com.lastminutedevice.punerator;

import eu.crydee.syllablecounter.SyllableCounter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.List;
/**
 * Parses input tokens
 */
public class Trainer {
    private final String pathToWordList;

    /**
     * Imports a word list to the local database and trie
     *
     * @param pathToWordList the path to a text file containing word tokens, one on each line
     */
    public Trainer(String pathToWordList) {
        this.pathToWordList = pathToWordList;
    }

    public void train() {
        SyllableCounter syllableCounter = new SyllableCounter();
        long beginTimeStamp = System.currentTimeMillis(); // TODO: proper profiling
        try {
            BufferedReader br = new BufferedReader(new FileReader(pathToWordList));
            int tally = 0;
            for(String line = br.readLine(); line != null; line = br.readLine()) {
                tally += 1;
                System.out.println(line + " " + syllableCounter.tokenize(line));
            }
            System.out.println("Tally: " + tally);
        } catch (IOException exception) {
            System.out.println("Unable to train due to IOException.");
            exception.printStackTrace();
        }

        System.out.println(String.format("Trained in: %s ms", System.currentTimeMillis() - beginTimeStamp));
    }

}
