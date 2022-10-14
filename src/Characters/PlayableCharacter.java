package Characters;

import ShadowDimension.ShadowDimension;
import Enemies.Enemy;

import bagel.Font;
import bagel.Image;
import bagel.Input;
import bagel.Keys;

import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;

/** This class represents player controlled characters
 * @author Vincent Luu, 1269979
 */

public abstract class PlayableCharacter extends Rectangle {

    private final static int MAX_HP = 100, MIN_HP = 0;
    private final static int DAMAGE = 20;
    private final static double PLAYER_STEP = 2;
    private final static double ATTACK_DURATION = 1, COOLDOWN_DURATION = 2, INVINCIBILITY_DURATION = 3;

    private final static double PLAYER_HP_TEXT_X = 20, PLAYER_HP_TEXT_Y = 25;

    public static final ArrayList<String> dontTriggerInvincible =
            new ArrayList<>() {{
                // Can potentially add more game objects that don't trigger invincibility but just Sinkhole for now
                add("Sinkhole");
            }};

    private final Health health = new Health(MAX_HP, MIN_HP);
    private boolean isFaceRight = true, isAttacking = false, isInvincible = false;
    private double attackTimer = 0, cooldownTimer = COOLDOWN_DURATION, invincibilityTimer = 0;
    // x and y coordinates refers to the top left corner
    private double xPos, yPos, prevX, prevY;

    /**
     * Creates a PlayableCharacter at (startingX, startingY) with dimensions of referenceImage
     */
    protected PlayableCharacter (double startingX, double startingY, Image referenceImage) {
        // Dimensions taken from the pixel size of leftImage which is assumed to be same as rightImage
        super(startingX, startingY, referenceImage.getWidth(), referenceImage.getHeight());
        this.xPos = startingX;
        this.yPos = startingY;
        prevX = startingX;
        prevY = startingY;
    }

    public abstract String getCharacterName();

    public Health getHealth() {
        return health;
    }

    /**
     * Method calls displayHP() from the Health class at the required location
     */
    public void displayHP(Font font) {
        health.displayHP(font, PLAYER_HP_TEXT_X, PLAYER_HP_TEXT_Y);
    }

    /**
     * Method returns the correct image depending on direction and character state
     */
    public abstract Image getImage();

    public boolean isFaceRight() {
        return isFaceRight;
    }

    /**
     * Method checks and updates the state of the character once every screen refresh
     */
    private void updateState() {

        cooldownTimer += 1.0/ ShadowDimension.REFRESH_RATE;

        if (isAttacking) {
            attackTimer += 1.0/ShadowDimension.REFRESH_RATE;
            if (attackTimer >= ATTACK_DURATION) {
                isAttacking = false;
                attackTimer = 0;
                cooldownTimer = 0;
            }
        }

        if (isInvincible) {
            invincibilityTimer += 1.0/ShadowDimension.REFRESH_RATE;
            if (invincibilityTimer >= INVINCIBILITY_DURATION) {
                isInvincible = false;
                invincibilityTimer = 0;
            }
        }

    }

    /**
     * Method takes user input and correspondingly moves the character
     */
    public void controlCharacter(Input input) {

        updateState();

        if (input.isDown(Keys.A) && cooldownTimer >= COOLDOWN_DURATION) {
            isAttacking = true;
        }

        // Two separate 'if-else' statements so that player can move diagonally, but not left-right or up-down
        // at the same time.
        if (input.isDown(Keys.RIGHT)) {
            isFaceRight = true;
            prevX = xPos;
            xPos += PLAYER_STEP;
        } else if (input.isDown(Keys.LEFT)) {
            isFaceRight = false;
            prevX = xPos;
            xPos -= PLAYER_STEP;
        }

        if (input.isDown(Keys.UP)) {
            prevY = yPos;
            yPos -= PLAYER_STEP;
        } else if (input.isDown(Keys.DOWN)) {
            prevY = yPos;
            yPos += PLAYER_STEP;
        }

        // Update the parent Rectangle to reflect movement
        super.moveTo(new Point(xPos, yPos));

    }

    /**
     * Method changes the character's x position back to its immediately preceding x position
     */
    public void xPosRollback() {
        xPos = prevX;
    }

    /**
     * Method changes the character's y position back to its immediately preceding y position
     */
    public void yPosRollback() {
        yPos = prevY;
    }

    public void setPosition(double xPos, double yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    /**
     * Method calls the intersects() method in the Rectangle class using the current image size
     * Resolves the fact that attack image and normal images are different sizes
     */
    @Override
    public boolean intersects(Rectangle rectangle) {
        return rectangle.intersects(getImage().getBoundingBoxAt(centre()));
    }

    /**
     * Method lowers the character's health points according to "damage" and prints log
     */
    public void takesDamage(String attacker, int damage) {

        // Damage has no affect while invincible
        if (isInvincible) return;

        // Don't trigger invincibility if attacker is on the "blacklist"
        if (!dontTriggerInvincible.contains(attacker)) isInvincible = true;

        health.takesDamage(damage, attacker, getCharacterName());

    }

    /**
     * Method calls the target's takeDamage() function using the character's damage value
     */
    public void dealsDamage(Enemy target) {
        target.takesDamage(getCharacterName(), DAMAGE);
    }

    public boolean isAttacking() {
        return isAttacking;
    }

}
