package Characters;

import bagel.Image;

public class CharacterFae extends PlayableCharacter{

    private final static String CHARACTER_NAME = "Fae";
    private final static Image FACE_LEFT = new Image("res/fae/faeLeft.png");
    private final static Image FACE_RIGHT = new Image("res/fae/faeRight.png");
    private final static Image ATTACK_LEFT = new Image("res/fae/faeAttackLeft.png");
    private final static Image ATTACK_RIGHT = new Image("res/fae/faeAttackRight.png");

    public CharacterFae(double startingX, double startingY) {
        super(startingX, startingY, FACE_LEFT);
    }

    @Override
    protected String getCharacterName() {
        return CHARACTER_NAME;
    }

    @Override
    public Image getImage() {
       if (isFaceRight() && isAttacking()) {
           return ATTACK_RIGHT;
       } else if (!isFaceRight() && isAttacking()) {
           return ATTACK_LEFT;
       } else if (isFaceRight()) {
           return FACE_RIGHT;
       } else {
           return FACE_LEFT;
       }
    }


}