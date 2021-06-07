package spaceman.sharedmodel;

import java.util.Random;

/** Class that contains all possible words for guessing. */
class WordDatabase {

  private static final String[] WORDS = {
    "Spaceman", "Alien", "Earthling", "Homo Sapiens",
  };

  /**
   * Return a randomly chosen word from the word database. Words are chosen randomly according to a
   * uniform distribution.
   */
  String getWord() {
    int guessedIndex = new Random().nextInt(WORDS.length);
    return WORDS[guessedIndex];
  }
}
