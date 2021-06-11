package spaceman.client.view.game;

import spaceman.client.controller.Controller;
import spaceman.client.view.util.Components;
import spaceman.sharedmodel.Spaceman;

import javax.swing.*;
import java.awt.*;

/**
 * Game screen for {@link spaceman.client.view.SwingGui}. Provides all necessary interactions and
 * displays for playing {@link Spaceman}.
 */
public class GameScreen extends JPanel {

    private static final long serialVersionUID = 4230162211355981591L;

    static final int PADDING_DEFAULT_BUTTONS = 5; // px

    private static final int BORDER = 15; // px

    private GameScreen(final Controller controller, final Spaceman model) {
        setLayout(new BorderLayout(BORDER, BORDER));

        add(Components.centerHorizontally(new TopBanner(model)), BorderLayout.NORTH);
        add(ImageDisplay.create(model), BorderLayout.CENTER);
        add(new RightPanel(controller, model), BorderLayout.EAST);
        add(new BottomPanel(controller, model), BorderLayout.SOUTH);
    }

    public static GameScreen create(final Controller controller, final Spaceman model) {
        return new GameScreen(controller, model);
    }
}
