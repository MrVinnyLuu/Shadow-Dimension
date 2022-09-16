import bagel.Image;
import bagel.util.Rectangle;

public abstract class Obstacle extends Rectangle {

    private static int numObstacles = 0;

    private final Image displayImage;

    public Obstacle (double xPos, double yPos, Image displayImage) {
        // Assume that the width and height of an obstacle is the same as its image dimensions
        super(xPos, yPos, displayImage.getWidth(), displayImage.getHeight());
        incrementNumObst();
        this.displayImage = displayImage;
    }

    public Image getImage() {
        return displayImage;
    }

    public boolean isntExhausted() {
        return true;
    }

    // Note that this method isn't abstract because the "default" behaviour is to do nothing (deal no damage)
    public void dealsDamage(PlayableCharacter player) {
        return;
    }

    /**
     * Method returns the total number of obstacles that have been initialized
     */
    public static int getNum() {
        return numObstacles;
    }

    /**
     * Method increments the number of obstacles that have been initialized
     */
    private static void incrementNumObst() {
        numObstacles++;
    }

}
