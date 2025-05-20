package com.programming.advanced.wordle.service;


import com.programming.advanced.wordle.dao.WordDAO;

import java.sql.SQLException;

// ゲームサービス
public class GameService {
    private String currentWord;

    private int remainingAttempts;
    private int wordLength; // 単語の長さ

    private boolean isInitialized = false;

    private final WordDAO WORD_DAO = new WordDAO();

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
        try {
            if (WORD_DAO.getWordIdByWord(inputWord) == -1) {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error occurred while checking the word.", e);
        }
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
        remainingAttempts--;
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
