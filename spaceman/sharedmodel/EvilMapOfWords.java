package spaceman.sharedmodel;


import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EvilMapOfWords {

    private final Map<String, ArrayList<WordToGuess>> evilMapOfWords = new ConcurrentHashMap<>();


}
