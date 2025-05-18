package com.programming.advanced.wordle.service;

import com.programming.advanced.wordle.model.Word;

// ゲームサービス
public class GameService {
    private String currentWord;

    private int remainingAttempts;
    private int wordLength; // 単語の長さ

    private boolean isInitialized = false;

    private static final GameService instance = new GameService();

    private GameService() {
    }

    public void startNewGame(String word, int attempts, int wordLength) {
        this.currentWord = word;
        this.remainingAttempts = attempts; // 6回の試行
        this.wordLength = wordLength; // 単語の長さ
        this.isInitialized = true;
    }

    public WordBoxStatus[] checkWord(String inputWord) {
        if (!isInitialized) {
            throw new IllegalStateException("Game not initialized.");
        }
        WordBoxStatus[] status = new WordBoxStatus[wordLength];
        if (inputWord.length() != wordLength) {
            throw new IllegalArgumentException("Input word length does not match the expected length.");
        }
        if (remainingAttempts <= 0) {
            throw new IllegalStateException("No remaining attempts.");
        }
        for (int i = 0; i < wordLength; i++) {
            if (inputWord.charAt(i) == currentWord.charAt(i)) {
                status[i] = WordBoxStatus.CORRECT;
            } else if (currentWord.contains(String.valueOf(inputWord.charAt(i)))) {
                status[i] = WordBoxStatus.IN_WORD;
            } else {
                status[i] = WordBoxStatus.NOT_IN_WORD;
            }
        }
        return status;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public int getRemainingAttempts() {
        return remainingAttempts;
    }

    public int getWordLength() {
        return wordLength;
    }

    public static GameService getInstance() {
        return instance;
    }
}
