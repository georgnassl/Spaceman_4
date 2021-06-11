package spaceman.communication.messages;

import spaceman.sharedmodel.GameState;

import java.io.Serializable;

/**
 * Message to be sent to the client as a response to {@link JoinGameRequest} and {@link
 * NewGameRequest}.
 */
public class JoinGameResponse implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * Creates a JoinGameResponse object.
   *
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

  private final int gameId;
  private final String playerName;
  private final GameState currentGameState;
  private final String currentPlayer;

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
