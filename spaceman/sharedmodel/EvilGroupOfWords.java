package spaceman.sharedmodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EvilGroupOfWords {

  private static final Map<String, ArrayList<WordToGuess>> evilMapOfWords =
      new ConcurrentHashMap<>();
  private static ArrayList<WordToGuess> groupOfEvilWords;
  private static String evilKeyword;

  /**
   * creates an object of this class, that performs all the actions to get a new list of evil
   * WordsTo Guess as a parameter.
   *
   * @param currentListOfWords the List of WordsToGuess in where the guessed character should be
   *     revealed and that should be evil processed.
   * @param guessedCharacter the newly guessed character.
   */
  public static EvilGroupOfWords createEvilGroupOfWords(
      CurrentListOfWords currentListOfWords, Character guessedCharacter) {
    EvilGroupOfWords evilGroupOfWords = new EvilGroupOfWords();
    produceEvilMap(currentListOfWords, guessedCharacter);
    findOutEvilKeyword(evilMapOfWords);
    groupOfEvilWords = extractGroupOfEvilWords(evilMapOfWords, evilKeyword);
    return evilGroupOfWords;
  }

  /**
   * Produces a evilMapOfWords, where all the words (with the newly revealed guessed character) are
   * ordered by the position of the new revealed character (this is performed by taking the string
   * of the word as keyword).
   *
   * @param listOfWords the List of WordsToGuess in where the guessed character should be revealed
   *     and that should be evil processed.
   * @param guessedCharacter the newly guessed character.
   */
  public static void produceEvilMap(CurrentListOfWords listOfWords, char guessedCharacter) {

    ArrayList<WordToGuess> newListOfWords = listOfWords.getCurrentListOfWords();

    evilMapOfWords.clear();
    for (WordToGuess word : newListOfWords) {
      word.revealChar(guessedCharacter);
      String keyword = listOfWords.convertToCurrentWordAsString(word);
      if (!evilMapOfWords.containsKey(keyword)) {
        evilMapOfWords.put(keyword, new ArrayList<>());
      }
      evilMapOfWords.get(keyword).add(word);
    }
  }

  /**
   * Finds the keyword(String) with the most elements. This is the evilKeyword
   *
   * @param evilMapOfWords the ConcurrentHashmap of Lists of WordsToGuess in order to the position
   *     of the newly revealed character.
   */
  public static void findOutEvilKeyword(Map<String, ArrayList<WordToGuess>> evilMapOfWords) {
    int countOfWordsToGuess = 0;

    for (String keyword : evilMapOfWords.keySet()) {
      int currentCountOfWordsToGuess = evilMapOfWords.get(keyword).size();
      if (currentCountOfWordsToGuess > countOfWordsToGuess) {
        countOfWordsToGuess = currentCountOfWordsToGuess;
        evilKeyword = keyword;
      }
    }
    // System.out.println(countOfWordsToGuess);
  }

  /**
   * Extracts the List of WordsToGuess from
   *
   * @param evilMapOfWords with the keyword
   * @param evilKeyword to get a new group (list) of the WordsToGuess with the most elements (also
   *     most possibilities to fail with a guess --> evilWords).
   */
  public static ArrayList<WordToGuess> extractGroupOfEvilWords(
      Map<String, ArrayList<WordToGuess>> evilMapOfWords, String evilKeyword) {
    return evilMapOfWords.get(evilKeyword);
  }

  public ArrayList<WordToGuess> getGroupOfEvilWords() {
    return groupOfEvilWords;
  }
}
