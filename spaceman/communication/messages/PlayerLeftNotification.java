package spaceman.communication.messages;

import java.io.Serializable;
import spaceman.sharedmodel.GameState;

/** Message to be sent to the clients if some player left the game. */
public class PlayerLeftNotification implements Serializable {

  /**
   * Creates a PlayerLeftNotification object.
   * @param playerName Name of the player who left.
   * @param currentPlayer Name of the current player.
   * @param currentGameState Current gamestate.
   */
  public PlayerLeftNotification(
      String playerName, String currentPlayer, GameState currentGameState) {
    this.playerName = playerName;
    this.currentPlayer = currentPlayer;
    this.currentGameState = currentGameState;
  }

  private static final long serialVersionUID = 1L;

  private String playerName;
  private String currentPlayer;
  private GameState currentGameState;

  public String getPlayerName() {
    return this.playerName;
  }

  public String getCurrentPlayer() {
    return this.currentPlayer;
  }

  public GameState getCurrentGameState() {
    return this.currentGameState;
  }
}
