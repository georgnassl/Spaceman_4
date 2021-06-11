package spaceman.client.view.selectmultiplayer;

import spaceman.client.controller.Controller;
import spaceman.client.view.util.Banner;
import spaceman.client.view.util.Components;

import javax.swing.*;
import java.awt.*;

/** A window that prompts the user to select the server address, game id and user name. */
public class SelectGameScreen extends JPanel {

  private static final long serialVersionUID = 1L;

  private static final int MARGIN_BETWEEN_COMPONENTS = 15; // px

  private final JLabel connectionErrorText;
  private final SelectGameButtons buttons;

  private SelectGameScreen(final Controller controller) {

    setLayout(new BorderLayout(MARGIN_BETWEEN_COMPONENTS, MARGIN_BETWEEN_COMPONENTS));
    add(Banner.create("Select Game To Join"), BorderLayout.NORTH);

    SelectGameInputs inputs = SelectGameInputs.create();
    add(inputs, BorderLayout.CENTER);

    connectionErrorText = new JLabel("", SwingConstants.CENTER);
    connectionErrorText.setForeground(Color.RED);
    connectionErrorText.setVisible(false);

    Container controls = Box.createVerticalBox();
    controls.add(connectionErrorText);

    buttons = SelectGameButtons.create(controller, inputs, this);
    controls.add(Components.centerHorizontally(buttons));

    add(controls, BorderLayout.SOUTH);
  }

  /** Creates a new NewGameScreen object. */
  public static SelectGameScreen create(final Controller controller) {
    return new SelectGameScreen(controller);
  }

  /** Displays a message that notifies the user that the given game id is invalid. */
  public void showUnknownGameIdMessage() {
    SwingUtilities.invokeLater(
        () -> {
          connectionErrorText.setText("No game with the given id exists.");
          connectionErrorText.setVisible(true);
        });
  }

  /** Displays a message that notifies the user that the given player name aready exists. */
  public void showDuplicatePlayerNameMessage() {
    SwingUtilities.invokeLater(
        () -> {
          connectionErrorText.setText("Player name already exists.");
          connectionErrorText.setVisible(true);
        });
  }

  /** Displays a message that notifies the user that the game id is required. */
  public void showGameIdRequiredMessage() {
    SwingUtilities.invokeLater(
        () -> {
          connectionErrorText.setText("Game Id is required when joining a game.");
          connectionErrorText.setVisible(true);
        });
  }

  /** Sets the buttons in this screen disabled. */
  public void setDisabled() {
    buttons.setDisabled();
  }

  /** Sets the buttons in this screen enabled. */
  public void setEnabled() {
    buttons.setEnabled();
  }
}
