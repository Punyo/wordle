package com.programming.advanced.wordle.dao;

import com.programming.advanced.wordle.model.RecordDTO;
import com.programming.advanced.wordle.model.Word;
import java.sql.*;

public class RecordDAOTest {
    public static void main(String[] args) {
        try {
            // データベースの初期化
            System.out.println("データベースの初期化を開始します...");
            DatabaseInitializer.initializeDatabase();
            
            // RecordDAOのテスト
            System.out.println("\nRecordDAOのテストを開始します...");
            RecordDAO recordDAO = new RecordDAO();
            WordDAO wordDAO = new WordDAO();
            
            // テスト用の単語を取得
            Word word = wordDAO.getRandomWord();
            int wordId = word.getId(); // 単語IDを保存
            System.out.printf("\nテスト対象の単語: ID=%d, 単語=%s%n", word.getId(), word.getWord());
            System.out.printf("現在の統計: プレイ回数=%d, 正解回数=%d, 失敗回数=%d%n",
                word.getPlayCount(), word.getClearCount(), word.getMissCount());
            
            // テスト用の記録を作成（クリア）
            RecordDTO record = new RecordDTO(wordId, 3, true); // 3回で正解
            
            // 記録を保存
            System.out.println("\n記録を保存します（クリア）：");
            int recordId = recordDAO.saveRecord(record);
            System.out.printf("保存された記録のID: %d%n", recordId);
            
            // 統計情報の更新を確認（同じ単語をIDで取得）
            word = wordDAO.getWordById(wordId);
            System.out.printf("\n更新後の統計: プレイ回数=%d, 正解回数=%d, 失敗回数=%d%n",
                word.getPlayCount(), word.getClearCount(), word.getMissCount());
            
            // 失敗の記録も作成
            RecordDTO failRecord = new RecordDTO(wordId, 6, false); // 6回で失敗
            
            // 失敗の記録を保存
            System.out.println("\n記録を保存します（失敗）：");
            recordId = recordDAO.saveRecord(failRecord);
            System.out.printf("保存された記録のID: %d%n", recordId);
            
            // 統計情報の更新を確認（同じ単語をIDで取得）
            word = wordDAO.getWordById(wordId);
            System.out.printf("\n更新後の統計: プレイ回数=%d, 正解回数=%d, 失敗回数=%d%n",
                word.getPlayCount(), word.getClearCount(), word.getMissCount());
            
            System.out.println("\nテストが正常に完了しました。");
            
        } catch (Exception e) {
            System.err.println("テスト中にエラーが発生しました:");
            e.printStackTrace();
        }
    }
}