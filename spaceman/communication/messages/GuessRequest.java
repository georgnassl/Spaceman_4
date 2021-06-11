package spaceman.communication.messages;

import java.io.Serializable;

/** Message to be sent to the server if a client made a guess. */
public class GuessRequest implements Serializable {

  public GuessRequest(char guess) {
    this.guess = guess;
  }

  private static final long serialVersionUID = 1L;

  private final char guess;

  public char getGuess() {
    return this.guess;
  }
}
