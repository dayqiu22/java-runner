package model;

import org.json.JSONObject;
import persistence.Writable;

// Class representing a non-character block in the game,
// with its own position and name
public class Block implements Writable {
    protected final Position position;
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

    // EFFECTS: returns this as a JSONObject
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("positionX", this.position.getPositionX());
        json.put("positionY", this.position.getPositionY());
        return json;
    }
}
