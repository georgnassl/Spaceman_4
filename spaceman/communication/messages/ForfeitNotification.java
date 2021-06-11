package spaceman.communication.messages;

import spaceman.sharedmodel.GameState;

import java.io.Serializable;

/** Message to be sent to the client if some player forfeited. */
public class ForfeitNotification implements Serializable {

  public ForfeitNotification(GameState newGameState) {
    this.newGameState = newGameState;
  }

  private static final long serialVersionUID = 1L;

  private final GameState newGameState;

  public GameState getNewGameState() {
    return this.newGameState;
  }
}
