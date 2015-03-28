package com.lastminutedevice.punerator;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.util.List;
import java.io.IOException;

/**
 * CLI for com.lastminutedevice.punerator.Punerator
 */
public class Command {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        OptionParser optionParser = new OptionParser();
        OptionSpec<Void> verbose = optionParser.accepts("v");

        OptionSet optionSet = optionParser.parse(args);
        String wordListSize = optionSet.has(verbose) ? "80" : "35";

        long startTime = System.currentTimeMillis();
        PuneratorModel model = new PuneratorModel();

        try {
            // Word list generated from material provided by: http://wordlist.aspell.net
            model.addWordsFromFile("src/main/resources/" + wordListSize + "_word_list.txt");   // todo: gzip for distribution
        } catch (IOException exception) {
            System.out.println("Unable to load dictionary file.");
        }

        System.out.println(String.format("Trained on %s words in %dms.", model.size(), System.currentTimeMillis() - startTime));

        PunComparator punComparator = new PunComparator();
        for (String input : (List<String>) optionSet.nonOptionArguments()) {
            startTime = System.currentTimeMillis();
            List<Candidate> results = Punerator.getPuns(model, input);
            System.out.println(String.format("Found: %s puns for %s in %dms.", results.size(), input, System.currentTimeMillis() - startTime));

            results.sort(punComparator);
            for (Candidate pun : results) {
                System.out.println(String.format("\t%s (%s)", pun.getFormatted(), pun.getOriginal()));
            }
        }
    }
}
