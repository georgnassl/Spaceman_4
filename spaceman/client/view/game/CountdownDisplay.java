package spaceman.client.view.game;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import spaceman.sharedmodel.GameState;
import spaceman.sharedmodel.Spaceman;

class CountdownDisplay extends JLabel implements PropertyChangeListener {

  private static final long serialVersionUID = 4230162211355981591L;

  public CountdownDisplay(final Spaceman model) {
    updateText(model.getState());
    model.addPropertyChangeListener(this);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals("GameState")) {
      updateText((GameState) evt.getNewValue());
    }
  }

  private void updateText(final GameState model) {
    final int countdownValue = model.getCountdownValue();
    SwingUtilities.invokeLater(() -> setText("Guesses left: " + countdownValue));
  }
}
