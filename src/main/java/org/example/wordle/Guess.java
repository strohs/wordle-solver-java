package org.example.wordle;

/**
 * Holds a word that was guessed in a Wordle turn, along with the Correctness "mask"
 * of the word. In a sense, it's really the outcome of a guess
 * @param word - the word that was guessed
 * @param mask - the word's correctnessPattern or "mask"
 */
public record Guess(String word, CorrectnessPattern mask) {

    /**
     * Compares `otherWord` against the word in this guess to see if `otherWord` could be a
     * plausible guess... a.k.a  a "match"
     * @return true - if `word` could be a plausible guess, false - if there is no possible way
     * that `word` would match this Guess based on the Guesses mask data
     */
    public boolean matches(String otherWord) {
        return CorrectnessPattern.compute(otherWord, this.word).equals(this.mask);
    }
}
