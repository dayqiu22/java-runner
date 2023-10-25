package ui;

import java.io.IOException;

// Runs the platformer game
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        GameTerminal gameWindow = new GameTerminal();

        gameWindow.startGame();
    }
}
