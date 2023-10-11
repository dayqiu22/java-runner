package model;

import java.util.ArrayList;
import java.util.List;

// Class representing the game as a whole, handles movement,
// handles power-up inventory (max 3 power-ups) and usage
public class Game {
    public static final int TICKS_PER_SECOND = 10;
    public static final double SECONDS_PER_TICK = 0.1;
    public static final int POWER_UP_SEC = 4;
    private static final Position STARTING_POS = new Position(3, 5);
    private static final int GRAVITY = 1;
    private final Character player;
    private final int maxX;
    private final int maxY;
    private List<Block> blocks;
    private List<PowerUp> inventory;
    private List<String> assignedKeys;
    private int timeSeconds;
    private int invulnerabilityEnd;
    private int speedEnd;
    private boolean ended;

    // EFFECTS: constructs a new game with maximum x and y coordinate boundaries; initializes the
    // player's character and power-up inventory
    public Game(int maxX, int maxY) {
        this.player = new Character(STARTING_POS);
        this.maxX = maxX;
        this.maxY = maxY;
        this.blocks = new ArrayList<>();
        this.inventory = new ArrayList<>();
        this.inventory.add(null);
        this.inventory.add(null);
        this.inventory.add(null);
        this.assignedKeys = new ArrayList<>();
        this.timeSeconds = 0;
        this.ended = false;
    }

    // MODIFIES: this
    // EFFECTS: progresses the game state
    public void tick() {

    }

    // MODIFIES: this
    // EFFECTS: moves character vertically then checks all blocks for collisions,
    // stop movement of character if collided with a normal block,
    // set game to ended if collision occurs with a hazard while not invulnerable,
    // collect and remove items from game blocks if inventory has space;
    // then move the character horizontally and repeat checking.
    private void resolveCollisions() {

    }

    // MODIFIES: this, pu
    // EFFECTS: adds power-up to the inventory and assigns a key for its use
    public void collectItem(PowerUp pu) {

    }

    // MODIFIES: this
    // EFFECTS: applies power-up effects to the character/game and keeps track of when effects expire
    public void usePowerUp(PowerUp powerup) {

    }

    // MODIFIES: this
    // EFFECTS: sets time for invulnerability to end
    private void setInvulnerabilityEndTime(int time) {

    }

    // EFFECTS: returns true if invulnerability ended
    private boolean checkInvulnerability(int time) {
        return true;
    }

    // MODIFIES: this
    // EFFECTS: sets time for speed-up to end
    private void setSpeedEndTime(int time) {

    }

    // EFFECTS: returns true if speed-up ended
    private boolean checkSpeed(int time) {
        return true;
    }


    public Character getPlayer() {
        return player;
    }

    public List<PowerUp> getInventory() {
        return inventory;
    }

    public List<String> getAssignedKeys() {
        return assignedKeys;
    }

    public double getTimeSeconds() {
        return timeSeconds;
    }

    public boolean isEnded() {
        return ended;
    }
}
