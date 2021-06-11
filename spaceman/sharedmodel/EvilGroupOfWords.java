package spaceman.sharedmodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EvilGroupOfWords {

    private static Map<String, ArrayList<WordToGuess>> evilMapOfWords = new ConcurrentHashMap<>();
    private static ArrayList<WordToGuess> evilGroupOfWords;

}
