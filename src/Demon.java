import bagel.Image;

public class Demon extends Enemy{

    private final static int MAX_HP = 40;
    private final static int MIN_HP = 0;
    private final static int DAMAGE = 10;
    private final static double ATTACK_RADIUS = 150;

    private final static Image FACE_LEFT = new Image("res/demonLeft.png");
    private final static Image FACE_RIGHT = new Image("res/demonRight.png");
    private final static Image INVINCIBLE_LEFT = new Image("res/demonInvincibleLeft.png");
    private final static Image INVINCIBLE_RIGHT = new Image("res/demonInvincibleRight.png");
    private final static Image FIRE = new Image("res/demonFire.png");

    public Demon (double xPos, double yPos) {
        super("Demon", xPos, yPos, FACE_LEFT, DAMAGE);
    }

}
