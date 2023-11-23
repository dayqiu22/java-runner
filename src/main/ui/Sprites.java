package ui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Sprites {
    private BufferedImage block;
    private BufferedImage fwd;
    private BufferedImage rev;
    private BufferedImage hazard;
    private BufferedImage invulnerability;
    private BufferedImage speed;
    private BufferedImage lastCharacter;

    public Sprites() {
        try {
            block = ImageIO.read(getClass().getResourceAsStream("/sprites/block.png"));
            fwd = ImageIO.read(getClass().getResourceAsStream("/sprites/character_fwd.png"));
            rev = ImageIO.read(getClass().getResourceAsStream("/sprites/character_rev.png"));
            hazard = ImageIO.read(getClass().getResourceAsStream("/sprites/hazard.png"));
            invulnerability = ImageIO.read(getClass().getResourceAsStream("/sprites/invulnerability.png"));
            speed = ImageIO.read(getClass().getResourceAsStream("/sprites/speed.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getBlock() {
        return block;
    }

    public BufferedImage getFwd() {
        return fwd;
    }

    public BufferedImage getRev() {
        return rev;
    }

    public BufferedImage getHazard() {
        return hazard;
    }

    public BufferedImage getInvulnerability() {
        return invulnerability;
    }

    public BufferedImage getSpeed() {
        return speed;
    }

    public BufferedImage getLastCharacter() {
        return lastCharacter;
    }

    public void setLastCharacter(BufferedImage lastCharacter) {
        this.lastCharacter = lastCharacter;
    }
}
