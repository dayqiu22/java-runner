package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {
    Position testPosition;
    Position testPosition2;

    @BeforeEach
    void runBefore() {
        testPosition = new Position(0,0);
        testPosition2 = new Position(1, 1);
    }

    @Test
    void testConstructor() {
        assertEquals(0, testPosition.getPositionX());
        assertEquals(0, testPosition.getPositionY());

        assertEquals(1, testPosition2.getPositionX());
        assertEquals(1, testPosition2.getPositionY());
    }
}
