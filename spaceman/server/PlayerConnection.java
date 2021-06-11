package spaceman.server;

import spaceman.communication.Connection;
import spaceman.communication.messages.*;
import spaceman.server.model.MultiplayerSpacemanServer;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles the communication to a player. This class represents a player on the abstraction level
 * between the socket connection and the game logic.
 */
class PlayerConnection implements Closeable {
  private final Connection connection;
  private final GamesManager gamesManager;
  private final String playerName;
  private boolean connected;

  private final ExecutorService readingPool;
  private final ExecutorService writingPool;

  PlayerConnection(String playerName, GamesManager gamesManager, Connection connection) {
    this.gamesManager = gamesManager;
    this.connection = connection;
    this.playerName = playerName;
    connected = true;

    readingPool = Executors.newSingleThreadExecutor();
    writingPool = Executors.newSingleThreadExecutor();
  }

  /**
   * Get the name of the player that is connected through this object.
   *
   * @return The name
   */
  String getPlayerName() {
    return playerName;
  }

  /**
   * Notifies the client in a new thread that some player joined.
   *
   * @param playerName Name of the player who joined.
   */
  void handlePlayerJoined(String playerName) {
    PlayerJoinedNotification message = new PlayerJoinedNotification(playerName);

    sendOrLeaveGame(message);
  }

  /**
   * Notifies the client in a new thread that some player left.
   *
   * @param playerWhoLeft Name of the player who left
   * @param model The model after the player left
   * @param currentPlayerChanged Whether the current player changed after the player left.
   */
  void handlePlayerLeft(
      String playerWhoLeft, MultiplayerSpacemanServer model, boolean currentPlayerChanged) {
    PlayerLeftNotification message =
        new PlayerLeftNotification(playerWhoLeft, model.getCurrentPlayer(), model.getState());

    sendOrLeaveGame(message);

    if (currentPlayerChanged && model.getCurrentPlayer().equals(playerName)) {
      awaitClientAction();
    }
  }

  /**
   * Notifies the client in a new thread that some player guessed.
   *
   * @param playerWhoGuessed Name of the player who guessed
   * @param model The model after the guess.
   */
  void handlePlayerGuessed(String playerWhoGuessed, MultiplayerSpacemanServer model) {
    PlayerGuessedNotification message =
        new PlayerGuessedNotification(playerWhoGuessed, model.getCurrentPlayer(), model.getState());

    sendOrLeaveGame(message);

    if (model.getCurrentPlayer().equals(playerName)) {
      awaitClientAction();
    }
  }

  /**
   * Notifies the client in a new thread that some player forfeited.
   *
   * @param model The model after the forfeit.
   */
  void handleForfeit(MultiplayerSpacemanServer model) {
    ForfeitNotification message = new ForfeitNotification(model.getState());

    sendOrLeaveGame(message);

    // Forfeit does not change the currenty player so we do not need to trigger awaitClientAction();
  }

  /** Listens for messages from the connected client in a new thread. */
  void awaitClientAction() {
    if (!connected) {
      return;
    }

    readingPool.execute(
        () -> {
          try {
            Object message = connection.readObject();

            if (message instanceof GuessRequest) {
              gamesManager.playerGuessed(this, ((GuessRequest) message).getGuess());
            } else if (message instanceof ForfeitRequest) {
              gamesManager.playerForfeit(this);
            } else {
              throw new AssertionError("Invalid Communication");
            }
          } catch (IOException e) {
            connected = false;
            gamesManager.playerLeft(this);
          }
        });
  }

  private void sendOrLeaveGame(Object message) {
    if (!connected) {
      return;
    }

    writingPool.execute(
        () -> {
          try {
            connection.writeObject(message);
          } catch (IOException e) {
            connected = false;
            gamesManager.playerLeft(this);
          }
        });
  }

  @Override
  public void close() throws IOException {
    connection.close();
    readingPool.shutdownNow();
    writingPool.shutdownNow();
  }
}
