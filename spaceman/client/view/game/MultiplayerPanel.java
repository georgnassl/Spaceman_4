package spaceman.client.view.game;

import spaceman.client.model.MultiplayerSpacemanClient;
import spaceman.sharedmodel.Spaceman;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

class MultiplayerPanel extends JPanel implements PropertyChangeListener {

  private static final long serialVersionUID = 1L;

  private final JLabel logMessages;

  MultiplayerPanel(final Spaceman model) {
    logMessages = new JLabel();

    if (!(model instanceof MultiplayerSpacemanClient)) {
      return;
    }

    MultiplayerSpacemanClient multiplayerModel = (MultiplayerSpacemanClient) model;
    final JLabel label = new JLabel("Game ID: " + multiplayerModel.getGameId());
    label.setOpaque(true);
    label.setBackground(Color.DARK_GRAY);
    label.setForeground(Color.WHITE);
    label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();

    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 0;
    c.gridy = 0;
    add(label, c);

    c.fill = GridBagConstraints.BOTH;
    c.gridy = 1;
    c.weighty = 1.0;
    add(getLayoutedLogMessages(logMessages), c);

    model.addPropertyChangeListener(this);
  }

  private static JComponent getLayoutedLogMessages(JLabel logMessages) {
    logMessages.setBorder(BorderFactory.createEmptyBorder(0, -25, 0, 10));

    Box logBox = Box.createVerticalBox();
    logBox.add(logMessages);
    logBox.setOpaque(true);
    logBox.setBackground(Color.WHITE);

    JScrollPane scrollPane = new JScrollPane(logBox);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

    return scrollPane;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName() == "Log") {
      String logContent = getLogContent((List<?>) evt.getNewValue());
      SwingUtilities.invokeLater(() -> logMessages.setText(logContent));
    }
  }

  private String getLogContent(List<?> events) {
    // Use HTML for the bullets
    StringBuilder sb = new StringBuilder();
    sb.append("<html>");
    sb.append("<ul>");

    for (Object event : events) {
      sb.append("<li>");
      sb.append((String) event);
      sb.append("</li>");
    }

    sb.append("</ul>");
    sb.append("</html>");
    return sb.toString();
  }
}
