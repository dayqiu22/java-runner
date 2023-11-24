package ui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

// Acts to store the png sprite representations of game components
// to be displayed in the game panel
public class Sprites {
    private BufferedImage block;
    private BufferedImage fwd;
    private BufferedImage rev;
    private BufferedImage fwdInv;
    private BufferedImage revInv;
    private BufferedImage hazard;
    private BufferedImage invulnerability;
    private BufferedImage speed;
    private BufferedImage lastCharacter;

    // inspired by Sprites and Animation tutorial by RyiSnow on YouTube
    // EFFECTS: instantiates an instance of this class and
    // loads in sprites from file sources and store in fields
    public Sprites() {
        try {
            block = ImageIO.read(getClass().getResourceAsStream("/sprites/block.png"));
            fwd = ImageIO.read(getClass().getResourceAsStream("/sprites/character_fwd.png"));
            rev = ImageIO.read(getClass().getResourceAsStream("/sprites/character_rev.png"));
            fwdInv = ImageIO.read(getClass().getResourceAsStream("/sprites/character_fwd_inv.png"));
            revInv = ImageIO.read(getClass().getResourceAsStream("/sprites/character_rev_inv.png"));
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

    public BufferedImage getFwdInv() {
        return fwdInv;
    }

    public BufferedImage getRevInv() {
        return revInv;
    }

    public void setLastCharacter(BufferedImage lastCharacter) {
        this.lastCharacter = lastCharacter;
    }
}
