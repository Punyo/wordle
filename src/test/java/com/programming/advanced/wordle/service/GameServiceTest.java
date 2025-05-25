package com.programming.advanced.wordle.service;

import com.programming.advanced.wordle.dao.DatabaseInitializer;
import com.programming.advanced.wordle.model.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private static final Word TEST_WORD = new Word(1, "あいあーる", "あいあーる", 0, 0, 0);
    private static final int TEST_ATTEMPTS = 1;
    private static final int TEST_WORD_LENGTH = 5;

    @BeforeEach
    void setUp() {
        DatabaseInitializer.initializeDatabase();
        GameService.getInstance().startNewGame(TEST_WORD, TEST_ATTEMPTS, TEST_WORD_LENGTH);
    }

    @Test
    void testCheckWordLength() {
        assertThrows(IllegalArgumentException.class, () -> GameService.getInstance().checkWord(TEST_WORD.getNormalizedWord().substring(0, 3)));
    }

    @Test
    void testCheckWordCorrect() {
        WordBoxStatus[] result = GameService.getInstance().checkWord(TEST_WORD.getNormalizedWord());
        for (WordBoxStatus status : result) {
            assertEquals(WordBoxStatus.CORRECT, status);
        }
    }

    @Test
    void testCheckWordStatus() {
        String inputWord = "あいきゆー";
        WordBoxStatus[] result = GameService.getInstance().checkWord(inputWord);
        assertEquals(WordBoxStatus.CORRECT, result[0]);
        assertEquals(WordBoxStatus.CORRECT, result[1]);
        assertEquals(WordBoxStatus.NOT_IN_WORD, result[2]);
        assertEquals(WordBoxStatus.NOT_IN_WORD, result[3]);
        assertEquals(WordBoxStatus.IN_WORD, result[4]);
    }

    @Test
    void testCheckWordNoRemainingAttempts() {
        String inputWord = "あいきゆー";
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