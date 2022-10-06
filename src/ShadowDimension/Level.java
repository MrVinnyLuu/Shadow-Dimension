package ShadowDimension;

import bagel.Image;

public class Level {

    private final static int LAST_LEVEL = 1;

    private int currentLevel = -1;
    private Image levelBackground;

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

    public int getCurrentLevel() {
        return currentLevel;
    }

    public Image getLevelBackground() {
        return levelBackground;
    }
}
