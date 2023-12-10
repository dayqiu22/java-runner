package model;

import org.json.JSONObject;
import persistence.Writable;

// Class representing a non-character block in the game,
// with its own position and name
public class Block extends GameEntity implements Writable {
    // EFFECTS: constructs a Block at given position with the given name
    public Block(int posX, int posY) {
        super(posX, posY, "block");
    }

    // EFFECTS: returns this as a JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", getName());
        json.put("positionX", getPositionX());
        json.put("positionY", getPositionY());
        return json;
    }
}
