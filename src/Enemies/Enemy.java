package Enemies;

import bagel.Font;
import bagel.Image;
import bagel.util.Rectangle;

public abstract class Enemy extends Rectangle {

    protected boolean isDead = false;

    public Enemy(double xPos, double yPos, Image referenceImage) {
        super(xPos, yPos, referenceImage.getWidth(), referenceImage.getHeight());
    }

    /**
     * Method calls displayHP() from the Health class at the required location
     */
    public abstract void displayHP(Font font);

    /**
     * Method returns the correct image depending on direction and character state
     */
    public abstract Image getImage();

    /**
     * Method checks and updates the state of the character once every screen refresh
     */
    public abstract void updateState();

    /**
     * Method reverses the direction of movement of the enemy
     */
    public abstract void reverseMovement();

    /**
     * Method lowers the character's health points according to "damage" and prints log
     */
    public abstract void takesDamage(String attacker, int damage);

    public boolean isDead() {
        return isDead;
    }

}
