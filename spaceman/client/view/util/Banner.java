package spaceman.client.view.util;

import javax.swing.*;
import java.awt.*;

/**
 * A Swing banner. A banner is a large text label with some padding to other components.
 */
public class Banner extends JLabel {

    private static final long serialVersionUID = 4230162211355981591L;

    private static final int DEFAULT_FONT_SIZE = 42; // pt
    private static final int MARGIN = 15; // px

    public static Banner create(final String bannerText) {
        return Banner.create(bannerText, DEFAULT_FONT_SIZE);
    }

    public static Banner create(final String bannerText, final int fontSize) {
        return new Banner(bannerText, DEFAULT_FONT_SIZE);
    }

    private Banner(final String bannerText, final int fontSize) {
        super(bannerText);
        final Font defaultFont = getFont();
        setFont(new Font(defaultFont.getFontName(), Font.PLAIN, fontSize));
        setBorder(BorderFactory.createEmptyBorder(MARGIN, MARGIN, MARGIN, MARGIN));
    }
}
