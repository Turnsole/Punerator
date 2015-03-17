package com.lastminutedevice.punerator;

import com.googlecode.concurrenttrees.common.PrettyPrinter;
import com.googlecode.concurrenttrees.radix.ConcurrentRadixTree;
import com.googlecode.concurrenttrees.radix.RadixTree;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.radix.node.util.PrettyPrintable;
import eu.crydee.syllablecounter.SyllableCounter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PuneratorModel {
    private List<String> wordList = new ArrayList<>();
    private RadixTree<Integer> tree = new ConcurrentRadixTree<>(new DefaultCharArrayNodeFactory());

    public void trainFromFile(String pathToWordList) {
        SyllableCounter syllableCounter = new SyllableCounter();
        try {
            BufferedReader br = new BufferedReader(new FileReader(pathToWordList));
            int index = wordList.size();
            for(String line = br.readLine(); line != null; line = br.readLine()) {
                wordList.add(index, line);
                tree.put(line, index);
                for (String syllable : syllableCounter.tokenize(line)) {
                    if(syllable.length() > 1) tree.put(syllable, index);
                }
                index += 1;
            }
        } catch (IOException exception) {
            System.out.println("Unable to train due to IOException.");
            exception.printStackTrace();
        }

        //PrettyPrinter.prettyPrint((PrettyPrintable) tree, System.out);
        //System.out.print(wordList.toString());
    }

    public List<String> getSuperStrings(String input) {
        List<String> results = new ArrayList<>();
        for(int index : tree.getValuesForKeysStartingWith(input)) {
            if(wordList.size() > index) {
                results.add(wordList.get(index));
            }
        }

        // If there were no exact matches, find something kind of close.
        if (results.size() == 0) {
            for (int index : tree.getValuesForClosestKeys(input)) {
                if(wordList.size() > index) {
                    results.add(wordList.get(index));
                }
            }
        }

        return results;
    }
}
