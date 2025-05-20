package com.programming.advanced.wordle.model;

// レコードの受け渡し用DTO
public class RecordDTO {
    private int wordId;        // 出題された単語のID
    private int answerCount;   // 回答回数
    private boolean clear;     // 正解したかどうか

    public RecordDTO() {
    }

    public RecordDTO(int wordId, int answerCount, boolean clear) {
        this.wordId = wordId;
        this.answerCount = answerCount;
        this.clear = clear;
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
} 