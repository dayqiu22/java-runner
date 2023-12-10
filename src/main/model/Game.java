package model;

import persistence.Writable;
import ui.GameGUI;
import ui.GameTerminal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

// Class representing the game as a whole, handles movement,
// handles power-up inventory (max 3 power-ups) and usage
public class Game implements Writable {
    public static final int UNIT_PER_TICK = 1;
    public static final int POWER_UP_TIME = 180;
    public static final String BLOCK = "block";
    public static final String SPEED = "speedup";
    public static final String INVULNERABLE = "invulnerability";
    public static final String HAZARD = "hazard";
    private static final int GRAVITY = 1;
    private final int maxX;
    private final int maxY;
    // Character's starting position as a field for the convenience of tests
    public final int startingPosX;
    public final int startingPosY;
    private final HashSet<Block> blocks;
    private final Inventory inventory;
    private Character character;
    private int time;
    private int invulnerabilityEnd;
    private int speedEnd;
    private boolean ended;

    // EFFECTS: constructs a new game with maximum x and y coordinate boundaries; initializes the
    // player's character and power-up inventory
    public Game(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.startingPosX = this.maxX / 4;
        this.startingPosY = this.maxY * 3 / 5;
        this.character = new Character(startingPosX, startingPosY);
        this.blocks = new HashSet<>();
        this.inventory = new Inventory(this);
        this.time = 0;
        this.invulnerabilityEnd = 0;
        this.speedEnd = 0;
        this.ended = false;
        EventLog.getInstance().logEvent(new Event("\nGame started!"));
    }

    // MODIFIES: this
    // EFFECTS: adds a block to list of blocks in the game
    public void addBlock(Block block) {
        this.blocks.add(block);
    }

    // MODIFIES: this
    // EFFECTS: progresses the game state, handles velocity changes
    // due to gravity and handles speed-up expiry before detecting collisions;
    // then handles any boundary behaviour
    public int tick() {
        this.time += UNIT_PER_TICK;
        this.character.setVelocityY(this.character.getVelocityY() + GRAVITY);
        if (this.time >= this.speedEnd) {
            int currentMultiplier = this.character.getVelocityXMultiplier();
            if (currentMultiplier > 0) {
                this.character.setVelocityXMultiplier(1);
            } else {
                this.character.setVelocityXMultiplier(-1);
            }
        }

        moveResolveCollisions();
        if (this.ended) {
            EventLog.getInstance().logEvent(new Event(
                    "Death from spike at " + (time / GameGUI.FPS) + " seconds"));
            return 1;
        }
        resolveBoundaries();

        if (atBottomBoundary()) {
            this.ended = true;
            EventLog.getInstance().logEvent(new Event(
                    "Fell to your demise at " + (time / GameGUI.FPS) + " seconds"));
        }
        return 0;
    }

    // REQUIRES: list of blocks in the game to not be empty
    // MODIFIES: this
    // EFFECTS: moves character vertically and checks all blocks for collisions,
    // behaviour depends on block in collision with; then moves character
    // horizontally and repeat the same procedure
    protected void moveResolveCollisions() {
//        moveResolveCollisionsY();
//        moveResolveCollisionsX();
        moveResolveCollisionsYGui();
        moveResolveCollisionsXGui();
    }

    // algorithm from Resolving Platform Collisions tutorial by Long Nguyen on YouTube
    // REQUIRES: list of blocks in the game to not be empty
    // MODIFIES: this
    // EFFECTS: moves character vertically then checks all blocks for collisions,
    // stop movement of character 1 position back if collided with a normal block,
    // set game to ended if collision occurs with a hazard while not invulnerable;
    // also handles collecting power-ups using a helper method
    protected void moveResolveCollisionsY() {
        int vy = this.character.getVelocityY();
        int unitVelocity = (vy < 0) ? -1 : 1;
        vy = (vy < 0) ? (vy * -1) : vy;

        for (int i = 0; i < vy; i++) {
            int originalY = this.character.getPositionY();
            this.character.setPositionY(originalY + unitVelocity);
            List<GameEntity> collided = checkCollisionList();
            if (collided.size() != 0) {
                String collisionType = collided.get(0).getName();
                if (collisionType.equals(HAZARD) && (this.time >= this.invulnerabilityEnd)) {
                    this.ended = true;
                    break;
                } else if (collisionType.equals(SPEED) ^ collisionType.equals(INVULNERABLE)) {
                    collectPowerUp((PowerUp) collided.get(0));
                } else if (collisionType.equals(BLOCK)) {
                    this.character.setPositionY(originalY);
                    this.character.setVelocityY(0);
                }
            }
        }
    }

    protected void moveResolveCollisionsYGui() {
        int vy = this.character.getVelocityY();
        int unitVelocity = (vy < 0) ? -1 : 1;
        vy = (vy < 0) ? (vy * -1) : vy;

        for (int i = 0; i < vy; i++) {
            int originalY = this.character.getPositionY();
            this.character.setPositionY(originalY + unitVelocity);
            List<GameEntity> collided = checkCollisionList();
            if (collided.size() != 0) {
                String collisionType = collided.get(0).getName();
                if (collisionType.equals(HAZARD) && (this.time >= this.invulnerabilityEnd)) {
                    this.ended = true;
                    break;
                } else if (collisionType.equals(SPEED) ^ collisionType.equals(INVULNERABLE)) {
                    collectPowerUp((PowerUp) collided.get(0));
                } else if (collisionType.equals(BLOCK)) {
                    if (unitVelocity == 1) {
                        this.character.setBottom(collided.get(0).getTop());
                    } else {
                        this.character.setTop(collided.get(0).getBottom());
                    }
                    this.character.setVelocityY(0);
                }
            }
        }
    }

    // algorithm from Resolving Platform Collisions tutorial by Long Nguyen on YouTube
    // REQUIRES: list of blocks in the game to not be empty
    // MODIFIES: this
    // EFFECTS: moves character horizontally then checks all blocks for collisions,
    // stop movement of character 1 position back if collided with a normal block,
    // set game to ended if collision occurs with a hazard while not invulnerable;
    // also handles collecting power-ups using a helper method
    protected void moveResolveCollisionsX() {
        int vx = this.character.getVelocityX() * this.character.getVelocityXMultiplier();
        int unitVelocity = (vx < 0) ? -1 : 1;
        vx = (vx < 0) ? (vx * -1) : vx;

        for (int i = 0; i < vx; i++) {
            int originalX = this.character.getPositionX();
            this.character.setPositionX(originalX + unitVelocity);
            List<GameEntity> collided = checkCollisionList();
            if (collided.size() != 0) {
                String collisionType = collided.get(0).getName();
                if (collisionType.equals(HAZARD) && (this.time >= this.invulnerabilityEnd)) {
                    this.ended = true;
                    break;
                } else if (collisionType.equals(SPEED) ^ collisionType.equals(INVULNERABLE)) {
                    collectPowerUp((PowerUp) collided.get(0));
                } else if (collisionType.equals(BLOCK)) {
                    this.character.setPositionX(originalX);
                    this.character.setVelocityX(0);
                }
            }
        }
    }

    protected void moveResolveCollisionsXGui() {
        int vx = this.character.getVelocityX() * this.character.getVelocityXMultiplier();
        int unitVelocity = (vx < 0) ? -1 : 1;
        vx = (vx < 0) ? (vx * -1) : vx;

        for (int i = 0; i < vx; i++) {
            int originalX = this.character.getPositionX();
            this.character.setPositionX(originalX + unitVelocity);
            List<GameEntity> collided = checkCollisionList();
            if (collided.size() != 0) {
                String collisionType = collided.get(0).getName();
                if (collisionType.equals(HAZARD) && (this.time >= this.invulnerabilityEnd)) {
                    this.ended = true;
                    break;
                } else if (collisionType.equals(SPEED) ^ collisionType.equals(INVULNERABLE)) {
                    collectPowerUp((PowerUp) collided.get(0));
                } else if (collisionType.equals(BLOCK)) {
                    if (unitVelocity == 1) {
                        this.character.setRight(collided.get(0).getLeft());
                    } else {
                        this.character.setLeft(collided.get(0).getRight());
                    }
                    this.character.setVelocityX(0);
                }
            }
        }
    }

    // EFFECTS: returns true if ge1 collided with ge2
    protected boolean isCollided(GameEntity ge1, GameEntity ge2) {
        int ge2X = ge2.getPositionX();
        int ge2Y = ge2.getPositionY();
        return (ge1.getPositionX() == ge2X && ge1.getPositionY() == ge2Y);
    }

    // EFFECTS: returns true if ge1 collided with ge2
    protected boolean checkCollided(GameEntity ge1, GameEntity ge2) {
        boolean notCollidedX = ge1.getRight() <= ge2.getLeft() || ge1.getLeft() >= ge2.getRight();
        boolean notCollidedY = ge1.getTop() >= ge2.getBottom() || ge1.getBottom() <= ge2.getTop();
        if (notCollidedX || notCollidedY) {
            return false;
        } else {
            return true;
        }
    }

    // algorithm from Sprite Collision Detection tutorial by Long Nguyen on YouTube
    // REQUIRES: list of blocks in the game to not be empty
    // EFFECTS: returns a list of blocks in collision with c
    protected List<GameEntity> checkCollisionList() {
        List<GameEntity> collided = new ArrayList<>();
        for (GameEntity ge : blocks) {
            if (checkCollided(character, ge)) {
                collided.add(ge);
            }
        }
        return collided;
    }

    // REQUIRES: list of blocks in the game to not be empty
    // EFFECTS: returns true if p is currently on a platform
    public boolean onPlatform() {
        int currentY = character.getPositionY();
        character.setPositionY(currentY + 1);
        List<GameEntity> collided = checkCollisionList();
        character.setPositionY(currentY);
        return (collided.size() != 0);
    }

    // MODIFIES: this
    // EFFECTS: returns true if p is at the edge of the game (but not bottom)
    protected void resolveBoundaries() {
        int charaX = this.character.getPositionX();
        int charaY = this.character.getPositionY();
        if (charaX < 0) {
            this.character.setPositionX(0);
            EventLog.getInstance().logEvent(new Event("Attempted to go beyond left edge"));
        } else if (charaX > maxX) {
            this.character.setPositionX(maxX);
            EventLog.getInstance().logEvent(new Event("Attempted to go beyond right edge"));
        }
        if (charaY < 0) {
            this.character.setPositionY(0);
            EventLog.getInstance().logEvent(new Event("Attempted to go beyond top edge"));
        }
    }

    // EFFECTS: returns true if c is at the bottom edge of the game
    protected boolean atBottomBoundary() {
        return character.getPositionY() > maxY;
    }

    // REQUIRES: pu in list of blocks in the game
    // MODIFIES: this, pu
    // EFFECTS: adds power-up to the inventory and assigns a key (1, 2, or 3) for
    // its use if available; returns true if collected and removes
    // pu from the list of game blocks
    public boolean collectPowerUp(PowerUp pu) {
        return inventory.collectPowerUp(pu);
    }

    // REQUIRES: pu in inventory of power-ups
    // MODIFIES: this, pu
    // EFFECTS: applies power-up effects to the character/game and keeps
    // track of when effects expire; unbinds key to power-up and removes
    // the used power-up from the inventory; only refreshes
    // duration if an identical power-up is already in use
    public void usePowerUp(PowerUp pu) {
        inventory.usePowerUp(pu);
    }

    // EFFECTS: returns this as a JSONObject
    // no need to keep track of available keys as the loading of
    // saved inventory will use a helper that assigns the original keys
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("maxX", this.maxX);
        json.put("maxY", this.maxY);
        json.put("character", this.character.toJson());
        json.put("blocks", blocksToJson());
        json.put("inventory", inventoryToJson());
        json.put("time", this.time);
        json.put("invulnerabilityEnd", this.invulnerabilityEnd);
        json.put("speedEnd", this.speedEnd);
        json.put("ended", this.ended);
        return json;
    }

    // EFFECTS: returns blocks in the game in the form of a JSONArray
    private JSONArray blocksToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Block b : this.blocks) {
            jsonArray.put(b.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: returns inventory of the player in the form of a JSON array
    private JSONArray inventoryToJson() {
        JSONArray jsonArray = new JSONArray();

        for (PowerUp pu : inventory.getInventory()) {
            jsonArray.put(pu.toJson());
        }
        return jsonArray;
    }

    // MODIFIES: this
    // EFFECTS: adds a power-up to the inventory
    public void addPowerUpToInventory(PowerUp pu) {
        inventory.addPowerUpToInventory(pu);
    }

    // MODIFIES: this
    // EFFECTS: removes a key from available key assignments for inventory
    public void removeAvailableKey(String key) {
        inventory.removeAvailableKey(key);
    }

    public int getInvulnerabilityEnd() {
        return invulnerabilityEnd;
    }

    public int getSpeedEnd() {
        return speedEnd;
    }

    public void setInvulnerabilityEnd(int invulnerabilityEnd) {
        this.invulnerabilityEnd = invulnerabilityEnd;
    }

    public void setSpeedEnd(int speedEnd) {
        this.speedEnd = speedEnd;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public HashSet<Block> getBlocks() {
        return blocks;
    }

    public List<PowerUp> getInventory() {
        return inventory.getInventory();
    }

    public List<String> getAvailableKeys() {
        return inventory.getAvailableKeys();
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }
}
