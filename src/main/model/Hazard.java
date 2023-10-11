package model;

// Class representing a hazard block in the game, which can move left or right
// within a certain range from its starting position
public class Hazard extends Block {
    private final String name = "hazard";
    private final Position startingPosition;
    private final int leftBoundX;
    private final int rightBoundX;
    private int velocity;
    private int direction;

    // EFFECTS: constructs a Hazard at given position;
    // records the starting position, initializes its movement boundary,
    // velocity (grid units/tick) and direction of movement
    public Hazard(Position position) {
        super(position);
        this.startingPosition = position;
        this.leftBoundX = startingPosition.getPositionX() - 4;
        this.rightBoundX = startingPosition.getPositionX() + 4;
        this.velocity = 1;
        this.direction = 1;
    }

    // MODIFIES: this
    // EFFECTS: moves the hazard block in its direction at velocity
    public void move() {

    }

    // MODIFIES: this
    // EFFECTS: changes the direction of movement when the hazard reaches the edge of its movement range
    public void changeDir() {

    }

    public String getName() {
        return name;
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

