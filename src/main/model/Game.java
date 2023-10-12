package model;

import java.util.ArrayList;
import java.util.List;

// Class representing the game as a whole, handles movement,
// handles power-up inventory (max 3 power-ups) and usage
public class Game {
    public static final int TICKS_PER_SECOND = 10;
    public static final int TENTH_PER_TICK = 1;
    public static final int POWER_UP_TIME = 30;
    private static final int GRAVITY = 1;
    private final int maxX;
    private final int maxY;
    private final Position startingPosition;
    private final Character character;
    private List<Block> blocks;
    private List<PowerUp> inventory;
    private List<String> availableKeys;
    private int time;
    private int invulnerabilityEnd;
    private int speedEnd;
    private boolean ended;

    // EFFECTS: constructs a new game with maximum x and y coordinate boundaries; initializes the
    // player's character and power-up inventory
    public Game(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.startingPosition = new Position(this.maxX / 3, this.maxY * 2 / 3);
        this.character = new Character(this.startingPosition);
        this.blocks = new ArrayList<>();
        this.inventory = new ArrayList<>();
        this.availableKeys = new ArrayList<>();
        this.availableKeys.add("1");
        this.availableKeys.add("2");
        this.availableKeys.add("3");
        this.time = 0;
        this.invulnerabilityEnd = 0;
        this.speedEnd = 0;
        this.ended = false;
    }

    // MODIFIES: this
    // EFFECTS: adds a block to list of blocks in the game
    public void addBlock(Block block) {

    }

    // MODIFIES: this
    // EFFECTS: progresses the game state, handles velocity changes
    // due to gravity/movement/jumping before detecting collisions,
    // then handles boundary behaviour
    public void tick() {

    }

    /**
     * We will assume the max velocity is 2 or -2 for this phase
     * since the terminal uses a grid system rather than
     * pixels; we will move the character 1 unit at a time
     * to avoid skipping over blocks and not detecting collision.
     */
    // MODIFIES: this
    // EFFECTS: moves character vertically then checks all blocks for collisions,
    // stop movement of character 1 position back if collided with a normal block,
    // set game to ended if collision occurs with a hazard while not invulnerable;
    // also handles collecting power-ups using a helper method
    public void moveResolveCollisions() {

    }

    // REQUIRES: list of blocks in the game to not be empty
    // EFFECTS: returns a list of blocks in collision with p
    public List<Block> checkCollisionList(Position p) {
        return null;
    }

    // EFFECTS: returns true if p is currently on a platform
    public boolean onPlatform(Position p) {
        return true;
    }

    // EFFECTS: returns true if p is at the edge of the game (but not bottom)
    public boolean atBoundary(Position p) {
        return p.getPositionX() <= 0 || p.getPositionX() >= maxX || p.getPositionY() <= 0;
    }

    // EFFECTS: returns true if p is at the bottom edge of the game
    public boolean atBottomBoundary(Position p) {
        return p.getPositionY() >= maxY;
    }

    // REQUIRES: pu in list of blocks in the game
    // MODIFIES: this, pu
    // EFFECTS: adds power-up to the inventory and assigns a key (1, 2, 3) for
    // its use if available; returns true if collected and removes
    // pu from the list of game blocks
    public boolean collectPowerUp(PowerUp pu) {
        return true;
    }

    // REQUIRES: pu in inventory of power-ups
    // MODIFIES: this, pu
    // EFFECTS: applies power-up effects to the character/game and keeps
    // track of when effects expire; unbinds key to power-up and removes
    // the used power-up from the inventory; only extends
    // duration if an identical power-up is already in use
    public void usePowerUp(PowerUp pu) {

    }


    public int getInvulnerabilityEnd() {
        return invulnerabilityEnd;
    }

    public int getSpeedEnd() {
        return speedEnd;
    }

    public void setInvulnerabilityEnd(int time) {
        this.invulnerabilityEnd = time + POWER_UP_TIME;
    }

    public void setSpeedEnd(int time) {
        this.speedEnd = time + POWER_UP_TIME;
    }

    public Character getCharacter() {
        return character;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public List<PowerUp> getInventory() {
        return inventory;
    }

    public List<String> getAvailableKeys() {
        return availableKeys;
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

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }
}
