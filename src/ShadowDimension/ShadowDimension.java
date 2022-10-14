package ShadowDimension;

import Characters.*;
import Enemies.*;
import Obstacles.*;

import bagel.*;
import bagel.util.Point;

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * // SWEN20003 Project 2, Semester 2, 2022 //
 * This class contains the main method and game specific attributes and methods
 * @author Vincent Luu, 1269979
 */

public class ShadowDimension extends AbstractGame {

    /* Display Constants */
    /**
     * The refresh rate of the monitor.
     * Note: Code only adheres to project specifications using a refresh rate of 60HZ
     */
    public final static int REFRESH_RATE = 144;
    private final static int WINDOW_WIDTH = 1024, WINDOW_HEIGHT = 768;

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

    private final static String CONGRATS = "CONGRATULATIONS!";
    private final static String GAME_OVER = "GAME OVER!";

    private final static int PLAYER_HP_FONT_SIZE = 30;
    private final Font PLAYER_HP_TEXT = new Font(FONT_FILENAME, PLAYER_HP_FONT_SIZE);

    private final static int ENEMY_HP_FONT_SIZE = 15;
    private final Font ENEMY_HP_TEXT = new Font(FONT_FILENAME, ENEMY_HP_FONT_SIZE);

    /* CSV Constants */
    private final static String DATA_FILEPATH = "res/level";
    private final static String DATA_FILETYPE = ".csv";
    private final static int DATA_NAME_COL = 0, DATA_X_COL = 1, DATA_Y_COL = 2;

    /* Game element Constants */
    private final static Level level = new Level();
    private final static int GAME_START = 0, GAME_PLAY = 1, LVL_FINISH = 2, LVL1_START = 3;
    private final static int GAME_WIN = -1, GAME_LOSE = -2;
    private final static double PORTAL_MIN_X = 950, PORTAL_MIN_Y = 670;

    /* Game Object Constants */
    private final static ArrayList<Enemy> enemies = new ArrayList<>();
    private final static int NAVEC_INDEX = 0;
    private final static ArrayList<Obstacle> obstacles = new ArrayList<>();
    private final static ArrayList<canAttack> attackers = new ArrayList<>();

    /* Attributes */
    private int gameState = GAME_START;
    private PlayableCharacter player;

    /**
     * Default constructor method for ShadowDimension
     */
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
     * Method takes the data from the CSV file and uses it to initialize the character,
     * boundaries and obstacles
     */
    private void initializeGameObjects() {

        ArrayList<String[]> csvData = readCSV();

        // Reset arraylists
        enemies.clear();
        obstacles.clear();

        // Set up the PlayableCharacter instance using the first row of the CSV data
        player = CharacterFae.getInstance(Double.parseDouble(csvData.get(0)[DATA_X_COL]),
                Double.parseDouble(csvData.get(0)[DATA_Y_COL]));

        // Set the boundaries using the last two rows of the CSV data as per assignment specifications
        level.setBottomRightCorner(new Point(Double.parseDouble(csvData.get(csvData.size()-1)[DATA_X_COL]),
                Double.parseDouble(csvData.get(csvData.size()-1)[DATA_Y_COL])));
        level.setTopLeftCorner(new Point(Double.parseDouble(csvData.get(csvData.size()-2)[DATA_X_COL]),
                Double.parseDouble(csvData.get(csvData.size()-2)[DATA_Y_COL])));

        // Populate obstacles & enemies using the rest of the rows of the CSV data
        for (String[] row : csvData) {
            switch (row[DATA_NAME_COL]) {
                case "Demon":
                    Demon tempDemon
                            = new Demon(Double.parseDouble(row[DATA_X_COL]), Double.parseDouble(row[DATA_Y_COL]));
                    enemies.add(tempDemon);
                    attackers.add(tempDemon);
                    break;
                case "Navec":
                    Navec tempNavec
                            = new Navec(Double.parseDouble(row[DATA_X_COL]), Double.parseDouble(row[DATA_Y_COL]));
                    enemies.add(NAVEC_INDEX, tempNavec);
                    attackers.add(tempNavec);
                    break;
                case "Sinkhole":
                    Sinkhole tempSinkhole =
                            new Sinkhole(Double.parseDouble(row[DATA_X_COL]), Double.parseDouble(row[DATA_Y_COL]));
                    obstacles.add(tempSinkhole);
                    attackers.add(tempSinkhole);
                    break;
                case "Wall":
                    obstacles.add(new Wall(Double.parseDouble(row[DATA_X_COL]), Double.parseDouble(row[DATA_Y_COL])));
                    break;
                case "Tree":
                    obstacles.add(new Tree(Double.parseDouble(row[DATA_X_COL]), Double.parseDouble(row[DATA_Y_COL])));
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

        try (BufferedReader br = new BufferedReader(
                new FileReader(DATA_FILEPATH + level.getCurrentLevel() + DATA_FILETYPE))) {

            while ((row = br.readLine()) != null) {
                data.add(row.split(","));
            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return data;

    }

    /**
     * Method loops through each enemy and updates its state
     */
    private void updateGameObjects() {

        // Check for and execute valid attacks
        for (canAttack attacker : attackers) {
            if (attacker.inRange(player)) {
                attacker.attack(player);
            }
        }

        // Update the state of each enemy
        for (Enemy anEnemy : enemies) {

            anEnemy.updateState();

            // Check if enemy has collided with the bounds
            if (level.xOutOfBounds(anEnemy.topLeft().x) || level.yOutOfBounds(anEnemy.topLeft().y)) {
                anEnemy.reverseMovement();
            }

            if (player.isAttacking() && player.intersects(anEnemy)) {
                player.dealsDamage(anEnemy);
            }

        }

        // Check if Navec is dead before removing
        if (level.getCurrentLevel() == 1 && enemies.get(NAVEC_INDEX).isDead()) return;

        // Remove dead enemies
        enemies.removeIf(Enemy::isDead);

    }

    /**
     * Method detects if the character or an enemy has collided
     */
    private void detectCollisions() {

        // Check if player has collided with the boundaries
        // Separate 'if' statements so player can "slide" along boundaries, but can't "escape" at corners
        if (level.xOutOfBounds(player.topLeft().x)) {
            player.xPosRollback();
        }
        if (level.yOutOfBounds(player.topLeft().y)) {
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

        } else if (gameState == LVL_FINISH) {

            level.levelCompleteTimer();
            double xPos = WINDOW_WIDTH/2.0 - STANDARD_TEXT.getWidth(LVL_COMPLETE)/2.0;
            double yPos = WINDOW_HEIGHT/2.0 + STANDARD_FONT_SIZE/2.0;
            STANDARD_TEXT.drawString(LVL_COMPLETE, xPos, yPos);

        } else if (gameState == LVL1_START) {

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
        level.getLevelBackground().draw(WINDOW_WIDTH/2.0, WINDOW_HEIGHT/2.0);

        // Display the obstacles
        for (Obstacle anObstacle : obstacles) {
            if (anObstacle.getImage() != null) {
                anObstacle.getImage().draw(anObstacle.centre().x, anObstacle.centre().y);
            }
        }

        // Display the enemies
        for (Enemy anEnemy : enemies) {
            anEnemy.getImage().draw(anEnemy.centre().x, anEnemy.centre().y);
            anEnemy.displayHP(ENEMY_HP_TEXT);
        }

        // Display the player
        player.getImage().draw(player.centre().x, player.centre().y);
        player.displayHP(PLAYER_HP_TEXT);

    }

    /**
     * Method checks what state the game should be in
     */
    private void updateGameState(Input input) {

        if (gameState == GAME_PLAY) level.timescaleControl(input);

        if (gameState == GAME_START && input.wasPressed(Keys.W)) {

            level.skipToEnd();
            initializeGameObjects();
            gameState = GAME_PLAY;

        } else if ((gameState == GAME_START || gameState == LVL1_START) && input.wasPressed(Keys.SPACE)) {

            level.nextLevel();
            initializeGameObjects();
            gameState = GAME_PLAY;

        } else if (gameState == LVL_FINISH && level.levelCompleteTimer()) {

            level.nextLevel();
            gameState = LVL1_START;

        // Level 0 win condition: Player position is in the portal
        } else if (gameState == GAME_PLAY && level.getCurrentLevel() == 0
                && player.centre().x >= PORTAL_MIN_X && player.centre().y >= PORTAL_MIN_Y) {

            gameState = LVL_FINISH;
            level.nextLevel();

        // Level 1 win condition: Navec is exhausted (i.e. has been killed)
        } else if (gameState == GAME_PLAY && level.getCurrentLevel() == 1 && enemies.get(NAVEC_INDEX).isDead()) {

            gameState = GAME_WIN;

        // Lose condition: Player HP reaches its minimum
        } else if (gameState == GAME_PLAY && player.getHealth().isDead()) {

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
            displayGame();
            updateGameObjects();
        } else {
            displayScreen();
        }

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

    }

}
