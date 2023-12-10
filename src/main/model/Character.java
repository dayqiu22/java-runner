package model;

import org.json.JSONObject;
import persistence.Writable;

// Class representing the player's character, its position, velocities (grid units/tick),
// and x-direction velocity multiplier (handles direction and power-up modifier)
public class Character extends GameEntity implements Writable {
    private static final int STARTING_VELOCITY = 0;
    private int velocityX;
    private int velocityXMultiplier;
    private int velocityY;

    // EFFECTS: constructs a Character at coordinate (x, y); initializes
    // velocity to 0 (grid units/tick), velocity multiplier to 1
    public Character(int posX, int posY) {
        super(posX, posY, "player");
        this.velocityX = STARTING_VELOCITY;
        this.velocityXMultiplier = 1;
        this.velocityY = STARTING_VELOCITY;
    }

    // EFFECTS: returns this as a JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("positionX", getPositionX());
        json.put("positionY", getPositionY());
        json.put("velocityX", this.velocityX);
        json.put("velocityXMultiplier", this.velocityXMultiplier);
        json.put("velocityY", this.velocityY);
        return json;
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
