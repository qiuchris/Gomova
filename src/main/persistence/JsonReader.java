package persistence;

import model.Board;
import model.Colour;
import model.Stone;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// class for reading a Board from a JSON file

public class JsonReader {
    private String path;

    // EFFECTS: creates a new JsonReader object with given path
    public JsonReader(String path) {
        this.path = path;
    }

    // EFFECTS: returns the board from the board.json file at the given path
    public Board read() throws IOException {
        Path filePath = Paths.get(path);
        String data = new String(Files.readAllBytes(filePath));
        JSONObject json = new JSONObject(data);

        JSONArray moveListArray = json.getJSONArray("moveList");
        Board b = new Board(json.getInt("boardSize"));
        for (Object s : moveListArray) {
            JSONObject stone = (JSONObject) s;
            Colour colour = Colour.valueOf(stone.getString("colour"));
            int positionX = stone.getInt("positionX");
            int positionY = stone.getInt("positionY");
            b.addStone(new Stone(colour, positionX, positionY));
        }
        return b;
    }
}
