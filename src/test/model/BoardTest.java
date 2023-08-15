package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoardTest {
    Board board;
    Stone stone1;
    Stone stone2;
    Stone stone3;
    Stone stone4;

    @BeforeEach
    void runBefore() {
        board = new Board(15);
        stone1 = new Stone(Colour.BLACK, 1, 2);
        stone2 = new Stone(Colour.WHITE, 3, 4);
        stone3 = new Stone(Colour.BLACK, 3, 4);
        stone4 = new Stone(Colour.WHITE, 5, 6);
    }

    @Test
    void testConstructor1() {
        assertEquals(15, board.getBoardSize());
        assertEquals(0, board.getMoveList().size());
        assertEquals(225, board.getStones().size());
        assertEquals(Colour.BLACK, board.getCurrentTurn());

        board = new Board(20);
        assertEquals(20, board.getBoardSize());
        assertEquals(0, board.getMoveList().size());
        assertEquals(400, board.getStones().size());
        assertEquals(Colour.BLACK, board.getCurrentTurn());

        board = new Board(5);
        assertEquals(5, board.getBoardSize());
        assertEquals(0, board.getMoveList().size());
        assertEquals(25, board.getStones().size());
        assertEquals(Colour.BLACK, board.getCurrentTurn());
    }

    @Test
    void testAddStone1() {
        board.addStone(stone1);
        assertEquals(1, board.getMoveList().size());
        assertEquals(Colour.BLACK, board.getStones().get(31).getColour());

        board.addStone(stone2);
        assertEquals(2, board.getMoveList().size());
        assertEquals(Colour.WHITE, board.getStones().get(63).getColour());

        board.addStone(stone3);
        assertEquals(2, board.getMoveList().size());
        assertEquals(Colour.WHITE, board.getStones().get(63).getColour());
    }

    @Test
    void testAddStone2() {
        board = new Board(20);

        board.addStone(stone1);
        assertEquals(1, board.getMoveList().size());
        assertEquals(Colour.BLACK, board.getStones().get(41).getColour());

        board.addStone(stone2);
        assertEquals(2, board.getMoveList().size());
        assertEquals(Colour.WHITE, board.getStones().get(83).getColour());

        board.addStone(stone3);
        assertEquals(2, board.getMoveList().size());
        assertEquals(Colour.WHITE, board.getStones().get(83).getColour());
    }

    @Test
    void testAddStone3() {
        board.addStone(stone1);
        assertEquals(1, board.getMoveList().size());
        assertEquals(Colour.BLACK, board.getStones().get(31).getColour());

        board.addStone(stone2);
        assertEquals(2, board.getMoveList().size());
        assertEquals(Colour.WHITE, board.getStones().get(63).getColour());

        board.addStone(stone4);
        assertEquals(2, board.getMoveList().size());
        assertEquals(Colour.BLACK, board.getCurrentTurn());
    }

    @Test
    void testAdvanceCurrentTurn() {
        assertEquals(Colour.BLACK, board.getCurrentTurn());
        board.changeCurrentTurn();
        assertEquals(Colour.WHITE, board.getCurrentTurn());
        board.changeCurrentTurn();
        assertEquals(Colour.BLACK, board.getCurrentTurn());
    }

    @Test
    void testUndoMove() {
        assertEquals(0, board.getMoveList().size());
        board.addStone(stone1);
        assertEquals(1, board.getMoveList().size());
        board.addStone(stone2);
        assertEquals(2, board.getMoveList().size());

        board.undoMove();
        assertEquals(1, board.getMoveList().size());
        board.undoMove();
        assertEquals(0, board.getMoveList().size());
        board.undoMove();
        assertEquals(0, board.getMoveList().size());
    }
}