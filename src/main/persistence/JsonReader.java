package persistence;

import model.Character;
import model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads workroom from JSON data stored in file
// Modelled after JsonSerializationDemo provided by CPSC 210 at UBC
public class JsonReader {
    private final String source;

    // EFFECTS: constructs reader to read from the source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads game state from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Game read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonGame = new JSONObject(jsonData);
        return parseGame(jsonGame);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses the game state from a JSON object and returns it
    private Game parseGame(JSONObject jsonGame) {
        int maxX = jsonGame.getInt("maxX");
        int maxY = jsonGame.getInt("maxY");
        Game state = new Game(maxX, maxY);
        state.setCharacter(parseCharacter(jsonGame.getJSONObject("character")));
        restoreBlocks(state, jsonGame);
        restoreInventory(state, jsonGame);
        state.setTime(jsonGame.getInt("time"));
        state.setInvulnerabilityEnd(jsonGame.getInt("invulnerabilityEnd"));
        state.setSpeedEnd(jsonGame.getInt("speedEnd"));
        state.setEnded(jsonGame.getBoolean("ended"));
        return state;
    }

    // EFFECTS: reconstructs the player character based on JSON information about the character
    private Character parseCharacter(JSONObject jsonCharacter) {
        int x = jsonCharacter.getInt("positionX");
        int y = jsonCharacter.getInt("positionY");
        Character character = new Character(x, y);
        character.setVelocityX(jsonCharacter.getInt("velocityX"));
        character.setVelocityXMultiplier(jsonCharacter.getInt("velocityXMultiplier"));
        character.setVelocityY(jsonCharacter.getInt("velocityY"));

        return character;
    }

    // EFFECTS: restores blocks in a game based on JSON information saved for
    // the list of game blocks
    private void restoreBlocks(Game state, JSONObject jsonGame) {
        JSONArray jsonBlocks = jsonGame.getJSONArray("blocks");
        for (Object json : jsonBlocks) {
            JSONObject jsonBlock = (JSONObject) json;
            int x = jsonBlock.getInt("positionX");
            int y = jsonBlock.getInt("positionY");
            if (jsonBlock.getString("name").equals("block")) {
                state.addBlock(new Block(x, y));
            } else if (jsonBlock.getString("name").equals("hazard")) {
                state.addBlock(new Hazard(x, y));
            } else {
                String name = jsonBlock.getString("name");
                state.addBlock(new PowerUp(x, y, name));
            }
        }
    }

    // EFFECTS: restores power-ups in the inventory based on JSON information
    // saved for the inventory of power-ups
    private void restoreInventory(Game state, JSONObject jsonGame) {
        JSONArray jsonInventory = jsonGame.getJSONArray("inventory");
        for (Object json : jsonInventory) {
            JSONObject jsonPowerUp = (JSONObject) json;
            String name = jsonPowerUp.getString("name");
            int x = jsonPowerUp.getInt("positionX");
            int y = jsonPowerUp.getInt("positionY");
            PowerUp pu = new PowerUp(x, y, name);
            pu.setKeyAssignment(jsonPowerUp.getString("keyAssignment"));
            state.removeAvailableKey(jsonPowerUp.getString("keyAssignment"));
            state.addPowerUpToInventory(pu);
        }
    }
}
