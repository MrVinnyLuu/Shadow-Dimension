package Obstacles;

import bagel.Image;

/**
 * This class represents Walls, a type of Obstacle
 * @author Vincent Luu, 1269979
 */

public class Wall extends Obstacle {

    private final static Image WALL_IMAGE = new Image("res/wall.png");

    /**
     * Creates a wall at (xPos, yPos)
     */
    public Wall(double xPos, double yPos) {
        super(xPos, yPos, WALL_IMAGE);
    }

    @Override
    public Image getImage() {
        return WALL_IMAGE;
    }

}
