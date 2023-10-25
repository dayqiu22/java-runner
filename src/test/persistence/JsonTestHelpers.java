package persistence;

import model.PowerUp;
import model.Character;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTestHelpers {
    protected void checkPowerUp(String name, int posX, int posY, String key, PowerUp pu) {
        assertEquals(name, pu.getName());
        assertEquals(posX, pu.getPosition().getPositionX());
        assertEquals(posY, pu.getPosition().getPositionY());
        assertEquals(key, pu.getKeyAssignment());
    }

    protected void checkCharacter(int posX, int posY, int velX, int multiplierX, int velY, Character character) {
        assertEquals(posX, character.getPosition().getPositionX());
        assertEquals(posY, character.getPosition().getPositionY());
        assertEquals(velX, character.getVelocityX());
        assertEquals(multiplierX, character.getVelocityXMultiplier());
        assertEquals(velY, character.getVelocityY());
    }
}
