package Obstacles;

import bagel.Image;

/**
 * // SWEN20003 Project 2, Semester 2, 2022 //
 * This class represents Walls, a type of Obstacle
 * @author Vincent Luu, 1269979
 */


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
