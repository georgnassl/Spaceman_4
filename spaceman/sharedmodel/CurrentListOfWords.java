package spaceman.sharedmodel;

import java.io.IOException;
import java.util.ArrayList;

public class CurrentListOfWords {
  private static ArrayList<WordToGuess> currentListOfWords;

  public static CurrentListOfWords createCurrentListOfWords(WordDatabase wordDatabase) {
    CurrentListOfWords currentList = new CurrentListOfWords();
    currentList.currentListOfWords = new ArrayList<>();
    return currentList;
  }

  public static ArrayList<WordToGuess> convertToCurrentListOfWords(WordDatabase wordDatabase) {
    ArrayList<String> wholeDatabase = wordDatabase.getWholeDatabase();
    currentListOfWords = new ArrayList<WordToGuess>();
    for (String word : wholeDatabase) {
      WordToGuess wordToGuess = new WordToGuess(word);
      currentListOfWords.add(wordToGuess);
    }
    return currentListOfWords;
  }
}
