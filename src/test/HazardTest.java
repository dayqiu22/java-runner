import model.Hazard;
import model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HazardTest {
    Hazard testHazard;

    @BeforeEach
    void runBefore() {
        testHazard = new Hazard(new Position(10,10));
    }

    @Test
    void testConstructor() {
        assertEquals(10, testHazard.getPosition().getPositionX());
        assertEquals(10, testHazard.getPosition().getPositionY());
        assertEquals("hazard", testHazard.getName());
        assertEquals(13, testHazard.getRightBoundX());
        assertEquals(7, testHazard.getLeftBoundX());
        assertEquals(1, testHazard.getVelocity());
        assertEquals(1, testHazard.getDirection());
    }

    @Test
    void testMove() {
        testHazard.move();
        assertEquals(11, testHazard.getPosition().getPositionX());
        assertEquals(10, testHazard.getPosition().getPositionY());
    }

    @Test
    void testMoveMultipleTimes() {
        testHazard.move();
        testHazard.move();
        testHazard.changeDir();
        testHazard.move();
        assertEquals(11, testHazard.getPosition().getPositionX());
        assertEquals(10, testHazard.getPosition().getPositionY());
    }

    @Test
    void testMoveAutoChangeDir() {
        testHazard.move();
        testHazard.move();
        testHazard.move();
        assertEquals(13, testHazard.getPosition().getPositionX());
        testHazard.move();
        testHazard.move();
        testHazard.move();
        assertEquals(10, testHazard.getPosition().getPositionX());
        assertEquals(10, testHazard.getPosition().getPositionY());
    }

    @Test
    void testChangeDir() {
        testHazard.changeDir();
        assertEquals(-1, testHazard.getDirection());

        testHazard.changeDir();
        assertEquals(1, testHazard.getDirection());
    }
}

