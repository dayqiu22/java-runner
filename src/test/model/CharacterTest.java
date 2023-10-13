package model;

import model.*;
import model.Character;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {
    private Character testCharacter;

    @BeforeEach
    void runBefore() {
        testCharacter = new Character(new Position(10, 10));
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