package spaceman.communication.messages;

import java.io.Serializable;
import spaceman.sharedmodel.GameState;

/**
 * Message to be sent to the client as a response to {@link JoinGameRequest} and {@link
 * NewGameRequest}.
 */
public class JoinGameResponse implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * Creates a JoinGameResponse object.
   * @param gameId The game id.
   * @param playerName Name of the player who wants to join.
   * @param currentGameState The current game state.
   * @param currentPlayer Name of current player.
   */
  public JoinGameResponse(
      int gameId, String playerName, GameState currentGameState, String currentPlayer) {
    this.gameId = gameId;
    this.playerName = playerName;
    this.currentGameState = currentGameState;
    this.currentPlayer = currentPlayer;
  }

  private int gameId;
  private String playerName;
  private GameState currentGameState;
  private String currentPlayer;

  public int getGameId() {
    return this.gameId;
  }

  public String getPlayerName() {
    return this.playerName;
  }

  public GameState getCurrentGameState() {
    return this.currentGameState;
  }

  public String getCurrentPlayer() {
    return this.currentPlayer;
  }
}
