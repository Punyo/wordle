package com.programming.advanced.wordle.controller;
import com.programming.advanced.wordle.MainApp;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MenuController {
    // メニュー画面のコントローラー
    @FXML
    private Button playButon;
    @FXML
    private Button recordButton;
    @FXML
    private Label title;

    @FXML
    private void onPlayButtonClicked(ActionEvent e) throws IOException {
        MainApp.setRoot("game");
    }

    @FXML
    private void onRecordButtonClicked(ActionEvent e) throws IOException {
        MainApp.setRoot("record");
    }

    // TODO: メニュー画面のロジック
}