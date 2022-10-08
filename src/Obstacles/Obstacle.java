package Obstacles;

import bagel.Image;
import bagel.util.Rectangle;

/**
 * // SWEN20003 Project 2, Semester 2, 2022 //
 * This class represents in-game obstacles
 * @author Vincent Luu, 1269979
 */

public abstract class Obstacle extends Rectangle {

    public Obstacle (double xPos, double yPos, Image referenceImage) {
        super(xPos, yPos, referenceImage.getWidth(), referenceImage.getHeight());
    }

    public abstract Image getImage();

    @Override
    public boolean intersects(Rectangle rect) {
        return super.intersects(rect);
    }

}
