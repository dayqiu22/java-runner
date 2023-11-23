package ui;

import javax.swing.*;
import java.awt.*;

import com.googlecode.lanterna.TextColor;
import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GameGUI extends JPanel implements ActionListener, Runnable {
    private static final int FPS = 60;
    private static final String JSON_STORE = "./data/save-state.json";
    private static final String FONT_NAME = "Consolas";
    private static final Font REGULAR_TEXT = new Font(FONT_NAME, Font.PLAIN, 14);
    private static final int GRID_UNIT = 50;
    private static final int MAX_COL = 20;
    private static final int MAX_ROW = 14;
    private static final int WIDTH_PX = GRID_UNIT * MAX_COL;
    private static final int HEIGHT_PX = GRID_UNIT * MAX_ROW;
    private final Sprites sprites = new Sprites();
    private int centerX;
    private int keyY;
    private Game game;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private InputHandler inputHandler = new InputHandler();
    private Thread gameThread;

    public GameGUI() {
        this.jsonWriter = new JsonWriter(JSON_STORE);
        this.jsonReader = new JsonReader(JSON_STORE);
        this.setPreferredSize(new Dimension(WIDTH_PX, HEIGHT_PX));
        this.centerX = WIDTH_PX / 2;
        this.keyY = HEIGHT_PX - 100;
        this.setBackground(Color.black);
        this.addKeyListener(inputHandler);
        this.setFocusable(true);

        this.game = new Game(WIDTH_PX, HEIGHT_PX);
        initializeTestMap(this.game);
    }

    // MODIFIES: this
    // EFFECTS: initializes the terminal screen, the game and game map,
    // and starts the tick cycle of the game state
    public void startGame() throws IOException, InterruptedException {
        Container menuContainer = drawMenu();
        this.add(menuContainer);
        this.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: draws the main menu with options to start a new game or load a saved game
    private Container drawMenu() {
        Container menuContainer = new JPanel();
        menuContainer.setLayout(null);
        menuContainer.setSize(1000, 700);

        JLabel welcome = new JLabel("J A V A   R U N N E R");
        welcome.setBounds(centerX - 150, 100, 500, 200);
        welcome.setFont(new Font(FONT_NAME, Font.BOLD, 30));
        menuContainer.add(welcome);

        JButton newButton = new JButton("New Game");
        newButton.setActionCommand("new");
        newButton.setBounds(centerX - 200, 350, 150, 40);
        newButton.setFont(REGULAR_TEXT);
        newButton.addActionListener(this);
        menuContainer.add(newButton);

        JButton loadButton = new JButton("Load Game");
        loadButton.setActionCommand("load");
        loadButton.setBounds(centerX + 75, 350, 150, 40);
        loadButton.setFont(REGULAR_TEXT);
        loadButton.addActionListener(this);
        menuContainer.add(loadButton);

        return menuContainer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case("new"):
                this.game = new Game(WIDTH_PX, HEIGHT_PX);
            case("load"):
                loadGame();
            default:
        }
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
        }
        System.exit(0);
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
        g2d.dispose();
    }

    // MODIFIES: this
    // EFFECTS: displays the blocks in the game using assigned symbols/characters
    private void drawBlocks(Graphics2D g2d) {
        for (Block block : this.game.getBlocks()) {
            drawBlock(block, g2d);
        }
    }

    // MODIFIES: this
    // EFFECTS: displays a single block at p using assigned symbols/characters
    private void drawBlock(Block block, Graphics2D g2d) {
        BufferedImage image = null;
        switch (block.getName()) {
            case Game.BLOCK:
                image = sprites.getBlock();
                break;
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
        g2d.drawImage(image, block.getPosition().getPositionX(),
                block.getPosition().getPositionY(),
                GRID_UNIT,
                GRID_UNIT, null);
    }

    private void drawCharacter(Graphics2D g2d) {
        BufferedImage image;
        if (game.getCharacter().getVelocityXMultiplier() > 0) {
            image = sprites.getFwd();
            sprites.setLastCharacter(image);
        } else if (game.getCharacter().getVelocityXMultiplier() < 0) {
            image = sprites.getRev();
            sprites.setLastCharacter(image);
        } else {
            image = sprites.getLastCharacter();
        }
        g2d.drawImage(image, game.getCharacter().getPosition().getPositionX(),
                game.getCharacter().getPosition().getPositionY(),
                GRID_UNIT,
                GRID_UNIT, null);
    }

    // modelled after JsonSerializationDemo provided by CPSC 210 at UBC
    // MODIFIES: this
    // EFFECTS: loads game state from file
    private void loadGame() {
        try {
            this.game = jsonReader.read();
        } catch (IOException e) {
            System.exit(1);
        }
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

    // temporary method for creating a test map for developers to test the UI
    private void initializeTestMap(Game testgame) {
        for (int i = 0; i < game.getMaxX(); i += 1) {
            testgame.addBlock(new Block(new Position(i, 470)));
        }

        testgame.addBlock(new Hazard(new Position(20, 469)));
        testgame.addBlock(new PowerUp(new Position(200, 469), Game.INVULNERABLE));
        testgame.addBlock(new PowerUp(new Position(400, 469), Game.SPEED));
        testgame.addBlock(new Hazard(new Position(600, 469)));
        testgame.addBlock(new PowerUp(new Position(800,469), Game.INVULNERABLE));
        testgame.addBlock(new PowerUp(new Position(750, 469), Game.SPEED));
    }
}
