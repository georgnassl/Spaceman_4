package spaceman.communication.messages;

import java.io.Serializable;
import spaceman.sharedmodel.GameState;

/** Message to be sent to the client if some player forfeited. */
public class ForfeitNotification implements Serializable {

  public ForfeitNotification(GameState newGameState) {
    this.newGameState = newGameState;
  }

  private static final long serialVersionUID = 1L;

  private GameState newGameState;

  public GameState getNewGameState() {
    return this.newGameState;
  }
}
