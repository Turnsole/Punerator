package com.lastminutedevice.punerator;

import com.googlecode.concurrenttrees.radix.ConcurrentRadixTree;
import com.googlecode.concurrenttrees.radix.RadixTree;
import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.suffix.ConcurrentSuffixTree;
import eu.crydee.syllablecounter.SyllableCounter;

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
            for(String line = br.readLine(); line != null; line = br.readLine()) {
                wordList.add(index, line);
                tree.put(Soundex.encode(line), index);
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
        for(int index : tree.getValuesForKeysContaining(Soundex.encode(input))) {
            if(wordList.size() > index) {
                results.add(wordList.get(index));
            }
        }

        return results;
    }

    public int size() {
        return wordList.size();
    }
}
