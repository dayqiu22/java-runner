package ui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

// Acts to store the png sprite representations of game components
// to be displayed in the game panel
public class Sprites {
    private static Sprites sprites;
    private static BufferedImage block;
    private static BufferedImage fwd;
    private static BufferedImage rev;
    private static BufferedImage fwdInv;
    private static BufferedImage revInv;
    private static BufferedImage hazard;
    private static BufferedImage invulnerability;
    private static BufferedImage speed;
    private static BufferedImage finish;
    private BufferedImage lastCharacter;

    // EFFECTS: private constructor to prevent external construction
    private Sprites() {

    }

    // EFFECTS: instantiates an instance of this class if not already present and
    // loads in sprites from file sources to store in fields;
    // returns the sole instance of this class
    public static Sprites getInstance() {
        if (sprites == null) {
            sprites = new Sprites();
        }
        try {
            block = ImageIO.read(Sprites.class.getResourceAsStream("/sprites/block.png"));
            fwd = ImageIO.read(Sprites.class.getResourceAsStream("/sprites/character_fwd.png"));
            rev = ImageIO.read(Sprites.class.getResourceAsStream("/sprites/character_rev.png"));
            fwdInv = ImageIO.read(Sprites.class.getResourceAsStream("/sprites/character_fwd_inv.png"));
            revInv = ImageIO.read(Sprites.class.getResourceAsStream("/sprites/character_rev_inv.png"));
            hazard = ImageIO.read(Sprites.class.getResourceAsStream("/sprites/hazard.png"));
            invulnerability = ImageIO.read(Sprites.class.getResourceAsStream("/sprites/invulnerability.png"));
            speed = ImageIO.read(Sprites.class.getResourceAsStream("/sprites/speed.png"));
            finish = ImageIO.read(Sprites.class.getResourceAsStream("/sprites/finish.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sprites;
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

    public BufferedImage getFinish() {
        return finish;
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
