package Obstacles;

import bagel.Image;

/**
 * // SWEN20003 Project 2, Semester 2, 2022 //
 * This class represents Trees, a type of Obstacle
 * @author Vincent Luu, 1269979
 */


public class Tree extends Obstacle {

    private static final Image TREE_IMAGE = new Image("res/tree.png");

    public Tree(double xPos, double yPos) {
        super(xPos, yPos, TREE_IMAGE);
    }

    @Override
    public Image getImage() {
        return TREE_IMAGE;
    }

}
