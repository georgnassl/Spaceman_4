package spaceman.client.view.gamecreation;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import spaceman.client.controller.Controller;
import spaceman.client.view.util.Components;

class CreateGameInteractions extends JPanel {

  private static final long serialVersionUID = 4230162211355981591L;

  private static final int TEXT_FIELD_WIDTH = 20; // columns

  private static final int NUMBER_OF_GRIDBAG_COLUMNS = 2;

  private CreateGameInteractions(final Controller controller) {
    final Component randomWordButton = createRandomWordButton(controller);
    final JTextComponent givenWordField = createWordField();
    final Component useWordButton = createWordButton(controller, givenWordField);

    setLayout(new GridBagLayout());
    add(randomWordButton, getRightEdgeOfFirstRow(NUMBER_OF_GRIDBAG_COLUMNS));
    add(givenWordField, getLeftEdgeOfSecondRow());
    add(useWordButton, getRightEdgeOfSecondRow(NUMBER_OF_GRIDBAG_COLUMNS));
  }

  public static JComponent create(final Controller controller) {
    return new CreateGameInteractions(controller);
  }

  private static GridBagConstraints getRightEdgeOfFirstRow(int numberOfColumns) {
    final GridBagConstraints constraint = new GridBagConstraints();
    constraint.gridx = getLastColumn(numberOfColumns); // start in last column
    constraint.gridy = 0; // first row
    return addGenericLayouting(constraint);
  }

  private static int getLastColumn(int numberOfColumns) {
    return numberOfColumns - 1;
  }

  private static GridBagConstraints getLeftEdgeOfSecondRow() {
    final GridBagConstraints constraint = new GridBagConstraints();
    constraint.gridx = 0; // first column
    constraint.gridy = 1; // second row
    return addGenericLayouting(constraint);
  }

  private static GridBagConstraints getRightEdgeOfSecondRow(int numberOfColumns) {
    final GridBagConstraints constraint = new GridBagConstraints();
    constraint.gridx = getLastColumn(numberOfColumns);
    constraint.gridy = 1; // second row
    return addGenericLayouting(constraint);
  }

  private static GridBagConstraints addGenericLayouting(GridBagConstraints constraint) {
    GridBagConstraints newConstraint = Components.clone(constraint);
    newConstraint.fill = GridBagConstraints.BOTH;
    return newConstraint;
  }

  private static JTextField createWordField() {
    return new JTextField(TEXT_FIELD_WIDTH);
  }

  private static Component createWordButton(
      final Controller controller, final JTextComponent givenWordField) {
    JButton useWordButton = new JButton("Use word");
    useWordButton.addActionListener(
        new ActionListener() {

          @Override
          public void actionPerformed(ActionEvent e) {
            final String wordToUse = givenWordField.getText();
            controller.newGame(wordToUse);
          }
        });
    return useWordButton;
  }

  private static Component createRandomWordButton(final Controller controller) {
    JButton button = new JButton("Random word");
    button.addActionListener(e -> controller.newGame());
    return button;
  }
}
