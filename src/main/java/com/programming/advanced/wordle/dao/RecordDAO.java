package com.programming.advanced.wordle.dao;

import com.programming.advanced.wordle.model.Record;
import com.programming.advanced.wordle.model.RecordDTO;
import java.sql.*;
import java.time.LocalDate;

// 記録データアクセスオブジェクト
public class RecordDAO {
    
    /**
     * プレイ記録を保存し、単語の統計情報を更新します
     * @param dto 保存する記録のDTO
     * @return 保存された記録のID
     * @throws SQLException データベースアクセスエラーが発生した場合
     */
    public int saveRecord(RecordDTO dto) throws SQLException {
        String insertRecordSql = "INSERT INTO records (wordId, answerCount, clear, date) VALUES (?, ?, ?, ?)";
        String updateWordStatsSql = "UPDATE words SET playCount = playCount + 1, " +
                                  "clearCount = clearCount + ?, " +
                                  "missCount = missCount + ? " +
                                  "WHERE id = ?";
        
        try (Connection conn = DatabaseInitializer.getConnection()) {
            conn.setAutoCommit(false); // トランザクション開始
            
            try {
                // レコードの保存
                int recordId;
                try (PreparedStatement pstmt = conn.prepareStatement(insertRecordSql, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setInt(1, dto.getWordId());
                    pstmt.setInt(2, dto.getAnswerCount());
                    pstmt.setBoolean(3, dto.isClear());
                    pstmt.setDate(4, Date.valueOf(LocalDate.now())); // 現在の日付を自動設定
                    
                    pstmt.executeUpdate();
                    
                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            recordId = rs.getInt(1);
                        } else {
                            throw new SQLException("レコードの保存に失敗しました");
                        }
                    }
                }
                
                // 単語の統計情報を更新
                try (PreparedStatement pstmt = conn.prepareStatement(updateWordStatsSql)) {
                    // clearCountの更新値（クリアした場合は1、失敗した場合は0）
                    pstmt.setInt(1, dto.isClear() ? 1 : 0);
                    // missCountの更新値（失敗した場合は1、クリアした場合は0）
                    pstmt.setInt(2, dto.isClear() ? 0 : 1);
                    pstmt.setInt(3, dto.getWordId());
                    
                    pstmt.executeUpdate();
                }
                
                conn.commit(); // トランザクションをコミット
                return recordId;
                
            } catch (SQLException e) {
                conn.rollback(); // エラーが発生した場合はロールバック
                throw e;
            } finally {
                conn.setAutoCommit(true); // 自動コミットを元に戻す
            }
        }
    }
}
