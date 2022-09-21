import bagel.DrawOptions;
import bagel.Image;

public class Navec extends Demon {

    private final static int NAVEC_MAX_HP = 2 * MAX_HP;
    private final static int NAVEC_DAMAGE = 2 * DAMAGE;
    private final static double NAVEC_ATTACK_RADIUS = 200;

    private final static Image NAVEC_FACE_LEFT = new Image("res/navec/navecLeft.png");
    private final static Image NAVEC_FACE_RIGHT = new Image("res/navec/navecRight.png");
    private final static Image NAVEC_INVINCIBLE_LEFT = new Image("res/navec/navecInvincibleLeft.png");
    private final static Image NAVEC_INVINCIBLE_RIGHT = new Image("res/navec/navecInvincibleRight.png");
    private final static Image NAVEC_FIRE = new Image("res/navec/navecFire.png");

    public Navec(double xPos, double yPos) {
        super(xPos, yPos, NAVEC_FACE_LEFT, NAVEC_MAX_HP);
    }

    @Override
    public double getAttackRadius() {
        return NAVEC_ATTACK_RADIUS;
    }

    @Override
    public void attack(PlayableCharacter player) {
        if (player.centre().x <= centre().x && player.centre().y <= centre().y) {
            NAVEC_FIRE.drawFromTopLeft(topLeft().x - NAVEC_FIRE.getWidth(), topLeft().y - NAVEC_FIRE.getHeight(),
                    new DrawOptions().setRotation(0));
        } else if (player.centre().x <= centre().x && player.centre().y > centre().y) {
            NAVEC_FIRE.drawFromTopLeft(bottomLeft().x - NAVEC_FIRE.getWidth(), bottomLeft().y,
                    new DrawOptions().setRotation(-Math.PI/2));
        } else if (player.centre().x > centre().x && player.centre().y <= centre().y) {
            NAVEC_FIRE.drawFromTopLeft(topRight().x, topRight().y - NAVEC_FIRE.getHeight(),
                    new DrawOptions().setRotation(Math.PI/2));
        } else if (player.centre().x > centre().x && player.centre().y > centre().y) {
            NAVEC_FIRE.drawFromTopLeft(bottomRight().x, bottomRight().y,
                    new DrawOptions().setRotation(Math.PI));
        }
    }

    @Override
    public Image getImage() {
        if (isFaceRight && isInvincible) {
            return NAVEC_INVINCIBLE_RIGHT;
        } else if (!isFaceRight && isInvincible) {
            return NAVEC_INVINCIBLE_LEFT;
        } else if (isFaceRight) {
            return NAVEC_FACE_RIGHT;
        } else {
            return NAVEC_FACE_LEFT;
        }
    }

    @Override
    public int getMaxHP() {
        return NAVEC_MAX_HP;
    }

}
