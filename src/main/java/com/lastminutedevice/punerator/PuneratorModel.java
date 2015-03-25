package com.lastminutedevice.punerator;

import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.suffix.ConcurrentSuffixTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PuneratorModel {
    private List<String> wordList = new ArrayList<>();
    private ConcurrentSuffixTree<Integer> tree = new ConcurrentSuffixTree<>(new DefaultCharArrayNodeFactory());

    public void trainFromFile(String pathToWordList) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(pathToWordList));
            int index = wordList.size();
            for (String line = br.readLine(); line != null && line.length() > 0; line = br.readLine()) {
                wordList.add(index, line);
                tree.put(Soundex.encode(line), index);
                index += 1;
            }
        } catch (IOException exception) {
            System.out.println("Unable to train due to IOException.");
            exception.printStackTrace();
        }
    }

    /**
     * getCandidates currently performs two searches since Soundex preserves the first character of a string
     * as a literal and encodes the rest as numbers. This leaves room for some improvement.
     *
     * @param input an input token to search for
     * @return all known tokens that are phonetically similar to the input token
     */
    public List<String> getCandidates(String input) {
        List<String> results = new ArrayList<>();
        if (input != null && input.length() > 1) {
            String encoded = Soundex.encode(input);

            // Get all known tokens which contain the exact first character, and similar substring.
            // Since Soundex notation only literally preserves the first character, this only captures
            // tokens that begin with the same start character.
            for (int index : tree.getValuesForKeysContaining(encoded)) {
                if (wordList.size() > index) {
                    results.add(wordList.get(index));
                }
            }

            // Get all known tokens which contain a phonetically similar substring, but do not begin with the
            // same character.
            String fullyEncodedInput = String.valueOf(Soundex.encodeChar(input.charAt(0)));
            if (encoded.length() > 1) {
                fullyEncodedInput += Soundex.encode(input).substring(1);
            }

            for (int index : tree.getValuesForKeysContaining(fullyEncodedInput)) {
                if (wordList.size() > index) {
                    results.add(wordList.get(index));
                }
            }

            // TODO: capture internal matches on repeating classes (ie, jug -> 22).
        }

        return results;
    }

    /**
     * @return how many words are in the model
     */
    public int size() {
        return wordList.size();
    }
}
