package spaceman.sharedmodel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

/** Class that contains all possible words for guessing. */
public class WordDatabase {

  private static final String[] WORDS = {
    "Spaceman", "Alien", "Earthling", "Homo Sapiens",
  };
  private ArrayList<String> wholeDatabase;
  static String filename = "dictionary.txt";

  /**
   * New:
   * Reads the words from the given file and returns a arrayList<String>.
   * @TODO: Turn System.out.println into comment for the Praktomat.
   */
  public ArrayList<String> readWordsFromFile() throws IOException {

    File dictionary = new File(Objects.requireNonNull(WordDatabase.class.getResource(filename)).getPath());
    FileReader reader = new FileReader(String.valueOf(dictionary));
    BufferedReader bufferedReader = new BufferedReader(reader);
    ArrayList<String> wholeDatabase = new ArrayList<String>();
    String currentLine = null;
    while ((currentLine = bufferedReader.readLine()) != null) {
      wholeDatabase.add(currentLine);
    }
    bufferedReader.close();
    System.out.println("WordDatabase: "+ wholeDatabase.size() + " words added to the database");
    return wholeDatabase;
  }

  /**
   * New: Now the word database changed to the array<String> wholeDatabase
   * Return a randomly chosen word from the word database. Words are chosen randomly according to a
   * uniform distribution.
   */
  String getWord() {
    //int guessedIndex = new Random().nextInt(WORDS.length);
    //return WORDS[guessedIndex];
    ArrayList<String> wholeDatabase = new ArrayList<String>();
    try {
      wholeDatabase = readWordsFromFile();
    } catch (IOException e) {
      System.out.println("File not found");
    }
    int guessedIndex = new Random().nextInt(wholeDatabase.size());
    this.wholeDatabase = wholeDatabase;
    return this.wholeDatabase.get(guessedIndex);
  }

  public ArrayList<String> getWholeDatabase() {
    return wholeDatabase;
  }
}
