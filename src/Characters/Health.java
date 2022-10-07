package Characters;

public class Health {

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
     * Minus damage from health, unless that would make health less than MIN_HP, in that case set health to MIN_HP
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
     * Method resets the player HP to max
     */
    public void resetHP() {
        currentHP = maxHP;
    }

}
