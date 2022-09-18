import bagel.Image;

public class Navec extends Demon {

    private final static int NAVEC_MAX_HP = 2*DEMON_MAX_HP;
    private final static int NAVEC_MIN_HP = 0;
    private final static int NAVEC_DAMAGE = 2*DEMON_DAMAGE;
    private final static double NAVEC_ATTACK_RADIUS = 200;

    private final static Image NAVEC_FACE_LEFT = new Image("res/navec/navecLeft.png");
    private final static Image NAVEC_FACE_RIGHT = new Image("res/navec/navecRight.png");
    private final static Image NAVEC_INVINCIBLE_LEFT = new Image("res/navec/navecInvincibleLeft.png");
    private final static Image NAVEC_INVINCIBLE_RIGHT = new Image("res/navec/navecInvincibleRight.png");
    private final static Image NAVEC_FIRE = new Image("res/navec/navecFire.png");

    public Navec(double xPos, double yPos) {
        super(xPos, yPos, NAVEC_FACE_LEFT, NAVEC_DAMAGE);
    }

    @Override
    public Image getImage() {
        return (super.isFaceRight) ? NAVEC_FACE_RIGHT : NAVEC_FACE_LEFT;
    }

}
