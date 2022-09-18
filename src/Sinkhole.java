import bagel.Image;

public class Sinkhole extends Enemy {

    private final static Image SINKHOLE_IMAGE = new Image("res/sinkhole.png");
    private final static int DAMAGE = 30;

    public Sinkhole (double xPos, double yPos) {
        super("Sinkhole", xPos, yPos, SINKHOLE_IMAGE, DAMAGE);
    }

    @Override
    public Image getImage() {
        return SINKHOLE_IMAGE;
    }

    @Override
    public void updateState() {
        return;
    }

    @Override
    public void reverseMovement() {
        return;
    }

}
