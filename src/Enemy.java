import bagel.Image;
import bagel.util.Rectangle;

public abstract class Enemy extends Rectangle {

    private final String enemyType;

    public Enemy(String enemyType, double xPos, double yPos, Image referenceImage) {
        super(xPos, yPos, referenceImage.getWidth(), referenceImage.getHeight());
        this.enemyType = enemyType;
    }

    public abstract void updateState();

    public abstract void reverseMovement();

    public abstract void takesDamage(String attacker, int damage);

    public abstract boolean isExhausted();

    public abstract double getAttackRadius();

    public abstract void attack(PlayableCharacter player);

    public abstract Image getImage();

    public int getHPPercent() {
        return -1;
    }

    public void  dealsDamage(PlayableCharacter player) {
        return;
    }

    public String getType() {
        return enemyType;
    }


}
