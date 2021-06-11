package spaceman.client.view.game;

import spaceman.client.controller.Controller;
import spaceman.sharedmodel.GameState;
import spaceman.sharedmodel.GuessChar;
import spaceman.sharedmodel.Spaceman;
import spaceman.sharedmodel.WordToGuess;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

class BottomPanel extends JPanel {

  private static final long serialVersionUID = 4230162211355981591L;

  BottomPanel(final Controller controller, final Spaceman model) {
    final Component wordDisplay = new WordDisplay(model);
    final GuessInteractions guessInteractions = new GuessInteractions(controller, model);

    setLayout(new GridBagLayout());
    GridBagConstraints left = new GridBagConstraints();
    left.gridx = 0;
    left.weightx = 1;
    left.weighty = 1;
    left.insets =
        new Insets(
            GameScreen.PADDING_DEFAULT_BUTTONS,
            GameScreen.PADDING_DEFAULT_BUTTONS,
            GameScreen.PADDING_DEFAULT_BUTTONS,
            GameScreen.PADDING_DEFAULT_BUTTONS);
    left.fill = GridBagConstraints.BOTH;
    left.anchor = GridBagConstraints.SOUTHWEST;
    add(wordDisplay, left);
    GridBagConstraints right = new GridBagConstraints();
    right.gridx = 2;
    right.fill = GridBagConstraints.BOTH;
    right.anchor = GridBagConstraints.EAST;
    right.weightx = 0.1;
    right.weighty = 0.1;
    add(guessInteractions, right);
  }

  private static class WordDisplay extends JPanel implements PropertyChangeListener {

    private static final long serialVersionUID = 4230162211355981591L;

    private static final float FONT_SIZE = 32;

    private final JLabel text = new JLabel();

    WordDisplay(final Spaceman model) {
      text.setFont(getFont().deriveFont(FONT_SIZE));
      text.setText(getDisplayString(model.getState()));

      JScrollPane scrollPane = new JScrollPane(text);
      scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
      scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

      final Dimension preferredSizeOfText = text.getPreferredSize();
      final int heightOfTextAndBar =
          preferredSizeOfText.height
              + scrollPane.getHorizontalScrollBar().getPreferredSize().height;
      text.setPreferredSize(new Dimension(preferredSizeOfText.width, heightOfTextAndBar));

      // Border layout to maximize content
      setLayout(new BorderLayout());
      add(scrollPane);

      model.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
      if (evt.getPropertyName().equals("GameState")) {
        adjustTo((GameState) evt.getNewValue());
      }
    }

    private String getDisplayString(final GameState state) {
      final WordToGuess word = state.getWord();
      final StringBuilder sb = new StringBuilder(" ");
      for (GuessChar c : word.getCharacters()) {
        if (c.maybeGetCharacter().isEmpty()) {
          sb.append("_");
        } else {
          sb.append(c.maybeGetCharacter().get());
        }
        sb.append(" ");
      }
      return sb.toString();
    }

    private void adjustTo(final GameState state) {
      final String displayedString = getDisplayString(state);
      SwingUtilities.invokeLater(() -> text.setText(displayedString));
    }
  }
}
