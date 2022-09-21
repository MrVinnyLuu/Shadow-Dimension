import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Rectangle;
import bagel.util.Point;

import java.awt.*;
import java.util.Random;

public class Demon extends Enemy {

    protected final static int MAX_HP = 40;
    private final static int MIN_HP = 0;
    protected final static int DAMAGE = 10;
    private final static double ATTACK_RADIUS = 150;
    private final static double INVINCIBILITY_DURATION = 3;

    private final static Image FACE_LEFT = new Image("res/demon/demonLeft.png");
    private final static Image FACE_RIGHT = new Image("res/demon/demonRight.png");
    private final static Image INVINCIBLE_LEFT = new Image("res/demon/demonInvincibleLeft.png");
    private final static Image INVINCIBLE_RIGHT = new Image("res/demon/demonInvincibleRight.png");
    private final static Image FIRE = new Image("res/demon/demonFire.png");

    private final static double MIN_BASE_SPEED = 0.2;
    private final static double MAX_BASE_SPEED = 0.7;

    private final static double SPEED_MULTIPLIER = 0.5;
    private final static double MAX_POS_TIMESCALE = 3;
    private final static double MAX_NEG_TIMESCALE = -3;
    private static double timescale = 0;

    private final Random RAND = new Random();

    private double xPos, yPos;
    private double horizontalSpeed = 0, verticalSpeed = 0;
    protected boolean isFaceRight, isInvincible = false;
    private double invincibilityTimer = 0;
    protected int healthPoints;

    public Demon (double xPos, double yPos) {
        super("Demon", xPos, yPos, FACE_LEFT);
        this.xPos = xPos;
        this.yPos = yPos;
        healthPoints = MAX_HP;
        initializeMovementSpeed(false);
    }

    public Demon (double xPos, double yPos, Image navecImage, int navecHP) {
        super("Navec", xPos, yPos, navecImage);
        this.xPos = xPos;
        this.yPos = yPos;
        healthPoints = navecHP;
        initializeMovementSpeed(true);
    }

    @Override
    public double getAttackRadius() {
        return ATTACK_RADIUS;
    }

    @Override
    public void attack(PlayableCharacter player) {

        double fireX, fireY;

        if (player.centre().x <= centre().x && player.centre().y <= centre().y) {

            fireX = topLeft().x - getAttackImage().getWidth();
            fireY = topLeft().y - getAttackImage().getHeight();

            getAttackImage().drawFromTopLeft(fireX, fireY, new DrawOptions().setRotation(0));

        } else if (player.centre().x <= centre().x && player.centre().y > centre().y) {

            fireX = bottomLeft().x - getAttackImage().getWidth();
            fireY = bottomLeft().y;

            getAttackImage().drawFromTopLeft(fireX, fireY, new DrawOptions().setRotation(-Math.PI/2));

        } else if (player.centre().x > centre().x && player.centre().y <= centre().y) {

            fireX = topRight().x;
            fireY = topRight().y - getAttackImage().getHeight();

            getAttackImage().drawFromTopLeft(fireX, fireY, new DrawOptions().setRotation(Math.PI/2));

        } else {

            fireX = bottomRight().x;
            fireY = bottomRight().y;

            getAttackImage().drawFromTopLeft(fireX, fireY, new DrawOptions().setRotation(Math.PI));

        }

        if (player.intersects(new Rectangle(fireX, fireY, getAttackImage().getWidth(),
                getAttackImage().getHeight()))) {

            player.takesDamage(getType(), getDamage());

        }

    }

    public Image getAttackImage() {
        return FIRE;
    }

    public int getDamage() {
        return DAMAGE;
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
        return (int) Math.round(healthPoints*100.0/getMaxHP());
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
        if (healthPoints <= MIN_HP) isExhausted = true;

        if (isInvincible) {
            invincibilityTimer += 1.0/ShadowDimension.REFRESH_RATE;
            if (invincibilityTimer >= INVINCIBILITY_DURATION) {
                isInvincible = false;
                invincibilityTimer = 0;
            }
        }

        double horizontalDisplacement = horizontalSpeed, verticalDisplacement = verticalSpeed;
        if (timescale > 0) {
            horizontalDisplacement = horizontalSpeed*Math.pow(1+SPEED_MULTIPLIER, timescale);
            verticalDisplacement = verticalSpeed*Math.pow(1+SPEED_MULTIPLIER, timescale);
        } else if (timescale < 0) {
            horizontalDisplacement = horizontalSpeed*Math.pow(1-SPEED_MULTIPLIER, -timescale);
            verticalDisplacement = verticalSpeed*Math.pow(1-SPEED_MULTIPLIER, -timescale);
        }

        xPos += horizontalDisplacement;
        yPos += verticalDisplacement;
        super.moveTo(new Point(xPos, yPos));

    }

    @Override
    public void reverseMovement() {
        if (horizontalSpeed != 0) {
            isFaceRight = !isFaceRight;
            horizontalSpeed *= -1;
        } else if (verticalSpeed != 0) {
            verticalSpeed *= -1;
        }
    }

    @Override
    public void takesDamage(String attacker, int damage) {

        if (isInvincible) return;
        isInvincible = true;

        // Minus damage from health, unless that would make health less than MIN_HP, in that case set health to MIN_HP
        healthPoints = Math.max(healthPoints - damage, MIN_HP);

        System.out.format("%s inflicts %d damage points on %s. %s's current health: %d/%d\n",
                attacker, damage, getType(), getType(), healthPoints, getMaxHP());

    }

    public static void changeSpeedMultiplier(int timescaleStep) {

        if (timescaleStep > 0 && timescale < MAX_POS_TIMESCALE) {
            timescale += timescaleStep;
            System.out.format("Sped up, Speed: " + timescale + "\n");
        } else if (timescaleStep < 0 && timescale > MAX_NEG_TIMESCALE) {
            timescale += timescaleStep;
            System.out.format("Slowed down, Speed: " + timescale + "\n");
        }

    }

}
