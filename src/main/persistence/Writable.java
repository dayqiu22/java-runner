package persistence;

import org.json.JSONObject;

// Represents a class that can be converted to a JSONObject and written to a file
// Modelled after JsonSerializationDemo provided by CPSC 210 at UBC
public interface Writable {
    // EFFECTS: returns this as a JSON object
    JSONObject toJson();
}
