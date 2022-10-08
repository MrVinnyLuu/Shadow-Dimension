package Characters;

import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;

/**
 * // SWEN20003 Project 2, Semester 2, 2022 //
 * This class encapsulates the health (health point) information of a PlayableCharacter or Enemy
 * @author Vincent Luu, 1269979
 */


public class Health {

    private final static Colour GREEN_HP = new Colour(0, 0.8, 0.2);
    private final static double GREEN_HP_THRESHOLD = 65;
    private final static Colour ORANGE_HP = new Colour(0.9, 0.6, 0);
    private final static double ORANGE_HP_THRESHOLD = 35;
    private final static Colour RED_HP = new Colour(1, 0, 0);

    private int currentHP;
    private final int maxHP, minHP;

    public Health(int maxHP, int minHP) {
        this.maxHP = maxHP;
        this.minHP = minHP;
        currentHP = maxHP;
    }

    @Override
    public String toString() {
        return currentHP + "/" + maxHP;
    }

    /**
     * Minus damage from health
     */
    public void takesDamage(int damage) {
        currentHP = Math.max(currentHP - damage, minHP);
    }

    /**
     * Method returns the current health point percentage
     */
    public int getHPPercent() {
        return (int) Math.round(currentHP*100.0/maxHP);
    }

    public int getHP() {
        return currentHP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getMinHP() {
        return minHP;
    }

    /**
     * Method resets the HP to max
     */
    public void resetHP() {
        currentHP = maxHP;
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
        Colour HP_colour;
        if (getHPPercent() >= GREEN_HP_THRESHOLD) {
            HP_colour = GREEN_HP;
        } else if (getHPPercent() >= ORANGE_HP_THRESHOLD) {
            HP_colour = ORANGE_HP;
        } else {
            HP_colour = RED_HP;
        }
        return HP_colour;
    }

}
