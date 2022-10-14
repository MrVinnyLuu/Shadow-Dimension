package Obstacles;

import Characters.PlayableCharacter;
import Enemies.canAttack;

import bagel.Image;

/** This class represents Sinkholes, a type of Obstacle that can attack
 * @author Vincent Luu, 1269979
 */

public class Sinkhole extends Obstacle implements canAttack {

    private final static String OBSTACLE_NAME = "Sinkhole";
    private final static Image SINKHOLE_IMAGE = new Image("res/sinkhole.png");
    private final static int DAMAGE = 30;

    /**
     * Creates a sinkhole at (xPos, yPos)
     */
    public Sinkhole (double xPos, double yPos) {
        super(xPos, yPos, SINKHOLE_IMAGE);
    }

    @Override
    public Image getImage() {
        return SINKHOLE_IMAGE;
    }

    @Override
    public boolean inRange(PlayableCharacter player) {
        return (!isExhausted() && player.intersects(this));
    }

    @Override
    public void attack(PlayableCharacter character) {
        character.takesDamage(OBSTACLE_NAME, DAMAGE);
        exhaust();
    }

}
