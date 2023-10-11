package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BlockTest {
    Block testBlock;
    Block testBlock2;

    @BeforeEach
    void runBefore() {
        testBlock = new Block(new Position(0,0));
        testBlock2 = new Block(new Position(-5,9));
    }

    @Test
    void testConstructor() {
        assertEquals(0, testBlock.getPosition().getPositionX());
        assertEquals(0, testBlock.getPosition().getPositionY());
        assertEquals("block", testBlock.getName());

        assertEquals(-5, testBlock2.getPosition().getPositionX());
        assertEquals(9, testBlock2.getPosition().getPositionY());
        assertEquals("block", testBlock2.getName());
    }
}
