package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StoneTest {
    Stone stone1;
    Stone stone2;
    Stone stone3;

    @BeforeEach
    void runBefore() {
        stone1 = new Stone(Colour.BLACK, 1, 2);
        stone2 = new Stone(Colour.WHITE, 3, 4);
        stone3 = new Stone(Colour.BLACK, 3, 4);
    }

    @Test
    void testConstructor() {
        assertEquals(Colour.BLACK, stone1.getColour());
        assertEquals(1, stone1.getX());
        assertEquals(2, stone1.getY());

        assertEquals(Colour.WHITE, stone2.getColour());
        assertEquals(3, stone2.getX());
        assertEquals(4, stone2.getY());

        assertEquals(Colour.BLACK, stone3.getColour());
        assertEquals(3, stone3.getX());
        assertEquals(4, stone3.getY());
    }
}
