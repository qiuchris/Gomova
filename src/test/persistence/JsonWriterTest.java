package persistence;

import model.Board;
import model.Colour;
import model.Stone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {
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
            JsonWriter writer = new JsonWriter("./data/invalid\nfile%nam:e.json");
            writer.open();
            fail("expected FileNotFoundException");
        } catch (FileNotFoundException e) {
            // expected
        }
    }

    @Test
    void testWriteEmptyBoard() {
        try {
            JsonWriter writer = new JsonWriter("./data/testJsonWriter.json");
            writer.open();
            writer.write(b1);
            writer.close();

            JsonReader reader = new JsonReader("./data/testJsonWriter.json");
            Board b = reader.read();
            assertEquals(15, b.getBoardSize());
            assertEquals(225, b.getStones().size());
            assertEquals(0, b.getMoveList().size());
            assertEquals(Colour.BLACK, b.getCurrentTurn());
        } catch (FileNotFoundException e) {
            fail("unexpected FileNotFoundException");
        } catch (IOException e) {
            fail("unexpected IOException");
        }
    }

    @Test
    void testWriteNonEmptyBoard() {
        try {
            JsonWriter writer = new JsonWriter("./data/testJsonWriter.json");
            writer.open();
            writer.write(b2);
            writer.close();

            JsonReader reader = new JsonReader("./data/testJsonWriter.json");
            Board b = reader.read();
            assertEquals(20, b.getBoardSize());
            assertEquals(400, b.getStones().size());
            assertEquals(3, b.getMoveList().size());
            assertEquals(Colour.WHITE, b.getCurrentTurn());
        } catch (FileNotFoundException e) {
            fail("unexpected FileNotFoundException");
        } catch (IOException e) {
            fail("unexpected IOException");
        }
    }
}
