package ui;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private GameGUI game;
    private final EndScreenGUI endScreen;
    private final CardLayout layout;
    private final JFrame mainWindow;
    private final JPanel mainPanel;

    public MainWindow() {
        mainWindow = new JFrame();
        mainWindow.setResizable(false);
        mainWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainWindow.setTitle("Java Runner");
        layout = new CardLayout();
        mainPanel = new JPanel();
        mainPanel.setLayout(layout);

        game = new GameGUI(this);
        MenuGUI menu = new MenuGUI(this);
        endScreen = new EndScreenGUI(this);
        mainPanel.add(game, "game");
        mainPanel.add(endScreen, "endScreen");
        mainPanel.add(menu, "menu");

        mainWindow.add(mainPanel);
        mainWindow.setLocationRelativeTo(null);
        layout.show(mainPanel, "game");
        mainWindow.pack();
        mainWindow.setVisible(true);
    }

    public void startNewGame() {
        // Create a new GameGUI instance and replace the existing one
        mainPanel.remove(game);
        game = new GameGUI(this);
        mainPanel.add(game, "game");

        // Create a new InputHandler
        game.setInputHandler(new InputHandler());

        // Pack and repaint
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
