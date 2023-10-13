package model;

import model.*;
import model.Character;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {
    private Character testCharacter;
    private Block testBlock1;
    private PowerUp testPowerUp1;
    private Hazard testHazard;
    private Block testBlock2;
    private Block testBlock3;
    private PowerUp testPowerUp2;

    @BeforeEach
    void runBefore() {
        testCharacter = new Character(new Position(10, 10));
        testBlock1 = new Block(new Position(11, 10));
        testPowerUp1 = new PowerUp(new Position(9, 10), "speedup");
        testHazard = new Hazard(new Position(13,10));
        testBlock2 = new Block(new Position(10,11));
        testBlock3 = new Block(new Position(10,9));
        testPowerUp2 = new PowerUp(new Position(10, 8), "invulnerable");
    }

    @Test
    void testConstructor() {
        assertEquals(10, testCharacter.getPosition().getPositionX());
        assertEquals(10, testCharacter.getPosition().getPositionY());
        assertEquals(0, testCharacter.getVelocityX());
        assertEquals(1, testCharacter.getVelocityXMultiplier());
        assertEquals(0, testCharacter.getVelocityY());
    }
}