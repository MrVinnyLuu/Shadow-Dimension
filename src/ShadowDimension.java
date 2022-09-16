import bagel.*;
import bagel.util.Point;
import bagel.util.Colour;
import java.io.FileReader;
import java.io.BufferedReader;

/**
 * SWEN20003 Project 1, Semester 2, 2022
 * Vincent Luu, 1269979
 */

public class ShadowDimension extends AbstractGame {

    /* Window Constants */
    private final static int WINDOW_WIDTH = 1024, WINDOW_HEIGHT = 768;
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");

    /* Text and font Constants */
    private final String FONT_FILE = "res/frostbite.ttf";
    private final static int STANDARD_FONT_SIZE = 75;
    private final Font STANDARD_TEXT = new Font(FONT_FILE, STANDARD_FONT_SIZE);

    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static double TITLE_X = 260, TITLE_Y = 250;

    private final static int INSTRUCT_FONT_SIZE = 40;
    private final Font INSTRUCTIONS_TEXT = new Font(FONT_FILE, INSTRUCT_FONT_SIZE);
    private final static String START_INSTRUCTION = "PRESS SPACE TO START";
    private final static double START_X_OFFSET = 90, START_Y_OFFSET = 190;
    private final static String CONTROLS_INSTRUCTION = "USE ARROW KEYS TO FIND GATE";
    // Drawn 45 pixels below appears to provide adequate spacing
    private final static double INSTRUCT_LINE_SPACING = 45;

    private final static String CONGRATS = "CONGRATULATIONS!";
    private final static String GAME_OVER = "GAME OVER!";

    private final static int HP_FONT_SIZE = 30;
    private final Font HP_TEXT = new Font(FONT_FILE, HP_FONT_SIZE);
    private final static double HP_TEXT_X = 20, HP_TEXT_Y = 25;
    private final static Colour GREEN_HP = new Colour(0, 0.8, 0.2);
    private final static double GREEN_HP_THRESHOLD = 65;
    private final static Colour ORANGE_HP = new Colour(0.9, 0.6, 0);
    private final static double ORANGE_HP_THRESHOLD = 35;
    private final static Colour RED_HP = new Colour(1, 0, 0);

    /* CSV Constants */
    private final static String DATA_FILE = "res/level0.csv";
    private final static int MAX_DATA_ENTRIES = 60;
    private final static int DATA_COLUMNS = 3;
    private final static int DATA_NAME_COL = 0, DATA_X_COL = 1, DATA_Y_COL = 2;

    /* Game element Constants */
    private final static int GAME_START = 1, GAME_PLAY = 2, GAME_WIN = 3, GAME_LOSE = 4;
    private final static double PORTAL_MIN_X = 950, PORTAL_MIN_Y = 670;
    // Max number of obstacles is max number of data entries minus entries for player, top left and bottom
    private final static Obstacle[] obstacles = new Obstacle[MAX_DATA_ENTRIES-3];

    /* Fae Constants */
    private final static String CHARACTER_NAME = "Fae";
    private final Image FAE_FACE_LEFT = new Image("res/fae/faeLeft.png");
    private final Image FAE_FACE_RIGHT = new Image("res/fae/faeRight.png");

    /* Attributes */
    private int gameState = GAME_START;
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

        String[][] csvData = readCSV();

        // Initialize playerFae using the first row of the CSV data as per assignment specifications
        player = new PlayableCharacter(CHARACTER_NAME, Double.parseDouble(csvData[0][DATA_X_COL]),
                Double.parseDouble(csvData[0][DATA_Y_COL]), FAE_FACE_LEFT, FAE_FACE_RIGHT);

        // Set the boundaries using the last two rows of the CSV data as per assignment specifications
        bottomRightCorner = new Point(Double.parseDouble(csvData[csvData.length-1][DATA_X_COL]),
                Double.parseDouble(csvData[csvData.length-1][DATA_Y_COL]));
        topLeftCorner = new Point(Double.parseDouble(csvData[csvData.length-2][DATA_X_COL]),
                Double.parseDouble(csvData[csvData.length-2][DATA_Y_COL]));

        // Populate obstacles[] with using the rest of the rows of the CSV data
        for (String[] row : csvData) {
            if (row[DATA_NAME_COL].equals("Wall")) {
                obstacles[Obstacle.getNum()] = new Wall(Double.parseDouble(row[DATA_X_COL]),
                        Double.parseDouble(row[DATA_Y_COL]));
            } else if (row[DATA_NAME_COL].equals("Sinkhole")) {
                obstacles[Obstacle.getNum()] = new Sinkhole(Double.parseDouble(row[DATA_X_COL]),
                        Double.parseDouble(row[DATA_Y_COL]));
            }
        }

    }

    /**
     * Method reads the CSV file and returns a string array of CSV data
     */
    private String[][] readCSV() {

        String row;
        String[][] data = new String[MAX_DATA_ENTRIES][DATA_COLUMNS];
        int i = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(DATA_FILE))) {
            while ((row = br.readLine()) != null) {
                data[i] = row.split(",");
                i++;
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
            double ctrlsXOffset = INSTRUCTIONS_TEXT.getWidth(START_INSTRUCTION)/2
                    - INSTRUCTIONS_TEXT.getWidth(CONTROLS_INSTRUCTION)/2;

            STANDARD_TEXT.drawString(GAME_TITLE, TITLE_X, TITLE_Y);
            INSTRUCTIONS_TEXT.drawString(START_INSTRUCTION, TITLE_X + START_X_OFFSET, TITLE_Y + START_Y_OFFSET);
            INSTRUCTIONS_TEXT.drawString(CONTROLS_INSTRUCTION,
                    TITLE_X + START_X_OFFSET + ctrlsXOffset, TITLE_Y + START_Y_OFFSET + INSTRUCT_LINE_SPACING);

        } else if (gameState == GAME_WIN) {

            double congratsXPos = WINDOW_WIDTH/2.0 - STANDARD_TEXT.getWidth(CONGRATS)/2.0;
            double congratsYPos = WINDOW_HEIGHT/2.0 + STANDARD_FONT_SIZE/2.0;
            STANDARD_TEXT.drawString(CONGRATS, congratsXPos, congratsYPos);

        } else if (gameState == GAME_LOSE) {

            double gameOverXPos = WINDOW_WIDTH/2.0 - STANDARD_TEXT.getWidth(GAME_OVER)/2;
            double gameOverYPos = WINDOW_HEIGHT/2.0 + STANDARD_FONT_SIZE/2.0;
            STANDARD_TEXT.drawString(GAME_OVER, gameOverXPos, gameOverYPos);

        }

    }

    /**
     * Method displays the main gameplay screen
     */
    private void displayGame() {

        // Display the background
        BACKGROUND_IMAGE.draw(WINDOW_WIDTH/2.0, WINDOW_HEIGHT/2.0);

        // Display the HP
        Colour HP_colour;
        if (player.getHPPercent() >= GREEN_HP_THRESHOLD) {
            HP_colour = GREEN_HP;
        } else if (player.getHPPercent() >= ORANGE_HP_THRESHOLD) {
            HP_colour = ORANGE_HP;
        } else {
            HP_colour = RED_HP;
        }

        HP_TEXT.drawString(String.format("%d%%", player.getHPPercent()), HP_TEXT_X, HP_TEXT_Y,
                new DrawOptions().setBlendColour(HP_colour));

        // Display the obstacles
        for (int i = 0; i < Obstacle.getNum(); i++) {
            if (obstacles[i].isntExhausted()) {
                obstacles[i].getImage().draw(obstacles[i].centre().x, obstacles[i].centre().y);
            }
        }

        // Display the player
        player.getImage().draw(player.centre().x, player.centre().y);

    }

    /**
     * Method detects if the character had moved out of bounds or inside an obstacle and reverses
     * that movement
     */
    private void detectCollisions() {

        // Check to see if player has collided with the boundaries
        // Separate 'if' statements so player can "slide" along boundaries, but can't "escape" at corners
        if (bottomRightCorner.x < player.topLeft().x || player.topLeft().x < topLeftCorner.x) {
            player.xPosRollback();
        }
        if (bottomRightCorner.y < player.topLeft().y || player.topLeft().y < topLeftCorner.y) {
            player.yPosRollback();
        }

        // Check to see if player has collided with an obstacle
        for (Obstacle anObstacle : obstacles) {
            if (anObstacle.intersects(player) && anObstacle.isntExhausted()) {
                anObstacle.dealsDamage(player);
                player.xPosRollback();
                player.yPosRollback();
            }
        }

    }

    /**
     * Method checks what state the game should be in
     */
    private void checkGameState(Input input) {

        if (gameState == GAME_START && input.wasPressed(Keys.SPACE)) {
            initializeLevel();
            gameState = GAME_PLAY;
        // Lose condition: Player HP reaches its minimum
        } else if (gameState == GAME_PLAY && player.getHP() == PlayableCharacter.getMinHP()) {
            gameState = GAME_LOSE;
        // Win condition: Player position is in the portal
        } else if (gameState == GAME_PLAY && player.centre().x >= PORTAL_MIN_X && player.centre().y >= PORTAL_MIN_Y) {
            gameState = GAME_WIN;
        }

    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        checkGameState(input);

        if (gameState == GAME_PLAY) {
            player.controlCharacter(input);
            detectCollisions();
            displayGame();
        } else {
            displayScreen();
        }

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

    }
}
