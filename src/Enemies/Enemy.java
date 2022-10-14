package Enemies;

import Characters.Health;
import bagel.Font;
import bagel.Image;
import bagel.util.Rectangle;

/** This class represents in-game enemies
 * @author Vincent Luu, 1269979
 */

public abstract class Enemy extends Rectangle {

    private static int timescale;

    protected final Health health;

    public Enemy(double xPos, double yPos, Image referenceImage, int maxHP, int minHP) {
        super(xPos, yPos, referenceImage.getWidth(), referenceImage.getHeight());
        health = new Health(maxHP, minHP);
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

    /**
     * Wrapper method for Health.isDead() for better information hiding of health attribute
     */
    public boolean isDead() {
        return health.isDead();
    }

    public static void setTimescale(int timescale) {
        Enemy.timescale = timescale;
    }

    public static int getTimescale() {
        return Enemy.timescale;
    }


}
