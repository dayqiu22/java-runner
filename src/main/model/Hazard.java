package model;

// Class representing a hazard block in the game
public class Hazard extends Block {
    // EFFECTS: constructs a Hazard at given position
    public Hazard(int posX, int posY) {
        super(posX, posY);
        setName("hazard");
    }
}