package org.example.wordle;

/**
 * A possible "correctness type" that can be returned by wordle after making a guess.
 *
 * Their are three responses that wordle gives for each character of a guess:
 *
 * Correct - Wordle Green Background, letter is in the correct position
 * Misplaced - Wordle yellow background, letter is in the answer but in the incorrect position
 * Wrong - Wordle grey background, letter is not in the answer at all
 */
public enum Correctness {
    // Wordle Green Background, letter is in the correct position
    CORRECT,
    // Wordle yellow background, letter is in the answer but in the incorrect position
    MISPLACED,
    // Wordle grey background, letter is not in the answer at all
    WRONG;
}
