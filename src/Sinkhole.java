import bagel.Image;

public class Sinkhole extends Obstacle {

    private final static Image SINKHOLE_IMAGE = new Image("res/sinkhole.png");
    private final static int DAMAGE = 30;

    private boolean isExhausted = false;

    public Sinkhole (double xPos, double yPos) {
        super(xPos, yPos, SINKHOLE_IMAGE);
    }

    /**
     * Method inflicts sinkhole's damage on "player" and prints out the required log to the command line
     */
    @Override
    public void dealsDamage(PlayableCharacter player) {
        player.takesDamage(DAMAGE);
        System.out.format("Sinkhole inflicts %d damage points on %s. %s's current health: %d/%d\n",
                DAMAGE, player.getName(), player.getName(), player.getHP(), PlayableCharacter.getMaxHP());
        isExhausted = true;
    }

    /**
     * Method returns whether the sinkhole isn't exhausted i.e. hasn't been fallen into once before
     */
    @Override
    public boolean isntExhausted() {
        return !isExhausted;
    }

}
