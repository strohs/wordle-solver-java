package org.example.wordle.algorithm;

import org.example.wordle.CorrectnessPattern;
import org.example.wordle.Guess;
import org.example.wordle.Guesser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A wordle solver algorithm that prunes correctness patterns and dictionary words
 * that can no longer be valid at each iteration of a guess.
 */
public class Prune implements Guesser {

    // a dictionary of five letter words that maps a word to its occurrence count
    private HashMap<String, Long> dictionary;

    // a list of CorrectnessPatterns that are still "matchable" during a game of wordle
    private List<CorrectnessPattern> patterns;


    public Prune(HashMap<String, Long> dictionary) {
        this.dictionary = dictionary;
        this.patterns = CorrectnessPattern.patterns();
    }

    /**
     * removes entries from dictionary if they do not match lastGuess
     * @param lastGuess - the last guess that was made
     */
    private void pruneRemaining(Guess lastGuess) {
        this.dictionary
                .keySet()
                .removeIf(word -> !lastGuess.matches(word));
    }

    @Override
    public String guess(List<Guess> history) {
        if (!history.isEmpty()) {
            Guess last = history.get(history.size() - 1);
            this.pruneRemaining(last);
        }

        // hardcode first guess to "tares", it is the best starting guess
        if (history.isEmpty()) {
            return "tares";
        } else {
            // there should still be patterns left in this.patterns if we are still guessing
            assert !this.patterns.isEmpty();
        }

        // the sum of the counts of all the remaining words in the dictionary
        long remainingWordCount = this.dictionary
                .values()
                .stream()
                .reduce(0L, Long::sum);

        // holds the best candidate word that was found
        Candidate best = null;

        for (Map.Entry<String, Long> words : this.dictionary.entrySet()) {
            // sum of all: prob_of_a_pattern * prob_of_a_pattern.log2
            Double sum = 0.0;

            Iterator<CorrectnessPattern> patternIterator = this.patterns.iterator();
            while (patternIterator.hasNext()) {
                CorrectnessPattern pattern = patternIterator.next();
                // sum of the count(s) of all words that match the pattern
                Long inPatternTotal = 0L;

                for (Map.Entry<String, Long> candidates : this.dictionary.entrySet()) {
                    Guess g = new Guess(words.getKey(), pattern);
                    if (g.matches(candidates.getKey())) {
                        inPatternTotal += candidates.getValue();
                    }
                }
                if (inPatternTotal == 0) {
                    // no candidate words matched pattern, remove the pattern
                    patternIterator.remove();
                } else {
                    double probOfThisPattern = (double) inPatternTotal / (double) remainingWordCount;
                    sum += probOfThisPattern * log2(probOfThisPattern);
                }
            }
            // compute the probability of the current `word` using its occurrence `count`
            Double probWord = (double) words.getValue() / (double) remainingWordCount;
            // the goodnees score of `word` a.k.a its entropy "bits"
            Double goodness = probWord * -sum;

            if (best == null) {
                best = new Candidate(words.getKey(), goodness);
            } else if (goodness > best.goodness()) {
                best = new Candidate(words.getKey(), goodness);
            }
        }
        assert best != null;
        return best.word();

    }

    // Function to calculate the log base 2 of a double
    private static double log2(double N)
    {
        return Math.log(N) / Math.log(2);
    }
}
