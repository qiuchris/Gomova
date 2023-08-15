package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Collection;

// class for a Board which stores multiple Stone objects

public class Board implements Writable {
    private int boardSize;
    private ArrayList<Stone> stones;
    private ArrayList<Stone> moveList;
    private Colour currentTurn;

    // REQUIRES: 5 <= size <= 20
    // EFFECTS: constructs a square Board with specified boardSize, with no Stone objects,
    //          no moves played, and with black going first
    public Board(int size) {
        boardSize = size;
        stones = new ArrayList<>(boardSize * boardSize);
        for (int i = 0; i < (boardSize * boardSize); i++) {
            stones.add(null);
        }
        moveList = new ArrayList<>();
        currentTurn = Colour.BLACK;
        String str = "Instantiated new Board with size " + size;
        EventLog.getInstance().logEvent(new Event(str));
    }

    // REQUIRES: stone.getX() < boardSize, stone.getY() < boardSize
    // MODIFIES: this
    // EFFECTS: adds a given Stone to the board, changes turn colour, returns true if stone is placed
    public boolean addStone(Stone stone) {
        int index = stone.getY() * boardSize + stone.getX();
        if (stones.get(index) == null && stone.getColour() == currentTurn) {
            stones.set(index, stone);
            moveList.add(stone);
            changeCurrentTurn();
            String str = "Placed " + stone.getColour() + " stone at " + stone.getX() + ", " + stone.getY();
            EventLog.getInstance().logEvent(new Event(str));
            return true;
        } else {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: changes the current turn to the next colour
    //          (black -> white, white -> black)
    public void changeCurrentTurn() {
        if (currentTurn == Colour.BLACK) {
            currentTurn = Colour.WHITE;
        } else {
            currentTurn = Colour.BLACK;
        }
    }

    // MODIFIES: this
    // EFFECTS: undo the last move, unless no moves have been played
    public void undoMove() {
        int lastMoveIndex = moveList.size() - 1;
        if (lastMoveIndex >= 0) {
            Stone stone = moveList.get(lastMoveIndex);
            moveList.remove(lastMoveIndex);
            stones.set(stone.getY() * boardSize + stone.getX(), null);
            changeCurrentTurn();
            String str = "Removed " + stone.getColour() + " stone from " + stone.getX() + ", " + stone.getY();
            EventLog.getInstance().logEvent(new Event(str));
        }
    }

    // getters
    public ArrayList<Stone> getStones() {
        return stones;
    }

    public Colour getCurrentTurn() {
        return currentTurn;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public ArrayList<Stone> getMoveList() {
        return moveList;
    }

    // EFFECTS: returns JSONObject representation of the current Board
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("boardSize", boardSize);
        json.put("stones", stonesToJson());
        json.put("moveList", moveListToJson());
        json.put("currentTurn", currentTurn);
        return json;
    }

    // EFFECTS: returns the JSONArray representation of the stones list
    private JSONArray stonesToJson() {
        JSONArray stonesArray = new JSONArray();
        for (Stone s : stones) {
            if (s == null) {
                stonesArray.put((Collection<?>) null);
            } else {
                stonesArray.put(s.toJson());
            }
        }
        return stonesArray;
    }

    // EFFECTS: returns the JSONArray representation of the move list
    private JSONArray moveListToJson() {
        JSONArray moveListArray = new JSONArray();
        for (Stone s : moveList) {
            moveListArray.put(s.toJson());
        }
        return moveListArray;
    }
}