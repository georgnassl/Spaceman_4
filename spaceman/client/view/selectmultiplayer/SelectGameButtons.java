package spaceman.client.view.selectmultiplayer;

import spaceman.client.controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SelectGameButtons extends Box {

    private static final long serialVersionUID = 1L;

    private static final int MARGIN = 15; // px
    private final JButton newGameButton;
    private final JButton joinGameButton;

    private SelectGameButtons(
            final Controller controller,
            final SelectGameInputs inputs,
            final SelectGameScreen gameScreen) {
        super(BoxLayout.X_AXIS);

        newGameButton = createNewGameButton(controller, inputs);
        add(newGameButton);

        joinGameButton = createJoinGameButton(controller, inputs, gameScreen);
        add(joinGameButton);

        // add some spacing to the window
        setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
    }

    public static SelectGameButtons create(
            final Controller controller,
            final SelectGameInputs inputs,
            final SelectGameScreen gameScreen) {
        return new SelectGameButtons(controller, inputs, gameScreen);
    }

    private static JButton createNewGameButton(
            final Controller controller, final SelectGameInputs inputs) {
        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        controller.startNewMultiplayerGame(inputs.getUserName(), inputs.getServerAddress());
                    }
                });
        return newGameButton;
    }

    private static JButton createJoinGameButton(
            final Controller controller,
            final SelectGameInputs inputs,
            final SelectGameScreen gameScreen) {
        JButton joinGameButton = new JButton("Join Game");
        joinGameButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String gameId = inputs.getGameId();
                        if (gameId == null || gameId.isBlank()) {
                            gameScreen.showGameIdRequiredMessage();
                            return;
                        }

                        controller.joinMultiplayerGame(
                                inputs.getUserName(), inputs.getServerAddress(), inputs.getGameId());
                    }
                });
        return joinGameButton;
    }

    void setDisabled() {
        newGameButton.setEnabled(false);
        joinGameButton.setEnabled(false);
    }

    void setEnabled() {
        newGameButton.setEnabled(true);
        joinGameButton.setEnabled(true);
    }
}
