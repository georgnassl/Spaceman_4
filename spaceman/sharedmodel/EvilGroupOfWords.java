package spaceman.sharedmodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EvilGroupOfWords {

  private static Map<String, ArrayList<WordToGuess>> evilMapOfWords = new ConcurrentHashMap<>();
  private static ArrayList<WordToGuess> evilGroupOfWords;
  private static CurrentListOfWords listOfWords;

  public static EvilGroupOfWords createEvilGroupOfWords (CurrentListOfWords currentListOfWords, Character guessedCharacter) {
    EvilGroupOfWords evilGroupOfWords = new EvilGroupOfWords();
    listOfWords = currentListOfWords;



    return evilGroupOfWords;
  }

  /**
  * Produces a evilMapOfWords, where all the words (with the newly revealed guessed character)
  * are ordered by the position of the new revealed character (this is performed by taking the string of the word as keyword).
  * @param listOfWords the List of WordsToGuess in where the guessed character should be revealed and that should be evil processed.
  * @param guessedCharacter the newly guessed character.
  */
  public static void produceEvilMap(CurrentListOfWords listOfWords, char guessedCharacter) {
    ArrayList<WordToGuess> newListOfWords = listOfWords.revealCharactersInWholeList(listOfWords.getCurrentListOfWords(), guessedCharacter);

    evilMapOfWords.clear();
    for (WordToGuess word : newListOfWords) {
        String keyword = listOfWords.convertToCurrentWordAsString(word);
        if (!evilMapOfWords.keySet().contains(keyword)) {
            evilMapOfWords.put(keyword, new ArrayList<WordToGuess>());
        }
        evilMapOfWords.get(keyword).add(word);
        }
    }

    /**
     * Produces the String of an evil pattern.
     *
     * @param wordList         the ArrayList of WordToGuess from which the pattern is produced
     * @param guessedCharacter the character the string of an evil pattern is produced with.
     * @return a String of an evil pattern.
     */
    public static String getEvilPattern(ArrayList<WordToGuess> wordList, char guessedCharacter) {

        //guess(wordList, guessedCharacter);

        candidates.clear();

        for (WordToGuess old : wordList) {
            WordToGuess newWord = WordToGuess.revealSpecificChar(old, guessedCharacter);
            String newPattern = convertCharactersToString(newWord.getCharacters());

            if (!candidates.keySet().contains(newPattern)) {
                candidates.put(newPattern, new ArrayList<>());
            }
            candidates.get(newPattern).add(newWord);
        }

        String currentEvilPattern = "";
        int maxNumberOfWords = 0;

        for (String pattern : candidates.keySet()) {
            int numberOfWords = candidates.get(pattern).size();

            if (numberOfWords > maxNumberOfWords) {
                maxNumberOfWords = numberOfWords;
                currentEvilPattern = pattern;
            }
        }
        //System.out.println(maxNumberOfWords);
        //System.out.println(candidates.get(currentEvilPattern));
        return currentEvilPattern;
    }

    /**
     * Produces an evil WordToGuess object.
     *
     * @param wordList         the ArrayList of WordToGuess the evil word is produced from.
     * @param guessedCharacter the character the evil word is produced with
     * @return an evil WordToGuess object.
     */
    public static WordToGuess getEvilWord(ArrayList<WordToGuess> wordList,
                                          char guessedCharacter) {
        String mostEvilPattern = getEvilPattern(wordList, guessedCharacter);
        //System.out.println(mostEvilPattern);
        ArrayList<WordToGuess> candidateWords = candidates.get(mostEvilPattern);
        //Collections.shuffle(candidateWords);

        return candidateWords.get(0);
    }


}
