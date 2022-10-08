package Obstacles;

import Characters.PlayableCharacter;
import Enemies.canAttack;
import bagel.Image;
import bagel.util.Rectangle;

/**
 * // SWEN20003 Project 2, Semester 2, 2022 //
 * This class represents Sinkholes, a type of Obstacle
 * @author Vincent Luu, 1269979
 */

public class Sinkhole extends Obstacle implements canAttack {

    private final static Image SINKHOLE_IMAGE = new Image("res/sinkhole.png");
    private final static int DAMAGE = 30;
    private boolean isExhausted = false;

    public Sinkhole (double xPos, double yPos) {
        super(xPos, yPos, SINKHOLE_IMAGE);
    }

    /**
     * Returns the sinkhole image only if it isn't exhausted
     */
    public Image getImage() {
        if (isExhausted) {
            return null;
        } else {
            return SINKHOLE_IMAGE;
        }
    }

    @Override
    public boolean inRange(PlayableCharacter player) {
        return (!isExhausted && player.intersects(this));
    }

    @Override
    public void attack(PlayableCharacter character) {
        if (!isExhausted) {
            character.takesDamage("Sinkhole", DAMAGE);
            isExhausted = true;
        }
    }

    /**
     * Determines if the sinkhole isn't exhausted and the player intersects it
     */
    @Override
    public boolean intersects(Rectangle rect) {
        if (isExhausted) {
            return false;
        } else {
            return super.intersects(rect);
        }
    }

}
