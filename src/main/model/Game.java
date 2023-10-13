package model;

import java.util.ArrayList;
import java.util.List;

// Class representing the game as a whole, handles movement,
// handles power-up inventory (max 3 power-ups) and usage
public class Game {
    public static final int TICKS_PER_SECOND = 10;
    private static final int TENTH_PER_TICK = 1;
    private static final int POWER_UP_TIME = 30;
    private static final String BLOCK = "block";
    private static final String SPEED = "speedup";
    private static final String INVULNERABLE = "invulnerability";
    private static final String HAZARD = "hazard";
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
        this.blocks.add(block);
    }

    // MODIFIES: this
    // EFFECTS: progresses the game state, handles velocity changes
    // due to gravity and handles speed-up expiry before detecting collisions;
    // then handles any boundary behaviour
    public int tick() {
        this.time += TENTH_PER_TICK;
        this.character.setVelocityY(this.character.getVelocityY() + GRAVITY);
        if (this.time >= this.speedEnd) {
            int currentMultiplier = this.character.getVelocityXMultiplier();
            if (currentMultiplier > 0) {
                this.character.setVelocityXMultiplier(1);
            } else if (currentMultiplier < 0) {
                this.character.setVelocityXMultiplier(-1);
            }
        }

        moveResolveCollisions();
        if (this.ended) {
            return 1;
        }
        resolveBoundaries();

        if (atBottomBoundary(this.character.getPosition())) {
            this.ended = true;
            return 2;
        }
        return 0;
    }

    /**
     * Since the terminal uses a grid system rather than
     * pixels; we will move the character 1 unit at a time
     * to avoid skipping over blocks and not detecting collision.
     */
    // REQUIRES: list of blocks in the game to not be empty
    // MODIFIES: this
    // EFFECTS: moves character vertically and checks all blocks for collisions,
    // behaviour depends on block in collision with; then moves character
    // horizontally and repeat the same procedure
    public void moveResolveCollisions() {
        moveResolveCollisionsY();
        moveResolveCollisionsX();
    }

    // REQUIRES: list of blocks in the game to not be empty
    // MODIFIES: this
    // EFFECTS: moves character vertically then checks all blocks for collisions,
    // stop movement of character 1 position back if collided with a normal block,
    // set game to ended if collision occurs with a hazard while not invulnerable;
    // also handles collecting power-ups using a helper method
    public void moveResolveCollisionsY() {
        int vy = this.character.getVelocityY();
        int unitVelocity = 1;
        if (vy < 0) {
            unitVelocity = -1;
            vy = vy * -1;
        }

        for (int i = 0; i < vy; i++) {
            int originalY = this.character.getPosition().getPositionY();
            this.character.getPosition().setPositionY(originalY + unitVelocity);
            List<Block> collided = checkCollisionList(this.character.getPosition());
            if (collided.size() != 0) {
                String collisionType = collided.get(0).getName();
                if (collisionType.equals(BLOCK)) {
                    this.character.getPosition().setPositionY(originalY);
                    this.character.setVelocityY(0);
                } else if (collisionType.equals(SPEED) || collisionType.equals(INVULNERABLE)) {
                    collectPowerUp((PowerUp) collided.get(0));
                } else if (collisionType.equals(HAZARD) && (this.time >= this.invulnerabilityEnd)) {
                    this.ended = true;
                    break;
                }
            }
        }
    }

    // REQUIRES: list of blocks in the game to not be empty
    // MODIFIES: this
    // EFFECTS: moves character horizontally then checks all blocks for collisions,
    // stop movement of character 1 position back if collided with a normal block,
    // set game to ended if collision occurs with a hazard while not invulnerable;
    // also handles collecting power-ups using a helper method
    public void moveResolveCollisionsX() {
        int vx = this.character.getVelocityX() * this.character.getVelocityXMultiplier();
        int unitVelocity = 1;
        if (vx < 0) {
            unitVelocity = -1;
            vx = vx * -1;
        }

        for (int i = 0; i < vx; i++) {
            int originalX = this.character.getPosition().getPositionX();
            this.character.getPosition().setPositionX(originalX + unitVelocity);
            List<Block> collided = checkCollisionList(this.character.getPosition());
            if (collided.size() != 0) {
                String collisionType = collided.get(0).getName();
                if (collisionType.equals(BLOCK)) {
                    this.character.getPosition().setPositionX(originalX);
                    this.character.setVelocityX(0);
                } else if (collisionType.equals(SPEED) || collisionType.equals(INVULNERABLE)) {
                    collectPowerUp((PowerUp) collided.get(0));
                } else if (collisionType.equals(HAZARD) && (this.time >= this.invulnerabilityEnd)) {
                    this.ended = true;
                    break;
                }
            }
        }
    }

    // EFFECTS: returns true if p collided with the given block
    public boolean isCollided(Position p, Block block) {
        int blockX = block.getPosition().getPositionX();
        int blockY = block.getPosition().getPositionY();
        return (p.getPositionX() == blockX && p.getPositionY() == blockY);
    }

    // REQUIRES: list of blocks in the game to not be empty
    // EFFECTS: returns a list of blocks in collision with p
    public List<Block> checkCollisionList(Position p) {
        List<Block> collided = new ArrayList<>();
        for (Block block : blocks) {
            if (isCollided(p, block)) {
                collided.add(block);
            }
        }
        return collided;
    }

    // REQUIRES: list of blocks in the game to not be empty
    // EFFECTS: returns true if p is currently on a platform
    public boolean onPlatform(Position p) {
        int currentY = p.getPositionY();
        p.setPositionY(currentY + 1);
        List<Block> collided = checkCollisionList(p);
        p.setPositionY(currentY);
        return (collided.size() != 0);
    }

    // MODIFIES: this
    // EFFECTS: returns true if p is at the edge of the game (but not bottom)
    public void resolveBoundaries() {
        Position charaPosition = this.character.getPosition();
        if (charaPosition.getPositionX() < 0) {
            charaPosition.setPositionX(0);
        } else if (charaPosition.getPositionX() > maxX) {
            charaPosition.setPositionX(maxX);
        }
        if (charaPosition.getPositionY() < 0) {
            charaPosition.setPositionY(0);
        }
    }

    // EFFECTS: returns true if p is at the bottom edge of the game
    public boolean atBottomBoundary(Position p) {
        return p.getPositionY() > maxY;
    }

    // REQUIRES: pu in list of blocks in the game
    // MODIFIES: this, pu
    // EFFECTS: adds power-up to the inventory and assigns a key (1, 2, or 3) for
    // its use if available; returns true if collected and removes
    // pu from the list of game blocks
    public boolean collectPowerUp(PowerUp pu) {
        this.availableKeys.sort(null);
        if (this.availableKeys.size() != 0) {
            pu.setKeyAssignment(this.availableKeys.get(0));
            this.availableKeys.remove(0);
            this.inventory.add(pu);
            this.blocks.remove(pu);
            return true;
        }
        return false;
    }

    // REQUIRES: pu in inventory of power-ups
    // MODIFIES: this, pu
    // EFFECTS: applies power-up effects to the character/game and keeps
    // track of when effects expire; unbinds key to power-up and removes
    // the used power-up from the inventory; only refreshes
    // duration if an identical power-up is already in use
    public void usePowerUp(PowerUp pu) {
        this.inventory.remove(pu);
        this.availableKeys.add(pu.getKeyAssignment());
        pu.setKeyAssignment(null);
        if (pu.getName().equals(INVULNERABLE)) {
            this.invulnerabilityEnd = this.time + POWER_UP_TIME;
        } else if (pu.getName().equals(SPEED)) {
            this.speedEnd = this.time + POWER_UP_TIME;
            int currentMultiplier = this.character.getVelocityXMultiplier();
            if (currentMultiplier < 2 && currentMultiplier > -2) {
                this.character.setVelocityXMultiplier(currentMultiplier * 2);
            }
        }
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
