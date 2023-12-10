package model;

import model.Hazard;
import model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HazardTest {
    Hazard testHazard;

    @BeforeEach
    void runBefore() {
        testHazard = new Hazard(10,10);
    }

    @Test
    void testConstructor() {
        assertEquals(10, testHazard.getPositionX());
        assertEquals(10, testHazard.getPositionY());
        assertEquals("hazard", testHazard.getName());
    }
}

