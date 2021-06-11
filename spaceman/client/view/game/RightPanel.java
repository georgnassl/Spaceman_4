package spaceman.client.view.game;

import spaceman.client.controller.Controller;
import spaceman.client.view.util.Components;
import spaceman.sharedmodel.Spaceman;

import javax.swing.*;
import java.awt.*;

class RightPanel extends JPanel {

    private static final long serialVersionUID = 4230162211355981591L;

    RightPanel(final Controller controller, final Spaceman model) {
        setLayout(new GridBagLayout());
        final CountdownDisplay countdownDisplay = new CountdownDisplay(model);
        final JComponent forfeitButton = new ForfeitButton(controller, model);
        final MultiplayerPanel multiplayerPanel = new MultiplayerPanel(model);
        final JButton newGameButton = new JButton("New game");
        newGameButton.addActionListener(
                e -> {
                    controller.requestNewGame();
                });

        GridBagConstraints layoutPlaceInNextRow = new GridBagConstraints();
        layoutPlaceInNextRow.gridx = 0;
        layoutPlaceInNextRow.weightx = 1;
        layoutPlaceInNextRow.insets =
                new Insets(
                        GameScreen.PADDING_DEFAULT_BUTTONS,
                        GameScreen.PADDING_DEFAULT_BUTTONS,
                        GameScreen.PADDING_DEFAULT_BUTTONS,
                        GameScreen.PADDING_DEFAULT_BUTTONS);
        layoutPlaceInNextRow.fill = GridBagConstraints.BOTH;
        GridBagConstraints largePaddingBottom = Components.clone(layoutPlaceInNextRow);
        largePaddingBottom.weighty = 1; // give free vertical space to countdown display

        add(countdownDisplay, layoutPlaceInNextRow);
        add(multiplayerPanel, largePaddingBottom);
        add(newGameButton, layoutPlaceInNextRow);
        add(forfeitButton, layoutPlaceInNextRow);
    }
}
