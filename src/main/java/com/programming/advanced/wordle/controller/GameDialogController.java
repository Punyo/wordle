package com.programming.advanced.wordle.controller;

import java.io.IOException;

import com.programming.advanced.wordle.MainApp;
import com.programming.advanced.wordle.service.GameService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GameDialogController {
    @FXML
    private Label resultLabel;
    @FXML
    private Label tryCountLabel;
    @FXML
    private Label answerLabel;
    @FXML
    private Button playAgainButton;
    @FXML
    private Button returnToMenuButton;

    private final GameService gameService = GameService.getInstance();

    public void initializeDialog(boolean isGameClear, int tryCount, String answer) {
        resultLabel.setText(isGameClear ? "ゲームクリア" : "ゲームオーバー");
        answerLabel.setText("答え: " + answer);
        tryCountLabel.setText("試行回数: " + tryCount);
    }

    @FXML
    private void handlePlayAgainButton(ActionEvent e) throws IOException {
        gameService.resetGame();
        ((Stage) playAgainButton.getScene().getWindow()).close();
        MainApp.setRoot("game");
    }

    @FXML
    private void handleReturnToMenuButton(ActionEvent e) throws IOException {
        gameService.resetGame();
        ((Stage) playAgainButton.getScene().getWindow()).close();
        MainApp.setRoot("menu");
    }

}
