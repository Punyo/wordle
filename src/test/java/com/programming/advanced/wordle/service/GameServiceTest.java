package com.programming.advanced.wordle.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private static final String TEST_WORD = "あいうえお";
    private static final int TEST_ATTEMPTS = 1;
    private static final int TEST_WORD_LENGTH = 5;

    @BeforeEach
    void setUp() {
        GameService.getInstance().startNewGame(TEST_WORD, TEST_ATTEMPTS, TEST_WORD_LENGTH);
    }

    @Test
    void testCheckWordLength() {
        assertThrows(IllegalArgumentException.class, () -> GameService.getInstance().checkWord("あいうえ"));
    }

    @Test
    void testCheckWordCorrect() {
        String inputWord = "あいうえお";
        WordBoxStatus[] result = GameService.getInstance().checkWord(inputWord);
        for (WordBoxStatus status : result) {
            assertEquals(WordBoxStatus.CORRECT, status);
        }
    }

    @Test
    void testCheckWordInWord() {
        String inputWord = "あいうおえ";
        WordBoxStatus[] result = GameService.getInstance().checkWord(inputWord);
        assertEquals(WordBoxStatus.CORRECT, result[0]);
        assertEquals(WordBoxStatus.CORRECT, result[1]);
        assertEquals(WordBoxStatus.CORRECT, result[2]);
        assertEquals(WordBoxStatus.IN_WORD, result[3]);
        assertEquals(WordBoxStatus.IN_WORD, result[4]);
    }

    @Test
    void testCheckWordNotInWord() {
        String inputWord = "かきくけこ";
        WordBoxStatus[] result = GameService.getInstance().checkWord(inputWord);
        for (WordBoxStatus status : result) {
            assertEquals(WordBoxStatus.NOT_IN_WORD, status);
        }
    }

    @Test
    void testCheckWordNoRemainingAttempts() {
        String inputWord = "かきくけこ";
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