package model;

// class for single game piece (Stone) with a colour and a position

import org.json.JSONObject;
import persistence.Writable;

public class Stone implements Writable {
    private final Colour colour;
    private final int positionX;
    private final int positionY;

    // REQUIRES: 0 <= x < board.getBoardSize(), 0 <= y < board.getBoardSize()
    // EFFECTS: creates a Stone object with given colour and position
    public Stone(Colour colour, int x, int y) {
        this.colour = colour;
        this.positionX = x;
        this.positionY = y;
    }

    // getters
    public Colour getColour() {
        return colour;
    }

    public int getX() {
        return positionX;
    }

    public int getY() {
        return positionY;
    }

    // EFFECTS: returns Stone object as a JSONObject
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("colour", colour);
        json.put("positionX", positionX);
        json.put("positionY", positionY);
        return json;
    }
}
