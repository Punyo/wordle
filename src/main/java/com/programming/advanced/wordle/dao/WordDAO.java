package com.programming.advanced.wordle.dao;

import com.programming.advanced.wordle.model.Word;
import java.sql.*;

// 単語データアクセスオブジェクト
public class WordDAO {
    // TODO: 単語データの取得・保存
    
    /**
     * データベースからランダムな単語を1つ取得します
     * @return ランダムな単語
     * @throws SQLException データベースアクセスエラーが発生した場合
     */
    public Word getRandomWord() throws SQLException {
        String sql = "SELECT * FROM words ORDER BY RANDOM() LIMIT 1";
        
        try (Connection conn = DatabaseInitializer.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            if (rs.next()) {
                return new Word(
                    rs.getInt("id"),
                    rs.getString("word"),
                    rs.getInt("playCount"),
                    rs.getInt("clearCount"),
                    rs.getInt("missCount")
                );
            }
            
            throw new SQLException("単語が見つかりません");
        }
    }

    /**
     * 指定されたIDの単語を取得します
     * @param id 取得する単語のID
     * @return 指定されたIDの単語
     * @throws SQLException データベースアクセスエラーが発生した場合
     */
    public Word getWordById(int id) throws SQLException {
        String sql = "SELECT * FROM words WHERE id = ?";
        
        try (Connection conn = DatabaseInitializer.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Word(
                        rs.getInt("id"),
                        rs.getString("word"),
                        rs.getInt("playCount"),
                        rs.getInt("clearCount"),
                        rs.getInt("missCount")
                    );
                }
                
                throw new SQLException("ID=" + id + "の単語が見つかりません");
            }
        }
    }
}
