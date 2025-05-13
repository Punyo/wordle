package com.programming.advanced.wordle.model;

// 入力モデル
public class Input {
    private int id; // 入力ID
    private int recordId; // 該当プレイ記録ID
    private Integer wordId; // 入力語ID（null可）
    private String word; // 入力語文字列
    private int placeCorrectCount; // 位置も文字も正しい数
    private int charCorrectCount; // 文字は正しいが位置は異なる数

    public Input() {
    }

    public Input(int id, int recordId, Integer wordId, String word, int placeCorrectCount, int charCorrectCount) {
        this.id = id;
        this.recordId = recordId;
        this.wordId = wordId;
        this.word = word;
        this.placeCorrectCount = placeCorrectCount;
        this.charCorrectCount = charCorrectCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public Integer getWordId() {
        return wordId;
    }

    public void setWordId(Integer wordId) {
        this.wordId = wordId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getPlaceCorrectCount() {
        return placeCorrectCount;
    }

    public void setPlaceCorrectCount(int placeCorrectCount) {
        this.placeCorrectCount = placeCorrectCount;
    }

    public int getCharCorrectCount() {
        return charCorrectCount;
    }

    public void setCharCorrectCount(int charCorrectCount) {
        this.charCorrectCount = charCorrectCount;
    }
}
