package com.programming.advanced.wordle.service;

import com.programming.advanced.wordle.dao.DatabaseInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private static final String TEST_WORD = "おとしだま";
    private static final int TEST_ATTEMPTS = 1;
    private static final int TEST_WORD_LENGTH = 5;

    @BeforeEach
    void setUp() {
        DatabaseInitializer.initializeDatabase();
        GameService.getInstance().startNewGame(TEST_WORD, TEST_ATTEMPTS, TEST_WORD_LENGTH);
    }

    @Test
    void testCheckWordLength() {
        assertThrows(IllegalArgumentException.class, () -> GameService.getInstance().checkWord(TEST_WORD.substring(0, 3)));
    }

    @Test
    void testCheckWordCorrect() {
        WordBoxStatus[] result = GameService.getInstance().checkWord(TEST_WORD);
        for (WordBoxStatus status : result) {
            assertEquals(WordBoxStatus.CORRECT, status);
        }
    }

    @Test
    void testCheckWordStatus() {
        String inputWord = "おほしさま";
        WordBoxStatus[] result = GameService.getInstance().checkWord(inputWord);
        assertEquals(WordBoxStatus.CORRECT, result[0]);
        assertEquals(WordBoxStatus.NOT_IN_WORD, result[1]);
        assertEquals(WordBoxStatus.CORRECT, result[2]);
        assertEquals(WordBoxStatus.NOT_IN_WORD, result[3]);
        assertEquals(WordBoxStatus.CORRECT, result[4]);
    }

    @Test
    void testCheckWordNoRemainingAttempts() {
        String inputWord = "おほしさま";
        GameService.getInstance().checkWord(inputWord);
        assertThrows(IllegalStateException.class, () -> GameService.getInstance().checkWord(inputWord));
    }

    @Test
    void testGetters() {
        assertEquals(TEST_WORD, GameService.getInstance().getCurrentWord());
        assertEquals(TEST_ATTEMPTS, GameService.getInstance().getRemainingAttempts());
        assertEquals(TEST_WORD_LENGTH, GameService.getInstance().getWordLength());
    }
}