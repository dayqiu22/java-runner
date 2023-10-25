package persistence;

import model.Game;
import org.json.JSONObject;
import java.io.*;

// Represents a writer that writes JSON representation of workroom to file
// Modelled after JsonSerializationDemo provided by CPSC 210 at UBC
public class JsonWriter {
    private static final int INDENT = 4;
    private final String destination;
    private PrintWriter writer;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of the game state to destination file
    public void write(Game state) {
        JSONObject json = state.toJson();
        saveToFile(json.toString(INDENT));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
