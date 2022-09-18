import bagel.Image;
import bagel.util.Rectangle;

public abstract class Enemy extends Rectangle {

    private final int damage;
    private final String enemyType;
    private boolean isExhausted = false;

    public Enemy(String enemyType, double xPos, double yPos, Image referenceImage, int damage) {
        super(xPos, yPos, referenceImage.getWidth(), referenceImage.getHeight());
        this.enemyType = enemyType;
        this.damage = damage;
    }

    /**
     * Method inflicts enemy's damage on "player" and prints out the required log to the command line
     */
    public void dealsDamage(PlayableCharacter player) {
        player.takesDamage(damage);
        System.out.format("%s inflicts %d damage points on %s. %s's current health: %d/%d\n",
                enemyType, damage, player.getName(), player.getName(), player.getHP(), PlayableCharacter.getMaxHP());
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
