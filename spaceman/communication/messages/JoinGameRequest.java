package spaceman.communication.messages;

import java.io.Serializable;

/**
 * Message to be sent to the server if a client wants to join a game with the given id as player
 * with the given player name.
 */
public class JoinGameRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int gameId;
    private final String playerName;

    public JoinGameRequest(int gameId, String playerName) {
        this.gameId = gameId;
        this.playerName = playerName;
    }

    public int getGameId() {
        return this.gameId;
    }

    public String getPlayerName() {
        return this.playerName;
    }
}
