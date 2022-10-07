package Enemies;

import bagel.Font;
import bagel.Image;
import bagel.util.Rectangle;

public abstract class Enemy extends Rectangle {

    protected boolean isExhausted = false;

    public Enemy(double xPos, double yPos, Image referenceImage) {
        super(xPos, yPos, referenceImage.getWidth(), referenceImage.getHeight());
    }

    public abstract void displayHP(Font font);

    public abstract Image getImage();

    public abstract void updateState();

    public abstract void reverseMovement();

    public abstract void takesDamage(String attacker, int damage);

    public boolean isExhausted() {
        return isExhausted;
    }

}
