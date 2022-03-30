package org.example.wordle;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CorrectnessPatternTest {

    @Test
    void parse_valid_5_chars() {
        String pattern = "ccmmw";
        Correctness[] carr = new Correctness[] {Correctness.CORRECT, Correctness.CORRECT, Correctness.MISPLACED, Correctness.MISPLACED, Correctness.WRONG};
        assertArrayEquals(CorrectnessPattern.parse(pattern).getPattern(), carr);
    }

    @Test
    void parse_all_correct_pattern() {
        String pattern = "ccccc";
        Correctness[] carr = new Correctness[] {Correctness.CORRECT, Correctness.CORRECT, Correctness.CORRECT, Correctness.CORRECT, Correctness.CORRECT};
        assertArrayEquals(CorrectnessPattern.parse(pattern).getPattern(), carr);
    }

    @Test
    void default_correctness_pattern_defaults_to_all_wrong() {
        Correctness[] carr = new Correctness[] {Correctness.WRONG, Correctness.WRONG, Correctness.WRONG, Correctness.WRONG, Correctness.WRONG};
        assertArrayEquals(new CorrectnessPattern().getPattern(), carr);
    }

    @Test
    void parse_invalid_char_throws_invalid_argument_exception() {
        String pattern = "ccqmw";
        assertThrows(IllegalArgumentException.class, () -> CorrectnessPattern.parse(pattern));
    }

    @Test
    void parse_pattern_gt_5_chars_throws_invalid_argument_exception() {
        String pattern = "cccmwm";
        assertThrows(IllegalArgumentException.class, () -> CorrectnessPattern.parse(pattern));
    }

    @Test
    void parse_empty_pattern_throws_invalid_argument_exception() {
        String pattern = "";
        assertThrows(IllegalArgumentException.class, () -> CorrectnessPattern.parse(pattern));
    }

    @Test
    void parse_pattern_of_1_char_throws_invalid_argument_exception() {
        String pattern = "w";
        assertThrows(IllegalArgumentException.class, () -> CorrectnessPattern.parse(pattern));
    }

    @Test
    void compute_returns_all_correct() {
        CorrectnessPattern mask = CorrectnessPattern.parse("ccccc");
        assertEquals(CorrectnessPattern.compute("abcde", "abcde"), mask);
    }

    @Test
    void compute_returns_all_wrong() {
        CorrectnessPattern mask = CorrectnessPattern.parse("wwwww");
        assertEquals(CorrectnessPattern.compute("abcde", "qwxyz"), mask);
    }

    @Test
    void compute_returns_all_yellow() {
        CorrectnessPattern mask = CorrectnessPattern.parse("mmmmm");
        assertEquals(CorrectnessPattern.compute("abcde", "eabcd"), mask);
    }

    @Test
    void compute_returns_two_correct() {
        CorrectnessPattern mask = CorrectnessPattern.parse("ccwww");
        assertEquals(CorrectnessPattern.compute("aabbb", "aaccc"), mask);
    }

    @Test
    void compute_returns_two_yellow() {
        CorrectnessPattern mask = CorrectnessPattern.parse("wwmmw");
        assertEquals(CorrectnessPattern.compute("aabbb", "ccaac"), mask);
    }

    @Test
    void compute_returns_one_correct_one_yellow() {
        CorrectnessPattern mask = CorrectnessPattern.parse("wcmww");
        assertEquals(CorrectnessPattern.compute("aabbb", "caacc"), mask);
    }

    @Test
    void compute_returns_one_correct_one_yellow_2() {
        CorrectnessPattern mask = CorrectnessPattern.parse("cmwww");
        assertEquals(CorrectnessPattern.compute("azzaz", "aaabb"), mask);
    }

    @Test
    void compute_returns_exactly_one_correct_a() {
        CorrectnessPattern mask = CorrectnessPattern.parse("wcwww");
        assertEquals(CorrectnessPattern.compute("baccc", "aaddd"), mask);
    }

    @Test
    void compute_returns_some_green_some_yellow() {
        CorrectnessPattern mask = CorrectnessPattern.parse("cwccc");
        assertEquals(CorrectnessPattern.compute("abcde", "aacde"), mask);
    }

    @Test
    void patterns_should_generate_243_patterns() {
        List<CorrectnessPattern> patterns = CorrectnessPattern.patterns();
        // 3 ^ 5 == 243 total patterns
        assertEquals(patterns.size(), 243);
    }
}