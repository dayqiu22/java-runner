package model;

import persistence.Writable;
import ui.GameGUI;

public abstract class GameEntity implements Writable {
    private int positionX;
    private int positionY;
    private String name;

    public GameEntity(int posX, int posY, String name) {
        positionX = posX;
        positionY = posY;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public int getLeft() {
        return getPositionX();
    }

    public void setLeft(int newLeft) {
        setPositionX(newLeft);
    }

    public int getRight() {
        return getPositionX() + GameGUI.GRID_UNIT - 1;
    }

    public void setRight(int newRight) {
        setPositionX(newRight - GameGUI.GRID_UNIT + 1);
    }

    public int getTop() {
        return getPositionY();
    }

    public void setTop(int newTop) {
        setPositionY(newTop);
    }

    public int getBottom() {
        return getPositionY() + GameGUI.GRID_UNIT - 1;
    }

    public void setBottom(int newBottom) {
        setPositionY(newBottom - GameGUI.GRID_UNIT + 1);
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
}
