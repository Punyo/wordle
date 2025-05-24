package com.programming.advanced.wordle.model;

// 単語モデル
public class Word {
    private int id; // 単語ID
    private String word; // 単語本体（5文字）
    private String normalizedWord; // 正規化された単語（小文字除去）
    private int playCount; // 出題された回数
    private int clearCount; // 正解された回数
    private int missCount; // 失敗された回数

    public Word() {
    }

    public Word(int id, String word, String normalizedWord, int playCount, int clearCount, int missCount) {
        this.id = id;
        this.word = word;
        this.normalizedWord = normalizedWord;
        this.playCount = playCount;
        this.clearCount = clearCount;
        this.missCount = missCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public String getNormalizedWord() {
        return normalizedWord;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public int getClearCount() {
        return clearCount;
    }

    public void setClearCount(int clearCount) {
        this.clearCount = clearCount;
    }

    public int getMissCount() {
        return missCount;
    }

    public void setMissCount(int missCount) {
        this.missCount = missCount;
    }
}
