package ShadowDimension;

import Enemies.Enemy;
import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.util.Point;

/**
 * This class encapsulates level information for a game
 * @author Vincent Luu, 1269979
 */

public class Level {

    private final static int LAST_LEVEL = 1;
    private final static double LVL_COMPLETE_DISPLAY_TIME = 3;

    private final static int TIMESCALE_INCREASE = 1;
    private final static int TIMESCALE_DECREASE = -1;
    private final static double MAX_POS_TIMESCALE = 3;
    private final static double MAX_NEG_TIMESCALE = -3;

    private Point topLeftCorner, bottomRightCorner;
    private int currentLevel = -1;
    private int timescale = 1;
    private Image levelBackground;
    private double levelCompleteTimer = 0;

    public void setTopLeftCorner(Point topLeftCorner) {
        this.topLeftCorner = topLeftCorner;
    }

    public void setBottomRightCorner(Point bottomRightCorner) {
        this.bottomRightCorner = bottomRightCorner;
    }

    /**
     * Determines if x is outside the horizontal level bounds
     */
    public boolean xOutOfBounds(double x) {
        return (bottomRightCorner.x < x || x < topLeftCorner.x);
    }

    /**
     * Determines if y is outside the vertical level bounds
     */
    public boolean yOutOfBounds(double y) {
        return (bottomRightCorner.y < y || y < topLeftCorner.y);
    }

    private void initializeBackground() {
        levelBackground = new Image("res/background" + currentLevel + ".png");
    }

    public Image getLevelBackground() {
        return levelBackground;
    }

    /**
     * Method causes the game to skip to the last level
     */
    public void skipToEnd() {
        currentLevel = LAST_LEVEL;
        initializeBackground();
    }

    /**
     * Method increments to the next level
     */
    public void nextLevel() {
        if (currentLevel != LAST_LEVEL) {
            currentLevel++;
            initializeBackground();
        }
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    /**
     * Method allows player input to change the timescale in level 1
     */
    public void timescaleControl(Input input) {

        // Note: Timescale control is in Level rather than in PlayableCharacter or Enemy as I think it
        // makes more sense here as it changes the game behaviour. I don't see it as a player control.
        if (currentLevel == 1 && input.wasPressed(Keys.L) && timescale < MAX_POS_TIMESCALE) {

            timescale += TIMESCALE_INCREASE;
            System.out.println("Sped up, Speed: " + timescale);
            // Currently, only Enemy classes are affected (notified) but this could be extended
            // using Observer Pattern if needed
            Enemy.setTimescale(timescale);

        } else if (currentLevel == 1 && input.wasPressed(Keys.K) && timescale > MAX_NEG_TIMESCALE) {

            timescale += TIMESCALE_DECREASE;
            System.out.println("Slowed down, Speed: " + timescale);
            Enemy.setTimescale(timescale);

        }

    }

    /**
     * Method keeps track of how long the "Level Complete!" screen should be shown
     * @return Returns true when the timer is done
     */
    public boolean levelCompleteTimer() {

        if (levelCompleteTimer >= LVL_COMPLETE_DISPLAY_TIME) {
            levelCompleteTimer = 0;
            return true;
        } else {
            levelCompleteTimer += 1.0/ShadowDimension.REFRESH_RATE;
            return false;
        }

    }

}
