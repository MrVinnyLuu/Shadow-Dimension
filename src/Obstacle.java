import bagel.Image;
import bagel.util.Rectangle;

public abstract class Obstacle extends Rectangle {

    private final Image displayImage;

    public Obstacle (double xPos, double yPos, Image displayImage) {
        // Assume that the width and height of an obstacle is the same as its image dimensions
        super(xPos, yPos, displayImage.getWidth(), displayImage.getHeight());
        this.displayImage = displayImage;
    }

    public Image getImage() {
        return displayImage;
    }

}
