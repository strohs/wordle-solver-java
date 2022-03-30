package org.example.wordle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuessTest {

    // checking if a word is still a match against the previous guess

    @Test
    void matches_allows_word_that_is_all_correct() {
        Guess g = new Guess("abcde", CorrectnessPattern.parse("ccccc"));
        assertTrue(g.matches("abcde"));
    }

    @Test
    void matches_disallows_word_that_has_a_wrong_character() {
        Guess g = new Guess("abcdf", CorrectnessPattern.parse("ccccc"));
        assertFalse(g.matches("abcde"));
    }

    @Test
    void matches_allows_a_word_that_is_still_all_wrong_characters() {
        Guess g = new Guess("abcde", CorrectnessPattern.parse("wwwww"));
        assertTrue(g.matches("fghij"));
    }

    @Test
    void matches_allows_a_word_that_is_all_misplaced_characters() {
        Guess g = new Guess("abcde", CorrectnessPattern.parse("mmmmm"));
        assertTrue(g.matches("eabcd"));
    }

    @Test
    void matches_allows_when_word_has_misplaced_and_matching_chars() {
        Guess g = new Guess("baaaa", CorrectnessPattern.parse("wcmww"));
        assertTrue(g.matches("aaccc"));
    }

    @Test
    void matches_disallows_a_word_that_is_in_different_order() {
        Guess g = new Guess("baaaa", CorrectnessPattern.parse("wcmww"));
        assertFalse(g.matches("caacc"));
    }

    @Test
    void matches_disallows_word2() {
        Guess g = new Guess("aaabb", CorrectnessPattern.parse("cmwww"));
        assertFalse(g.matches("accaa"));
    }

    @Test
    void matches_disallows_word3() {
        Guess g = new Guess("tares", CorrectnessPattern.parse("wmmww"));
        assertFalse(g.matches("brink"));
    }
}