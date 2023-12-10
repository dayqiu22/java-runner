package model;

import model.Block;
import model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BlockTest {
    Block testBlock;
    Block testBlock2;

    @BeforeEach
    void runBefore() {
        testBlock = new Block(10,10);
        testBlock2 = new Block(11,9);
    }

    @Test
    void testConstructor() {
        assertEquals(10, testBlock.getPositionX());
        assertEquals(10, testBlock.getPositionY());
        assertEquals("block", testBlock.getName());

        assertEquals(11, testBlock2.getPositionX());
        assertEquals(9, testBlock2.getPositionY());
        assertEquals("block", testBlock2.getName());
    }
}
