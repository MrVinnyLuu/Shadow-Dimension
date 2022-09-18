import bagel.Image;
import bagel.util.Rectangle;

public abstract class Enemy extends Rectangle {

    private final int damage;
    private final String enemyType;
    private boolean isExhausted = false;
    protected int healthPoints;

    public Enemy(String enemyType, double xPos, double yPos, Image referenceImage, int damage, int healthPoints) {
        super(xPos, yPos, referenceImage.getWidth(), referenceImage.getHeight());
        this.enemyType = enemyType;
        this.damage = damage;
        this.healthPoints = healthPoints;
    }

    protected void exhaust() {
        isExhausted = true;
    }

    public void updateState() {
        return;
    }

    public void reverseMovement() {
        return;
    }

    public int getHPPercent() {
        return -1;
    }

    public void takesDamage(String attacker, int damage) {
        return;
    }

    public void dealsDamage(PlayableCharacter player) {
        player.takesDamage(enemyType, damage);

        isExhausted = true;
    }

    public String getType() {
        return enemyType;
    }

    public abstract Image getImage();

    /**
     * Method returns whether the enemy isn't exhausted i.e. hasn't been killed/fallen into
     */
    public boolean isExhausted() {
        return isExhausted;
    }

}
