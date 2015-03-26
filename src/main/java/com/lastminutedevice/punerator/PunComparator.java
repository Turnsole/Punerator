package com.lastminutedevice.punerator;

import java.util.Comparator;

/**
 * Compares candidate puns by looking at their scores.
 */
public class PunComparator implements Comparator<Candidate> {

    @Override
    public int compare(Candidate one, Candidate two) {
        if (one.getScore() > two.getScore()) return -1;
        if (one.getScore() < two.getScore()) return 1;
        return 0;
    }
}
