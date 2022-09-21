import bagel.*;
import bagel.util.Point;
import bagel.util.Colour;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * SWEN20003 Project 1, Semester 2, 2022
 * Vincent Luu, 1269979
 */

public class ShadowDimension extends AbstractGame {

    /* Display Constants */
    public final static int REFRESH_RATE = 144;
    private final static int WINDOW_WIDTH = 1024, WINDOW_HEIGHT = 768;

    private final Image LVL0_BACKGROUND = new Image("res/background0.png");
    private final Image LVL1_BACKGROUND = new Image("res/background1.png");

    /* Text and font Constants */
    private final String FONT_FILENAME = "res/frostbite.ttf";
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

    private final static int PLAYER_HP_FONT_SIZE = 30;
    private final Font PLAYER_HP_TEXT = new Font(FONT_FILENAME, PLAYER_HP_FONT_SIZE);
    private final static double PLAYER_HP_TEXT_X = 20, PLAYER_HP_TEXT_Y = 25;
    private final static int ENEMY_HP_FONT_SIZE = 15;
    private final Font ENEMY_HP_TEXT = new Font(FONT_FILENAME, ENEMY_HP_FONT_SIZE);
    private final static double ENEMY_HP_TEXT_Y_OFFSET = -6;
    private final static Colour GREEN_HP = new Colour(0, 0.8, 0.2);
    private final static double GREEN_HP_THRESHOLD = 65;
    private final static Colour ORANGE_HP = new Colour(0.9, 0.6, 0);
    private final static double ORANGE_HP_THRESHOLD = 35;
    private final static Colour RED_HP = new Colour(1, 0, 0);

    /* CSV Constants */
    private final static String DATA_FILEPATH = "res/level";
    private final static int DATA_NAME_COL = 0, DATA_X_COL = 1, DATA_Y_COL = 2;

    /* Game element Constants */
    private final static int GAME_START = 0, GAME_PLAY = 1, FIN_LVL0 = 2, START_LVL1 = 3;
    private final static int GAME_WIN = -1, GAME_LOSE = -2;
    private final static double PORTAL_MIN_X = 950, PORTAL_MIN_Y = 670;

    /* Game Object Constants */
    private final static ArrayList<Enemy> enemies = new ArrayList<>();
    private final static int SPEED_INCREASE = 1;
    private final static int SPEED_DECREASE = -1;
    private final static ArrayList<Obstacle> obstacles = new ArrayList<>();
    private final Image WALL_IMAGE = new Image("res/wall.png");
    private final Image TREE_IMAGE = new Image("res/tree.png");

    /* Attributes */
    private int gameState = GAME_START;
    private int currentLevel = 0;
    private double lvlCompleteTimer = 0;
    private PlayableCharacter player;
    private Point topLeftCorner, bottomRightCorner;

    public ShadowDimension() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    /**
     * Method takes the data from the CSV file and uses it to populate level with the character,
     * boundaries and obstacles
     */
    private void initializeLevel() {

        ArrayList<String[]> csvData = readCSV();

        enemies.clear();
        obstacles.clear();
        player = new FaeCharacter(Double.parseDouble(csvData.get(0)[DATA_X_COL]),
                Double.parseDouble(csvData.get(0)[DATA_Y_COL]));
//        if (currentLevel == 0) {
//            // Initialize playerFae using the first row of the CSV data as per assignment specifications
//            player = new PlayableCharacter(CHARACTER_NAME, Double.parseDouble(csvData.get(0)[DATA_X_COL]),
//                    Double.parseDouble(csvData.get(0)[DATA_Y_COL]), CHARACTER_FACE_LEFT, CHARACTER_FACE_RIGHT);
//        } else {
//            enemies.clear();
//            obstacles.clear();
//            player.resetHP();
//            player.setPosition(Double.parseDouble(csvData.get(0)[DATA_X_COL]),
//                    Double.parseDouble(csvData.get(0)[DATA_Y_COL]));
//        }

        // Set the boundaries using the last two rows of the CSV data as per assignment specifications
        bottomRightCorner = new Point(Double.parseDouble(csvData.get(csvData.size()-1)[DATA_X_COL]),
                Double.parseDouble(csvData.get(csvData.size()-1)[DATA_Y_COL]));
        topLeftCorner = new Point(Double.parseDouble(csvData.get(csvData.size()-2)[DATA_X_COL]),
                Double.parseDouble(csvData.get(csvData.size()-2)[DATA_Y_COL]));

        // Populate obstacles & enemies using the rest of the rows of the CSV data
        for (String[] row : csvData) {
            switch (row[DATA_NAME_COL]) {
                case "Sinkhole":
                    enemies.add(new Sinkhole(Double.parseDouble(row[DATA_X_COL]),
                            Double.parseDouble(row[DATA_Y_COL])));
                    break;
                case "Demon":
                    enemies.add(new Demon(Double.parseDouble(row[DATA_X_COL]),
                            Double.parseDouble(row[DATA_Y_COL])));
                    break;
                case "Navec":
                    enemies.add(new Navec(Double.parseDouble(row[DATA_X_COL]),
                            Double.parseDouble(row[DATA_Y_COL])));
                    break;
                case "Wall":
                    obstacles.add(new Obstacle(Double.parseDouble(row[DATA_X_COL]),
                            Double.parseDouble(row[DATA_Y_COL]), WALL_IMAGE));
                    break;
                case "Tree":
                    obstacles.add(new Obstacle(Double.parseDouble(row[DATA_X_COL]),
                            Double.parseDouble(row[DATA_Y_COL]), TREE_IMAGE));
                    break;
            }
        }

    }

    /**
     * Method reads the CSV file and returns a string array of CSV data
     */
    private ArrayList<String[]> readCSV() {

        String row;
        ArrayList<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILEPATH + currentLevel + ".csv"))) {
            while ((row = br.readLine()) != null) {
                data.add(row.split(","));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;

    }

    /**
     * Method displays screens other than the main gameplay screen
     * i.e. Title screen and win/lose screens
    */
    private void displayScreen() {

        if (gameState == GAME_START) {

            // Get offset required to centre the "controls" instructions relative to the "start" instructions
            double ctrlsXOffset = INSTRUCTIONS_TEXT.getWidth(START_INSTRUCTION) / 2
                    - INSTRUCTIONS_TEXT.getWidth(MOVE_CTRLS_INSTRUCT) / 2;

            STANDARD_TEXT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            INSTRUCTIONS_TEXT.drawString(START_INSTRUCTION, TITLE_X + LVL0_START_X_OFFSET,
                    TITLE_Y + LVL0_START_Y_OFFSET);
            INSTRUCTIONS_TEXT.drawString(MOVE_CTRLS_INSTRUCT,
                    TITLE_X + LVL0_START_X_OFFSET + ctrlsXOffset, TITLE_Y + LVL0_START_Y_OFFSET + LINE_SPACING);

        } else if (gameState == FIN_LVL0) {

            lvlCompleteTimer += 1.0/REFRESH_RATE;
            double xPos = WINDOW_WIDTH/2.0 - STANDARD_TEXT.getWidth(LVL_COMPLETE)/2.0;
            double yPos = WINDOW_HEIGHT/2.0 + STANDARD_FONT_SIZE/2.0;
            STANDARD_TEXT.drawString(LVL_COMPLETE, xPos, yPos);

        } else if (gameState == START_LVL1) {

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

            double xPos = WINDOW_WIDTH/2.0 - STANDARD_TEXT.getWidth(CONGRATS)/2.0;
            double yPos = WINDOW_HEIGHT/2.0 + STANDARD_FONT_SIZE/2.0;
            STANDARD_TEXT.drawString(CONGRATS, xPos, yPos);

        } else if (gameState == GAME_LOSE) {

            double xPos = WINDOW_WIDTH/2.0 - STANDARD_TEXT.getWidth(GAME_OVER)/2;
            double yPos = WINDOW_HEIGHT/2.0 + STANDARD_FONT_SIZE/2.0;
            STANDARD_TEXT.drawString(GAME_OVER, xPos, yPos);

        }

    }

    /**
     * Method displays the main gameplay screen
     */
    private void displayGame() {

        // Display the background
        if (currentLevel == 0) {
            LVL0_BACKGROUND.draw(WINDOW_WIDTH/2.0, WINDOW_HEIGHT/2.0);
        } else if (currentLevel == 1) {
            LVL1_BACKGROUND.draw(WINDOW_WIDTH/2.0, WINDOW_HEIGHT/2.0);
        }

        // Display the obstacles
        for (Obstacle anObstacle : obstacles) {
            anObstacle.getImage().draw(anObstacle.centre().x, anObstacle.centre().y);
        }

        // Display the enemies
        for (Enemy anEnemy : enemies) {
            anEnemy.getImage().draw(anEnemy.centre().x, anEnemy.centre().y);
            if (anEnemy.getHPPercent() >= 0) {
                ENEMY_HP_TEXT.drawString(String.format("%d%%", anEnemy.getHPPercent()), anEnemy.topLeft().x,
                        anEnemy.topLeft().y + ENEMY_HP_TEXT_Y_OFFSET,
                        new DrawOptions().setBlendColour(getHPColour(anEnemy.getHPPercent())));
            }
        }

        // Display the player
        player.getImage().draw(player.centre().x, player.centre().y);
        PLAYER_HP_TEXT.drawString(String.format("%d%%", player.getHPPercent()), PLAYER_HP_TEXT_X, PLAYER_HP_TEXT_Y,
                new DrawOptions().setBlendColour(getHPColour(player.getHPPercent())));

    }

    private Colour getHPColour(int HPPercent) {
        Colour HP_colour;
        if (HPPercent >= GREEN_HP_THRESHOLD) {
            HP_colour = GREEN_HP;
        } else if (HPPercent >= ORANGE_HP_THRESHOLD) {
            HP_colour = ORANGE_HP;
        } else {
            HP_colour = RED_HP;
        }
        return HP_colour;
    }

    private void updateEnemyStates() {

        for (Enemy anEnemy : enemies) {

            anEnemy.updateState();

            if (anEnemy.getType().equals("Navec") && anEnemy.isExhausted()) {
                gameState = GAME_WIN;
                return;
            }

            if (player.isAttacking() && player.intersects(anEnemy)) {
                player.dealsDamage(anEnemy);
            }

            // Check if enemy has collided with the bounds
            if (bottomRightCorner.x < anEnemy.topLeft().x || anEnemy.topLeft().x < topLeftCorner.x
                    || bottomRightCorner.y < anEnemy.topLeft().y || anEnemy.topLeft().y < topLeftCorner.y) {
                anEnemy.reverseMovement();
            }

            // Check if player has collided with a sinkhole
            if (anEnemy.getType().equals("Sinkhole") && anEnemy.intersects(player)) {
                anEnemy.dealsDamage(player);
                player.xPosRollback();
                player.yPosRollback();
            }

        }

        enemies.removeIf(Enemy::isExhausted);

    }

    /**
     * Method detects if the character or an enemy has collided
     */
    private void detectCollisions() {

        // Check if player has collided with the boundaries
        // Separate 'if' statements so player can "slide" along boundaries, but can't "escape" at corners
        if (bottomRightCorner.x < player.topLeft().x || player.topLeft().x < topLeftCorner.x) {
            player.xPosRollback();
        }
        if (bottomRightCorner.y < player.topLeft().y || player.topLeft().y < topLeftCorner.y) {
            player.yPosRollback();
        }

        // Check if player or an enemy has collided with an obstacle
        for (Obstacle anObstacle : obstacles) {

            if (anObstacle.intersects(player)) {
                player.xPosRollback();
                player.yPosRollback();
            }

            for (Enemy anEnemy: enemies) {
                if (anObstacle.intersects(anEnemy)) {
                    anEnemy.reverseMovement();
                }
            }

        }

    }

    /**
     * Method checks what state the game should be in
     */
    private void updateGameState(Input input) {

        if (gameState == GAME_PLAY && currentLevel == 1 && input.wasPressed(Keys.L)) {
            Demon.changeSpeedMultiplier(SPEED_INCREASE);
        } else if (gameState == GAME_PLAY && currentLevel == 1 && input.wasPressed(Keys.K)) {
            Demon.changeSpeedMultiplier(SPEED_DECREASE);
        }

        if (gameState == GAME_START && input.wasPressed(Keys.W)) {
            currentLevel = 1;
            initializeLevel();
            gameState = GAME_PLAY;
        } else if ((gameState == GAME_START || gameState == START_LVL1) && input.wasPressed(Keys.SPACE)) {
            initializeLevel();
            gameState = GAME_PLAY;
        // Level 0 win condition: Player position is in the portal
        } else if (gameState == GAME_PLAY && currentLevel == 0
                && player.centre().x >= PORTAL_MIN_X && player.centre().y >= PORTAL_MIN_Y) {
            gameState = FIN_LVL0;
        } else if (gameState == FIN_LVL0 && currentLevel == 0 && lvlCompleteTimer >= LVL_COMPLETE_DISPLAY_TIME) {
            currentLevel = 1;
            gameState = START_LVL1;
        // Lose condition: Player HP reaches its minimum
        } else if (gameState == GAME_PLAY && player.getHP() == PlayableCharacter.getMinHP()) {
            gameState = GAME_LOSE;
        }

    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        updateGameState(input);

        if (gameState == GAME_PLAY) {
            player.controlCharacter(input);
            detectCollisions();
            updateEnemyStates();
            displayGame();
        } else {
            displayScreen();
        }

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

    }
}
