package persistence;

import model.Board;
import model.Colour;
import model.Stone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest {
    Board b1;
    Board b2;
    Stone s1;
    Stone s2;
    Stone s3;

    @BeforeEach
    void runBefore() {
        b1 = new Board(15);
        b2 = new Board(20);
        s1 = new Stone(Colour.BLACK, 4,2);
        s2 = new Stone(Colour.WHITE, 8,8);
        s3 = new Stone(Colour.BLACK, 7,2);

        b2.addStone(s1);
        b2.addStone(s2);
        b2.addStone(s3);
    }

    @Test
    void testInvalidFile() {
        try {
            JsonReader reader = new JsonReader("./data/invalidfilename.json");
            reader.read();
            fail("expected IOException");
        } catch (IOException e) {
            // expected
        }
    }
    
    @Test
    void testReadEmptyBoard() {
        try {
            JsonReader reader = new JsonReader("./data/testJsonReader1.json");
            Board b = reader.read();
            assertEquals(15, b.getBoardSize());
            assertEquals(225, b.getStones().size());
            assertEquals(0, b.getMoveList().size());
            assertEquals(Colour.BLACK, b.getCurrentTurn());
        } catch (IOException e) {
            fail("unexpected IOException");
        }
    }

    @Test
    void testReadNonEmptyBoard() {
        try {
            JsonReader reader = new JsonReader("./data/testJsonReader2.json");
            Board b = reader.read();
            assertEquals(20, b.getBoardSize());
            assertEquals(400, b.getStones().size());
            assertEquals(3, b.getMoveList().size());
            assertEquals(Colour.WHITE, b.getCurrentTurn());
        } catch (IOException e) {
            fail("unexpected IOException");
        }
    }
}
