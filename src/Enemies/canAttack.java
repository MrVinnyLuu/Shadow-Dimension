package Enemies;

import Characters.PlayableCharacter;

public interface canAttack {

    boolean canAttack(PlayableCharacter player);
    void attack(PlayableCharacter character);
}
