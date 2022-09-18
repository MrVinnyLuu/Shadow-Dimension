import bagel.Image;
import bagel.util.Point;

import java.util.Random;

public class Demon extends Enemy {

    protected final static int MAX_HP = 40;
    protected final static int MIN_HP = 0;
    protected final static int DAMAGE = 10;
    private final static double ATTACK_RADIUS = 150;
    private final static double INVINCIBILITY_DURATION = 3;

    private final static Image FACE_LEFT = new Image("res/demon/demonLeft.png");
    private final static Image FACE_RIGHT = new Image("res/demon/demonRight.png");
    private final static Image INVINCIBLE_LEFT = new Image("res/demon/demonInvincibleLeft.png");
    private final static Image INVINCIBLE_RIGHT = new Image("res/demon/demonInvincibleRight.png");
    private final static Image FIRE = new Image("res/demon/demonFire.png");

    private final static double MAX_BASE_SPEED = 0.2;
    private final static double MIN_BASE_SPEED = 0.7;

    private final Random RAND = new Random();

    private double xPos, yPos;
    private double horizontalSpeed = 0, verticalSpeed = 0;
    protected boolean isFaceRight, isInvincible = false;
    private double invincibilityTimer = 0;

    public Demon (double xPos, double yPos) {
        super("Demon", xPos, yPos, FACE_LEFT, DAMAGE, MAX_HP);
        this.xPos = xPos;
        this.yPos = yPos;
        initializeMovementSpeed(false);
    }

    public Demon (double xPos, double yPos, Image navecImage, int navecDamage, int navecHP) {
        super("Navec", xPos, yPos, navecImage, navecDamage, navecHP);
        this.xPos = xPos;
        this.yPos = yPos;
        initializeMovementSpeed(true);
    }

    @Override
    public Image getImage() {
        if (isFaceRight && isInvincible) {
            return INVINCIBLE_RIGHT;
        } else if (!isFaceRight && isInvincible) {
            return INVINCIBLE_LEFT;
        } else if (isFaceRight) {
            return FACE_RIGHT;
        } else {
            return FACE_LEFT;
        }
    }

    @Override
    public int getHPPercent() {
        return (int) Math.round(healthPoints*100.0/MAX_HP);
    }

    public int getMaxHP() {
        return MAX_HP;
    }

    private void initializeMovementSpeed(boolean isNavec) {

        // 50% chance to be aggressive (Navec is always aggressive)
        if (RAND.nextBoolean() || isNavec) {
            // 50% chance to move up and down. 50% chance to move left and right
            if (RAND.nextBoolean()) {
                verticalSpeed = RAND.nextDouble()*(MAX_BASE_SPEED-MIN_BASE_SPEED) + MIN_BASE_SPEED;
            } else {
                horizontalSpeed = RAND.nextDouble()*(MAX_BASE_SPEED-MIN_BASE_SPEED) + MIN_BASE_SPEED;
            }
            isFaceRight = (horizontalSpeed > 0);
        } else {
            // 50% chance for passive demon to face right
            isFaceRight = RAND.nextBoolean();
        }

    }

    @Override
    public void updateState() {

        if (healthPoints <= MIN_HP) exhaust();

        if (isInvincible) {
            invincibilityTimer += 1.0/ShadowDimension.REFRESH_RATE;
            if (invincibilityTimer >= INVINCIBILITY_DURATION) {
                isInvincible = false;
                invincibilityTimer = 0;
            }
        }

        xPos += horizontalSpeed;
        yPos += verticalSpeed;
        super.moveTo(new Point(xPos, yPos));

    }

    @Override
    public void reverseMovement() {
//        if (horizontalSpeed != 0) isFaceRight = !isFaceRight;
        isFaceRight = !isFaceRight;
        horizontalSpeed *= -1;
        verticalSpeed *= -1;
    }

    @Override
    public void takesDamage(String attacker, int damage) {

        if (isInvincible) return;

        // Minus damage from health, unless that would make health less than MIN_HP, in that case set health to MIN_HP
        healthPoints = Math.max(healthPoints - damage, MIN_HP);
        System.out.format("%s inflicts %d damage points on %s. %s's current health: %d/%d\n",
                attacker, damage, getType(), getType(), healthPoints, getMaxHP());
        isInvincible = true;

    }

}
