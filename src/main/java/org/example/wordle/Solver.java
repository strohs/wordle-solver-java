package org.example.wordle;

import org.example.wordle.algorithm.Prune;

import java.io.*;
import java.util.*;

/**
 * An interactive, command line driven, wordle solver.
 */
public class Solver {

    // the name of the dictionary file
    private static final String dictionaryFileName = "dictionary.txt";

    // the guessing algorithm that will be used to solve a wordle challenge
    private Guesser guesser;

    // a history of guesses that have been made at each turn of a wordle challenge
    private List<Guess> guessHistory;

    Scanner scanner;

    public Solver() {
        HashMap<String, Long> dictionary = loadDictionary();
        System.out.println("loaded " + dictionary.size() + " words from " + dictionaryFileName);
        this.guesser = new Prune(dictionary);
        this.guessHistory = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        Solver solver = new Solver();
        int turn = 1;

        System.out.println("Enter a guess and its resulting correctness pattern separated by a space then press ENTER, example:'tares ccwmm'  CTRL-C to exit");
        System.out.print(String.format("Turn %d:", turn));
        while (solver.scanner.hasNextLine()) {
            Guess g = solver.parseNextLine();
            if (g != null) {
                solver.guessHistory.add(g);
                String bestWord = solver.guesser.guess(solver.guessHistory);
                System.out.println("try this word: " + bestWord + "\n");
                turn++;
            } else {
                // error occurred on current line, advance to next
                solver.scanner.nextLine();
            }
            System.out.print(String.format("Turn %d:", turn));
        }
        solver.scanner.close();
    }

    /**
     * reads and parses the next guess from STDIN using the given scanner.
     * A guess consists of a 5 character word and a five character correctness pattern
     * @return the Guess that was entered, or null if an error occurred while trying to
     * parse the input
     */
    private Guess parseNextLine() {
        Guess guess = null;
        // each line of input should be of the form: \\w{5} \\w{5}
        try {
            String word = this.scanner.next("\\w{5}");
            String pattern = this.scanner.next("\\w{5}");
            CorrectnessPattern cp = CorrectnessPattern.parse(pattern.toLowerCase(Locale.ROOT));
            guess = new Guess(word.toLowerCase(Locale.ROOT), cp);
        } catch (IllegalArgumentException e) {
            System.out.println("INVALID PATTERN: correctness pattern must be 5 characters and consist of the letters 'c' 'm' or 'w'");
        } catch (InputMismatchException ime) {
            System.out.println("INVALID PATTERN: word and correctness pattern must each be 5 characters");
        }

        return guess;
    }

    /**
     * reads the words and counts stored in dictionary.txt into a HashMap
     * @return A hashMap that maps words to their occurrence count
     */
    private HashMap<String,Long> loadDictionary() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        File dictFile = new File(classLoader.getResource(dictionaryFileName).getFile());

        HashMap<String, Long> dictionary = new HashMap<>();
        try (FileReader fr = new FileReader(dictFile);
             BufferedReader br = new BufferedReader(fr)
        ) {
            br.lines()
                    .forEach(line -> {
                        String[] split = line.split(" ");
                        dictionary.put(split[0], Long.parseLong(split[1], 10));
                    });
        } catch (IOException e) {
            System.err.println("error reading dictionary file " + e);
        }

        return dictionary;
    }
}
