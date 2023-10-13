import model.*;
import model.Character;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {
    private Game testGame;
    private Block testBlock1;
    private Block testBlock2;
    private Block testBlock3;
    private Block testBlock4;
    private Block testBlock4a;
    private Block testBlock4b;
    private Block testBlock4c;
    private Block testBlock5;
    private Block testBlock6;
    private PowerUp testPowerUp1;
    private PowerUp testPowerUp2;
    private PowerUp testPowerUp3;
    private PowerUp testPowerUp4;
    private Hazard testHazard1;
    private Hazard testHazard2;

    @BeforeEach
    void runBefore() {
        testGame = new Game(30,30);
        testBlock1 = new Block(new Position(11, 20));
        testBlock2 = new Block(new Position(8,20));
        testBlock3 = new Block(new Position(10,22));
        testBlock4 = new Block(new Position(10,21));
        testBlock4a = new Block(new Position(11,21));
        testBlock4b = new Block(new Position(12,21));
        testBlock4c = new Block(new Position(13,21));
        testBlock5 = new Block(new Position(12,18));
        testBlock6 = new Block(new Position(10,18));
        testPowerUp1 = new PowerUp(new Position(11, 18), "speedup");
        testPowerUp2 = new PowerUp(new Position(9, 18), "invulnerability");
        testPowerUp3 = new PowerUp(new Position(13, 18), "speedup");
        testPowerUp4 = new PowerUp(new Position(7, 18), "invulnerability");
        testHazard1 = new Hazard(new Position(10,17));
        testHazard2 = new Hazard(new Position(12,20));
    }

    @Test
    void testConstructor() {
        assertEquals(30, testGame.getMaxX());
        assertEquals(30, testGame.getMaxY());
        Character player = testGame.getCharacter();
        assertEquals(10, player.getPosition().getPositionX());
        assertEquals(20, player.getPosition().getPositionY());
        assertEquals(0, testGame.getInventory().size());
        assertEquals(3, testGame.getAvailableKeys().size());
        assertEquals(0, testGame.getTime());
        assertEquals(0, testGame.getInvulnerabilityEnd());
        assertEquals(0, testGame.getSpeedEnd());
        assertFalse(testGame.isEnded());
    }

    @Test
    void testTickFreeFall() {
        Character character = testGame.getCharacter();
        assertEquals(0, testGame.tick());
        assertEquals(1, testGame.getTime());
        assertEquals(1, character.getVelocityY());
        assertEquals(21, character.getPosition().getPositionY());
        assertEquals(0, testGame.tick());
        assertEquals(2, testGame.getTime());
        assertEquals(2, character.getVelocityY());
        assertEquals(23, character.getPosition().getPositionY());
        assertEquals(0, testGame.tick());
        assertEquals(3, testGame.getTime());
        assertEquals(3, character.getVelocityY());
        assertEquals(26, character.getPosition().getPositionY());
        assertEquals(0, testGame.tick());
        assertEquals(4, testGame.getTime());
        assertEquals(4, character.getVelocityY());
        assertEquals(30, character.getPosition().getPositionY());
        assertFalse(testGame.isEnded());
        assertEquals(2, testGame.tick());
        assertTrue(testGame.isEnded());
    }

    @Test
    void testTickMoveAndEndSpeed() {
        testGame.addBlock(testBlock4);
        testGame.addBlock(testBlock4a);
        testGame.addBlock(testBlock4b);
        testGame.addBlock(testBlock4c);
        Character character = testGame.getCharacter();
        character.setVelocityXMultiplier(2);
        character.setVelocityX(1);
        testGame.setSpeedEnd(2);

        testGame.tick();
        assertEquals(12, character.getPosition().getPositionX());
        testGame.tick();
        assertEquals(13, character.getPosition().getPositionX());
        assertEquals(1, character.getVelocityXMultiplier());

        character.setVelocityXMultiplier(-2);
        testGame.setSpeedEnd(4);
        testGame.tick();
        assertEquals(11, character.getPosition().getPositionX());
        testGame.tick();
        assertEquals(10, character.getPosition().getPositionX());
        assertEquals(-1, character.getVelocityXMultiplier());
    }

    @Test
    void testTickHazardCollision() {
        testGame.addBlock(testBlock4);
        testGame.addBlock(testBlock4a);
        testGame.addBlock(testBlock4b);
        testGame.addBlock(testBlock4c);
        testGame.addBlock(testHazard2);
        Character character = testGame.getCharacter();
        character.setVelocityX(3);

        assertEquals(1, testGame.tick());
        assertEquals(12, character.getPosition().getPositionX());
        assertTrue(testGame.isEnded());
    }

    @Test
    void testTickSimulateJumpAndCollect() {
        testGame.addBlock(testBlock4);
        testGame.addBlock(testPowerUp1);
        Character character = testGame.getCharacter();
        character.setVelocityY(-3);
        character.setVelocityX(1);

        testGame.tick();
        assertEquals(-2, character.getVelocityY());
        assertEquals(18, character.getPosition().getPositionY());
        assertEquals(11, character.getPosition().getPositionX());
        assertEquals(1, testGame.getInventory().size());
        assertTrue(testGame.getInventory().contains(testPowerUp1));

        testGame.tick();
        testGame.tick();
        testGame.tick();
        testGame.tick();
        assertEquals(2, character.getVelocityY());
        assertEquals(20, character.getPosition().getPositionY());
        assertEquals(15, character.getPosition().getPositionX());
    }

    @Test
    void testMoveResolveCollisionsX() {
        testGame.addBlock(testBlock1);
        testGame.addBlock(testBlock2);
        Character character = testGame.getCharacter();

        character.setVelocityX(1);
        testGame.moveResolveCollisionsX();
        assertEquals(10, character.getPosition().getPositionX());

        character.setVelocityX(-2);
        testGame.moveResolveCollisionsX();
        assertEquals(9, character.getPosition().getPositionX());
    }

    @Test
    void testMoveResolveCollisionsY() {
        testGame.addBlock(testBlock4);
        testGame.addBlock(testHazard1);
        Character character = testGame.getCharacter();

        character.setVelocityY(2);
        testGame.moveResolveCollisionsY();
        assertEquals(20, character.getPosition().getPositionY());

        character.setVelocityY(-2);
        testGame.setInvulnerabilityEnd(1);
        testGame.moveResolveCollisionsY();
        testGame.moveResolveCollisionsY();
        assertEquals(16, character.getPosition().getPositionY());
        assertFalse(testGame.isEnded());

        character.setVelocityY(2);
        testGame.setTime(2);
        testGame.moveResolveCollisionsY();
        assertEquals(17, character.getPosition().getPositionY());
        assertTrue(testGame.isEnded());
    }

    @Test
    void testMoveResolveCollisionsDiagonal() {
        testGame.addBlock(testBlock5);
        Character character = testGame.getCharacter();

        character.setVelocityY(-2);
        character.setVelocityX(2);
        testGame.moveResolveCollisions();
        assertEquals(18, character.getPosition().getPositionY());
        assertEquals(11, character.getPosition().getPositionX());

        testGame.addBlock(testBlock6);
        character.setVelocityX(2);
        character.setPosition(new Position(10, 20));
        testGame.moveResolveCollisions();
        assertEquals(19, character.getPosition().getPositionY());
        assertEquals(12, character.getPosition().getPositionX());
    }

    @Test
    void testCheckCollisionList() {
        testGame.addBlock(testBlock1);
        testGame.addBlock(testBlock2);
        Position testPosition1 = new Position(11, 20);
        Position testPosition2 = new Position(8, 20);

        assertEquals(0, testGame.checkCollisionList(testGame.getCharacter().getPosition()).size());
        assertEquals(1, testGame.checkCollisionList(testPosition1).size());
        assertTrue(testGame.checkCollisionList(testPosition1).contains(testBlock1));
        assertEquals(1, testGame.checkCollisionList(testPosition2).size());
        assertTrue(testGame.checkCollisionList(testPosition2).contains(testBlock2));
    }

    @Test
    void testOnPlatform() {
        assertFalse(testGame.onPlatform(testGame.getCharacter().getPosition()));
        testGame.addBlock(testBlock3);
        assertFalse(testGame.onPlatform(testGame.getCharacter().getPosition()));
        testGame.addBlock(testBlock4);
        assertTrue(testGame.onPlatform(testGame.getCharacter().getPosition()));
    }

    @Test
    void testResolveBoundaries() {
        Character character = testGame.getCharacter();
        Position originalPosition = character.getPosition();
        testGame.resolveBoundaries();
        assertEquals(originalPosition, character.getPosition());

        character.getPosition().setPositionX(31);
        character.getPosition().setPositionY(-20);
        testGame.resolveBoundaries();
        assertEquals(30, character.getPosition().getPositionX());
        assertEquals(0, character.getPosition().getPositionY());

        character.getPosition().setPositionX(-2);
        character.getPosition().setPositionY(-1);
        testGame.resolveBoundaries();
        assertEquals(0, character.getPosition().getPositionX());
        assertEquals(0, character.getPosition().getPositionY());
    }

    @Test
    void testAtBottomBoundary() {
        assertFalse(testGame.atBottomBoundary(testGame.getCharacter().getPosition()));

        assertFalse(testGame.atBottomBoundary(new Position(10, testGame.getMaxY())));
        assertFalse(testGame.atBottomBoundary(new Position(10, testGame.getMaxY() - 1)));
        assertTrue(testGame.atBottomBoundary(new Position(10, testGame.getMaxY() + 1)));
    }

    @Test
    void testCollectPowerUp() {
        testGame.addBlock(testPowerUp1);
        testGame.addBlock(testPowerUp2);
        testGame.addBlock(testPowerUp3);
        testGame.addBlock(testPowerUp4);

        assertTrue(testGame.collectPowerUp(testPowerUp1));
        assertEquals(1,testGame.getInventory().size());
        assertEquals(3,testGame.getBlocks().size());
        assertEquals("1",testPowerUp1.getKeyAssignment());
        assertFalse(testGame.getAvailableKeys().contains("1"));

        assertTrue(testGame.collectPowerUp(testPowerUp2));
        assertEquals(2,testGame.getInventory().size());
        assertEquals(2,testGame.getBlocks().size());
        assertEquals("2",testPowerUp2.getKeyAssignment());
        assertFalse(testGame.getAvailableKeys().contains("2"));

        assertTrue(testGame.collectPowerUp(testPowerUp3));
        assertEquals(3,testGame.getInventory().size());
        assertEquals(1,testGame.getBlocks().size());
        assertEquals("3",testPowerUp3.getKeyAssignment());
        assertFalse(testGame.getAvailableKeys().contains("3"));

        assertFalse(testGame.collectPowerUp(testPowerUp4));
        assertEquals(3,testGame.getInventory().size());
        assertEquals(1,testGame.getBlocks().size());
        assertNull(testPowerUp4.getKeyAssignment());
    }

    @Test
    void testUseItem() {
        testGame.collectPowerUp(testPowerUp1);
        testGame.collectPowerUp(testPowerUp2);
        testGame.collectPowerUp(testPowerUp3);

        testGame.usePowerUp(testPowerUp1);
        testGame.usePowerUp(testPowerUp2);
        assertEquals(30, testGame.getSpeedEnd());
        assertEquals(30, testGame.getInvulnerabilityEnd());
        assertNull(testPowerUp1.getKeyAssignment());
        assertNull(testPowerUp2.getKeyAssignment());
        assertEquals(2,testGame.getAvailableKeys().size());
        assertEquals(1,testGame.getInventory().size());
        assertEquals(2, testGame.getCharacter().getVelocityXMultiplier());

        testGame.usePowerUp(testPowerUp3);
        assertEquals(30, testGame.getSpeedEnd());
        assertEquals(2, testGame.getCharacter().getVelocityXMultiplier());
        assertNull(testPowerUp3.getKeyAssignment());
        assertEquals(3,testGame.getAvailableKeys().size());
        assertEquals(0,testGame.getInventory().size());

        testGame.collectPowerUp(testPowerUp4);
        assertEquals("1",testPowerUp4.getKeyAssignment());
        testGame.usePowerUp(testPowerUp4);
        assertEquals(30, testGame.getInvulnerabilityEnd());
        assertNull(testPowerUp4.getKeyAssignment());
        assertEquals(3,testGame.getAvailableKeys().size());
        assertEquals(0,testGame.getInventory().size());
    }

    @Test
    void testAddBlock() {
        testGame.addBlock(testBlock1);
        assertTrue(testGame.getBlocks().contains(testBlock1));
        assertEquals(1, testGame.getBlocks().size());

        testGame.addBlock(testBlock2);
        testGame.addBlock(testBlock3);
        assertTrue(testGame.getBlocks().contains(testBlock2));
        assertTrue(testGame.getBlocks().contains(testBlock3));
        assertEquals(3, testGame.getBlocks().size());
    }

    @Test
    void testIsCollidedSetX() {
        testGame.addBlock(testBlock1);
        testGame.addBlock(testBlock2);
        testGame.addBlock(testBlock4);
        testGame.addBlock(testPowerUp1);

        Position testPosition = new Position(11, 20);
        assertTrue(testGame.isCollided(testPosition, testBlock1));
        assertFalse(testGame.isCollided(testPosition, testBlock2));
        assertFalse(testGame.isCollided(testPosition, testBlock4));
        assertFalse(testGame.isCollided(testPosition, testPowerUp1));
    }

    @Test
    void testIsCollidedSetY() {
        testGame.addBlock(testBlock1);
        testGame.addBlock(testBlock3);
        testGame.addBlock(testBlock4);
        testGame.addBlock(testPowerUp1);

        Position testPosition = new Position(10, 21);
        assertFalse(testGame.isCollided(testPosition, testBlock1));
        assertFalse(testGame.isCollided(testPosition, testBlock3));
        assertTrue(testGame.isCollided(testPosition, testBlock4));
        assertFalse(testGame.isCollided(testPosition, testPowerUp1));
    }
}