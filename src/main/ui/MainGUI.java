package ui;

import javax.swing.*;
import java.io.IOException;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

// Runs the platformer game
public class MainGUI {
    public static void main(String[] args) throws IOException, InterruptedException {
        JFrame gameWindow = new JFrame();
        gameWindow.setResizable(false);
        gameWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        gameWindow.setTitle("Java Runner");

        GameGUI gameTerminal = new GameGUI();
        gameWindow.add(gameTerminal);
        gameWindow.pack();

        gameWindow.setLocationRelativeTo(null);
        gameWindow.setVisible(true);
        gameTerminal.startGameThread();
    }
}
