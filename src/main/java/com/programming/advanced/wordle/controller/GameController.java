package com.programming.advanced.wordle.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.programming.advanced.wordle.MainApp;
import com.programming.advanced.wordle.dao.RecordDAO;
import com.programming.advanced.wordle.dao.WordDAO;
import com.programming.advanced.wordle.model.RecordDTO;
import com.programming.advanced.wordle.service.GameService;
import com.programming.advanced.wordle.service.WordBoxStatus;
import com.programming.advanced.wordle.util.Latin2Hira;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;

// Game画面のコントローラー
public class GameController extends BaseController {
    // ゲーム画面のUI制御
    @FXML
    private GridPane wordgrid;
    @FXML
    private GridPane keyboard;

    // ゲーム設定
    private static final int WORD_LENGTH = 5; // 文字の長さ
    private static final int MAX_TRIES = 6; // 試行回数

    private final WordDAO WORD_DAO = new WordDAO();
    private final RecordDAO RECORD_DAO = new RecordDAO();

    private final GameService GAME_SERVICE = GameService.getInstance();

    private String currentTryInput = ""; // 現在の単語

    // ゲームの状態管理
    private TextField[][] gridCells;

    // ローマ字からひらがなに変換するやつ
    private Latin2Hira latin2Hira = new Latin2Hira();

    // 初期化
    @FXML
    public void initialize() throws SQLException {
        gridCells = new TextField[MAX_TRIES][WORD_LENGTH];
        GAME_SERVICE.startNewGame(WORD_DAO.getRandomWord(), MAX_TRIES, WORD_LENGTH);
        setupWordGrid();
        setupKeyboard();
    }

    private int getCurrentRow() {
        return MAX_TRIES - GAME_SERVICE.getRemainingAttempts();
    }

    private TextField getCurrentCell() {
        if (currentTryInput.isEmpty()) {
            return gridCells[getCurrentRow()][currentTryInput.length()];
        } else if (currentTryInput.length() < WORD_LENGTH) {
            return gridCells[getCurrentRow()][currentTryInput.length()];
        } else {
            return gridCells[getCurrentRow()][WORD_LENGTH - 1];
        }
    }

    private void setFocusOnCurrentCell() {
        TextField currentCell = getCurrentCell();
        currentCell.setEditable(true);
        currentCell.requestFocus();
    }

    private void putLetterOnCurrentCell(char letter) {
        TextField currentCell = getCurrentCell();
        currentCell.setText(String.valueOf(letter));
        currentTryInput += letter;
        setFocusOnCurrentCell();
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
        setFocusOnCurrentCell();
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
            if (currentTryInput.length() < WORD_LENGTH) {
                if (newValue.isEmpty()) {
                    return;
                }
                // ひらがなに変換
                String hiragana = latin2Hira.latin2Hira(newValue.toLowerCase());
                Platform.runLater(() -> {
                    for (char c : hiragana.toCharArray()) {
                        putLetterOnCurrentCell(c);
                        if (currentTryInput.length() >= WORD_LENGTH) {
                            break;
                        }
                    }
                });
            }
        });

        cell.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case ENTER:
                    if (currentTryInput.length() >= WORD_LENGTH) {
                        handleEnter();
                    }
                    break;
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
                handleEnter();
            }
        });
        return button;
    }

    private void handleEnter() {
        WordBoxStatus[] status = GAME_SERVICE.checkWord(currentTryInput);
        if (status != null) {
            boolean isClear = true;
            for (WordBoxStatus s : status) {
                if (s != WordBoxStatus.CORRECT) {
                    isClear = false;
                    break;
                }
            }
            if (isClear) {
                try {
                    showGameDialog(true);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (GAME_SERVICE.getRemainingAttempts() == 0) {
                try {
                    showGameDialog(false);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            updateCurrentRowCellsColor(status, getCurrentRow() - 1);
            updateKeyboardColor(status);
            currentTryInput = "";
            setFocusOnCurrentCell();
        }

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

    // backspaceの処理
    private void handleBackspace() {
        if (!currentTryInput.isEmpty()) {
            TextField currentCell = getCurrentCell();
            currentCell.setText("");
            currentTryInput = currentTryInput.substring(0, currentTryInput.length() - 1);
            setFocusOnCurrentCell();
        }
    }

    public void updateCurrentRowCellsColor(WordBoxStatus[] status, int row) {
        TextField[] cells = gridCells[row];
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
        if (currentTryInput.length() <= WORD_LENGTH) {
            putLetterOnCurrentCell(letter.charAt(0));
        }
    }

    private void showGameDialog(boolean isClear) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("gameDialog.fxml"));
            Parent root = loader.load();
            GameDialogController controller = loader.getController();
            controller.initializeDialog(isClear, GAME_SERVICE.getRemainingAttempts(), GAME_SERVICE.getCurrentWord().getWord());
            RECORD_DAO.saveRecord(new RecordDTO(GAME_SERVICE.getCurrentWord().getId(), GAME_SERVICE.getRemainingAttempts(), isClear));
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setScene(new Scene(root));
            dialog.setResizable(false);
            dialog.showAndWait();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
