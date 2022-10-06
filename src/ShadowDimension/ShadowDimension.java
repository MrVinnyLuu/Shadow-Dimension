package ShadowDimension;

import Characters.*;
import Enemies.*;
import Obstacles.*;

import bagel.*;
import bagel.util.Point;
import bagel.util.Colour;

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * SWEN20003 Project 2, Semester 2, 2022
 * @author Vincent Luu, 1269979
 */

public class ShadowDimension extends AbstractGame {

    /* Display Constants */
    public final static int REFRESH_RATE = 60;
    public final static int WINDOW_WIDTH = 1024, WINDOW_HEIGHT = 768;

    /* Text and font Constants */
    private final static String FONT_FILENAME = "res/frostbite.ttf";

    private final static String GAME_TITLE = "SHADOW DIMENSION";

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
    private final static String DATA_FILETYPE = ".csv";
    private final static int DATA_NAME_COL = 0, DATA_X_COL = 1, DATA_Y_COL = 2;

    /* Game element Constants */
    private final static Level level = new Level();
    private final static int GAME_SCREEN = 0, GAME_PLAY = 1, GAME_WIN = 2, GAME_LOSE = 3;
    private final static double PORTAL_MIN_X = 950, PORTAL_MIN_Y = 670;
    private final static int TIMESCALE_INCREASE = 1;
    private final static int TIMESCALE_DECREASE = -1;

    /* Game Object Constants */
    private final static ArrayList<Enemy> enemies = new ArrayList<>();
    private final static int NAVEC_INDEX = 0;
    private final static ArrayList<Obstacle> obstacles = new ArrayList<>();

    /* Attributes */
    private int gameState = GAME_SCREEN;
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
     * Method takes the data from the CSV file and uses it to initialize the character,
     * boundaries and obstacles
     */
    private void initializeGameObjects() {

        ArrayList<String[]> csvData = readCSV();

        enemies.clear();
        obstacles.clear();

        player = CharacterFae.getInstance(Double.parseDouble(csvData.get(0)[DATA_X_COL]),
                Double.parseDouble(csvData.get(0)[DATA_Y_COL]));

        // Set the boundaries using the last two rows of the CSV data as per assignment specifications
        bottomRightCorner = new Point(Double.parseDouble(csvData.get(csvData.size()-1)[DATA_X_COL]),
                Double.parseDouble(csvData.get(csvData.size()-1)[DATA_Y_COL]));
        topLeftCorner = new Point(Double.parseDouble(csvData.get(csvData.size()-2)[DATA_X_COL]),
                Double.parseDouble(csvData.get(csvData.size()-2)[DATA_Y_COL]));

        // Populate obstacles & enemies using the rest of the rows of the CSV data
        for (String[] row : csvData) {
            switch (row[DATA_NAME_COL]) {
                case "Sinkhole":
                    enemies.add(new Sinkhole(Double.parseDouble(row[DATA_X_COL]), Double.parseDouble(row[DATA_Y_COL])));
                    break;
                case "Demon":
                    enemies.add(new Demon(Double.parseDouble(row[DATA_X_COL]), Double.parseDouble(row[DATA_Y_COL])));
                    break;
                case "Navec":
                    enemies.add(NAVEC_INDEX, new Navec(Double.parseDouble(row[DATA_X_COL]),
                            Double.parseDouble(row[DATA_Y_COL])));
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

        try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILEPATH + level.getCurrentLevel() + DATA_FILETYPE))) {
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
    private void updateEnemyStates() {

        for (Enemy anEnemy : enemies) {

            anEnemy.updateState();

            // Check if enemy has collided with the bounds
            if (bottomRightCorner.x < anEnemy.topLeft().x || anEnemy.topLeft().x < topLeftCorner.x
                    || bottomRightCorner.y < anEnemy.topLeft().y || anEnemy.topLeft().y < topLeftCorner.y) {
                anEnemy.reverseMovement();
            }

            if (player.isAttacking() && player.intersects(anEnemy)) {
                player.dealsDamage(anEnemy);
            }

            if (anEnemy.centre().distanceTo(player.centre()) <= anEnemy.getAttackRadius()) {
                anEnemy.attack(player);
            }

            for (Enemy anotherEnemy: enemies) {
                if (anEnemy.intersects(anotherEnemy)) {
                    anEnemy.collidesWith(anotherEnemy);
                }
            }

        }

        if (level.getCurrentLevel() == 1 && enemies.get(NAVEC_INDEX).isExhausted()) return;

        enemies.removeIf(Enemy::isExhausted);

    }

    /**
     * Method detects if the character or an enemy has collided
     */
    private void detectCollisions() {

        // Check if player has collided with the boundaries
        // Separate 'if' statements so player can "slide" along boundaries, but can't "escape" at corners
        if (bottomRightCorner.x <= player.topLeft().x || player.topLeft().x <= topLeftCorner.x) {
            player.xPosRollback();
        }
        if (bottomRightCorner.y < player.topLeft().y || player.topLeft().y < topLeftCorner.y) {
            player.yPosRollback();
        }

        // Check if player or an enemy has collided with an obstacle
        for (Obstacle anObstacle : obstacles) {

            if (player.intersects(anObstacle)) {
                player.xPosRollback();
                player.yPosRollback();
            }

            for (Enemy anEnemy: enemies) {
                if (anEnemy.intersects(anObstacle)) {
                    anEnemy.reverseMovement();
                }
            }

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

    /**
     * Method computes the display colour of a given health point percentage
     */
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

    /**
     * Method checks what state the game should be in
     */
    private void updateGameState(Input input) {

        if (gameState == GAME_PLAY && level.getCurrentLevel() == 1 && input.wasPressed(Keys.L)) {
            Demon.changeSpeedMultiplier(TIMESCALE_INCREASE);
        } else if (gameState == GAME_PLAY && level.getCurrentLevel() == 1 && input.wasPressed(Keys.K)) {
            Demon.changeSpeedMultiplier(TIMESCALE_DECREASE);
        }

//        if (gameState == GAME_START && input.wasPressed(Keys.W)) {
//            level.skipToEnd();
//            initializeGameObjects();
//            gameState = GAME_PLAY;
//        } else
        if (gameState == GAME_SCREEN && input.wasPressed(Keys.SPACE)) {
            level.nextLevel();
            initializeGameObjects();
            gameState = GAME_PLAY;
        // Level 0 win condition: Player position is in the portal
        } else if (gameState == GAME_PLAY && level.getCurrentLevel() == 0
                && player.centre().x >= PORTAL_MIN_X && player.centre().y >= PORTAL_MIN_Y) {
            gameState = GAME_SCREEN;
            level.nextLevel();
        // Level 1 win condition: Navec is exhausted (i.e. has been killed)
        } else if (gameState == GAME_PLAY && level.getCurrentLevel() == 1 && enemies.get(NAVEC_INDEX).isExhausted()) {
            gameState = GAME_WIN;
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
            displayGame();
            updateEnemyStates();
        } else {
            level.displayScreen(gameState);
        }

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

    }
}
