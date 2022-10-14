package Obstacles;

import bagel.Image;
import bagel.util.Rectangle;

/**
 * This class represents in-game obstacles
 * @author Vincent Luu, 1269979
 */

public abstract class Obstacle extends Rectangle {

    private boolean isExhausted = false; // Flag to determine if the obstacle is invalid (e.g. not used up or broken)

    /**
     * Creates an obstacle at (xPos, yPos) using referenceImage for dimensions
     */
    public Obstacle (double xPos, double yPos, Image referenceImage) {
        super(xPos, yPos, referenceImage.getWidth(), referenceImage.getHeight());
    }

    /**
     * Returns the image of the obstacle
     */
    public abstract Image getImage();

    /**
     * Method sets the obstacle's status to exhausted
     */
    public void exhaust() {
        isExhausted = true;
    }

    public boolean isExhausted() {
        return isExhausted;
    }

}
