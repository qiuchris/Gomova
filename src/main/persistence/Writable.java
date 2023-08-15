package persistence;

import org.json.JSONObject;

// interface for object to json

public interface Writable {
    // EFFECTS: returns this as a JSONObject
    JSONObject toJson();
}
