package model;

import ui.GameGUI;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private final List<PowerUp> inventory;
    private final List<String> availableKeys;
    private final Game game;

    public Inventory(Game game) {
        this.inventory = new ArrayList<>();
        this.availableKeys = new ArrayList<>();
        this.availableKeys.add("1");
        this.availableKeys.add("2");
        this.availableKeys.add("3");
        this.game = game;
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
            EventLog.getInstance().logEvent(new Event(
                    "Picked up \"" + pu.getName() + "\" power-up. \n "
                            + "Assigned \"" + this.availableKeys.get(0) + "\" key to " + pu.getName() + " power-up"));
            this.availableKeys.remove(0);
            this.inventory.add(pu);
            game.getBlocks().remove(pu);
            return true;
        }
        EventLog.getInstance().logEvent(new Event(
                "Inventory full. Did not pick up \"" + pu.getName() + "\" power-up."));
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
        if (pu.getName().equals(Game.INVULNERABLE)) {
            game.setInvulnerabilityEnd(game.getTime() + Game.POWER_UP_TIME);
        } else {
            game.setSpeedEnd(game.getTime() + Game.POWER_UP_TIME);
            int currentMultiplier = game.getCharacter().getVelocityXMultiplier();
            if (currentMultiplier == 1 ^ currentMultiplier == -1) {
                game.getCharacter().setVelocityXMultiplier(currentMultiplier * 2);
            }
        }
        EventLog.getInstance().logEvent(new Event(
                "Used " + pu.getName() + " from slot \"" + pu.getKeyAssignment() + "\""));
        pu.setKeyAssignment(null);
    }

    // MODIFIES: this
    // EFFECTS: adds a power-up to the inventory
    public void addPowerUpToInventory(PowerUp pu) {
        this.inventory.add(pu);
    }

    // MODIFIES: this
    // EFFECTS: removes a key from available key assignments
    public void removeAvailableKey(String key) {
        this.availableKeys.remove(key);
    }

    public List<PowerUp> getInventory() {
        return inventory;
    }

    public List<String> getAvailableKeys() {
        return availableKeys;
    }
}
