import bagel.Image;

public class Sinkhole extends Enemy {

    private final static Image SINKHOLE_IMAGE = new Image("res/sinkhole.png");
    private final static int DAMAGE = 30;
    private final static double ATTACK_RADIUS = SINKHOLE_IMAGE.getWidth();

    private boolean isExhausted = false;

    public Sinkhole (double xPos, double yPos) {
        super("Sinkhole", xPos, yPos, SINKHOLE_IMAGE);
    }

    @Override
    public double getAttackRadius() {
        return ATTACK_RADIUS;
    }

    @Override
    public void attack(PlayableCharacter player) {
        player.takesDamage("Sinkhole", DAMAGE);
        isExhausted = true;
    }

    @Override
    public Image getImage() {
        return SINKHOLE_IMAGE;
    }

    @Override
    public boolean isExhausted() {
        return isExhausted;
    }

    public void updateState() {
        return;
    }

    public void reverseMovement() {
        return;
    }

    public void takesDamage(String attacker, int damage) {
        return;
    }

}
