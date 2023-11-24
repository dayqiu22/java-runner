package ui;

import javax.swing.*;
import java.awt.*;

import model.*;
import persistence.JsonWriter;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.List;

public class GameGUI extends JPanel implements Runnable {
    private static final int FPS = 60;
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
    private InputHandler inputHandler = new InputHandler();
    private Thread gameThread;

    public GameGUI(MainWindow display) {
        this.jsonWriter = new JsonWriter(JSON_STORE);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(WIDTH_PX, HEIGHT_PX));
        this.display = display;
        this.centerX = WIDTH_PX / 2;
        this.keyY = HEIGHT_PX - 100;
        this.setBackground(Color.black);
        this.addKeyListener(inputHandler);
        this.setFocusable(true);

        this.game = new Game(WIDTH_PX, HEIGHT_PX);
        initializeTestMap(this.game);
        startGameThread();
    }

    public void startGameThread() {
        this.gameThread = new Thread(this);
        this.gameThread.start();
    }

    @Override
    public void run() {
        double tickInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + tickInterval;
        while (this.gameThread != null && !this.game.isEnded()) {
            guiTick();
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
            System.out.println("Looping");
        }
        JLabel ggTime = new JLabel("Game ended in " + (this.game.getTime() / FPS) + " seconds.");
        ggTime.setBounds(centerX - 160, 175, 500, 200);
        ggTime.setFont(new Font(FONT_NAME, Font.PLAIN, 24));
        this.display.getEndScreen().add(ggTime);
        this.display.getCardLayout().show(this.display.getMainPanel(), "endScreen");
    }

    public void guiTick() {
        handleUserInputs();
        this.game.tick();
    }

    private void handleUserInputs() {
        int currentMultiplier = this.game.getCharacter().getVelocityXMultiplier();
        if (inputHandler.isLeftPress()) {
            kickStart();
            negateMultiplier("left", currentMultiplier);
        } else if (inputHandler.isRightPress()) {
            kickStart();
            negateMultiplier("right", currentMultiplier);
        } else if (inputHandler.isSpacePress()) {
            if (this.game.onPlatform(game.getCharacter().getPosition())) {
                this.game.getCharacter().setVelocityY(-20);
            }
        } else if (inputHandler.isItem1Press()) {
            searchAndUse("1");
        } else if (inputHandler.isItem2Press()) {
            searchAndUse("2");
        } else if (inputHandler.isItem3Press()) {
            searchAndUse("3");
        } else if (inputHandler.isSavePress()) {
            saveGame();
        } else {
            this.game.getCharacter().setVelocityX(0);
        }
    }

    // MODIFIES: this
    // EFFECTS: sets the game character's base velocity to 1 grid unit (50 px)/tick
    private void kickStart() {
        this.game.getCharacter().setVelocityX(3);
    }

    private void negateMultiplier(String dir, int currentMultiplier) {
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
    private void searchAndUse(String key) {
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
    // EFFECTS: displays the blocks in the game using assigned symbols/characters
    private void drawBlocks(Graphics2D g2d) {
        for (Block block : this.game.getBlocks()) {
            drawBlock(block.getPosition(), block.getName(), g2d);
        }
    }

    // MODIFIES: this
    // EFFECTS: displays a single block at p using assigned symbols/characters
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
    // EFFECTS: displays the player's inventory of power-ups
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
    private void saveGame() {
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

    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    // temporary method for creating a test map for developers to test the UI
    public void initializeTestMap(Game testgame) {
        for (int i = 0; i < game.getMaxX(); i += 1) {
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
