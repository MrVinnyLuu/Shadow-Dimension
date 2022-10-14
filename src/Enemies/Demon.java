package Enemies;

import Characters.Health;
import ShadowDimension.ShadowDimension;
import Characters.PlayableCharacter;
import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Rectangle;
import bagel.util.Point;

import java.util.Random;

/**
 * This class represents Demons, a type of Enemy
 * @author Vincent Luu, 1269979
 */

public class Demon extends Enemy implements canAttack {

    private final static double DEMON_HP_TEXT_Y_OFFSET = -6;
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

    private final static double MIN_BASE_SPEED = 0.2;
    private final static double MAX_BASE_SPEED = 0.7;

    private final static double SPEED_MULTIPLIER = 0.5;

    private final Random RAND = new Random();

    private String demonType = "Demon"; // Default type is a "regular demon"
    private double xPos, yPos;
    private double horizontalSpeed = 0, verticalSpeed = 0;
    private boolean isFaceRight, isInvincible = false;
    private double invincibilityTimer = 0;

    /**
     * Creates a "regular demon"
     */
    public Demon (double xPos, double yPos) {
        super(xPos, yPos, FACE_LEFT, MAX_HP, MIN_HP);
        this.xPos = xPos;
        this.yPos = yPos;
        initializeMovement(false);
    }

    /**
     * Creates a "special demon" (i.e. not a "regular demon")
     */
    public Demon (String specialDemon, double xPos, double yPos, Image specialDemonImage, int maxHP, int minHP) {
        super(xPos, yPos, specialDemonImage, maxHP, minHP);
        demonType = specialDemon;
        this.xPos = xPos;
        this.yPos = yPos;
        initializeMovement(true);
    }

    @Override
    public boolean inRange(PlayableCharacter player) {
        return (!health.isDead() && player.centre().distanceTo(this.centre()) <= getAttackRadius());
    }

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

            player.takesDamage(demonType, getDamage());

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

    public boolean isFaceRight() {
        return isFaceRight;
    }

    public boolean isInvincible() {
        return isInvincible;
    }

    /**
     * Method initializes the demon's aggressiveness, movement speed and face direction
     * @param guaranteedAggressive determines if the demon is guaranteed to be aggressive,
     *                             rather having than a 50% chance to be.
     */
    private void initializeMovement(boolean guaranteedAggressive) {

        // 50% chance to be aggressive (Navec is always aggressive)
        if (RAND.nextBoolean() || guaranteedAggressive) {
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

        if (isInvincible) {
            invincibilityTimer += 1.0/ ShadowDimension.REFRESH_RATE;
            if (invincibilityTimer >= INVINCIBILITY_DURATION) {
                isInvincible = false;
                invincibilityTimer = 0;
            }
        }

        double horizontalDisplacement = horizontalSpeed, verticalDisplacement = verticalSpeed;

        if (getTimescale() > 0) {
            horizontalDisplacement = horizontalSpeed*Math.pow(1+SPEED_MULTIPLIER, getTimescale());
            verticalDisplacement = verticalSpeed*Math.pow(1+SPEED_MULTIPLIER, getTimescale());
        } else if (getTimescale() < 0) {
            horizontalDisplacement = horizontalSpeed*Math.pow(1-SPEED_MULTIPLIER, -getTimescale());
            verticalDisplacement = verticalSpeed*Math.pow(1-SPEED_MULTIPLIER, -getTimescale());
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
    public void displayHP(Font font) {
        health.displayHP(font, xPos, yPos+DEMON_HP_TEXT_Y_OFFSET);
    }

    @Override
    public void takesDamage(String attacker, int damage) {

        if (isInvincible) return;
        isInvincible = true;

        health.takesDamage(damage, attacker, demonType);

    }


}
