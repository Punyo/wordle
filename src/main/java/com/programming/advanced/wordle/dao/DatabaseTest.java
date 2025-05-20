package com.programming.advanced.wordle.dao;

import java.sql.*;

// データベースのテストを行うクラス
public class DatabaseTest {
    public static void main(String[] args) {
        try {
            // データベースの初期化
            System.out.println("データベースの初期化を開始します...");
            DatabaseInitializer.initializeDatabase();
            
            // 接続のテスト
            System.out.println("\nデータベース接続のテストを開始します...");
            try (Connection conn = DatabaseInitializer.getConnection();
                 Statement stmt = conn.createStatement()) {
                
                // テーブル一覧の取得
                System.out.println("\nテーブル一覧を取得します...");
                try (ResultSet rs = stmt.executeQuery(
                        "SELECT name FROM sqlite_master WHERE type='table'")) {
                    while (rs.next()) {
                        System.out.println("テーブル: " + rs.getString("name"));
                    }
                }
                
                // 各テーブルの構造確認
                System.out.println("\nテーブルの構造を確認します...");
                try (ResultSet rs = stmt.executeQuery("PRAGMA table_info(words)")) {
                    System.out.println("\nwordsテーブルの構造:");
                    while (rs.next()) {
                        System.out.println("カラム: " + rs.getString("name") + 
                                         " 型: " + rs.getString("type"));
                    }
                }
                
                try (ResultSet rs = stmt.executeQuery("PRAGMA table_info(records)")) {
                    System.out.println("\nrecordsテーブルの構造:");
                    while (rs.next()) {
                        System.out.println("カラム: " + rs.getString("name") + 
                                         " 型: " + rs.getString("type"));
                    }
                }
                
                try (ResultSet rs = stmt.executeQuery("PRAGMA table_info(inputs)")) {
                    System.out.println("\ninputsテーブルの構造:");
                    while (rs.next()) {
                        System.out.println("カラム: " + rs.getString("name") + 
                                         " 型: " + rs.getString("type"));
                    }
                }
            }
            
            System.out.println("\nテストが正常に完了しました。");
            
        } catch (Exception e) {
            System.err.println("テスト中にエラーが発生しました:");
            e.printStackTrace();
        }
    }
} 