package model;

// Class representing a hazard block in the game, which can move left or right
// within a certain range from its starting position
public class Hazard extends Block {
    // EFFECTS: constructs a Hazard at given position
    public Hazard(int posX, int posY) {
        super(posX, posY);
        setName("hazard");
    }
}