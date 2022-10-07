package Enemies;

import Characters.PlayableCharacter;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public abstract class Enemy extends Rectangle implements canAttack {

    protected boolean isExhausted = false;

    public Enemy(double xPos, double yPos, Image referenceImage) {
        super(xPos, yPos, referenceImage.getWidth(), referenceImage.getHeight());
    }

    public abstract Image getImage();

    public abstract void updateState();

    public abstract void reverseMovement();

    public abstract void takesDamage(String attacker, int damage);

    public boolean isExhausted() {
        return isExhausted;
    }

    public abstract int getHPPercent();

}
