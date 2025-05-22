package com.programming.advanced.wordle.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameServiceNotInitializedTest {
    @Test
    void testCheckWordNotInitialized() {
        GameService gameService = GameService.getInstance();
        gameService.resetGame();
        String inputWord = "おほしさま";
        assertThrows(IllegalStateException.class, () -> gameService.checkWord(inputWord));
    }
}
