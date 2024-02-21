package ui;

import model.Event;
import model.EventLog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

// Represents the window of the GUI that will display application components
public class MainWindow extends JFrame implements WindowListener {
    private GameGUI game;
    private final EndScreenGUI endScreen;
    private final CardLayout layout;
    private final JFrame mainWindow;
    private final JPanel mainPanel;
    private String map = "/maps/testmap.csv";

    // EFFECTS: constructs a new window to hold the panel components of
    // the game application; initializes the window to have a card-layout
    // with menu and end screen panels; shows the menu panel first
    public MainWindow() {
        super();
        mainWindow = new JFrame();
        mainWindow.setResizable(false);
        mainWindow.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        mainWindow.setTitle("Java Runner");
        layout = new CardLayout();
        mainPanel = new JPanel();
        mainPanel.setLayout(layout);

        MenuGUI menu = new MenuGUI(this);
        endScreen = new EndScreenGUI(this);
        mainPanel.add(endScreen, "endScreen");
        mainPanel.add(menu, "menu");

        mainWindow.addWindowListener(this);
        mainWindow.add(mainPanel);
        mainWindow.setLocationRelativeTo(null);
        layout.show(mainPanel, "menu");
        mainWindow.pack();
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: removes existing game panel and creates a new game panel;
    // repacks and redraws components of the GUI
    public void startNewGame() {
        if (game != null) {
            mainPanel.remove(game);
        }
        game = new GameGUI(this, map);
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

    public void setMap(String map) {
        this.map = map;
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        for (Event next : EventLog.getInstance()) {
            System.out.println(next.getDescription());
        }
        this.dispose();
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
