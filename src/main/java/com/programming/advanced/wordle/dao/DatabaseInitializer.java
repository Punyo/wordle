package com.programming.advanced.wordle.dao;

import java.sql.*;

// データベースの初期化を行うクラス
public class DatabaseInitializer {
    private static final String DB_URL = "jdbc:sqlite:wordle.db";
    
    // データベースを初期化する
    public static void initializeDatabase() {
        try {
            // SQLite JDBCドライバを登録
            Class.forName("org.sqlite.JDBC");
            
            // データベースに接続
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement()) {
                
                // Wordsテーブルの作成
                String createWordsTable = 
                    "CREATE TABLE IF NOT EXISTS words (" +
                    "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "    word TEXT NOT NULL UNIQUE CHECK(length(word) = 5)," +
                    "    playCount INTEGER NOT NULL DEFAULT 0," +
                    "    clearCount INTEGER NOT NULL DEFAULT 0," +
                    "    missCount INTEGER NOT NULL DEFAULT 0" +
                    ")";
                stmt.execute(createWordsTable);
                
                // Recordsテーブルの作成
                String createRecordsTable = 
                    "CREATE TABLE IF NOT EXISTS records (" +
                    "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "    wordId INTEGER NOT NULL," +
                    "    answerCount INTEGER NOT NULL," +
                    "    clear BOOLEAN NOT NULL," +
                    "    date DATE NOT NULL," +
                    "    FOREIGN KEY (wordId) REFERENCES words(id)" +
                    ")";
                stmt.execute(createRecordsTable);
                
                // Inputsテーブルの作成
                String createInputsTable = 
                    "CREATE TABLE IF NOT EXISTS inputs (" +
                    "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "    recordId INTEGER NOT NULL," +
                    "    wordId INTEGER," +
                    "    word TEXT NOT NULL CHECK(length(word) = 5)," +
                    "    placeCorrectCount INTEGER NOT NULL CHECK(placeCorrectCount BETWEEN 0 AND 5)," +
                    "    charCorrectCount INTEGER NOT NULL CHECK(charCorrectCount BETWEEN 0 AND 5)," +
                    "    FOREIGN KEY (recordId) REFERENCES records(id)," +
                    "    FOREIGN KEY (wordId) REFERENCES words(id)" +
                    ")";
                stmt.execute(createInputsTable);
                
                System.out.println("データベースの初期化が完了しました");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBCドライバが見つかりません", e);
        } catch (SQLException e) {
            throw new RuntimeException("データベースの初期化に失敗しました", e);
        }
    }
    
    // データベース接続を取得する
    // @return データベース接続
    // @throws SQLException データベース接続に失敗した場合
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
} 