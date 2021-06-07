package spaceman.client.view.selectmultiplayer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import spaceman.client.view.util.Components;

class SelectGameInputs extends Box {

  private static final long serialVersionUID = 1L;

  private final JTextComponent userNameInput;
  private final JTextComponent serverAddressInput;
  private final JTextComponent gameIdInput;

  private static final int MARGIN = 15; // px

  private SelectGameInputs() {
    super(BoxLayout.Y_AXIS);

    userNameInput = new JTextField();
    serverAddressInput = new JTextField();
    gameIdInput = new JTextField();

    add(new JLabel("User Name"));
    add(Components.centerHorizontally(userNameInput));
    add(new JLabel("Server IP"));
    add(Components.centerHorizontally(serverAddressInput));
    add(new JLabel("Game Id (only for join game)"));
    add(Components.centerHorizontally(gameIdInput));

    // add some spacing to the window
    setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
  }

  static SelectGameInputs create() {
    return new SelectGameInputs();
  }

  String getUserName() {
    return userNameInput.getText();
  }

  String getServerAddress() {
    return serverAddressInput.getText();
  }

  String getGameId() {
    return gameIdInput.getText();
  }
}
