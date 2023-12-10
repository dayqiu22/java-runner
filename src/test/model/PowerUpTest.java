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
        testPowerUp = new PowerUp(10,10, "speedup");
        testPowerUp2 = new PowerUp(1,1, "invulnerable");
    }

    @Test
    void testConstructor() {
        assertEquals(10, testPowerUp.getPositionX());
        assertEquals(10, testPowerUp.getPositionY());
        assertEquals("speedup", testPowerUp.getName());

        assertEquals(1, testPowerUp2.getPositionX());
        assertEquals(1, testPowerUp2.getPositionY());
        assertEquals("invulnerable", testPowerUp2.getName());
    }
}

