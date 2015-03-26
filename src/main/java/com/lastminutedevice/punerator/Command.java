package com.lastminutedevice.punerator;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.util.List;

/**
 * CLI for com.lastminutedevice.punerator.Punerator
 */
public class Command {

    public static void main(String[] args) {
        OptionParser optionParser = new OptionParser();
        OptionSpec<Void> verbose = optionParser.accepts("v");

        OptionSet optionSet = optionParser.parse(args);
        String wordListSize = optionSet.has(verbose) ? "80" : "35";

        long startTime = System.currentTimeMillis();
        PuneratorModel model = new PuneratorModel();

        // Word list generated from material provided by: http://wordlist.aspell.net
        model.trainFromFile("src/main/resources/" + wordListSize + "_word_list.txt");   // todo: gzip for distribution
        System.out.println(String.format("Trained on %s words in %d ms.", model.size(), System.currentTimeMillis() - startTime));

        PunComparator punComparator = new PunComparator();
        for (String input : (List<String>) optionSet.nonOptionArguments()) {
            List<Candidate> results = Punerator.getPuns(model, input);
            System.out.println(String.format("Found: %s puns for %s.", results.size(), input));

            results.sort(punComparator);
            for (Candidate pun : results) {
                System.out.println(String.format("\t%s (%s)", pun.getFormatted(), pun.getOriginal()));
            }
        }
    }
}
