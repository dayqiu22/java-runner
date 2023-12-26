package model;

// Class representing the finish line in the game
public class FinishLine extends Block {
    // EFFECTS: constructs a FinishLine at the given position
    public FinishLine(int posX, int posY) {
        super(posX, posY);
        setName("finish");
    }
}
