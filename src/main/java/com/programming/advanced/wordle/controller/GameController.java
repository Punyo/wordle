package com.programming.advanced.wordle.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

// Game画面のコントローラー
public class GameController {
    // ゲーム画面のUI制御
    @FXML
    private GridPane wordgrid;
    @FXML
    private GridPane keyboard;

    // ゲーム設定
    private static final int WORD_LENGTH = 5;
    private static final int MAX_TRIES = 6; // 試行回数

    // ゲームの状態管理
    private TextField[][] gridCells;
    private int currentRow = 0;
    private int currentCol = 0;

    // 初期化
    @FXML
    public void initialize() {
        gridCells = new TextField[MAX_TRIES][WORD_LENGTH];
        setupWordGrid();
        setupKeyboard();
    }

    private void setupWordGrid() {
        for (int row = 0; row < MAX_TRIES; row++) {
            for (int col = 0; col < WORD_LENGTH; col++) {
                TextField cell = createGridCell();
                gridCells[row][col] = cell;
                wordgrid.add(cell, col, row);

                // 最初の行以外は編集できない
                if (row != 0) {
                    cell.setEditable(false);
                    cell.setFocusTraversable(false);
                }
            }
        }
    }

    private TextField createGridCell() {
        TextField cell = new TextField();
        cell.setPrefSize(50, 50);
        cell.getStyleClass().add("letter-box");
        cell.setAlignment(javafx.geometry.Pos.CENTER);

        // 1文字以上入力できない
        cell.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 1) {
                cell.setText(newValue.substring(0, 1));
            }
        });

        cell.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case BACK_SPACE:
                    // バックスペースキーの処理
                    if (cell.getText().isEmpty() && currentCol > 0 ) {
                        currentCol--;
                        gridCells[currentRow][currentCol].requestFocus();
                    }
                    break;
                default:
                    // 文字入力されたときの処理
                    if (cell.getText().length() == 1) {
                        if (currentCol < WORD_LENGTH - 1) {
                            currentCol++;
                            gridCells[currentRow][currentCol].requestFocus();
                        }
                    }
                    break;
            }
        });

        return cell;
    }

    private void setupKeyboard() {
        String[] rows = {
                "わらやまはなたさかあ",
                "をり　みひにちしきい",
                "んるゆむふぬつすくう",
                "　れ　めへねてせけえ",
                "ーろよもほのとそこお"
        };
        
        // キーボードの構築
        for (int row = 0; row < rows.length; row++) {
            String keys = rows[row];
            for (int col = 0; col < keys.length(); col++) {
                String letter = String.valueOf(keys.charAt(col));
                if (letter.equals("　")) {
                    Region spacer = new Region();
                    spacer.setPrefSize(40, 40);
                    keyboard.add(spacer, col, row);
                } else {
                    Button key = createKeyboardButton(String.valueOf(keys.charAt(col)));
                    keyboard.add(key, col, row);
                }
            }
        }
    }

    private Button createKeyboardButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("keyboard-button");
        button.setOnAction(e -> handleKeyPress(text));
        return button;
    }

    private void handleKeyPress(String letter) {
        // TODO: キーボード押したときの処理
    }

    // TODO: ゲーム画面のロジック
}
