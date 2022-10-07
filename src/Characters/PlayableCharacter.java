package Characters;

import ShadowDimension.ShadowDimension;
import Enemies.Enemy;
import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class PlayableCharacter extends Rectangle {

    private final static int MAX_HP = 100, MIN_HP = 0;
    private final static int DAMAGE = 20;
    private final static double PLAYER_STEP = 2;
    private final static double ATTACK_DURATION = 1, COOLDOWN_DURATION = 2, INVINCIBILITY_DURATION = 3;

    /**
     * Attribute that stores information about the character's health
     */
    public Health health = new Health(MAX_HP, MIN_HP);
    private boolean isFaceRight = true, isAttacking = false, isInvincible = false;
    private double attackTimer = 0, cooldownTimer = COOLDOWN_DURATION, invincibilityTimer = 0;
    // x and y coordinates refers to the top left corner
    private double xPos, yPos, prevX, prevY;


    protected PlayableCharacter (double startingX, double startingY, Image referenceImage) {
        // Dimensions taken from the pixel size of leftImage which is assumed to be same as rightImage
        super(startingX, startingY, referenceImage.getWidth(), referenceImage.getHeight());
        this.xPos = startingX;
        this.yPos = startingY;
        prevX = startingX;
        prevY = startingY;
    }

    protected abstract String getCharacterName();

    public abstract Image getImage();

    private void updateState() {

        cooldownTimer += 1.0/ ShadowDimension.REFRESH_RATE;

        if (isAttacking) {

            attackTimer += 1.0/ ShadowDimension.REFRESH_RATE;
            if (attackTimer >= ATTACK_DURATION) {
                isAttacking = false;
                attackTimer = 0;
                cooldownTimer = 0;
            }
        }

        if (isInvincible) {
            invincibilityTimer += 1.0/ ShadowDimension.REFRESH_RATE;
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

    @Override
    public boolean intersects(Rectangle rectangle) {
        // Resolves the fact that attack image and normal images are different sizes
        return new Rectangle(topLeft(), getImage().getWidth(), getImage().getHeight()).intersects(rectangle);
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
     * Method lowers the character's health points according to "damage"
     */
    public void takesDamage(String attacker, int damage) {

        if (isInvincible) return;
        if (!attacker.equals("Sinkhole")) isInvincible = true;

        health.takesDamage(damage);

        System.out.format("%s inflicts %d damage points on %s. %s's current health: %s\n",
                attacker, damage, getCharacterName(), getCharacterName(), health);

    }

    public void dealsDamage(Enemy target) {
        target.takesDamage(getCharacterName(), DAMAGE);
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    protected boolean isFaceRight() {
        return isFaceRight;
    }

}
