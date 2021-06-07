package spaceman.communication.messages;

import java.io.Serializable;
import spaceman.sharedmodel.GameState;

/** Message to be sent to the clients to notify them that some player guessed. */
public class PlayerGuessedNotification implements Serializable {

  /**
   * Creates a PlayerGuessedNotification object.
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

  private String playerWhoGuessed;
  private String nextPlayer;
  private GameState gameStateAfterGuess;

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
