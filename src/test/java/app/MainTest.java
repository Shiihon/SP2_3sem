package app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void add() {
        Main main = new Main();
        assertEquals(5, main.add(2, 3));
    }
}