package spaceman.client.model;

import spaceman.communication.Connection;
import spaceman.communication.messages.*;
import spaceman.sharedmodel.GameState;
import spaceman.sharedmodel.Spaceman;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Represents the game logic for a multiplayer game on the client side. This includes communication
 * with the server.
 */
public class MultiplayerSpacemanClient implements Spaceman, Closeable {
    private static final int PORT = 4441;

    private Connection connection;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final ExecutorService executorService;

    private GameState gameState;
    private String currentPlayer;
    private int gameId;
    private String playerName;

    private List<String> events;

    private MultiplayerSpacemanClient() {
        executorService = Executors.newSingleThreadExecutor();
    }

    public static class UnknownGameIdException extends Exception {
        private static final long serialVersionUID = 1L;
    }

    public static class DuplicatePlayerNameException extends Exception {
        private static final long serialVersionUID = 1L;
    }

    /**
     * Sends a message to the server to request joining a game with the given id. Upon successful
     * joining, listeners are notified.
     *
     * @param userName      User name for the player
     * @param serverAddress Address of the game server
     * @param gameId        Id of the game to join
     * @throws IOException                  If there is a communication problem
     * @throws UnknownGameIdException       If the game id in unknown to the server
     * @throws DuplicatePlayerNameException If the player name already exists for the game id
     */
    public void joinGame(final String userName, final String serverAddress, final int gameId)
            throws IOException, UnknownGameIdException, DuplicatePlayerNameException {
        connection = establishConnection(serverAddress, PORT);
        events = new ArrayList<>();

        JoinGameRequest joinGameRequest = new JoinGameRequest(gameId, userName);
        connection.writeObject(joinGameRequest);

        finishConnectionSetup();
    }

    /**
     * Send a message to the server to start a new game. Upon successful creation and joining,
     * listeners are notified.
     *
     * @param userName      User name for the player
     * @param serverAddress Address of the game server
     * @throws IOException                  If there is a communication problem
     * @throws DuplicatePlayerNameException If the player name already exists for the game id
     */
    public void newGame(final String userName, final String serverAddress)
            throws IOException, DuplicatePlayerNameException {
        connection = establishConnection(serverAddress, PORT);
        events = new ArrayList<>();

        NewGameRequest newGameRequest = new NewGameRequest(userName);
        connection.writeObject(newGameRequest);

        try {
            finishConnectionSetup();
        } catch (UnknownGameIdException e) {
            // Server should never have returned GameDoesNotExistResponse
            throw new AssertionError("Invalid Communication", e);
        }
    }

    private void finishConnectionSetup()
            throws IOException, UnknownGameIdException, DuplicatePlayerNameException {
        Object response = connection.readObject();
        // We use exceptions instead of boolean returns or integer return codes, because
        // the errors are so severe that this whole object is unusable. So they should not be ignored.
        if (response instanceof GameDoesNotExistResponse) {
            throw new UnknownGameIdException();
        } else if (response instanceof PlayerNameAlreadyExistsResponse) {
            throw new DuplicatePlayerNameException();
        } else if (response instanceof JoinGameResponse) {
            JoinGameResponse joinGameResponse = (JoinGameResponse) response;

            gameState = joinGameResponse.getCurrentGameState();
            gameId = joinGameResponse.getGameId();
            playerName = joinGameResponse.getPlayerName();
            currentPlayer = joinGameResponse.getCurrentPlayer();
        }

        executorService.execute(
                () -> {
                    // Constantly listen for asynchronous server messages like "Player joined" in a separate
                    // thread
                    while (true) {
                        try {
                            receiveServerMessage();
                        } catch (IOException e) {
                            break;
                        }
                    }
                });
    }

    private static Connection establishConnection(final String serverAddress, final int port)
            throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(serverAddress, port));

        return new Connection(socket);
    }

    private void receiveServerMessage() throws IOException {
        Object message = connection.readObject();
        if (message instanceof PlayerJoinedNotification) {
            handlePlayerJoinedMessage((PlayerJoinedNotification) message);
        } else if (message instanceof PlayerLeftNotification) {
            handlePlayerLeftMessage((PlayerLeftNotification) message);
        } else if (message instanceof ForfeitNotification) {
            handleForfeitMessage((ForfeitNotification) message);
        } else if (message instanceof PlayerGuessedNotification) {
            handlePlayerGuessedMessage((PlayerGuessedNotification) message);
        } else {
            throw new AssertionError("Unknown communication");
        }
    }

    private void handlePlayerJoinedMessage(PlayerJoinedNotification message) {
        events.add("Player " + message.getPlayerName() + " joined.");

        publishLogChange();
    }

    private void publishLogChange() {
        support.firePropertyChange("Log", null, events);
    }

    private void publishCurrentPlayerChange() {
        support.firePropertyChange("CurrentPlayer", null, currentPlayer);
    }

    private void publishGameStateChange() {
        support.firePropertyChange("GameState", null, gameState);
    }

    private void handlePlayerLeftMessage(PlayerLeftNotification message) {
        events.add("Player " + message.getPlayerName() + " left.");

        currentPlayer = message.getCurrentPlayer();
        gameState = message.getCurrentGameState();

        publishCurrentPlayerChange();
        publishGameStateChange();
        publishLogChange();
    }

    private void handleForfeitMessage(ForfeitNotification message) {
        gameState = message.getNewGameState();
        events.add("Forfeit! Game over...");

        publishLogChange();
        publishGameStateChange();
    }

    private void handlePlayerGuessedMessage(PlayerGuessedNotification message) {
        events.add(
                "Player "
                        + message.getPlayerWhoGuessed()
                        + " guessed. Next is player "
                        + message.getNextPlayer()
                        + ".");

        gameState = message.getGameStateAfterGuess();
        currentPlayer = message.getNextPlayer();

        publishLogChange();
        publishCurrentPlayerChange();
        publishGameStateChange();
    }

    public int getGameId() {
        return gameId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public static MultiplayerSpacemanClient create() {
        return new MultiplayerSpacemanClient();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener changeListener) {
        support.addPropertyChangeListener(changeListener);
    }

    @Override
    public GameState getState() {
        return gameState;
    }

    @Override
    public synchronized void guess(char guessedCharacter) {
        GuessRequest message = new GuessRequest(guessedCharacter);
        try {
            connection.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Connection lost");
        }
    }

    @Override
    public synchronized void forfeit() {
        try {
            connection.writeObject(new ForfeitRequest());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Connection lost");
        }
    }

    @Override
    public void close() throws IOException {
        executorService.shutdownNow();
        connection.close();
    }
}
