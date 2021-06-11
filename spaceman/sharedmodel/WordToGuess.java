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
  private CurrentListOfWords currentListOfWords;
  private ArrayList<WordToGuess> updatedCurrentListOfWords;

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
   * <p>NEW: before the regular guess, the model searches for the group of evil words, that seem to
   * have the most possibilities to fail in the further game progress.
   *
   * @param guessedCharacter character to guess
   * @return <code>true</code>if the character is in the word. <code>false</code> otherwise @TODO:
   *     Turn System.out.println into comment for the Praktomat.
   */
  boolean guess(final char guessedCharacter) {

    // System.out.println(currentListOfWords.getCurrentListOfWords().size());

    updatedCurrentListOfWords =
        currentListOfWords.updateCurrentListOfWords(
            currentListOfWords.getCurrentListOfWords(), this);
    currentListOfWords.setCurrentListOfWords(updatedCurrentListOfWords);
    // System.out.println(currentListOfWords.getCurrentListOfWords().size());

    EvilGroupOfWords evilGroupOfWords =
        EvilGroupOfWords.createEvilGroupOfWords(currentListOfWords, guessedCharacter);
    // Here we get just an placeholder (element at 0. position, to get no IndexOutOfBoundsException)
    // for the whole evilGroup, that represents the revealed characters.
    WordToGuess newEvilWordToGuess = evilGroupOfWords.getGroupOfEvilWords().get(0);
    this.revealedCharacters = newEvilWordToGuess.getCharacters();
    this.completeWord = newEvilWordToGuess.getCompleteWord();
    // System.out.println(completeWord);

    // From here the classic way continues. The possibility to reveal before (in EvilGroupOfWords)
    // was not chosen because of this.
    char guessedLower = Character.toLowerCase(guessedCharacter);
    char guessedUpper = Character.toUpperCase(guessedCharacter);

    if (completeWord.indexOf(guessedUpper) == -1 && completeWord.indexOf(guessedLower) == -1) {
      return false;
    }

    for (int i = 0; i < getWordLength(); i++) {
      char originalLower = Character.toLowerCase(completeWord.charAt(i));
      if (originalLower == guessedLower) {
        revealCharacterAt(i);
      }
    }
    // the guessed character is revealed in the whole evilGroupOfWords here now.
    currentListOfWords.revealCharactersInWholeList(
        evilGroupOfWords.getGroupOfEvilWords(), guessedCharacter);
    currentListOfWords.setCurrentListOfWords(evilGroupOfWords.getGroupOfEvilWords());
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

  /**
   * Reveals every position that contains the given Char.
   *
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

  public void setCurrentListOfWords(CurrentListOfWords currentListOfWords) {
    this.currentListOfWords = currentListOfWords;
  }

  public CurrentListOfWords getCurrentListOfWords() {
    return currentListOfWords;
  }
}
