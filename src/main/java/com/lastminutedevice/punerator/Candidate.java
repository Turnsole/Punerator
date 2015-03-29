package com.lastminutedevice.punerator;

/**
 * Represents a pun candidate.
 */
public class Candidate {
    private String original;
    private String formatted;
    private double score;

    /**
     * An pun candidate with its original form, the form formatted to make the pun more obvious, and a score
     * representing how good the pun is (by what measure depends on implementation).
     *
     * @param original  the form of the candidate as found in a dictionary
     * @param formatted the matched substring capitalized to make the pun more obvious
     * @param score     a score representing how good a pun is expected to be of whatever the input word was
     */
    public Candidate(String original, String formatted, double score) {
        this.original = original;
        this.formatted = formatted;
        this.score = score;
    }

    public String getOriginal() {
        return original;
    }

    public String getFormatted() {
        return formatted;
    }

    public double getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Candidate candidate = (Candidate) o;

        if (Double.compare(candidate.score, score) != 0) return false;
        if (formatted != null ? !formatted.equals(candidate.formatted) : candidate.formatted != null) return false;
        if (original != null ? !original.equals(candidate.original) : candidate.original != null) return false;

        return true;
    }
}
