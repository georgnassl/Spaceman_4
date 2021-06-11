package spaceman.client.view.util;

import javax.swing.*;
import java.awt.*;

/**
 * This class provides utility methods for handling different swing components.
 */
public final class Components {

    // util class, so no instances needed
    private Components() {
    }

    /**
     * Centers the given component horizontally.
     *
     * @param component the component to center
     * @return a new component, with the given component centered horizontally.
     */
    public static JComponent centerHorizontally(final JComponent component) {
        JComponent center = new Box(BoxLayout.X_AXIS);
        center.add(Box.createHorizontalGlue());
        center.add(component);
        center.add(Box.createHorizontalGlue());
        return center;
    }

    /**
     * Clones the given {@link GridBagConstraints}.
     *
     * @param constraint object to clone
     * @return clone of the given object
     */
    public static GridBagConstraints clone(final GridBagConstraints constraint) {
        return (GridBagConstraints)
                constraint.clone(); // BEWARE: shallow clone! we may want to change this in the future.
    }
}
