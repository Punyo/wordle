package com.programming.advanced.wordle.dao;

import java.sql.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                    "    word_normalized TEXT NOT NULL UNIQUE CHECK(length(word_normalized) = 5)," +
                    "    playCount INTEGER NOT NULL DEFAULT 0," +
                    "    clearCount INTEGER NOT NULL DEFAULT 0," +
                    "    missCount INTEGER NOT NULL DEFAULT 0" +
                    ")";
                stmt.execute(createWordsTable);
                
                // word_normalizedカラムにインデックスを作成
                stmt.execute("CREATE INDEX IF NOT EXISTS idx_word_normalized ON words(word_normalized)");
                
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
        
        // words.txtから単語を読み込む
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    DatabaseInitializer.class.getResourceAsStream("/words.txt"), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.length() == 5) {
                    words.add(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("words.txtの読み込みに失敗しました", e);
        }
        
        // 単語の追加
        String insertSql = "INSERT INTO words (word, word_normalized) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSql)) {
            for (String word : words) {
                pstmt.setString(1, word);
                pstmt.setString(2, normalizeWord(word));
                pstmt.executeUpdate();
            }
            System.out.println(words.size() + "個の単語を追加しました");
        }
    }
    
    /**
     * 単語を正規化します（小文字を大文字に変換）
     * @param word 正規化する単語
     * @return 正規化された単語
     */
    private static String normalizeWord(String word) {
        StringBuilder normalized = new StringBuilder();
        for (char c : word.toCharArray()) {
            switch (c) {
                case 'ぁ': normalized.append('あ'); break;
                case 'ぃ': normalized.append('い'); break;
                case 'ぅ': normalized.append('う'); break;
                case 'ぇ': normalized.append('え'); break;
                case 'ぉ': normalized.append('お'); break;
                case 'っ': normalized.append('つ'); break;
                case 'ゃ': normalized.append('や'); break;
                case 'ゅ': normalized.append('ゆ'); break;
                case 'ょ': normalized.append('よ'); break;
                default: normalized.append(c);
            }
        }
        return normalized.toString();
    }
    
    // データベース接続を取得する
    // @return データベース接続
    // @throws SQLException データベース接続に失敗した場合
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
} 