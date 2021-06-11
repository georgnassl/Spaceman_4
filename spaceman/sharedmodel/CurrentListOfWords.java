package spaceman.sharedmodel;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrentListOfWords {
  private static ArrayList<WordToGuess> currentListOfWords;
  private static ArrayList<String> currentListOfWordsAsString;

  /**
   * Creates an initial CurrentListOfWords-Instance with a starting CurrentListOfWords-list in WordToGuess-format.
   * This list is supposed to be updated after every guess.
   * @return a CurrentListOfWords.
   * @TODO: Turn System.out.println into comment for the Praktomat.
   */
  public static CurrentListOfWords createCurrentListOfWords(WordDatabase wordDatabase) {
    CurrentListOfWords currentList = new CurrentListOfWords();
    currentListOfWords = new ArrayList<>();
    convertToCurrentListOfWords(wordDatabase);
    System.out.println(currentListOfWords.size() + " converted wordsToGuess from the database");
    return currentList;
  }

  /**
   * Converts the List of strings from Database to a List of WordToGuess to work with in case of a guess.
   * This list is supposed to be updated after every guess.
   * @return a List of WordToGuess.
   */
  public static ArrayList<WordToGuess> convertToCurrentListOfWords(WordDatabase wordDatabase) {
    ArrayList<String> wholeDatabase = wordDatabase.getWholeDatabase();
    currentListOfWords = new ArrayList<WordToGuess>();
    for (String word : wholeDatabase) {
      WordToGuess wordToGuess = new WordToGuess(word);
      currentListOfWords.add(wordToGuess);
    }
    return currentListOfWords;
  }

  /**
   * Converts the given WordToGuess to a String.
   * @return a String.
   */
  public String convertToCurrentWordAsString(WordToGuess currentWord) {
    List<GuessChar> listOfCharacters = currentWord.getCharacters();
    StringBuilder sb = new StringBuilder();
      for (GuessChar character : listOfCharacters) {
        if (character.maybeGetCharacter().isEmpty()) {
          sb.append("_");
        } else {
          sb.append(character.maybeGetCharacter().get());
        }
      }
      return sb.toString();
  }

  /**
   * Converts the List of WordToGuess to a List of String to compare it with the current WordToGuess (as String).
   * @return a List of String.
   */
  public ArrayList<String> convertToCurrentListOfWordsAsString(ArrayList<WordToGuess> currentListOfWords) {
    currentListOfWordsAsString.clear();
    for (WordToGuess word : currentListOfWords) {
      String wordAsString = convertToCurrentWordAsString(word);
      currentListOfWordsAsString.add(wordAsString);
      }
    return currentListOfWordsAsString;
  }

  /**
   * Compares the given List of WordToGuess (converted as String) to the given current WordToGuess (converted as String).
   * @return a updated List of WordToGuess matching to the currentWord.
   */
  public ArrayList<WordToGuess> updateCurrentListOfWords(ArrayList<WordToGuess> formerCurrentListOfWords, WordToGuess currentWord) {
    currentListOfWords.clear();
    String currentWordAsString = convertToCurrentWordAsString(currentWord);
    for (WordToGuess word : formerCurrentListOfWords) {
      String possibleNewWord = convertToCurrentWordAsString(word);
      if (currentWordAsString.equals(possibleNewWord)) {
      currentListOfWordsAsString.add(possibleNewWord);
      }
    }
  return currentListOfWords;
  }

  /**
   * Reveals all guessed characters in the WordsToGuess from currenListOfWords.
   * @param formerCurrentListOfWords the List of WordToGuess to process.
   * @param guessedCharacter the character to reveal.
   * @return an updated currentListOfWords.
   */
  public ArrayList<WordToGuess> revealCharactersInWholeList(ArrayList<WordToGuess> formerCurrentListOfWords,
                                                            char guessedCharacter) {
    ArrayList<WordToGuess> newCurrentListOfWords = new ArrayList<>();
    for (WordToGuess word : formerCurrentListOfWords) {
      word.revealChar(guessedCharacter);
      newCurrentListOfWords.add(word);
    }
    return newCurrentListOfWords;
  }

  public ArrayList<WordToGuess> getCurrentListOfWords() {
    return currentListOfWords;
  }
}
