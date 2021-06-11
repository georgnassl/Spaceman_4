package spaceman.sharedmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Represent a word that needs to be guessed. Characters in this word can be hidden or not. */
public class WordToGuess implements Serializable {

  private static final long serialVersionUID = 1L;

  private String completeWord;
  private List<GuessChar> revealedCharacters;

  WordToGuess(final String word) {
    completeWord = word;

    revealedCharacters = new ArrayList<>(word.length());
    for (char character : word.toCharArray()) {
      if (character == ' ') {
        revealedCharacters.add(new GuessChar(' '));
      } else {
        revealedCharacters.add(new GuessChar());
      }
    }
  }

  /** Return the complete word. */
  String getCompleteWord() {
    return completeWord;
  }

  /** Return the length of the word. */
  public int getWordLength() {
    return completeWord.length();
  }

  /**
   * Get the list of characters in the word. Characters can be revealed or hidden. This information
   * is defined by the used {@link GuessChar}.
   *
   * @return the list of characters in the word (as <code>GuessChar</code>s)
   */
  public List<GuessChar> getCharacters() {
    return revealedCharacters;
  }

  /**
   * Guess the given character. If the character is in the word, all occurrences of the character
   * are revealed in the word.
   *
   * @param guessedCharacter character to guess
   * @return <code>true</code>if the character is in the word. <code>false</code> otherwise
   */
  boolean guess(final char guessedCharacter) {
    char guessedLower = Character.toLowerCase(guessedCharacter);
    char guessedUpper = Character.toUpperCase(guessedCharacter);

    if (completeWord.indexOf(guessedUpper) == -1 && completeWord.indexOf(guessedLower) == -1) {
      return false;
    }

    for (int i = 0; i < getWordLength(); i++) {
      char originialLower = Character.toLowerCase(completeWord.charAt(i));
      if (originialLower == guessedLower) {
        revealCharacterAt(i);
      }
    }
    return true;
  }

  boolean isFullyRevealed() {
    for (GuessChar c : revealedCharacters) {
      if (c.maybeGetCharacter().isEmpty()) {
        return false;
      }
    }
    return true;
  }

  /** Reveal all characters. */
  void revealAll() {
    for (int i = 0; i < getWordLength(); i++) {
      Optional<Character> maybeCharacter = revealedCharacters.get(i).maybeGetCharacter();
      if (maybeCharacter.isEmpty()) {
        revealCharacterAt(i);
      }
    }
  }

  /** Reveals every position that contains the given Char.
   * @param guessedCharacter the character which is supposed to be revealed in the WordToGuess.
   */
   void revealChar(char guessedCharacter) {
    String wordAsString = this.getCompleteWord();
    List<GuessChar> revealedWord = this.getCharacters();
    char guessedLower = Character.toLowerCase(guessedCharacter);
    for (int i = 0; i < wordAsString.length(); i++) {
      char originalLower = Character.toLowerCase(wordAsString.charAt(i));
      if (originalLower == guessedLower) {
        GuessChar revealedChar = new GuessChar(wordAsString.charAt(i));
        revealedWord.set(i, revealedChar);
      }
    }
    this.revealedCharacters = revealedWord;
  }

  private void revealCharacterAt(int atIndex) {
    GuessChar revealedChar = new GuessChar(completeWord.charAt(atIndex));
    revealedCharacters.set(atIndex, revealedChar);
  }
}
