package Enemies;

import Characters.PlayableCharacter;
import bagel.Image;
import bagel.util.Rectangle;

public abstract class Enemy extends Rectangle {

    private final String enemyType;
    protected boolean isExhausted = false;

    public Enemy(String enemyType, double xPos, double yPos, Image referenceImage) {
        super(xPos, yPos, referenceImage.getWidth(), referenceImage.getHeight());
        this.enemyType = enemyType;
    }

    public void updateState() {
        return;
    }

    public void reverseMovement() {
        return;
    }

    public void takesDamage(String attacker, int damage) {
        return;
    }

    public boolean isExhausted() {
        return isExhausted;
    }

    public abstract double getAttackRadius();

    public abstract void attack(PlayableCharacter player);

    public abstract Image getImage();

    public int getHPPercent() {
        return -1;
    }

    public String getType() {
        return enemyType;
    }


}
