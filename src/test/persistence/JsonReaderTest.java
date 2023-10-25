package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// Tests modelled after JsonSerializationDemo provided by CPSC 210 at UBC
class JsonReaderTest extends JsonTestHelpers {
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/fake-state.json");
        try {
            Game state = reader.read();
            fail("IOException expected but not caught.");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    void testReaderEmptyGameBlocks() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyGameBlocks.json");
        try {
            Game state = reader.read();
            assertEquals(0, state.getBlocks().size());
            assertEquals(0, state.getInventory().size());
            assertEquals(3, state.getAvailableKeys().size());
        } catch (IOException e) {
            fail("Unexpected exception.");
        }
    }

    @Test
    void testReaderGeneralGame() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralGame.json");
        try {
            Game state = reader.read();
            checkCharacter(39, 16, 1, -2, 0, state.getCharacter());
            assertEquals(71, state.getBlocks().size());
            assertEquals(1, state.getInventory().size());
            assertEquals(2, state.getAvailableKeys().size());
            assertEquals(67, state.getTime());
            assertEquals(0, state.getInvulnerabilityEnd());
            assertEquals(95, state.getSpeedEnd());
            assertFalse(state.isEnded());
            checkPowerUp("invulnerability", 28, 14, "2", state.getInventory().get(0));
        } catch (IOException e) {
            fail("Unexpected exception.");
        }
    }
}