# Java Wordle Solver
A command line wordle solver that uses information theory to solve a game of [wordle](https://www.nytimes.com/games/wordle/index.html). 
It's inspired by [3Blue1Brown](https://www.youtube.com/watch?v=v68zYyaEmEA) and
[Jon Gjenset's](https://www.youtube.com/watch?v=doFowk4xj7Q) YouTube videos on this topic.

This project is a port of my [Rust wordle solver](https://github.com/strohs/wordle-solver-rust)

This solver is able to solve most challenges on turn 4, especially if you use a starting guess of "tares" or "crate".

## Running
This solver is a command line program that is used at the same time you are making guesses into the wordle web application.
You make your first guess into the web app, and then switch over to this solver and enter that guess along with its 
corresponding "correctness" pattern. 

The correctness pattern is a five character string that represents the correctness of each letter of your guess as shown
in the wordle web application.
It consists of the characters `c`,`m`, or `w`.
- `c` a letter of the guess is in the **correct** position, the wordle web app shows these with a green background
- `m` a letter of the guess is in the answer but is **misplaced**, or in the incorrect position, wordle shows these with a yellow background
- `w` a letter of the guess is **wrong** and not in the answer at all, wordle shows these with a grey background

For example, suppose your first guess is "event" and wordle displayed the following info for each letter (assume the real wordle answer is "depot"):
- `e` with a yellow background, this character is `misplaced`. It is in the answer but in the wrong position
- `v` with a grey background, this character is `wrong`
- `e` with a grey background, this character is `wrong` because there is only one 'e' in 'depot'
- `n` with a grey background, this character is also `wrong`
- `t` with a green background, this character is `correct`

The final correctness pattern is `mwwwe`

### Example
To run the solver you'll need to have installed Java 16 and Apache Maven (at least version 3).

From the project's root directory run:

> mvn clean compile exec:java

- go to the wordle web application and enter your first guess. i.e. "tares".
- enter your guess and correctness pattern into the solver:
> tares mwwwc

- the solver will recommend the next, "best", guess you should try
> Try this word: least
- type `least` into the wordle web application
- then enter `least` and it's correctness pattern into the solver
- repeat until the correct word is guessed
- press CTRL-C to exit the wordle solver



## The Wordle Solver Algorithm
In general, the wordle solver implemented here uses the formula for
[Entropy of an information source](https://en.wikipedia.org/wiki/Information_theory#Entropy_of_an_information_source) to
compute the "best" word to guess.

On any given turn, the best guess is the word that yields the highest amount of **information**, called "bits" in information theory.
Or to put it another way, which guess would reduce the space of possibilities the most (i.e. eliminate the most words at every turn).
The algorithm also needs to take into account a word's frequency data. Or how common it is in everyday use. This is because not all
five-letter words are equally possible in a game of wordle. Wordle will use more common words like: `jelly` or `shark`
versus more obscure words like: `iller` or `jeely`.

So the final formula to compute if a word, `w`, is the best guess is: 

`BestWord = Pw * -Sum( Ppat * log2(Ppat) )`
- `Pw` is the probability of the word occurring in general, (based on its occurrence count data)
- `Ppat` is the probability of a wordle correctness pattern occurring (that could still potentially match `w`)


After each guess, the algorithm removes any words and correctness patterns that could not possibly be a match based on 
all the guesses that have been made so far. This pruning step boosts performance the most as you could potentially be 
reducing a huge portion of your search space after every turn.


### Data files used
`dictionary.txt` This file contains 12,947 five-letter words used by wordle along with the "occurrence count" of that 
word. This data is taken from Google Books' [Ngram Viewer](https://storage.googleapis.com/books/ngrams/books/datasetsv3.html).
