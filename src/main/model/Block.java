package model;

// Class representing a non-character block in the game,
// with its own position and name
public class Block {
    private final Position position;
    protected String name = "block";

    // EFFECTS: constructs a Block at given position with the given name
    public Block(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }
}
