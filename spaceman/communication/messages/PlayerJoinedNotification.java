package spaceman.communication.messages;

import java.io.Serializable;

/** Message to be sent to the clients if some player joined the game. */
public class PlayerJoinedNotification implements Serializable {

  public PlayerJoinedNotification(String playerName) {
    this.playerName = playerName;
  }

  private static final long serialVersionUID = 1L;

  private String playerName;

  public String getPlayerName() {
    return this.playerName;
  }
}
