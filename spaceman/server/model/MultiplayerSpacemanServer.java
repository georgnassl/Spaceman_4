package spaceman.server.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import spaceman.sharedmodel.GameState;
import spaceman.sharedmodel.SingleplayerSpaceman;
import spaceman.sharedmodel.Spaceman;

/**
 * Represents the game logic of a multiplayer game on the server side. This includes management of
 * the current game state, the list of players and the current player.
 */
public class MultiplayerSpacemanServer implements Spaceman {
  private Spaceman game;
  private int currentPlayerIndex;
  private final List<String> players;
  private final int id;

  /**
   * Create a new game with the given id.
   *
   * @param id The game id.
   */
  public MultiplayerSpacemanServer(int id) {
    players = new ArrayList<>();
    game = SingleplayerSpaceman.create();
    this.id = id;
  }

  public synchronized int getId() {
    return id;
  }

  public synchronized String getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  public synchronized int getNumPlayers() {
    return players.size();
  }

  /**
   * Adds a new player with the given name to the game. If the player is the only player it will
   * become the currentPlayer.
   *
   * @param playerName the name of the player.
   */
  public synchronized void addNewPlayer(final String playerName) {
    players.add(playerName);

    if (players.size() == 1) {
      currentPlayerIndex = 0;
    }
  }

  /**
   * Removes the player with the given name. The current player is updated if the given player was
   * the current one. The method does nothing if no player with the given name exists in the game.
   *
   * @param playerName the name of the player.
   */
  public synchronized void removePlayer(final String playerName) {
    int playerIndex = players.indexOf(playerName);
    if (playerIndex == -1) {
      // Possibly the player has already been removed
      return;
    }

    players.remove(playerName);

    if (playerIndex == currentPlayerIndex) {
      if (players.size() == 0) {
        currentPlayerIndex = -1;
      } else {
        currentPlayerIndex = currentPlayerIndex % players.size();
      }
    }
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener changeListener) {
    throw new UnsupportedOperationException();
  }

  @Override
  public synchronized GameState getState() {
    return game.getState();
  }

  @Override
  public synchronized void guess(char guessedCharacter) {
    game.guess(guessedCharacter);

    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
  }

  @Override
  public synchronized void forfeit() {
    game.forfeit();
  }
}
