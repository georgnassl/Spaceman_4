package spaceman.client.view.game;

import java.awt.Component;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import spaceman.client.controller.Controller;
import spaceman.client.model.MultiplayerSpacemanClient;
import spaceman.sharedmodel.GameState;
import spaceman.sharedmodel.Phase;
import spaceman.sharedmodel.Spaceman;

class GuessInteractions extends JPanel {

  private static final long serialVersionUID = 4230162211355981591L;

  GuessInteractions(final Controller controller, final Spaceman model) {
    final JTextComponent characterInput = new CharInput(model);
    final Component guessButton = new GuessButton(controller, model, characterInput);

    setLayout(new GridLayout(1, 2));
    add(characterInput);
    add(guessButton);
  }

  static class CharInput extends JTextField implements PropertyChangeListener {

    private static final long serialVersionUID = 4230162211355981591L;

    private static final int COLUMNS = 2;
    private static final float FONT_SIZE = 32;

    private final String playerName;
    private boolean currentTurn;
    private boolean gameRunning;

    CharInput(final Spaceman model) {
      super(COLUMNS);
      setFont(getFont().deriveFont(FONT_SIZE));

      gameRunning = model.getState().getCurrentPhase() == Phase.RUNNING;
      if (model instanceof MultiplayerSpacemanClient) {
        MultiplayerSpacemanClient multiplayerModel = (MultiplayerSpacemanClient) model;
        playerName = multiplayerModel.getPlayerName();
        adjustTo(multiplayerModel.getCurrentPlayer());
      } else {
        playerName = "";
        currentTurn = true;
      }

      adjustTo(model.getState());
      model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if (evt.getPropertyName().equals("GameState")) {
        adjustTo((GameState) evt.getNewValue());
      } else if (evt.getPropertyName().equals("CurrentPlayer")) {
        adjustTo((String) evt.getNewValue());
      }
    }

    private void adjustTo(final GameState state) {
      gameRunning = state.getCurrentPhase() == Phase.RUNNING;
      final boolean isEnabled = currentTurn && gameRunning;
      SwingUtilities.invokeLater(
          () -> {
            setText("");
            setEnabled(isEnabled);
          });
    }

    private void adjustTo(final String currentPlayer) {
      currentTurn = currentPlayer.equals(playerName);
      final boolean isEnabled = currentTurn && gameRunning;
      SwingUtilities.invokeLater(
          () -> {
            setEnabled(isEnabled);
          });
    }
  }

  static class GuessButton extends JButton implements PropertyChangeListener {

    private static final long serialVersionUID = 4230162211355981591L;

    private final String playerName;
    private boolean currentTurn;
    private boolean gameRunning;

    GuessButton(
        final Controller controller, final Spaceman model, final JTextComponent characterInput) {
      super("Guess");

      gameRunning = model.getState().getCurrentPhase() == Phase.RUNNING;
      if (model instanceof MultiplayerSpacemanClient) {
        MultiplayerSpacemanClient multiplayerModel = (MultiplayerSpacemanClient) model;
        playerName = multiplayerModel.getPlayerName();
        adjustTo(multiplayerModel.getCurrentPlayer());
      } else {
        playerName = "";
        currentTurn = true;
      }

      adjustTo(model.getState());
      model.addPropertyChangeListener(this);
      addActionListener(
          e -> {
            controller.madeGuess(characterInput.getText());
            characterInput.setText("");
          });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if (evt.getPropertyName().equals("GameState")) {
        adjustTo((GameState) evt.getNewValue());
      } else if (evt.getPropertyName().equals("CurrentPlayer")) {
        adjustTo((String) evt.getNewValue());
      }
    }

    private void adjustTo(final GameState state) {
      gameRunning = state.getCurrentPhase() == Phase.RUNNING;
      final boolean isEnabled = currentTurn && gameRunning;
      SwingUtilities.invokeLater(() -> setEnabled(isEnabled));
    }

    private void adjustTo(final String currentPlayer) {
      currentTurn = currentPlayer.equals(playerName);
      final boolean isEnabled = currentTurn && gameRunning;
      SwingUtilities.invokeLater(
          () -> {
            setEnabled(isEnabled);
          });
    }
  }
}
