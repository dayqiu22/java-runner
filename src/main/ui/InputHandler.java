package ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {
    private boolean leftPress;
    private boolean rightPress;
    private boolean spacePress;
    private boolean item1Press;
    private boolean item2Press;
    private boolean item3Press;
    private boolean savePress;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case (KeyEvent.VK_LEFT):
                this.leftPress = true;
            case (KeyEvent.VK_RIGHT):
                this.rightPress = true;
            case (KeyEvent.VK_SPACE):
                this.spacePress = true;
            case (KeyEvent.VK_1):
                this.item1Press = true;
            case (KeyEvent.VK_2):
                this.item2Press = true;
            case (KeyEvent.VK_3):
                this.item3Press = true;
            case (KeyEvent.VK_S):
                this.savePress = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case (KeyEvent.VK_LEFT):
                this.leftPress = false;
            case (KeyEvent.VK_RIGHT):
                this.rightPress = false;
            case (KeyEvent.VK_SPACE):
                this.spacePress = false;
            case (KeyEvent.VK_1):
                this.item1Press = false;
            case (KeyEvent.VK_2):
                this.item2Press = false;
            case (KeyEvent.VK_3):
                this.item3Press = false;
            case (KeyEvent.VK_S):
                this.savePress = false;
        }
    }

    public boolean isLeftPress() {
        return leftPress;
    }

    public boolean isRightPress() {
        return rightPress;
    }

    public boolean isSpacePress() {
        return spacePress;
    }

    public boolean isItem1Press() {
        return item1Press;
    }

    public boolean isItem2Press() {
        return item2Press;
    }

    public boolean isItem3Press() {
        return item3Press;
    }

    public boolean isSavePress() {
        return savePress;
    }
}
