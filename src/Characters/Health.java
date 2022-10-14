package Characters;

import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;

/**
 * This class encapsulates the health (health point) information of a PlayableCharacter or Enemy
 * @author Vincent Luu, 1269979
 */

public class Health {

    private final static Colour GREEN_HP = new Colour(0, 0.8, 0.2);
    private final static double GREEN_HP_THRESHOLD = 65;
    private final static Colour ORANGE_HP = new Colour(0.9, 0.6, 0);
    private final static double ORANGE_HP_THRESHOLD = 35;
    private final static Colour RED_HP = new Colour(1, 0, 0);

    private final int maxHP, minHP;
    private int currentHP;

    /**
     * Creates a Health object with a minimum HP of minHP and maximum HP of maxHP
     */
    public Health(int maxHP, int minHP) {
        this.maxHP = maxHP;
        this.minHP = minHP;
        currentHP = maxHP;
    }

    /**
     * Method minuses damage from health and prints out log
     */
    public void takesDamage(int damage, String attacker, String victim) {

        currentHP = Math.max(currentHP - damage, minHP);

        System.out.format("%s inflicts %d damage points on %s. %s's current health: %d/%d\n",
                attacker, damage, victim, victim, currentHP, maxHP);

    }

    /**
     * Method returns the current health point percentage
     */
    public int getHPPercent() {
        return (int) Math.round(currentHP*100.0/maxHP);
    }

    /**
     * Method resets the HP to max
     */
    public void resetHP() {
        currentHP = maxHP;
    }

    /**
     * Method returns true if currentHP is less than or equal to minHP
     */
    public boolean isDead() {
        return (currentHP <= minHP);
    }

    /**
     * Method displays the HP percent at the specified position using the specified font
     */
    public void displayHP(Font font, double x, double y) {
        font.drawString(String.format("%d%%", getHPPercent()), x, y,
                new DrawOptions().setBlendColour(getHPColour()));
    }

    /**
     * Method computes the display colour of a given health point percentage
     */
    public Colour getHPColour() {

        if (getHPPercent() >= GREEN_HP_THRESHOLD) {
            return GREEN_HP;
        } else if (getHPPercent() >= ORANGE_HP_THRESHOLD) {
            return ORANGE_HP;
        } else {
            return RED_HP;
        }

    }

}
