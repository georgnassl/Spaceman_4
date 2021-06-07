package spaceman.client.view.game;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import spaceman.sharedmodel.GameState;
import spaceman.sharedmodel.Phase;
import spaceman.sharedmodel.Spaceman;

class TopBanner extends JLabel implements PropertyChangeListener {

  private static final long serialVersionUID = 4230162211355981591L;

  private static final float FONT_SIZE = 42;

  TopBanner(final Spaceman model) {
    setFont(getFont().deriveFont(FONT_SIZE));
    adjustTo(model.getState());
    model.addPropertyChangeListener(this);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals("GameState")) {
      adjustTo((GameState) evt.getNewValue());
    }
  }

  private void adjustTo(final GameState state) {
    if (state.getCurrentPhase() == Phase.FINISHED) {
      setGameOverMessage(state);
    } else {
      display("");
    }
  }

  private void setGameOverMessage(final GameState state) {
    assert state.getCurrentPhase() == Phase.FINISHED;
    if (state.isGameWon()) {
      display("Congratulations, you win!");
    } else {
      display("You lost.");
    }
  }

  private void display(final String text) {
    SwingUtilities.invokeLater(() -> setText(text));
  }
}
