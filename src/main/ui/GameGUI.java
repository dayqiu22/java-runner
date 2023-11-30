package ui;

import javax.swing.*;
import java.awt.*;

import model.*;
import persistence.JsonWriter;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.List;

// Represents the component of the GUI that will display all gameplay components
public class GameGUI extends JPanel implements Runnable {
    public static final int FPS = 60;
    private static final String JSON_STORE = "./data/save-state.json";
    private static final int GRID_UNIT = 50;
    private static final int MAX_COL = 20;
    private static final int MAX_ROW = 14;
    private static final int WIDTH_PX = GRID_UNIT * MAX_COL;
    private static final int HEIGHT_PX = GRID_UNIT * MAX_ROW;
    private static final String FONT_NAME = "Consolas";
    private static final Font REGULAR_TEXT = new Font(FONT_NAME, Font.PLAIN, 14);
    private final Sprites sprites = new Sprites();
    private final MainWindow display;
    private final int centerX;
    private final int keyY;
    private final JsonWriter jsonWriter;
    private Game game;
    private Thread gameThread;

    // EFFECTS: constructs a panel to represent the GUI for the game,
    // initializes writer for saving the game state, a new game, and key bindings;
    // initializes a test map for the game and starts a separate thread for the game
    public GameGUI(MainWindow display) {
        this.jsonWriter = new JsonWriter(JSON_STORE);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(WIDTH_PX, HEIGHT_PX));
        this.display = display;
        this.centerX = WIDTH_PX / 2;
        this.keyY = HEIGHT_PX - 100;
        this.setBackground(Color.black);
        setKeyBindings();
        this.setFocusable(true);

        this.game = new Game(WIDTH_PX, HEIGHT_PX);
        initializeTestMap(this.game);
        startGameThread();
    }

    // MODIFIES: this
    // EFFECTS: maps keystrokes to values in the input map,
    // and values to actions in the action map
    private void setKeyBindings() {
        ActionMap actionMap = getActionMap();
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "VK_LEFT");
        inputMap.put(KeyStroke.getKeyStroke("released LEFT"), "VK_LEFT_release");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "VK_RIGHT");
        inputMap.put(KeyStroke.getKeyStroke("released RIGHT"), "VK_RIGHT_release");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "VK_SPACE");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), "VK_1");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), "VK_2");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0), "VK_3");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "VK_S");

        actionMap.put("VK_LEFT", new KeyAction(this, "VK_LEFT"));
        actionMap.put("VK_RIGHT", new KeyAction(this,"VK_RIGHT"));
        actionMap.put("VK_LEFT_release", new KeyAction(this, "VK_LEFT_release"));
        actionMap.put("VK_RIGHT_release", new KeyAction(this,"VK_RIGHT_release"));
        actionMap.put("VK_SPACE", new KeyAction(this, "VK_SPACE"));
        actionMap.put("VK_1", new KeyAction(this,"VK_1"));
        actionMap.put("VK_2", new KeyAction(this, "VK_2"));
        actionMap.put("VK_3", new KeyAction(this,"VK_3"));
        actionMap.put("VK_S", new KeyAction(this, "VK_S"));
    }

    // MODIFIES: this
    // EFFECTS: creates a new Thread representing the game loop timeline
    public void startGameThread() {
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    // game loop from The Mechanism of 2D games tutorial by RyiSnow on YouTube
    // REQUIRES: this.gameThread.isAlive()
    // MODIFIES: this
    // EFFECTS: runs the game in its own thread, progressing the game state
    // 60 times per second and redrawing any changes through the graphics;
    // if the game is ended display the end screen panel with the time
    // taken to complete the game
    @Override
    public void run() {
        double tickInterval = (double) 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + tickInterval;
        while (this.gameThread != null && !this.game.isEnded()) {
            game.tick();
            repaint();
            try {
                double remainingTime = (nextDrawTime - System.nanoTime()) / 1000000;
                if (remainingTime < 0) {
                    remainingTime = 0;
                }
                Thread.sleep((long) remainingTime);
                nextDrawTime += tickInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.display.getEndScreen().getGgTime().setText("Game ended in " + (this.game.getTime() / FPS) + " seconds.");
        this.display.getCardLayout().show(this.display.getMainPanel(), "endScreen");
    }

    // MODIFIES: this
    // EFFECTS: sets the game character's base velocity to 1 grid unit (50 px)/tick
    public void kickStart() {
        this.game.getCharacter().setVelocityX(3);
    }

    // REQUIRES: dir is "left" or "right"
    // MODIFIES: this
    // EFFECTS: changes direction of the velocity if not already in the same direction
    // where < 0 is left and > 0 is right
    public void negateMultiplier(String dir, int currentMultiplier) {
        if (currentMultiplier > 0 && dir.equals("left")) {
            this.game.getCharacter().setVelocityXMultiplier(currentMultiplier * -1);
        } else if (currentMultiplier < 0 && dir.equals("right")) {
            this.game.getCharacter().setVelocityXMultiplier(currentMultiplier * -1);
        }
    }

    // REQUIRES: key is "1", "2," or "3"
    // MODIFIES: this
    // EFFECTS: if the key is not available to be assigned, a power-up can be
    // located from the inventory with the assigned key and will be used
    public void searchAndUse(String key) {
        if (!this.game.getAvailableKeys().contains(key)) {
            PowerUp toUse = null;
            for (PowerUp pu : this.game.getInventory()) {
                if (pu.getKeyAssignment().equals(key)) {
                    toUse = pu;
                    break;
                }
            }
            if (toUse != null) {
                this.game.usePowerUp(toUse);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: draws components of the game
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        drawBlocks(g2d);
        drawCharacter(g2d);
        drawInventory(g2d);
        drawHUD(g2d);
        g2d.dispose();
    }

    // MODIFIES: this
    // EFFECTS: displays the blocks in the game
    private void drawBlocks(Graphics2D g2d) {
        for (Block block : this.game.getBlocks()) {
            drawBlock(block.getPosition(), block.getName(), g2d);
        }
    }

    // MODIFIES: this
    // EFFECTS: displays a single block at pos using assigned sprites
    private void drawBlock(Position pos, String name, Graphics2D g2d) {
        BufferedImage image = null;
        switch (name) {
            case Game.BLOCK:
                image = sprites.getBlock();
                g2d.drawImage(image, pos.getPositionX(),
                        pos.getPositionY() + 50,
                        GRID_UNIT,
                        GRID_UNIT, null);
                return;
            case Game.HAZARD:
                image = sprites.getHazard();
                break;
            case Game.SPEED:
                image = sprites.getSpeed();
                break;
            case Game.INVULNERABLE:
                image = sprites.getInvulnerability();
                break;
        }
        g2d.drawImage(image, pos.getPositionX(),
                pos.getPositionY(),
                GRID_UNIT,
                GRID_UNIT, null);
    }

    // MODIFIES: this
    // EFFECTS: displays the player's character in the game with a sprite,
    // varies if the character is facing left, right, or is invulnerable
    private void drawCharacter(Graphics2D g2d) {
        BufferedImage image;
        boolean inv = game.getTime() < game.getInvulnerabilityEnd();
        if (game.getCharacter().getVelocityXMultiplier() > 0) {
            if (inv) {
                image = sprites.getFwdInv();
            } else {
                image = sprites.getFwd();
            }
            sprites.setLastCharacter(image);
        } else if (game.getCharacter().getVelocityXMultiplier() < 0) {
            if (inv) {
                image = sprites.getRevInv();
            } else {
                image = sprites.getRev();
            }
            sprites.setLastCharacter(image);
        } else {
            image = sprites.getLastCharacter();
        }
        g2d.drawImage(image, game.getCharacter().getPosition().getPositionX(),
                game.getCharacter().getPosition().getPositionY(),
                GRID_UNIT,
                GRID_UNIT, null);
    }

    // MODIFIES: this
    // EFFECTS: displays the player's inventory of power-ups using sprites
    private void drawInventory(Graphics2D g2d) {
        List<PowerUp> inventory = this.game.getInventory();

        g2d.setColor(Color.WHITE);
        g2d.setFont(REGULAR_TEXT);
        g2d.drawString("ITEMS:", centerX - 200, keyY + 25);
        g2d.drawString("1:", centerX - 125, keyY + 25);
        g2d.drawString("2:", centerX - 25, keyY + 25);
        g2d.drawString("3:", centerX + 75, keyY + 25);

        if (inventory.size() != 0) {
            for (PowerUp pu : inventory) {
                switch (pu.getKeyAssignment()) {
                    case "1":
                        drawBlock(new Position(centerX - 100, keyY), pu.getName(), g2d);
                        break;
                    case "2":
                        drawBlock(new Position(centerX, keyY), pu.getName(), g2d);
                        break;
                    case "3":
                        drawBlock(new Position(centerX + 100, keyY), pu.getName(), g2d);
                        break;
                }
            }
        }
    }

    // based on drawScore() method in the TerminalGame class of SnakeConsole-Lanterna by Mazen Kotb
    // MODIFIES: this
    // EFFECTS: displays the time since game start in seconds
    private void drawHUD(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(REGULAR_TEXT);
        g2d.drawString("TIME:", 25, 25);

        g2d.setColor(Color.GREEN);
        g2d.setFont(REGULAR_TEXT);
        g2d.drawString(String.valueOf(this.game.getTime() / FPS), 100, 25);

        g2d.setColor(Color.PINK);
        g2d.setFont(REGULAR_TEXT);
        g2d.drawString("PRESS (S) TO SAVE PROGRESS", WIDTH_PX - 250, 25);
    }

    // modelled after JsonSerializationDemo provided by CPSC 210 at UBC
    // EFFECTS: saves the game state to file
    public void saveGame() {
        try {
            jsonWriter.open();
            jsonWriter.write(this.game);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.exit(1);
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    // temporary method for creating a test map for developers to test the UI
    private void initializeTestMap(Game testgame) {
        for (int i = 1; i <= game.getMaxX(); i += 1) {
            testgame.addBlock(new Block(new Position(i, 470)));
        }

        for (int i = 600; i < 850; i += 1) {
            testgame.addBlock(new Block(new Position(i, 335)));
        }

        testgame.addBlock(new Hazard(new Position(20, 469)));
        testgame.addBlock(new PowerUp(new Position(200, 469), Game.INVULNERABLE));
        testgame.addBlock(new PowerUp(new Position(400, 469), Game.SPEED));
        testgame.addBlock(new Hazard(new Position(600, 469)));
        testgame.addBlock(new PowerUp(new Position(800,469), Game.INVULNERABLE));
        testgame.addBlock(new PowerUp(new Position(750, 469), Game.SPEED));
    }
}
