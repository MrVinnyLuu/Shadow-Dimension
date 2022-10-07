package Obstacles;

import bagel.Image;
import bagel.util.Rectangle;

public class Tree extends Obstacle {

    private static final Image TREE_IMAGE = new Image("res/tree.png");

    public Tree(double xPos, double yPos) {
        super(xPos, yPos, TREE_IMAGE);
    }

    @Override
    public Image getImage() {
        return TREE_IMAGE;
    }

    @Override
    public boolean contacts(Rectangle rect) {
        return intersects(rect);
    }

}
