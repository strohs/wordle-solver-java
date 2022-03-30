package org.example.wordle.algorithm;

import java.util.List;

/**
 * A Candidate word and its "goodness" score
 * @param word - the word
 * @param goodness - the overall goodness score of the word.
 * @see org.example.wordle.Guesser#guess(List) for details of how the goodness score is computed.
 */
public record Candidate(String word, Double goodness) {
}
