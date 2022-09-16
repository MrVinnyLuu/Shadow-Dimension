import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;
import bagel.util.Rectangle;

public class PlayableCharacter extends Rectangle {

    // Assume that every playable character has 0 to 100 HP and moves at 2 pixels per frame
    private final static int MAX_HP = 100;
    private final static int MIN_HP = 0;
    private final static double PLAYER_STEP = 2;

    private final String name;
    private final Image leftImage, rightImage;

    // Set initial health to maximum health
    private int healthPoints = MAX_HP;
    private boolean isFaceRight = true;
    // x and y coordinates refers to the top left corner
    private double xPos, yPos, prevX, prevY;

    public PlayableCharacter (String name, double startingX, double startingY, Image leftImage, Image rightImage) {
        // Dimensions taken from the pixel size of leftImage which is assumed to be same as rightImage
        super(startingX, startingY, leftImage.getWidth(), leftImage.getHeight());
        this.xPos = startingX;
        this.yPos = startingY;
        prevX = startingX;
        prevY = startingY;
        this.name = name;
        this.leftImage = leftImage;
        this.rightImage = rightImage;
    }

    public String getName() {
        return name;
    }

    /**
     * Method returns the correct facing image
     */
    public Image getImage() {
        return (isFaceRight) ? rightImage : leftImage;
    }

    /**
     * Method takes user input and correspondingly moves the character
     */
    public void controlCharacter(Input input) {

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
     * Method lowers the character's health points according to "damage"
     */
    public void takesDamage(int damage) {
        // Minus damage from health, unless that would make health less than MIN_HP, in that case set health to MIN_HP
        healthPoints = Math.max(healthPoints - damage, MIN_HP);
    }

}
