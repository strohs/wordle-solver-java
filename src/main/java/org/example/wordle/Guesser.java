package org.example.wordle;

import java.util.List;

/**
 * Guesser contains an wordle guessing algorithm.
 *
 */
public interface Guesser {

    /**
     * A guessing algorithm for wordle.
     * We need to find the 'goodness' score of each word remaining in the dictionary
     * and then return the one with the highest goodness.
     * We'll use information theory to compute the expected amount of information we would
     * gain if a word isn't the answer, combined with the probability of words that are likely
     * to be the answer. This is the formula we will use:
     *
     * `- SUM_i prob_i * log_2(prob_i)`
     *
     * # Example
     * Imagine we have a list of candidate words: [word_1, word_2, ..., word_n] that could still be the
     * answer to a game of wordle and we want to determine the "goodness" score of word_i.
     * The goodness is the sum of the goodness score of each possible pattern we MIGHT see
     * as a result of guessing it, multiplied by the likely-hood of that pattern occurring.
     * @param history - a list of past guesses that were made, with the most recent guess at the end
     *                of the list.
     * @return a String that would be the best guess
     */
    String guess(List<Guess> history);
}
