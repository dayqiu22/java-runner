package ui;

import persistence.JsonReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MenuGUI extends JPanel implements ActionListener {
    private static final String JSON_STORE = "./data/save-state.json";
    private static final String FONT_NAME = "Consolas";
    private static final Font REGULAR_TEXT = new Font(FONT_NAME, Font.PLAIN, 14);
    private static final int WIDTH_PX = 1000;
    private static final int HEIGHT_PX = 700;
    private final int centerX;
    private final JsonReader jsonReader;
    private final MainWindow display;

    public MenuGUI(MainWindow display) {
        this.jsonReader = new JsonReader(JSON_STORE);
        this.centerX = WIDTH_PX / 2;
        this.display = display;
        this.setFocusable(true);
        drawMenu();
    }

    // MODIFIES: this
    // EFFECTS: draws the main menu with options to start a new game or load a saved game
    private void drawMenu() {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(WIDTH_PX, HEIGHT_PX));
        JLabel welcome = new JLabel("J A V A   R U N N E R");
        welcome.setBounds(centerX - 165, 100, 500, 200);
        welcome.setFont(new Font(FONT_NAME, Font.BOLD, 30));
        this.add(welcome);

        JButton newButton = new JButton("New Game");
        newButton.setActionCommand("new");
        newButton.setBounds(centerX - 200, 350, 150, 40);
        newButton.setFont(REGULAR_TEXT);
        newButton.addActionListener(this);
        this.add(newButton);

        JButton loadButton = new JButton("Load Game");
        loadButton.setActionCommand("load");
        loadButton.setBounds(centerX + 75, 350, 150, 40);
        loadButton.setFont(REGULAR_TEXT);
        loadButton.addActionListener(this);
        this.add(loadButton);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        this.display.startNewGame();
        GameGUI gameGUI = this.display.getGame();
        String actionCommand = e.getActionCommand();
        if (actionCommand.equals("new")) {
            startGameAndShow(gameGUI);
        } else if (actionCommand.equals("load")) {
            loadGame();
            startGameAndShow(gameGUI);
        }
    }

    private void startGameAndShow(GameGUI gameGUI) {
//        gameGUI.addKeyListener(new InputHandler());
        gameGUI.requestFocusInWindow();
        this.display.getCardLayout().show(this.display.getMainPanel(), "game");
    }

    // modelled after JsonSerializationDemo provided by CPSC 210 at UBC
    // MODIFIES: this
    // EFFECTS: loads game state from file
    private void loadGame() {
        try {
            this.display.getGame().setGame(jsonReader.read());
        } catch (IOException e) {
            System.exit(1);
        }
    }
}
