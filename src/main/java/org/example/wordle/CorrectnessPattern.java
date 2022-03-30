package org.example.wordle;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CorrectnessPattern contains the details of which characters of a guessed word
 * are Correct, Misplaced, or Wrong.
 *
 */
public class CorrectnessPattern {

    // the correctness pattern stores as an array of Correctness of length 5
    private final Correctness[] pattern;

    public CorrectnessPattern() {
        this.pattern = new Correctness[] {
                Correctness.WRONG,
                Correctness.WRONG,
                Correctness.WRONG,
                Correctness.WRONG,
                Correctness.WRONG,
        };
    }

    public CorrectnessPattern(Correctness[] pattern) {
        this.pattern = pattern;
    }

    public CorrectnessPattern(List<Correctness> pattern) {
        assert pattern.size() == 5;
        this.pattern = pattern.toArray(new Correctness[5]);
    }

    /**
     * @return the pattern array of this CorrectnessPattern
     */
    public Correctness[] getPattern() {
        return pattern;
    }

    /**
     *
     * @return the correctness enum at the sepcified index of this pattern
     */
    public Correctness get(int index) {
        return pattern[index];
    }

    public void set(int index, Correctness correctness) {
        this.pattern[index] = correctness;
    }

    /**
     * computes and returns a new CorrectnessPattern, or "mask", for each character
     * of the given `guess` when compared against the characters of the given `answer`.
     * @param answer - the wordle answer to compare the guess's characters against
     * @param guess - the word that will be compared against the answer
     * @return a CorrectnessPattern containing results of the comparison
     */
    public static CorrectnessPattern compute(String answer, String guess) {
        assert answer.length() == 5;
        assert guess.length() == 5;

        CorrectnessPattern cp = new CorrectnessPattern();

        // mark green characters
        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) == guess.charAt(i)) {
                cp.set(i, Correctness.CORRECT);
            }
        }

        // mark yellow, misplaced, characters
        boolean[] used = new boolean[] {false, false, false, false, false};
        for (int i = 0; i < cp.pattern.length; i++) {
            if (cp.get(i) == Correctness.CORRECT) {
                used[i] = true;
            }
        }
        for (int i = 0; i < guess.length(); i++) {
            if (cp.get(i) == Correctness.CORRECT) {
                // position already marked green
                continue;
            }
            for (int ai = 0; ai < answer.length(); ai++) {
                if (answer.charAt(ai) == guess.charAt(i) && !used[ai]) {
                    used[ai] = true;
                    cp.set(i, Correctness.MISPLACED);
                    break;
                }
            }
        }
        return cp;
    }


    /**
     * parses a String containing correctness pattern characters: 'c','m', or 'w', into a new
     * CorrectnessPattern type.
     * @param pattern - the String to parse
     * @return a new CorrectnessPattern
     * @throws IllegalArgumentException if pattern is not 5 characters or does not contain a valid
     * correctness pattern character
     */
    public static CorrectnessPattern parse(String pattern) throws IllegalArgumentException {
        // check length of pattern == 5
        if (pattern.length() != 5) {
            throw new IllegalArgumentException("correctness pattern must be 5 characters");
        }

        Correctness[] cp = new Correctness[5];

        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            switch (c) {
                case 'c' -> cp[i] = Correctness.CORRECT;
                case 'm' -> cp[i] = Correctness.MISPLACED;
                case 'w' -> cp[i] = Correctness.WRONG;
                default -> throw new IllegalArgumentException("correctness pattern characters must be either 'c', 'm', or 'w'");
            }
        }

        return new CorrectnessPattern(cp);
    }


    /**
     * computes the Cartesian Product of all possible correctness patterns for a 5 letter word.
     * There are 3 correctness patterns for each of the 5 character positions in a word, so the
     * total patterns will be of length 3^5.
     * Some patterns are impossible to reach so in reality this would be slightly
     * less than 3^5, but it should not affect our calculations. We'll generate the Cartesian
     * Product and optimize later
     * @return a List of CorrectnessPattern
     */
    public static List<CorrectnessPattern> patterns() {
        return com.google.common.collect.Lists.cartesianProduct(
                List.of(Correctness.CORRECT, Correctness.MISPLACED, Correctness.WRONG),
                List.of(Correctness.CORRECT, Correctness.MISPLACED, Correctness.WRONG),
                List.of(Correctness.CORRECT, Correctness.MISPLACED, Correctness.WRONG),
                List.of(Correctness.CORRECT, Correctness.MISPLACED, Correctness.WRONG),
                List.of(Correctness.CORRECT, Correctness.MISPLACED, Correctness.WRONG)
        ).stream()
                .map(CorrectnessPattern::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CorrectnessPattern that = (CorrectnessPattern) o;
        return Arrays.equals(pattern, that.pattern);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(pattern);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(5);
        for (Correctness c : this.pattern) {
            switch (c) {
                case CORRECT -> sb.append("C");
                case MISPLACED -> sb.append("M");
                case WRONG -> sb.append("W");
            }
        }
        return sb.toString();
    }
}
