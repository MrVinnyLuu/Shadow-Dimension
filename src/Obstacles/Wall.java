package Obstacles;

import bagel.Image;

public class Wall extends Obstacle {

    private final static Image WALL_IMAGE = new Image("res/wall.png");

    public Wall(double xPos, double yPos) {
        super(xPos, yPos, WALL_IMAGE);
    }

    @Override
    public Image getImage() {
        return WALL_IMAGE;
    }

}
