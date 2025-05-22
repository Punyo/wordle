package com.programming.advanced.wordle.controller;

import com.programming.advanced.wordle.service.GameService;
import com.programming.advanced.wordle.service.WordBoxStatus;
import com.programming.advanced.wordle.util.Latin2Hira;

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
    private static final int WORD_LENGTH = 5; // 文字の長さ
    private static final int MAX_TRIES = 6; // 試行回数

    private final GameService gameService = GameService.getInstance();

    private String currentTryInput = ""; // 現在の単語

    // ゲームの状態管理
    private TextField[][] gridCells;
    private int currentRow = 0;
    private int currentCol = 0;

    // ローマ字からひらがなに変換するやつ
    private Latin2Hira latin2Hira = new Latin2Hira();

    // 初期化
    @FXML
    public void initialize() {
        String word = "あいうえお"; // TODO: 単語を取得する
        gridCells = new TextField[MAX_TRIES][WORD_LENGTH];
        gameService.startNewGame(word, MAX_TRIES, WORD_LENGTH);
        setupWordGrid();
        setupKeyboard();
    }

    private void setupWordGrid() {
        for (int row = 0; row < MAX_TRIES; row++) {
            for (int col = 0; col < WORD_LENGTH; col++) {
                TextField cell = createGridCell();
                gridCells[row][col] = cell;
                wordgrid.add(cell, col, row);

                // 最初の文字以外は編集できない
                if (row != 0 || col != 0) {
                    cell.setEditable(false);
                }
            }
        }
        // 最初のcellの編集権限とフォーカスを設定
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

        // ひらがなに変換する
        cell.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                return;
            }
            // ひらがなに変換
            String hiragana = latin2Hira.latin2Hira(newValue.toLowerCase());
            // 変換できない場合はそのまま表示
            if (hiragana.isEmpty()) {
                cell.setText(newValue);
                return;
            }
            // 変換後が1文字の場合
            if (hiragana.length() == 1) {
                cell.setText(hiragana);
                moveToNextCell();
                return;
            }
            // 変換後が2文字以外の場合
            if (currentCol + hiragana.length() <= WORD_LENGTH) {
                for (int i = 0; i < hiragana.length(); i++) {
                    gridCells[currentRow][currentCol + i].setText(String.valueOf(hiragana.charAt(i)));
                }
                currentCol += hiragana.length() - 1;
                moveToNextCell();
            }
        });

        cell.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case BACK_SPACE:
                    handleBackspace();
                    break;
                default:
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
            } else if (text.equals("enter") && currentTryInput.length() == WORD_LENGTH) {
                WordBoxStatus[] status = gameService.checkWord(currentTryInput);
                updateCurrentRowCellsColor(status);
                updateKeyboardColor(status);
                moveToNextRow();
                currentTryInput = "";
            }
        });
        return button;
    }

    // 文字キーの作成
    private Button createKeyboardButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("keyboard-button");
        button.setFocusTraversable(false);
        button.setOnAction(e -> {
            handleKeyPress(text);
        });
        return button;
    }

    // 次のcellに移動する
    private void moveToNextCell() {
        if (currentCol < WORD_LENGTH - 1) {
            currentCol++;
            gridCells[currentRow][currentCol].setEditable(true);
            gridCells[currentRow][currentCol].requestFocus();
        }
    }

    private void moveToNextRow() {
        if (currentRow < MAX_TRIES) {
            currentRow++;
            currentCol = 0;
            gridCells[currentRow][currentCol].setEditable(true);
            gridCells[currentRow][currentCol].requestFocus();
        }
    }

    // backspaceの処理
    private void handleBackspace() {
        if (currentCol > 0) {
            TextField currentCell = gridCells[currentRow][currentCol];
            if (!currentCell.getText().isEmpty()) {
                // 現在のセルに文字がある場合は、その文字を消す
                currentCell.setText("");
            } else {
                // 現在のセルが空の場合は、前のセルに移動
                currentCol--;
                currentCell = gridCells[currentRow][currentCol];
                currentCell.setText("");
                currentCell.setEditable(true);
                currentCell.requestFocus();
            }
        }
    }

    public void updateCurrentRowCellsColor(WordBoxStatus[] status) {
        TextField[] cells = gridCells[currentRow];
        for (int i = 0; i < cells.length; i++) {
            cells[i].getStyleClass().removeAll("correct-letter", "present-letter", "absent-letter");

            switch (status[i]) {
                case CORRECT -> cells[i].getStyleClass().add("correct-letter");
                case IN_WORD -> cells[i].getStyleClass().add("present-letter");
                case NOT_IN_WORD -> cells[i].getStyleClass().add("absent-letter");
            }
        }
    }

    public void updateKeyboardColor(WordBoxStatus[] status) {
        var keys = keyboard.getChildren();
        for (int i = 0; i < WORD_LENGTH; i++) {
            char currentChar = currentTryInput.charAt(i);
            for (var node : keys) {
                if (node instanceof Button button) {
                    if (button.getText().equals(String.valueOf(currentChar))) {
                        button.getStyleClass().removeAll("correct-letter", "present-letter", "absent-letter");
                        switch (status[i]) {
                            case CORRECT -> button.getStyleClass().add("correct-letter");
                            case IN_WORD -> button.getStyleClass().add("present-letter");
                            case NOT_IN_WORD -> button.getStyleClass().add("absent-letter");
                        }
                    }
                }
            }
        }

    }

    // 文字キーの入力処理
    private void handleKeyPress(String letter) {
        TextField currentCell = gridCells[currentRow][currentCol];
        if (currentCol < WORD_LENGTH && currentCell.getText().isEmpty()) {
            currentCell.setText(letter);
            currentTryInput += letter;
            moveToNextCell();
        }
    }

    // TODO: ゲーム画面のロジック
}
