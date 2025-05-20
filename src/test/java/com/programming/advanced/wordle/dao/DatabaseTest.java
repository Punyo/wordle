package com.programming.advanced.wordle.dao;

import com.programming.advanced.wordle.model.Record;
import com.programming.advanced.wordle.model.RecordDTO;
import com.programming.advanced.wordle.model.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

// データベースのテストを行うクラス
public class DatabaseTest {
    private RecordDAO recordDAO;
    private WordDAO wordDAO;
    private Word testWord;
    
    @BeforeEach
    void setUp() {
        try {
            // データベースの初期化
            DatabaseInitializer.initializeDatabase();
            recordDAO = new RecordDAO();
            wordDAO = new WordDAO();
            testWord = wordDAO.getRandomWord();
        } catch (Exception e) {
            e.printStackTrace();
            fail("setUpで例外発生: " + e.getMessage());
        }
    }
    
    @Test
    void testDatabaseConnection() throws SQLException {
        try (Connection conn = DatabaseInitializer.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // テーブル一覧の取得
            try (ResultSet rs = stmt.executeQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%'")) {
                List<String> tables = List.of("words", "records", "inputs");
                while (rs.next()) {
                    String tableName = rs.getString("name");
                    assertTrue(tables.contains(tableName), 
                        "予期しないテーブルが存在します: " + tableName);
                }
            }
            
            // 各テーブルの構造確認
            verifyTableStructure(stmt, "words", List.of(
                "id INTEGER", "word TEXT", "playCount INTEGER", 
                "clearCount INTEGER", "missCount INTEGER"));
            
            verifyTableStructure(stmt, "records", List.of(
                "id INTEGER", "wordId INTEGER", "answerCount INTEGER", 
                "clear BOOLEAN", "date DATE"));
            
            verifyTableStructure(stmt, "inputs", List.of(
                "id INTEGER", "recordId INTEGER", "wordId INTEGER", 
                "word TEXT", "placeCorrectCount INTEGER", "charCorrectCount INTEGER"));
        }
    }
    
    private void verifyTableStructure(Statement stmt, String tableName, List<String> expectedColumns) 
            throws SQLException {
        try (ResultSet rs = stmt.executeQuery("PRAGMA table_info(" + tableName + ")")) {
            int columnCount = 0;
            while (rs.next()) {
                String columnDef = rs.getString("name") + " " + rs.getString("type");
                assertTrue(expectedColumns.stream()
                    .anyMatch(expected -> columnDef.contains(expected)),
                    "予期しないカラム定義: " + columnDef);
                columnCount++;
            }
            assertEquals(expectedColumns.size(), columnCount, 
                tableName + "テーブルのカラム数が一致しません");
        }
    }
    
    @Test
    void testSaveRecordAndGetAllRecords() throws SQLException {
        // 初期状態の確認
        List<Record> initialRecords = recordDAO.getAllRecords();
        int initialSize = initialRecords.size();
        
        // クリアの記録を保存
        RecordDTO clearRecord = new RecordDTO(testWord.getId(), 3, true);
        int clearRecordId = recordDAO.saveRecord(clearRecord);
        assertTrue(clearRecordId > 0, "クリア記録の保存に失敗しました");
        
        // 失敗の記録を保存
        RecordDTO failRecord = new RecordDTO(testWord.getId(), 6, false);
        int failRecordId = recordDAO.saveRecord(failRecord);
        assertTrue(failRecordId > 0, "失敗記録の保存に失敗しました");
        
        // 全レコード取得のテスト
        List<Record> records = recordDAO.getAllRecords();
        assertEquals(initialSize + 2, records.size(), "レコード数が期待値と異なります");
        
        // 最新のレコードが失敗記録であることを確認
        Record latestRecord = records.get(0);
        assertEquals(failRecordId, latestRecord.getId(), "最新のレコードIDが一致しません");
        assertEquals(testWord.getWord(), latestRecord.getWord(), "単語が一致しません");
        assertEquals(6, latestRecord.getAnswerCount(), "回答回数が一致しません");
        assertFalse(latestRecord.isClear(), "クリア状態が一致しません");
        
        // 2番目のレコードがクリア記録であることを確認
        Record secondRecord = records.get(1);
        assertEquals(clearRecordId, secondRecord.getId(), "2番目のレコードIDが一致しません");
        assertEquals(testWord.getWord(), secondRecord.getWord(), "単語が一致しません");
        assertEquals(3, secondRecord.getAnswerCount(), "回答回数が一致しません");
        assertTrue(secondRecord.isClear(), "クリア状態が一致しません");
    }
    
    @Test
    void testWordStatisticsUpdate() throws SQLException {
        // 初期状態の確認
        Word initialWord = wordDAO.getWordById(testWord.getId());
        int initialPlayCount = initialWord.getPlayCount();
        int initialClearCount = initialWord.getClearCount();
        int initialMissCount = initialWord.getMissCount();
        
        // クリアの記録を保存
        RecordDTO clearRecord = new RecordDTO(testWord.getId(), 3, true);
        recordDAO.saveRecord(clearRecord);
        
        // 失敗の記録を保存
        RecordDTO failRecord = new RecordDTO(testWord.getId(), 6, false);
        recordDAO.saveRecord(failRecord);
        
        // 統計情報の更新を確認
        Word updatedWord = wordDAO.getWordById(testWord.getId());
        assertEquals(initialPlayCount + 2, updatedWord.getPlayCount(), "プレイ回数の更新が正しくありません");
        assertEquals(initialClearCount + 1, updatedWord.getClearCount(), "クリア回数の更新が正しくありません");
        assertEquals(initialMissCount + 1, updatedWord.getMissCount(), "失敗回数の更新が正しくありません");
    }
} 