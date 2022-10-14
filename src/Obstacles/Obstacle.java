package Obstacles;

import bagel.Image;
import bagel.util.Rectangle;

/**
 * This class represents in-game obstacles
 * @author Vincent Luu, 1269979
 */

public abstract class Obstacle extends Rectangle {

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
     * Copy of Rectangle.intersects() so that it can be overridden in certain obstacles
     */
    @Override
    public boolean intersects(Rectangle rect) {
        return super.intersects(rect);
    }

}
