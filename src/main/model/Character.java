package model;

// Class representing the player's character, its position, velocities (grid units/tick),
// and x-direction velocity multiplier (handles direction and power-up modifier)
public class Character {
    private static final int STARTING_VELOCITY = 0;
    private Position position;
    private int velocityX;
    private int velocityXMultiplier;
    private int velocityY;

    // EFFECTS: constructs a Character at coordinate (x, y); initializes
    // velocity to 0 (grid units/tick), velocity multiplier to 1
    public Character(Position position) {
        this.position = position;
        this.velocityX = STARTING_VELOCITY;
        this.velocityXMultiplier = 1;
        this.velocityY = STARTING_VELOCITY;
    }

    // EFFECTS: returns true if character collided with any blocks
    public boolean isCollided(Block block) {
        return false;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    public int getVelocityXMultiplier() {
        return velocityXMultiplier;
    }

    public void setVelocityXMultiplier(int velocityXMultiplier) {
        this.velocityXMultiplier = velocityXMultiplier;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }
}
