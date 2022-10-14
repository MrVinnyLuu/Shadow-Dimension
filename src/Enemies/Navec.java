package Enemies;

import Characters.Health;
import bagel.Image;

/**
 * This class represents Navecs, a type of Demon
 * @author Vincent Luu, 1269979
 */

public class Navec extends Demon {

    private final static String NAVEC_NAME = "Navec";

    private final static int NAVEC_MAX_HP = 2 * MAX_HP;
    private final static int NAVEC_DAMAGE = 2 * DAMAGE;
    private final static double NAVEC_ATTACK_RADIUS = 200;

    private final static Image NAVEC_FACE_LEFT = new Image("res/navec/navecLeft.png");
    private final static Image NAVEC_FACE_RIGHT = new Image("res/navec/navecRight.png");
    private final static Image NAVEC_INVINCIBLE_LEFT = new Image("res/navec/navecInvincibleLeft.png");
    private final static Image NAVEC_INVINCIBLE_RIGHT = new Image("res/navec/navecInvincibleRight.png");
    private final static Image NAVEC_FIRE = new Image("res/navec/navecFire.png");

    /**
     * Creates a Navec (a "special demon")
     */
    public Navec(double xPos, double yPos) {
        super(NAVEC_NAME, xPos, yPos, NAVEC_FACE_LEFT, NAVEC_MAX_HP, MIN_HP);
    }

    @Override
    public double getAttackRadius() {
        return NAVEC_ATTACK_RADIUS;
    }

    @Override
    public Image getAttackImage() {
        return NAVEC_FIRE;
    }

    @Override
    public int getDamage() {
        return NAVEC_DAMAGE;
    }

    @Override
    public Image getImage() {
        if (isFaceRight() && isInvincible()) {
            return NAVEC_INVINCIBLE_RIGHT;
        } else if (!isFaceRight() && isInvincible()) {
            return NAVEC_INVINCIBLE_LEFT;
        } else if (isFaceRight()) {
            return NAVEC_FACE_RIGHT;
        } else {
            return NAVEC_FACE_LEFT;
        }
    }

}
