package spaceman.communication.messages;

import spaceman.sharedmodel.GameState;

import java.io.Serializable;

/** Message to be sent to the clients to notify them that some player guessed. */
public class PlayerGuessedNotification implements Serializable {

  /**
   * Creates a PlayerGuessedNotification object.
   *
   * @param playerWhoGuessed The player who guessed
   * @param nextPlayer THe name of the next player
   * @param gameStateAfterGuess Game state after the guess
   */
  public PlayerGuessedNotification(
      String playerWhoGuessed, String nextPlayer, GameState gameStateAfterGuess) {
    this.playerWhoGuessed = playerWhoGuessed;
    this.nextPlayer = nextPlayer;
    this.gameStateAfterGuess = gameStateAfterGuess;
  }

  private static final long serialVersionUID = 1L;

  private final String playerWhoGuessed;
  private final String nextPlayer;
  private final GameState gameStateAfterGuess;

  public String getPlayerWhoGuessed() {
    return this.playerWhoGuessed;
  }

  public String getNextPlayer() {
    return this.nextPlayer;
  }

  public GameState getGameStateAfterGuess() {
    return this.gameStateAfterGuess;
  }
}
