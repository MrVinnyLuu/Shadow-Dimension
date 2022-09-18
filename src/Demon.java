import bagel.Image;

public class Demon extends Enemy{

    protected final static int DEMON_MAX_HP = 40;
    private final static int MIN_HP = 0;
    protected final static int DEMON_DAMAGE = 10;
    private final static double ATTACK_RADIUS = 150;

    private final static Image FACE_LEFT = new Image("res/demon/demonLeft.png");
    private final static Image FACE_RIGHT = new Image("res/demon/demonRight.png");
    private final static Image INVINCIBLE_LEFT = new Image("res/demon/demonInvincibleLeft.png");
    private final static Image INVINCIBLE_RIGHT = new Image("res/demon/demonInvincibleRight.png");
    private final static Image FIRE = new Image("res/demon/demonFire.png");

    public Demon (double xPos, double yPos) {
        super("Demon", xPos, yPos, FACE_LEFT, DEMON_DAMAGE);
    }

    public Demon (double xPos, double yPos, Image navecImage, int navecDamge) {
        super("Navec", xPos, yPos, navecImage, navecDamge);
    }

    @Override
    public Image getImage() {
        return FACE_RIGHT;
    }

}
