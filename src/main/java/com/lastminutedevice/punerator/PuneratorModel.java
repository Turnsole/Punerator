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
                tree.put(Soundex.encode(line, false), index);
                index += 1;
            }
        } catch (IOException exception) {
            System.out.println("Unable to train due to IOException.");
            exception.printStackTrace();
        }
    }

    /**
     * @param input an input token to search for
     * @return all known tokens that are phonetically similar to the input token
     */
    public List<String> getCandidates(String input) {
        List<String> results = new ArrayList<>();

        // Get all known tokens which contain the exact first character, and similar substring.
        // Since Soundex notation only literally preserves the first character, this only captures
        // tokens that begin with the same start character.
        for (int index : tree.getValuesForKeysContaining(Soundex.encode(input, false))) {
            if (wordList.size() > index) {
                results.add(wordList.get(index));
            }
        }

        // Get all known tokens which contain a phonetically similar substring, but do not begin with the
        // same character.
        for (int index : tree.getValuesForKeysContaining(Soundex.encode(input, true))) {
            if (wordList.size() > index) {
                results.add(wordList.get(index));
            }
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
