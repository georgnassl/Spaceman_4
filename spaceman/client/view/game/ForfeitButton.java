package spaceman.client.view.game;

import spaceman.client.controller.Controller;
import spaceman.client.model.MultiplayerSpacemanClient;
import spaceman.sharedmodel.GameState;
import spaceman.sharedmodel.Phase;
import spaceman.sharedmodel.Spaceman;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

class ForfeitButton extends JButton implements PropertyChangeListener {

  private static final long serialVersionUID = 4230162211355981591L;

  private final String playerName;
  private boolean currentTurn;
  private boolean gameRunning;

  public ForfeitButton(final Controller controller, final Spaceman model) {
    super("Forfeit");

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
    addActionListener(e -> controller.forfeit());
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals("GameState")) {
      adjustTo(((GameState) evt.getNewValue()));
    } else if (evt.getPropertyName().equals("CurrentPlayer")) {
      adjustTo((String) evt.getNewValue());
    }
  }

  private void adjustTo(final GameState state) {
    gameRunning = state.getCurrentPhase() == Phase.RUNNING;
    final boolean isButtonEnabled = currentTurn && gameRunning;
    SwingUtilities.invokeLater(() -> setEnabled(isButtonEnabled));
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
