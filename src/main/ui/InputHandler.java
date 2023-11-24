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
        if (key == KeyEvent.VK_LEFT) {
            this.leftPress = true;
        } else if (key == KeyEvent.VK_RIGHT) {
            this.rightPress = true;
        } else if (key == KeyEvent.VK_SPACE) {
            this.spacePress = true;
        } else if (key == KeyEvent.VK_1) {
            this.item1Press = true;
        } else if (key == KeyEvent.VK_2) {
            this.item2Press = true;
        } else if (key == KeyEvent.VK_3) {
            this.item3Press = true;
        } else if (key == KeyEvent.VK_S) {
            this.savePress = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            this.leftPress = false;
        } else if (key == KeyEvent.VK_RIGHT) {
            this.rightPress = false;
        } else if (key == KeyEvent.VK_SPACE) {
            this.spacePress = false;
        } else if (key == KeyEvent.VK_1) {
            this.item1Press = false;
        } else if (key == KeyEvent.VK_2) {
            this.item2Press = false;
        } else if (key == KeyEvent.VK_3) {
            this.item3Press = false;
        } else if (key == KeyEvent.VK_S) {
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
