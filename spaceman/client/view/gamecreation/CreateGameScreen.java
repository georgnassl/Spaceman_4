package spaceman.client.view.gamecreation;

import spaceman.client.controller.Controller;
import spaceman.client.view.util.Banner;

import javax.swing.*;
import java.awt.*;

/**
 * New-game screen for Spaceman. Allows creating a new game with a random or a given word-to-guess.
 */
public class CreateGameScreen extends JPanel {

    private static final long serialVersionUID = 4230162211355981591L;

    private static final int MARGIN_BETWEEN_COMPONENTS = 15; // px

    private CreateGameScreen() {
    }

    /**
     * Creates a new NewGameScreen object.
     */
    public static CreateGameScreen create(final Controller controller) {
        CreateGameScreen screen = new CreateGameScreen();
        // Alternative to Box/BoxLayout: BorderLayout
        screen.setLayout(new BorderLayout(MARGIN_BETWEEN_COMPONENTS, MARGIN_BETWEEN_COMPONENTS));
        screen.add(Banner.create("Create game"), BorderLayout.NORTH);
        screen.add(CreateGameInteractions.create(controller), BorderLayout.CENTER);
        return screen;
    }
}
