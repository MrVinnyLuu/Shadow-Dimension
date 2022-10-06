package ShadowDimension;

import bagel.Image;

public class Level {

    private final static int LAST_LEVEL = 1;
    private final static double LVL_COMPLETE_DISPLAY_TIME = 3;

    private int currentLevel = -1;
    private Image levelBackground;
    private double levelCompleteTimer = 0;

    private void initializeBackground() {
        levelBackground = new Image("res/background" + currentLevel + ".png");
    }

    public void skipToEnd() {
        currentLevel = LAST_LEVEL;
        initializeBackground();
    }

    public void nextLevel() {
        if (currentLevel != LAST_LEVEL) {
            currentLevel++;
            initializeBackground();
        }
    }

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
