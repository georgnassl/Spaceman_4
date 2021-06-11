package spaceman.client.view.welcome;

import spaceman.client.controller.Controller;
import spaceman.client.view.util.Banner;
import spaceman.client.view.util.Components;

import javax.swing.*;

/**
 * Welcome screen for spaceman. Shows the application title 'Spaceman' and allows selecting
 * singleplayer or multiplayer.
 */
public class WelcomeScreen extends Box {

    private static final long serialVersionUID = 4230162211355981591L;

    private static final int MARGIN = 15; // px

    private WelcomeScreen(final Controller controller) {
        super(BoxLayout.Y_AXIS);

        JButton newSinglePlayerButton = new JButton("Singleplayer");
        newSinglePlayerButton.addActionListener(e -> controller.requestedSinglePlayerMode());
        JButton newMultiPlayerButton = new JButton("Multiplayer");
        newMultiPlayerButton.addActionListener(e -> controller.requestedMultiPlayerMode());

        add(Components.centerHorizontally(Banner.create("Spaceman")));
        add(Components.centerHorizontally(newSinglePlayerButton));
        add(Components.centerHorizontally(newMultiPlayerButton));

        // add some spacing to the window
        setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
    }

    public static WelcomeScreen create(final Controller controller) {
        return new WelcomeScreen(controller);
    }
}
