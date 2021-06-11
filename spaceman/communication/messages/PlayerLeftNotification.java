package spaceman.communication.messages;

import spaceman.sharedmodel.GameState;

import java.io.Serializable;

/** Message to be sent to the clients if some player left the game. */
public class PlayerLeftNotification implements Serializable {

  /**
   * Creates a PlayerLeftNotification object.
   *
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

  private final String playerName;
  private final String currentPlayer;
  private final GameState currentGameState;

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
