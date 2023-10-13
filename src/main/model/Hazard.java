package model;

// Class representing a hazard block in the game, which can move left or right
// within a certain range from its starting position
public class Hazard extends Block {
    private static final int RANGE = 3;
    private final Position startingPosition;
    private final int leftBoundX;
    private final int rightBoundX;
    private final int velocity;
    private int direction;

    // EFFECTS: constructs a Hazard at given position;
    // records the starting position, initializes its movement boundary,
    // velocity (grid units/tick) and direction of movement
    public Hazard(Position position) {
        super(position);
        this.name = "hazard";
        this.startingPosition = position;
        this.leftBoundX = startingPosition.getPositionX() - RANGE;
        this.rightBoundX = startingPosition.getPositionX() + RANGE;
        this.velocity = 1;
        this.direction = 1;
    }

    // MODIFIES: this
    // EFFECTS: moves the hazard block in its direction at velocity, change direction if at left/right bound
    public void move() {
        int currentX = this.position.getPositionX();
        if (currentX >= rightBoundX || currentX <= leftBoundX) {
            changeDir();
        }
        int newX = currentX + (velocity * direction);
        this.position.setPositionX(newX);
    }

    // MODIFIES: this
    // EFFECTS: changes the direction of movement
    public void changeDir() {
        this.direction = this.direction * -1;
    }

    public int getDirection() {
        return direction;
    }

    public int getLeftBoundX() {
        return leftBoundX;
    }

    public int getRightBoundX() {
        return rightBoundX;
    }

    public int getVelocity() {
        return velocity;
    }
}

