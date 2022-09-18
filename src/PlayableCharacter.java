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

    // Set initial health to maximum health
    private int healthPoints = MAX_HP;
    private boolean isFaceRight = true, isAttacking = false, isInvincible = false;
    private double attackTimer = 0, cooldownTimer = COOLDOWN_DURATION, invincibilityTimer = 0;
    // x and y coordinates refers to the top left corner
    private double xPos, yPos, prevX, prevY;


    public PlayableCharacter (double startingX, double startingY, Image referenceImage) {
        // Dimensions taken from the pixel size of leftImage which is assumed to be same as rightImage
        super(startingX, startingY, referenceImage.getWidth(), referenceImage.getHeight());
        this.xPos = startingX;
        this.yPos = startingY;
        prevX = startingX;
        prevY = startingY;
    }

    public abstract String getName();

    public abstract Image getImage();

    public void updatePlayerState() {

        cooldownTimer += 1.0/ShadowDimension.REFRESH_RATE;

        if (isAttacking) {
            attackTimer += 1.0/ShadowDimension.REFRESH_RATE;
            if (attackTimer >= ATTACK_DURATION) {
                isAttacking = false;
                attackTimer = 0;
                cooldownTimer = 0;
            }
        }

    }

    /**
     * Method takes user input and correspondingly moves the character
     */
    public void controlCharacter(Input input) {

        updatePlayerState();

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

    public int getHP() {
        return healthPoints;
    }

    /**
     * Method returns the character's current health point percentage
     * Note that using a MAX_HP of 100, HPPercent() is the same as getHP().
     */
    public int getHPPercent() {
        return (int) Math.round(healthPoints*100.0/MAX_HP);
    }

    public static int getMaxHP() {
        return MAX_HP;
    }

    public static int getMinHP() {
        return MIN_HP;
    }

    /**
     * Method resets the player HP to max
     */
    public void resetHP() {
        healthPoints = MAX_HP;
    }

    public void setPosition(double xPos, double yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    /**
     * Method lowers the character's health points according to "damage"
     */
    public void takesDamage(String attacker, int damage) {
        // Minus damage from health, unless that would make health less than MIN_HP, in that case set health to MIN_HP
        healthPoints = Math.max(healthPoints - damage, MIN_HP);
        System.out.format("%s inflicts %d damage points on %s. %s's current health: %d/%d\n",
                attacker, damage, getName(), getName(), healthPoints, MAX_HP);
    }

    public void dealsDamage(Enemy target) {
        target.takesDamage(getName(), DAMAGE);
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public boolean isFaceRight() {
        return isFaceRight;
    }

}
