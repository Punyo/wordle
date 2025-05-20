package com.programming.advanced.wordle.dao;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

// データベースの初期化を行うクラス
public class DatabaseInitializer {
    private static final String DB_URL = "jdbc:sqlite:wordle.db";
    
    // 初期データとして使用する単語リスト
    private static final List<String> INITIAL_WORDS = Arrays.asList(
        "ぷろぐらま", "はなみずき", "こんぱいら", "くりすます", "なまびーる", 
        "おとしだま", "ふきのとう", "まんじゅう", "こかんせつ", "かれんだー", 
        "とりきぞく", "おほしさま", "ちゃーはん", "れもねーど", "かめらまん"
    );
    
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
                
                // 初期データの追加
                insertInitialWords(conn);
                
                System.out.println("データベースの初期化が完了しました");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBCドライバが見つかりません", e);
        } catch (SQLException e) {
            throw new RuntimeException("データベースの初期化に失敗しました", e);
        }
    }
    
    // 初期データとして単語を追加する
    private static void insertInitialWords(Connection conn) throws SQLException {
        // 既存のデータを確認
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM words")) {
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("既に単語データが存在するため、初期データの追加をスキップします");
                return;
            }
        }
        
        // 単語の追加
        String insertSql = "INSERT INTO words (word) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            for (String word : INITIAL_WORDS) {
                pstmt.setString(1, word);
                pstmt.executeUpdate();
            }
            System.out.println(INITIAL_WORDS.size() + "個の単語を追加しました");
        }
    }
    
    // データベース接続を取得する
    // @return データベース接続
    // @throws SQLException データベース接続に失敗した場合
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
} 