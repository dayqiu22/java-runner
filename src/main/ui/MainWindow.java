package ui;

import javax.swing.*;
import java.awt.*;

// Represents the window of the GUI that will display application components
public class MainWindow extends JFrame {
    private GameGUI game;
    private final EndScreenGUI endScreen;
    private final CardLayout layout;
    private final JFrame mainWindow;
    private final JPanel mainPanel;

    // EFFECTS: constructs a new window to hold the panel components of
    // the game application; initializes the window to have a card-layout
    // with menu and end screen panels; shows the menu panel first
    public MainWindow() {
        mainWindow = new JFrame();
        mainWindow.setResizable(false);
        mainWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainWindow.setTitle("Java Runner");
        layout = new CardLayout();
        mainPanel = new JPanel();
        mainPanel.setLayout(layout);

        MenuGUI menu = new MenuGUI(this);
        endScreen = new EndScreenGUI(this);
        mainPanel.add(endScreen, "endScreen");
        mainPanel.add(menu, "menu");

        mainWindow.add(mainPanel);
        mainWindow.setLocationRelativeTo(null);
        layout.show(mainPanel, "menu");
        mainWindow.pack();
        mainWindow.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: removes existing game panel and creates a new game panel;
    // repacks and redraws components of the GUI
    public void startNewGame() {
        if (game != null) {
            mainPanel.remove(game);
        }
        game = new GameGUI(this);
        mainPanel.add(game, "game");

        mainWindow.pack();
        mainWindow.repaint();
    }

    public GameGUI getGame() {
        return game;
    }

    public EndScreenGUI getEndScreen() {
        return endScreen;
    }

    public CardLayout getCardLayout() {
        return layout;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
