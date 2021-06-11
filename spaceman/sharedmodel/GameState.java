package spaceman.sharedmodel;

import java.io.Serializable;

/**
 * Represents the internal state of the game.
 */
public class GameState implements Serializable {

    private static final long serialVersionUID = 1L;

    private Phase currentPhase;
    private final WordToGuess wordToGuess;
    private final Countdown countdown;
    private final int initialCountdownValue;

    GameState(final String word, final int countdownValue) {
        wordToGuess = new WordToGuess(word);
        countdown = new Countdown(countdownValue);
        initialCountdownValue = countdownValue;
        currentPhase = Phase.RUNNING;
    }

    /**
     * Returns whether the current game is won. A game is won if the user guessed the whole word
     * before the countdown reached zero.
     *
     * @return <code>true</code> if the game was won, <code>false</code> otherwise (i.e., the game is
     * still running or was lost)
     */
    public boolean isGameWon() {
        return countdown.getCurrentValue() > 0 && currentPhase.equals(Phase.FINISHED);
    }

    /**
     * Updates the state after an incorrect guess.
     */
    void handleIncorrectGuess() {
        countdown.decrease();
        if (getCountdownValue() == 0) {
            endGame();
        }
    }

    /**
     * Updates the state to represent a finished (lost/forfeit) game.
     */
    void endGame() {
        currentPhase = Phase.FINISHED;
        wordToGuess.revealAll();
    }

    /**
     * Return the current phase of the game.
     *
     * @return the current phase.
     */
    public Phase getCurrentPhase() {
        return currentPhase;
    }

    public WordToGuess getWord() {
        return wordToGuess;
    }

    public int getCountdownValue() {
        return countdown.getCurrentValue();
    }

    void setCountdownToZero() {
        countdown.setToZero();
    }

    public int getMaximumCountdownValue() {
        return initialCountdownValue;
    }

}
