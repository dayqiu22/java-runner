package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// Modelled after JsonSerializationDemo provided by CPSC 210 at UBC
class JsonWriterTest extends JsonTestHelpers {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            Game state = new Game(78, 22);
            JsonWriter writer = new JsonWriter("./data/\fake-state.json");
            writer.open();
            fail("IOException was expected but not caught.");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    void testWriterInitialGame() {
        try {
            Game state = new Game(78, 22);
            JsonWriter writer = new JsonWriter("./data/testWriterInitialGame.json");
            writer.open();
            writer.write(state);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterInitialGame.json");
            state = reader.read();
            checkCharacter(state.startingPosX, state.startingPosY,
                    0, 1, 0, state.getCharacter());
            assertEquals(0, state.getBlocks().size());
            assertEquals(0, state.getInventory().size());
            assertEquals(3, state.getAvailableKeys().size());
            assertEquals(0, state.getTime());
            assertEquals(0, state.getInvulnerabilityEnd());
            assertEquals(0, state.getSpeedEnd());
            assertFalse(state.isEnded());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralGame() {
        try {
            Game state = new Game(78, 22);

            for (int i = 25; i > 4; i--) {
                state.addBlock(new Block(i, 15));
            }
            state.addBlock(new Hazard(20, 14));
            PowerUp testSpeed = new PowerUp(33, 16, Game.SPEED);
            PowerUp testInvulnerability = new PowerUp(28, 14, Game.INVULNERABLE);
            state.addBlock(testSpeed);
            state.addBlock(testInvulnerability);
            state.addBlock(new Hazard(50, 16));

            state.collectPowerUp(testSpeed);
            state.collectPowerUp(testInvulnerability);
            state.usePowerUp(state.getInventory().get(0));
            state.getCharacter().setPositionX(27);
            state.getCharacter().setPositionY(12);
            state.getCharacter().setVelocityY(-2);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralGame.json");
            writer.open();
            writer.write(state);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralGame.json");
            state = reader.read();
            checkCharacter(27, 12, 0, 2, -2, state.getCharacter());
            assertEquals(23, state.getBlocks().size());
            assertEquals(1, state.getInventory().size());
            assertEquals(2, state.getAvailableKeys().size());
            assertEquals(0, state.getTime());
            assertEquals(0, state.getInvulnerabilityEnd());
            assertEquals(0 + Game.POWER_UP_TIME, state.getSpeedEnd());
            assertFalse(state.isEnded());
            checkPowerUp("invulnerability", 28, 14, "2", state.getInventory().get(0));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}