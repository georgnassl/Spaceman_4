package spaceman.sharedmodel;

import java.beans.PropertyChangeListener;

/**
 * Represents a spaceman game. Serves as an interface for e.g. the UI.
 */
public interface Spaceman {
    void addPropertyChangeListener(final PropertyChangeListener changeListener);

    GameState getState();

    /**
     * Guess the given character. If the character is in the current word to guess, all occurrences of
     * the character are revealed in the word. If the character is not in the current word to guess,
     * the countdown decreases by one.
     *
     * <p>This method can only be called on an active game. Otherwise, an <code>IllegalStateException
     * </code> is thrown.
     *
     * <p>If the guess is correct and the whole word is revealed as a consequence, the game is
     * stopped.
     *
     * @param guessedCharacter character to guess
     * @throws IllegalStateException if the current Spaceman game is not running
     * @see Phase
     */
    void guess(char guessedCharacter);

    /**
     * Forfeit the current game. Fully reveal the word-to-guess and end the current game.
     *
     * <p>This method can only be called on an active game. Otherwise, an <code>IllegalStateException
     * </code> is thrown.
     *
     * @throws IllegalStateException if the current Spaceman game is not running
     */
    void forfeit();
}
