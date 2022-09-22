package Obstacles;

import bagel.Image;
import bagel.util.Rectangle;

public abstract class Obstacle extends Rectangle {

    public Obstacle (double xPos, double yPos, Image referenceImage) {
        super(xPos, yPos, referenceImage.getWidth(), referenceImage.getHeight());
    }

    public abstract Image getImage();

}
