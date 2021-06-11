package spaceman.communication.messages;

import java.io.Serializable;

/**
 * Message to be sent to the client as a response to {@link JoinGameRequest} or {@link
 * NewGameRequest} if the player name already exists in the game.
 */
public class PlayerNameAlreadyExistsResponse implements Serializable {
  private static final long serialVersionUID = 1L;
}
