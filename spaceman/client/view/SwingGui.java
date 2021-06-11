package spaceman.client.view;

import spaceman.client.controller.Controller;
import spaceman.client.view.game.GameScreen;
import spaceman.client.view.gamecreation.CreateGameScreen;
import spaceman.client.view.selectmultiplayer.SelectGameScreen;
import spaceman.client.view.welcome.WelcomeScreen;
import spaceman.sharedmodel.Spaceman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Optional;
import java.util.function.Consumer;

public class SwingGui extends JFrame {

  private static final long serialVersionUID = 4230162211355981591L;

  private static final int MINIMUM_FRAME_WIDTH = 400;
  private static final int MINIMUM_FRAME_HEIGHT = 200;

  private final Controller controller;

  /**
   * Creates a new SwingGui window for {@link Spaceman}, with the given {@link Controller}. The
   * window provides three views: A welcome screen, a new-game screen, and a game-screen. By
   * default, the welcome screen is shown.
   *
   * <p>The window is only made visible once {@link #makeVisible()} is called.
   *
   * @see #setWelcomeScreen()
   * @see #setNewSinglePlayerGameScreen()
   * @see #setGameScreen(Spaceman)
   */
  public SwingGui(final Controller controller) {
    super("Spaceman");
    setMinimumSize(new Dimension(MINIMUM_FRAME_WIDTH, MINIMUM_FRAME_HEIGHT));
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.controller = controller;

    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            controller.closeWindow();
          }
        });

    // default screen when starting
    setWelcomeScreen();
  }

  /**
   * Sets the welcome screen. This screen shows the spaceman start screen and allows to change to
   * creating a new game.
   */
  public void setWelcomeScreen() {
    clearAllContent();
    add(WelcomeScreen.create(controller));
    pack();
  }

  /**
   * Sets the new-game screen. This screen provides utilities to create a new {@link Spaceman} game.
   */
  public void setNewSinglePlayerGameScreen() {
    clearAllContent();
    add(CreateGameScreen.create(controller));
    pack();
  }

  /**
   * Sets the multiplayer new-game screen. This screen provides utilities to create a new
   * multiplayer game. This means providing a server address, port and game id to join
   */
  public void setNewMultiPlayerGameScreen() {
    clearAllContent();
    add(SelectGameScreen.create(controller));
    pack();
  }

  /**
   * Sets the game screen for the given {@link Spaceman} game.
   *
   * @param game Spaceman game to create the view for.
   */
  public void setGameScreen(final Spaceman game) {
    clearAllContent();
    add(GameScreen.create(controller, game));
    pack();
  }

  /** Shows the GUI. If the GUI is already visible, this method has no effect. */
  public void makeVisible() {
    if (isVisible()) {
      return;
    }
    pack();
    setVisible(true);
  }

  /** Show the given error message to the user. */
  public void showError(String message) {
    JOptionPane.showMessageDialog(this, message, "Error!", JOptionPane.ERROR_MESSAGE);
  }

  /** Show a message that notifies the user that the given game id does not exist. */
  public void showUnknownGameIdMessage() {
    Optional<SelectGameScreen> maybeSelectGameScreen = getSelectGameScreenIfActive();
    if (maybeSelectGameScreen.isPresent()) {
      maybeSelectGameScreen.get().showUnknownGameIdMessage();
    } else {
      showError("Unknown game id");
    }
  }

  /** Show a message that notifies the user that the given user name is already in use. */
  public void showDuplicatePlayerNameMessage() {
    Optional<SelectGameScreen> maybeSelectGameScreen = getSelectGameScreenIfActive();
    if (maybeSelectGameScreen.isPresent()) {
      maybeSelectGameScreen.get().showDuplicatePlayerNameMessage();
    } else {
      showError("Player name already exists.");
    }
  }

  private void clearAllContent() {
    getContentPane().removeAll();
  }

  /** Shows the loading animation. */
  public void showLoadingScreen() {
    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    getSelectGameScreenIfActive()
        .ifPresent(
            new Consumer<SelectGameScreen>() {
              @Override
              public void accept(SelectGameScreen gameScreen) {
                gameScreen.setDisabled();
              }
            });
  }

  /** Hides the loading animation. */
  public void hideLoadingScreen() {
    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    getSelectGameScreenIfActive().ifPresent((gameScreen) -> gameScreen.setEnabled());
  }

  private Optional<SelectGameScreen> getSelectGameScreenIfActive() {
    for (Component component : getContentPane().getComponents()) {
      if (component instanceof SelectGameScreen) {
        return Optional.of((SelectGameScreen) component);
      }
    }
    return Optional.empty();
  }
}
