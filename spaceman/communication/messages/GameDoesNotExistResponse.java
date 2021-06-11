package spaceman.communication.messages;

import java.io.Serializable;

/**
 * Message to be sent to the client if the client sent a {@link JoinGameRequest} with a non-existend
 * game id.
 */
public class GameDoesNotExistResponse implements Serializable {
    private static final long serialVersionUID = 1L;
}
