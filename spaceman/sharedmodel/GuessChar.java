package spaceman.sharedmodel;

import java.io.Serializable;
import java.util.Optional;

/**
 * A single character guess. This can be either an unrevealed character or a revealed character.
 */
public class GuessChar implements Serializable {

    private static final long serialVersionUID = 1L;

    // Optional.empty() if the character was not guessed yet.
    // Optional.of(char), otherwise.
    private final Character character;

    /**
     * Creates an empty GuessChar.
     */
    GuessChar() {
        character = null;
    }

    /**
     * Creates a GuessChar with the corresponding revealed character.
     */
    GuessChar(char revealedChar) {
        character = revealedChar;
    }

    /**
     * Returns the character represented by this instance. If the character was not reveald yet, the
     * Optional is empty, otherwise it contains the revealed character.
     *
     * @return an Optional maybe holding the character
     */
    public Optional<Character> maybeGetCharacter() {
        return Optional.ofNullable(character);
    }
}
