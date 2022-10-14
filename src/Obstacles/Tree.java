package Obstacles;

import bagel.Image;

/**
 * This class represents Trees, a type of Obstacle
 * @author Vincent Luu, 1269979
 */

public class Tree extends Obstacle {

    private static final Image TREE_IMAGE = new Image("res/tree.png");

    /**
     * Creates a tree at (xPos, yPos)
     */
    public Tree(double xPos, double yPos) {
        super(xPos, yPos, TREE_IMAGE);
    }

    @Override
    public Image getImage() {
        return TREE_IMAGE;
    }

}
