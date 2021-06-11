package spaceman.sharedmodel;

import java.io.Serializable;

/**
 * Countdown - if 0 is reached, the game is lost.
 */
public class Countdown implements Serializable {

    private static final long serialVersionUID = 1L;

    private int value;

    public Countdown(final int initialValue) {
        value = initialValue;
    }

    /**
     * Decrease the countdown value by one.
     *
     * @return the current countdown value after decreasing.
     */
    int decrease() {
        assert value > 0 : "The countdown value can't be decreased if it already is " + value;
        value--;
        assert value >= 0 : "The countdown value can never be less than 0 (is " + value + ")";
        return value;
    }

    /**
     * Set the countdown to zero.
     */
    void setToZero() {
        assert value > 0 : "Countdown value can only be set to zero if it is positive. It is " + value;
        value = 0;
    }

    public int getCurrentValue() {
        return value;
    }
}
