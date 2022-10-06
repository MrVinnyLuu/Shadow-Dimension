package ShadowDimension;

import bagel.Font;
import bagel.Image;
import bagel.Input;
import bagel.Keys;

public class Level {

    private final static int GAME_SCREEN = 0, GAME_PLAY = 1, GAME_WIN = 2, GAME_LOSE = 3;
    private final static int LAST_LEVEL = 1;

    /* Text and font Constants */
    private final static String FONT_FILENAME = "res/frostbite.ttf";
    private final static int STANDARD_FONT_SIZE = 75;
    private final Font STANDARD_TEXT = new Font(FONT_FILENAME, STANDARD_FONT_SIZE);

    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static double TITLE_X = 260, TITLE_Y = 250;

    private final static int INSTRUCT_FONT_SIZE = 40;
    private final Font INSTRUCTIONS_TEXT = new Font(FONT_FILENAME, INSTRUCT_FONT_SIZE);
    private final static double LINE_SPACING = 45; // Drawn 45 pixels below appears to provide adequate spacing

    private final static String START_INSTRUCTION = "PRESS SPACE TO START";
    private final static double LVL0_START_X_OFFSET = 90, LVL0_START_Y_OFFSET = 190;
    private final static double LVL1_START_X = 350, LVL1_START_Y = 350;

    private final static String MOVE_CTRLS_INSTRUCT = "USE ARROW KEYS TO FIND GATE";
    private final static String ATTACK_CTRL_INSTRUCT = "PRESS A TO ATTACK";
    private final static String NAVEC_INSTRUCT = "DEFEAT NAVEC TO WIN";

    private final static String LVL_COMPLETE = "LEVEL COMPLETE!";
    private final static double LVL_COMPLETE_DISPLAY_TIME = 3;
    private final static String CONGRATS = "CONGRATULATIONS!";
    private final static String GAME_OVER = "GAME OVER!";

    private int currentLevel = -1;
    private Image levelBackground;
    private double lvlCompleteTimer = 0;

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

    /**
     * Method displays screens other than the main gameplay screen
     * i.e. Title screen and win/lose screens
     */
    public void displayScreen(int gameState) {

        if (gameState == GAME_SCREEN && currentLevel == -1) {

            // Get offset required to centre the "controls" instructions relative to the "start" instructions
            double ctrlsXOffset = INSTRUCTIONS_TEXT.getWidth(START_INSTRUCTION) / 2
                    - INSTRUCTIONS_TEXT.getWidth(MOVE_CTRLS_INSTRUCT) / 2;

            STANDARD_TEXT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            INSTRUCTIONS_TEXT.drawString(START_INSTRUCTION, TITLE_X + LVL0_START_X_OFFSET,
                    TITLE_Y + LVL0_START_Y_OFFSET);
            INSTRUCTIONS_TEXT.drawString(MOVE_CTRLS_INSTRUCT,
                    TITLE_X + LVL0_START_X_OFFSET + ctrlsXOffset, TITLE_Y + LVL0_START_Y_OFFSET + LINE_SPACING);

        } else if (gameState == GAME_SCREEN && lvlCompleteTimer <= LVL_COMPLETE_DISPLAY_TIME) {

            lvlCompleteTimer += 1.0/ShadowDimension.REFRESH_RATE;
            double xPos = ShadowDimension.WINDOW_WIDTH/2.0 - STANDARD_TEXT.getWidth(LVL_COMPLETE)/2.0;
            double yPos = ShadowDimension.WINDOW_HEIGHT/2.0 + STANDARD_FONT_SIZE/2.0;
            STANDARD_TEXT.drawString(LVL_COMPLETE, xPos, yPos);

        } else if (gameState == GAME_SCREEN) {

            INSTRUCTIONS_TEXT.drawString(START_INSTRUCTION, LVL1_START_X, LVL1_START_Y);

            double attackCtrlXOffset = INSTRUCTIONS_TEXT.getWidth(START_INSTRUCTION) / 2
                    - INSTRUCTIONS_TEXT.getWidth(ATTACK_CTRL_INSTRUCT) / 2;
            INSTRUCTIONS_TEXT.drawString(ATTACK_CTRL_INSTRUCT,
                    LVL1_START_X + attackCtrlXOffset, LVL1_START_Y + LINE_SPACING);

            double navecInstructXOffset = INSTRUCTIONS_TEXT.getWidth(START_INSTRUCTION) / 2
                    - INSTRUCTIONS_TEXT.getWidth(NAVEC_INSTRUCT) / 2;
            INSTRUCTIONS_TEXT.drawString(NAVEC_INSTRUCT,
                    LVL1_START_X + navecInstructXOffset, LVL1_START_Y + 2*LINE_SPACING);

        } else if (gameState == GAME_WIN) {

            double xPos = ShadowDimension.WINDOW_WIDTH/2.0 - STANDARD_TEXT.getWidth(CONGRATS)/2.0;
            double yPos = ShadowDimension.WINDOW_HEIGHT/2.0 + STANDARD_FONT_SIZE/2.0;
            STANDARD_TEXT.drawString(CONGRATS, xPos, yPos);

        } else if (gameState == GAME_LOSE) {

            double xPos = ShadowDimension.WINDOW_WIDTH/2.0 - STANDARD_TEXT.getWidth(GAME_OVER)/2;
            double yPos = ShadowDimension.WINDOW_HEIGHT/2.0 + STANDARD_FONT_SIZE/2.0;
            STANDARD_TEXT.drawString(GAME_OVER, xPos, yPos);

        }

    }
}
