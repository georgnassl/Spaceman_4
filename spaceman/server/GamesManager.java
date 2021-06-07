package spaceman.server;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import spaceman.communication.Connection;
import spaceman.communication.messages.GameDoesNotExistResponse;
import spaceman.communication.messages.JoinGameRequest;
import spaceman.communication.messages.JoinGameResponse;
import spaceman.communication.messages.NewGameRequest;
import spaceman.communication.messages.PlayerNameAlreadyExistsResponse;
import spaceman.server.model.MultiplayerSpacemanServer;

/**
 * This is the 'controller' of the server. It manages games (i.e. the model) and handles the
 * communication between players (i.e. 'view') and the respective game.
 */
class GamesManager implements Closeable {
  private final Map<Integer, MultiplayerSpacemanServer> games;
  private final Map<Integer, ExecutorService> gameThreadPools;
  private final Map<PlayerConnection, Integer> playerToGameId;

  GamesManager() {
    games = new ConcurrentHashMap<>();
    playerToGameId = new ConcurrentHashMap<>();
    gameThreadPools = new ConcurrentHashMap<>();
  }

  /**
   * Registers an incoming connection. That is, either a {@link NewGameRequest} or a {@link
   * JoinGameRequest} is expected to be received on the given Socket's input stream. The requests
   * are then handled accordingly. This means that possibly a new game is created and the connection
   * is added as a new player. The respective request messages get validated and the client is
   * notified if the validation fails.
   *
   * @param connectionSocket Socket connected to the client
   * @throws IOException If connection breaks
   */
  void handleNewPlayerConnection(Socket connectionSocket) throws IOException {
    Connection connection = new Connection(connectionSocket);

    try {
      // Initially expect a message from the clients to know what they want.
      Object registrationMessage = connection.readObject();
      if (registrationMessage instanceof JoinGameRequest) {
        handleJoinGameRequest(connection, (JoinGameRequest) registrationMessage);
      } else if (registrationMessage instanceof NewGameRequest) {
        handleNewGameRequest(connection, (NewGameRequest) registrationMessage);
      } else {
        // There is a bug in the code. we can simply abort.
        connection.close();
        throw new AssertionError("Unknown Message!");
      }
    } catch (IOException e) {
      // Attempt to close
      // (Probably useless because connection is already broken)
      connection.close();
      throw e;
    }
  }

  private void handleJoinGameRequest(Connection connection, JoinGameRequest joinGameRequest)
      throws IOException {
    String playerName = joinGameRequest.getPlayerName();
    int gameId = joinGameRequest.getGameId();

    if (!games.containsKey(gameId)) {
      handleGameIdDoesNotExist(connection);
    } else {
      addNewPlayerToGame(playerName, connection, gameId);
    }
  }

  private void handleNewGameRequest(Connection connection, NewGameRequest newGameRequest) {
    String playerName = newGameRequest.getPlayerName();

    int gameId = startNewGame();
    addNewPlayerToGame(playerName, connection, gameId);
  }

  private void handleGameIdDoesNotExist(Connection connection) throws IOException {
    GameDoesNotExistResponse response = new GameDoesNotExistResponse();

    connection.writeObject(response);
    connection.close();
  }

  private void addNewPlayerToGame(String playerName, Connection connection, int gameId) {
    MultiplayerSpacemanServer game = games.get(gameId);
    gameThreadPools
        .get(gameId)
        .execute(
            () -> {
              try {

                String actualPlayerName = playerName;
                if (playerName == null || playerName.isBlank()) {
                  actualPlayerName = "unnamed";
                }

                if (isDuplicatePlayerName(actualPlayerName, gameId)) {
                  handleDuplicatePlayerName(connection);
                  return;
                }

                game.addNewPlayer(actualPlayerName);
                try {
                  sendJoinGameResponse(connection, game, actualPlayerName);
                } catch (IOException e) {
                  game.removePlayer(actualPlayerName);
                  if (game.getNumPlayers() == 0) {
                    removeGame(gameId);
                  }

                  return;
                }

                // Send notification to others
                Set<PlayerConnection> players = getPlayersByGameId(gameId);
                for (PlayerConnection player : players) {
                  player.handlePlayerJoined(actualPlayerName);
                }

                PlayerConnection playerConnection =
                    new PlayerConnection(actualPlayerName, this, connection);
                playerToGameId.put(playerConnection, gameId);
                if (game.getCurrentPlayer().equals(actualPlayerName)) {
                  playerConnection.awaitClientAction();
                }
              } catch (IOException e) {
                // Connection is gone - do nothing
              }
            });
  }

  private boolean isDuplicatePlayerName(String playerName, int gameId) {
    for (Map.Entry<PlayerConnection, Integer> playerAndGameId : playerToGameId.entrySet()) {
      String otherPlayerName = playerAndGameId.getKey().getPlayerName();
      if (playerAndGameId.getValue() == gameId && otherPlayerName.equals(playerName)) {
        return true;
      }
    }
    return false;
  }

  private void handleDuplicatePlayerName(Connection connection) throws IOException {
    connection.writeObject(new PlayerNameAlreadyExistsResponse());
    connection.close();
  }

  private int startNewGame() {
    int gameId = getRandomGameId();
    MultiplayerSpacemanServer newGame = new MultiplayerSpacemanServer(gameId);
    games.put(gameId, newGame);
    gameThreadPools.put(gameId, Executors.newSingleThreadExecutor());

    return gameId;
  }

  private void sendJoinGameResponse(
      Connection connection, MultiplayerSpacemanServer initialModel, String playerName)
      throws IOException {
    JoinGameResponse response =
        new JoinGameResponse(
            initialModel.getId(),
            playerName,
            initialModel.getState(),
            initialModel.getCurrentPlayer());

    connection.writeObject(response);
  }

  /**
   * Notifies the model and other players of a game that a player left. Each player is notified
   * through the respective method in {@link PlayerConnection}.
   *
   * @param connectionThatLeft Connecion to the player who left.
   */
  void playerLeft(PlayerConnection connectionThatLeft) {
    if (!playerToGameId.containsKey(connectionThatLeft)) {
      return;
    }

    int gameId = playerToGameId.get(connectionThatLeft);
    gameThreadPools
        .get(gameId)
        .execute(
            () -> {
              MultiplayerSpacemanServer game = games.get(gameId);
              final String playerBefore = game.getCurrentPlayer();

              game.removePlayer(connectionThatLeft.getPlayerName());

              playerToGameId.remove(connectionThatLeft);

              if (game.getNumPlayers() == 0) {
                removeGame(gameId);
                return;
              }

              boolean currentPlayerChanged = !playerBefore.equals(game.getCurrentPlayer());
              Set<PlayerConnection> players = getPlayersByGameId(game.getId());
              for (PlayerConnection player : players) {
                player.handlePlayerLeft(
                    connectionThatLeft.getPlayerName(), game, currentPlayerChanged);
              }
            });
  }

  private void removeGame(int gameId) {
    games.remove(gameId);
    gameThreadPools.get(gameId).shutdownNow();
    gameThreadPools.remove(gameId);
  }

  /**
   * Notifies the model and other players of a game that a player guessed. Each player is notified
   * through the respective method in {@link PlayerConnection}.
   *
   * @param player The connection of the player who guessed.
   * @param guess The guess.
   */
  void playerGuessed(PlayerConnection player, char guess) {
    int gameId = playerToGameId.get(player);
    gameThreadPools
        .get(gameId)
        .execute(
            () -> {
              MultiplayerSpacemanServer game = games.get(gameId);
              assert player.getPlayerName().equals(game.getCurrentPlayer());

              game.guess(guess);

              Set<PlayerConnection> players = getPlayersByGameId(game.getId());
              for (PlayerConnection otherPlayer : players) {
                otherPlayer.handlePlayerGuessed(player.getPlayerName(), game);
              }
            });
  }

  /**
   * Notifies the model and other players of a game that a player forfeited. Each player is notified
   * through the respective method in {@link PlayerConnection}.
   *
   * @param player The connection of the player who forfeited.
   */
  void playerForfeit(PlayerConnection player) {
    int gameId = playerToGameId.get(player);
    gameThreadPools
        .get(gameId)
        .execute(
            () -> {
              MultiplayerSpacemanServer game = games.get(gameId);
              assert player.getPlayerName().equals(game.getCurrentPlayer());

              game.forfeit();

              Set<PlayerConnection> players = getPlayersByGameId(game.getId());
              for (PlayerConnection otherPlayer : players) {
                otherPlayer.handleForfeit(game);
              }
            });
  }

  private static int getRandomGameId() {
    return (int) (System.currentTimeMillis() % 100000);
  }

  private Set<PlayerConnection> getPlayersByGameId(int gameId) {
    Set<PlayerConnection> players = new HashSet<>();
    for (Map.Entry<PlayerConnection, Integer> entry : playerToGameId.entrySet()) {
      if (entry.getValue() == gameId) {
        players.add(entry.getKey());
      }
    }

    return players;
  }

  @Override
  public void close() throws IOException {
    for (PlayerConnection connection : playerToGameId.keySet()) {
      try {
        connection.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    for (ExecutorService executorService : gameThreadPools.values()) {
      executorService.shutdownNow();
    }
  }
}
