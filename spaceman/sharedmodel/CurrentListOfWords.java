package spaceman.sharedmodel;

import java.io.IOException;
import java.util.ArrayList;

public class CurrentListOfWords {
  private ArrayList<WordToGuess> currentListOfWords;

  public CurrentListOfWords createCurrentListOfWords(WordDatabase wordDatabase) throws IOException {
    CurrentListOfWords currentList = new CurrentListOfWords();
    currentList.currentListOfWords = new ArrayList<>();
    ArrayList<String> wholeDatabase = wordDatabase.readWordsFromFile();
    return currentList;
  }
}
