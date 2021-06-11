package spaceman.communication.messages;

import java.io.Serializable;

/**
 * Message to be sent to the server if a client request to create a new game and join it with the
 * given name.
 */
public class NewGameRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    public NewGameRequest(String playerName) {
        this.playerName = playerName;
    }

    private final String playerName;

    public String getPlayerName() {
        return this.playerName;
    }
}
