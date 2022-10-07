package Enemies;

import Characters.PlayableCharacter;

public interface canAttack {

    /**
     * Method determines whether the player is in the attack range the object and the object can attack
     */
    boolean inRange(PlayableCharacter player);

    /**
     * Method executes the objects attack on the player
     */
    void attack(PlayableCharacter character);
}
