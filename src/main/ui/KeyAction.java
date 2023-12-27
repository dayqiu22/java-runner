package ui;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

// Represents an action to perform in game when a key is pressed
public class KeyAction extends AbstractAction {
    private final GameGUI gameGUI;

    // REQUIRES: actionCommand is one of "VK_LEFT", "VK_RIGHT", "VK_SPACE",
    // "VK_1", "VK_2", "VK_3", "VK_S", "VK_LEFT_release", or "VK_RIGHT_release"
    // EFFECTS: creates a new key action affecting the game panel
    // with the given action command value
    public KeyAction(GameGUI gameGUI, String actionCommand) {
        this.gameGUI = gameGUI;
        putValue(ACTION_COMMAND_KEY, actionCommand);
    }

    // MODIFIES: this
    // EFFECTS: moves the character left or right based on arrow key pressed,
    // stops the character on release of the key;
    // makes the character jump if space is pressed and the character is currently on a platform;
    // a power-up may be used if '1', '2', or '3' is pressed
    @Override
    public void actionPerformed(ActionEvent e) {
        int currentMultiplier = gameGUI.getGame().getCharacter().getVelocityXMultiplier();
        String actionCommand = e.getActionCommand();
        if (actionCommand.equals("VK_LEFT")) {
            gameGUI.kickStart();
            gameGUI.negateMultiplier("left", currentMultiplier);
        } else if (actionCommand.equals("VK_RIGHT")) {
            gameGUI.kickStart();
            gameGUI.negateMultiplier("right", currentMultiplier);
        } else if (actionCommand.equals("VK_SPACE")) {
            if (gameGUI.getGame().onPlatform()) {
                gameGUI.getGame().getCharacter().setVelocityY(-21);
            }
        } else if (actionCommand.equals("VK_1")) {
            gameGUI.searchAndUse("1");
        } else if (actionCommand.equals("VK_2")) {
            gameGUI.searchAndUse("2");
        } else if (actionCommand.equals("VK_3")) {
            gameGUI.searchAndUse("3");
        } else if (actionCommand.equals("VK_S")) {
            gameGUI.saveGame();
        } else if (actionCommand.equals("VK_LEFT_release") ^ actionCommand.equals("VK_RIGHT_release")) {
            gameGUI.getGame().getCharacter().setVelocityX(0);
        }
    }
}
