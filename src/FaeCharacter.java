import bagel.Image;

public class FaeCharacter extends PlayableCharacter{

    private final static Image FACE_LEFT = new Image("res/fae/faeLeft.png");
    private final static Image FACE_RIGHT = new Image("res/fae/faeRight.png");
    private final static Image ATTACK_LEFT = new Image("res/fae/faeAttackLeft.png");
    private final static Image ATTACK_RIGHT = new Image("res/fae/faeAttackRight.png");

    public FaeCharacter(double startingX, double startingY) {
        super(startingX, startingY, FACE_LEFT);
    }

    /////////////////// Attack image is different size

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

    @Override
    public String getName() {
        return "Fae";
    }

}
