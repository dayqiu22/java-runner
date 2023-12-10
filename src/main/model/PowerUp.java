package model;

import org.json.JSONObject;

// Class representing a power-up block in the game, which can
// be assigned a key for usage after being picked up at a certain position
public class PowerUp extends Block {
    private String keyAssignment;

    // REQUIRES: name is "speedup" or "invulnerability"
    // EFFECTS: constructs a PowerUp at given position;
    // does not initialize key assignment
    public PowerUp(int posX, int posY, String name) {
        super(posX, posY);
        setName(name);
    }

    public String getKeyAssignment() {
        return keyAssignment;
    }

    public void setKeyAssignment(String keyAssignment) {
        this.keyAssignment = keyAssignment;
    }

    // EFFECTS: returns this as a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", getName());
        json.put("positionX", getPositionX());
        json.put("positionY", getPositionY());
        json.put("keyAssignment", this.keyAssignment);
        return json;
    }
}

