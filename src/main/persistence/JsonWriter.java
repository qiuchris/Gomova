package persistence;

import model.Board;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

// class for writing a Board to a JSON file

public class JsonWriter {
    private static final int INDENT_FACTOR = 4;

    private String path;
    private PrintWriter printWriter;

    // EFFECTS: creates a JsonWriter object with given path
    public JsonWriter(String path) {
        this.path = path;
    }

    // MODIFIES: this
    // EFFECTS: opens file for writing, throws FileNotFoundException
    //          if file cannot be opened
    public void open() throws FileNotFoundException {
        printWriter = new PrintWriter(path);
    }

    // MODIFIES: this
    // EFFECTS: writes the current Board to this path
    public void write(Board b) {
        JSONObject json = b.toJson();
        printWriter.print(json.toString(INDENT_FACTOR));
    }

    // MODIFIES: this
    // EFFECTS: closes the printWriter
    public void close() {
        printWriter.close();
    }
}
