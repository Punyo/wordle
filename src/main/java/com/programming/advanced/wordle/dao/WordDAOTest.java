package com.programming.advanced.wordle.dao;

import com.programming.advanced.wordle.model.Word;
import java.sql.SQLException;

public class WordDAOTest {
    public static void main(String[] args) {
        try {
            // データベースの初期化
            System.out.println("データベースの初期化を開始します...");
            DatabaseInitializer.initializeDatabase();
            
            // WordDAOのテスト
            System.out.println("\nWordDAOのテストを開始します...");
            WordDAO wordDAO = new WordDAO();
            
            // ランダムな単語を5回取得してテスト
            System.out.println("\nランダムな単語を5回取得します：");
            for (int i = 0; i < 5; i++) {
                Word word = wordDAO.getRandomWord();
                System.out.printf("取得%d: ID=%d, 単語=%s, 出題回数=%d, 正解回数=%d, 失敗回数=%d%n",
                    i + 1,
                    word.getId(),
                    word.getWord(),
                    word.getPlayCount(),
                    word.getClearCount(),
                    word.getMissCount()
                );
            }
            
            System.out.println("\nテストが正常に完了しました。");
            
        } catch (Exception e) {
            System.err.println("テスト中にエラーが発生しました:");
            e.printStackTrace();
        }
    }
} 