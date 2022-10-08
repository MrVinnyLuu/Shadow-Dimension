package ShadowDimension;

import bagel.Image;

/**
 * // SWEN20003 Project 2, Semester 2, 2022 //
 * This class encapsulates level information for a game
 * @author Vincent Luu, 1269979
 */

public class Level {

    private final static int LAST_LEVEL = 1;
    private final static double LVL_COMPLETE_DISPLAY_TIME = 3;

    private int currentLevel = -1;
    private Image levelBackground;
    private double levelCompleteTimer = 0;

    private void initializeBackground() {
        levelBackground = new Image("res/background" + currentLevel + ".png");
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

    public int getCurrentLevel() {
        return currentLevel;
    }

    public Image getLevelBackground() {
        return levelBackground;
    }

}
