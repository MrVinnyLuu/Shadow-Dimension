package Obstacles;

import Characters.PlayableCharacter;
import Enemies.canAttack;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Sinkhole extends Obstacle implements canAttack {

    private final static Image SINKHOLE_IMAGE = new Image("res/sinkhole.png");
    private final static int DAMAGE = 30;
    private final static double ATTACK_RADIUS = SINKHOLE_IMAGE.getWidth();
    private boolean isExhausted = false;

    public Sinkhole (double xPos, double yPos) {
        super(xPos, yPos, SINKHOLE_IMAGE);
    }

    public Image getImage() {
        if (isExhausted) return null;
        return SINKHOLE_IMAGE;
    }

    public boolean canAttack(PlayableCharacter player) {
        return (!isExhausted && player.intersects(this));
    }

    @Override
    public void attack(PlayableCharacter character) {
        if (isExhausted) return;
        character.takesDamage("Sinkhole", DAMAGE);
        isExhausted = true;
    }

    @Override
    public boolean contacts(Rectangle rect) {
        if (isExhausted) {
            return false;
        }
        return intersects(rect);
    }

}
