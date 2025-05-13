package com.programming.advanced.wordle.model;

import java.time.LocalDate;

// 記録モデル
public class Record {
    private int id; // レコードID
    private int wordId; // 出題語のID
    private int answerCount; // 回答回数（最大6）
    private boolean clear; // 正解したか否か
    private LocalDate date; // プレイ日付

    public Record() {
    }

    public Record(int id, int wordId, int answerCount, boolean clear, LocalDate date) {
        this.id = id;
        this.wordId = wordId;
        this.answerCount = answerCount;
        this.clear = clear;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public int getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public boolean isClear() {
        return clear;
    }

    public void setClear(boolean clear) {
        this.clear = clear;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
