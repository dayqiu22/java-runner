package model;

// Class representing an object's x and y coordinates in the game
public class Position {
    private int positionX;
    private int positionY;

    // EFFECTS: constructs a Position representing coordinate (x, y)
    public Position(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
}
