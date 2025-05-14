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
                if (row != 0 || col != 0) {
                    cell.setEditable(false);
                }
            }
        }
        gridCells[0][0].setFocusTraversable(true);
        gridCells[0][0].requestFocus();
    }

    private TextField createGridCell() {
        TextField cell = new TextField();
        cell.setPrefSize(50, 50);
        cell.getStyleClass().add("letter-box");
        cell.setAlignment(javafx.geometry.Pos.CENTER);

        // マウス操作でフォーカスを移動しない
        cell.setMouseTransparent(true);

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
                    if (cell.getText().isEmpty() && currentCol > 0) {
                        handleBackspace();
                    }
                    break;
                default:
                    // 文字入力されたときの処理
                    if (cell.getText().length() == 1) {
                        if (currentCol < WORD_LENGTH - 1) {
                            currentCol++;
                            gridCells[currentRow][currentCol].setEditable(true);
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
                "をり みひにちしきい",
                "んるゆむふぬつすくう",
                " れ めへねてせけえ",
                "ーろよもほのとそこお"
        };

        // キーボードの構築
        for (int row = 0; row < rows.length; row++) {
            String keys = rows[row];
            for (int col = 0; col < keys.length(); col++) {
                String letter = String.valueOf(keys.charAt(col));
                if (letter.equals(" ")) {
                    Region spacer = new Region();
                    spacer.setPrefSize(40, 40);
                    keyboard.add(spacer, col, row);
                } else {
                    Button key = createKeyboardButton(String.valueOf(keys.charAt(col)));
                    keyboard.add(key, col, row);
                }
            }
        }

        // アクションキー
        Button backspaceKey = createActionKeyButton("backspace");
        Button enterKey = createActionKeyButton("enter");

        keyboard.add(backspaceKey, rows[0].length() + 1, 0);
        keyboard.add(enterKey, rows[0].length() + 1, 4);
    }

    // アクションキーの作成
    private Button createActionKeyButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().addAll("keyboard-button", "action-key");
        button.setFocusTraversable(false);
        button.setOnAction(e -> {
            if (text.equals("backspace")) {
                handleBackspace();
            } else if (text.equals("enter") || currentCol == WORD_LENGTH) {
                // TODO: 答えをチェックする
            }
        });
        return button;
    }

    // 文字キーの作成
    private Button createKeyboardButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("keyboard-button");
        button.setFocusTraversable(false);
        button.setOnAction(e -> handleKeyPress(text));
        return button;
    }

    // backspaceの処理
    private void handleBackspace() {
        if (currentCol > 0) {
            TextField currentCell = gridCells[currentRow][currentCol];
            if (!currentCell.getText().isEmpty()) {
                currentCell.setText("");
            } else {
                currentCol--;
                currentCell = gridCells[currentRow][currentCol];
                currentCell.setText("");
                currentCell.setEditable(true);
                currentCell.requestFocus();
            }
            
        }
    }

    private void handleKeyPress(String letter) {
        // TODO: キーボード押したときの処理
    }

    // TODO: ゲーム画面のロジック
}
