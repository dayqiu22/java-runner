package model;

import model.Position;
import model.PowerUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PowerUpTest {
    PowerUp testPowerUp;
    PowerUp testPowerUp2;

    @BeforeEach
    void runBefore() {
        testPowerUp = new PowerUp(new Position(10,10), "speedup");
        testPowerUp2 = new PowerUp(new Position(1,1), "invulnerable");
    }

    @Test
    void testConstructor() {
        assertEquals(10, testPowerUp.getPosition().getPositionX());
        assertEquals(10, testPowerUp.getPosition().getPositionY());
        assertEquals("speedup", testPowerUp.getName());

        assertEquals(1, testPowerUp2.getPosition().getPositionX());
        assertEquals(1, testPowerUp2.getPosition().getPositionY());
        assertEquals("invulnerable", testPowerUp2.getName());
    }
}

