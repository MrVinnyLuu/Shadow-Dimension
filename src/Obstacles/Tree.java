package Obstacles;

import bagel.Image;

public class Tree extends Obstacle{

    private static final Image TREE_IMAGE = new Image("res/tree.png");

    public Tree(double xPos, double yPos) {
        super(xPos, yPos, TREE_IMAGE);
    }

}
