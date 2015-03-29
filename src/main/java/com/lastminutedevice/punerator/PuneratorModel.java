package com.lastminutedevice.punerator;

import com.googlecode.concurrenttrees.radix.node.concrete.DefaultCharArrayNodeFactory;
import com.googlecode.concurrenttrees.suffix.ConcurrentSuffixTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Search structure for retrieving pun suggestions.
 */
public class PuneratorModel {
    private List<String> wordList = new ArrayList<>();
    private ConcurrentSuffixTree<Integer> tree = new ConcurrentSuffixTree<>(new DefaultCharArrayNodeFactory());

    /**
     * Construct the search structures for finding pun candidates.
     *
     * @param pathToWordList the path to a file with each token on its own line
     * @throws IOException if the given path is to an invalid text file
     */
    public void addWordsFromFile(String pathToWordList) throws IOException {
        int index = wordList.size();
        BufferedReader br = new BufferedReader(new FileReader(pathToWordList));
        for (String line; (line = br.readLine()) != null; ) {
            if (line.length() > 1) {
                wordList.add(index, line);
                // Soundex leaves the first character of a string unencoded. Since the model will later
                // return candidates that match on substrings, the trie needs to be fully encoded.
                String token = Soundex.encodeChar(line.charAt(0)) + Soundex.encode(line).substring(1);
                tree.put(token, index);
                index += 1;
            }
        }
    }

    /**
     * Get a list of potential pun matches for a given token.
     * TODO: Tokens which are Latin or Greek root words will have a lot of matches that aren't really puns,
     * and so perhaps an exclusion strategy should prevent literal superstrings from being included for those words.
     *
     * @param input an input token to search for
     * @return all known tokens that are phonetically similar to the input token
     */
    public List<String> getCandidates(String input) {
        List<String> results = new ArrayList<>();
        if (input != null && input.length() > 1) {
            String token = Soundex.encodeChar(input.charAt(0)) + Soundex.encode(input).substring(1);

            for (int index : tree.getValuesForKeysContaining(token)) {
                if (wordList.size() > index) {
                    results.add(wordList.get(index));
                }
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
