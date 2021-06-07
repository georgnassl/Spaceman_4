package spaceman.sharedmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/** Represents a spaceman game. Serves as an interface for e.g. the UI. */
public class SingleplayerSpaceman implements Spaceman {

  /** Value the game countdown is started with. */
  private static final int COUNTDOWN_START = 7;

  private GameState state;

  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  private SingleplayerSpaceman(final String wordToGuess) {
    state = new GameState(wordToGuess, COUNTDOWN_START);
  }

  protected SingleplayerSpaceman() {}

  /**
   * Create a new Spaceman game with a word chosen randomly from the {@link WordDatabase}.
   *
   * @return Spaceman instance with the random word
   */
  public static Spaceman create() {
    String randomWord = new WordDatabase().getWord();
    return new SingleplayerSpaceman(randomWord);
  }

  /**
   * Create a Spaceman object with the given word.
   *
   * @param wordToGuess word to use for the game
   * @return Spaceman instance for the given word
   */
  public static Spaceman create(String wordToGuess) {
    return new SingleplayerSpaceman(wordToGuess);
  }

  @Override
  public void addPropertyChangeListener(final PropertyChangeListener changeListener) {
    support.addPropertyChangeListener(changeListener);
  }

  private void notifyListeners() {
    support.firePropertyChange("GameState", null, this.getState());
  }

  @Override
  public GameState getState() {
    return state;
  }

  @Override
  public void guess(char guessedCharacter) {
    if (state.getCurrentPhase() != Phase.RUNNING) {
      throw new IllegalStateException("Game not running.");
    }

    boolean isGuessCorrect = state.getWord().guess(guessedCharacter);
    if (!isGuessCorrect) {
      state.handleIncorrectGuess();
    } else if (state.getWord().isFullyRevealed()) {
      state.endGame();
    }

    notifyListeners();
  }

  @Override
  public void forfeit() {
    state.setCountdownToZero();
    state.endGame();
    notifyListeners();
  }
}
