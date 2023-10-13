package ui;

import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import model.*;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import model.Character;

import java.io.IOException;


public class GameTerminal {
    private static final char CHARACTER = '⛄';
    private static final char BLOCK = '▣';
    private static final char HAZARD = '❌';
    private static final char SPEED = '⚡';
    private static final char INVULNERABLE = '✨';
    private Game game;
    private Screen screen;
    private WindowBasedTextGUI endGui;

    // temporary method for creating a test map for developers to test the UI
    private void initializeTestMap(Game testgame) {
        testgame.addBlock(new Block(new Position(26, 15)));

        testgame.addBlock(new Block(new Position(27, 15)));
        testgame.addBlock(new Block(new Position(28, 16)));
        testgame.addBlock(new Block(new Position(29, 16)));
        testgame.addBlock(new Block(new Position(30, 17)));
        testgame.addBlock(new Block(new Position(31, 17)));
        testgame.addBlock(new Block(new Position(32, 17)));
        testgame.addBlock(new Block(new Position(33, 17)));
        testgame.addBlock(new Block(new Position(34, 17)));
        testgame.addBlock(new Block(new Position(35, 17)));
        testgame.addBlock(new Block(new Position(35, 16)));
        testgame.addBlock(new Block(new Position(25, 15)));
        testgame.addBlock(new Block(new Position(24, 15)));
        testgame.addBlock(new Block(new Position(23, 15)));
        testgame.addBlock(new Block(new Position(22, 15)));
        testgame.addBlock(new Block(new Position(21, 15)));
        testgame.addBlock(new Block(new Position(20, 15)));
        testgame.addBlock(new Block(new Position(19, 15)));
        testgame.addBlock(new Block(new Position(18, 15)));
        testgame.addBlock(new Block(new Position(18, 14)));

        testgame.addBlock(new Hazard(new Position(20, 14)));
        testgame.addBlock(new PowerUp(new Position(28, 14), Game.INVULNERABLE));
        testgame.addBlock(new PowerUp(new Position(33, 16), Game.SPEED));
    }

    public void startGame() throws IOException, InterruptedException {
        screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();

        TerminalSize terminalSize = screen.getTerminalSize();

        game = new Game(terminalSize.getColumns() - 2,terminalSize.getRows() - 2);
        initializeTestMap(game);
        cycleTicks();
    }

    private void cycleTicks() throws IOException, InterruptedException {
        while (!game.isEnded() || endGui.getActiveWindow() != null) {
            uiTick();
            Thread.sleep(1000L / Game.TICKS_PER_SECOND);
        }

        System.exit(0);
    }

    private void uiTick() throws IOException {
        handleUserInputs();

        game.tick();

        screen.setCursorPosition(new TerminalPosition(0, 0));
        screen.clear();
        render();
        screen.refresh();
    }

    private void handleUserInputs() throws IOException {
        KeyStroke key = screen.pollInput();
        if (key == null) {
            return;
        }
        int currentMultiplier = game.getCharacter().getVelocityXMultiplier();
        switch (key.getKeyType()) {
            case Character:
                handleCharacter(key.getCharacter());
                break;
            case ArrowRight:
                kickStart();
                if (currentMultiplier < 0) {
                    game.getCharacter().setVelocityXMultiplier(currentMultiplier * -1);
                }
                break;
            case ArrowLeft:
                kickStart();
                if (currentMultiplier > 0) {
                    game.getCharacter().setVelocityXMultiplier(currentMultiplier * -1);
                }
                break;
            default:
        }
    }

    private void handleCharacter(char character) {
        switch (character) {
            case (' '):
                if (game.onPlatform(game.getCharacter().getPosition())) {
                    game.getCharacter().setVelocityY(-3);
                }
                break;
            case ('1'):
                searchAndUse("1");
                break;
            case ('2'):
                searchAndUse("2");
                break;
            case ('3'):
                searchAndUse("3");
                break;
        }
    }

    private void searchAndUse(String key) {
        if (!game.getAvailableKeys().contains(key)) {
            for (PowerUp pu : game.getInventory()) {
                if (pu.getKeyAssignment().equals(key)) {
                    game.usePowerUp(pu);
                }
            }
        }
    }

    private void kickStart() {
        if (game.getCharacter().getVelocityX() == 0) {
            game.getCharacter().setVelocityX(1);
        }
    }


    private void render() {
        if (game.isEnded()) {
            if (endGui == null) {
                drawEndScreen();
            }

            return;
        }

        drawTime();
        drawBlocks();
        drawCharacter();
    }

    private void drawEndScreen() {
        endGui = new MultiWindowTextGUI(screen);

        MessageDialogBuilder messageDialogBuilder = new MessageDialogBuilder()
                .setTitle("Game over!")
                .setText("Game ended in " + (game.getTime() / Game.TICKS_PER_SECOND) + " seconds!"
                        + "\nClick ENTER to close.")
                .addButton(MessageDialogButton.Close);

        MessageDialog dialog = messageDialogBuilder.build();
        dialog.showDialog(endGui);
    }

    private void drawTime() {
        TextGraphics text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.WHITE);
        text.putString(1, 0, "TIME: ");

        text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.GREEN);
        text.putString(8, 0, String.valueOf(game.getTime() / Game.TICKS_PER_SECOND));
    }

    private void drawBlocks() {
        for (Block block : game.getBlocks()) {
            switch (block.getName()) {
                case Game.BLOCK:
                    drawPosition(block.getPosition(), TextColor.ANSI.YELLOW, BLOCK);
                    break;
                case Game.HAZARD:
                    drawPosition(block.getPosition(), TextColor.ANSI.RED, HAZARD);
                    break;
                case Game.SPEED:
                    drawPosition(block.getPosition(), TextColor.ANSI.GREEN, SPEED);
                    break;
                case Game.INVULNERABLE:
                    drawPosition(block.getPosition(), TextColor.ANSI.MAGENTA, INVULNERABLE);
                    break;
            }
        }
    }

    private void drawCharacter() {
        Character character = game.getCharacter();
        drawPosition(character.getPosition(), TextColor.ANSI.WHITE, CHARACTER);
    }

    private void drawPosition(Position pos, TextColor color, char c) {
        TextGraphics text = screen.newTextGraphics();
        text.setForegroundColor(color);
        text.putString(pos.getPositionX(), pos.getPositionY(), String.valueOf(c));
    }

}
