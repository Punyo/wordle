package com.programming.advanced.wordle.controller;
import java.io.IOException;

import com.programming.advanced.wordle.MainApp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {
    // メニュー画面のコントローラー
    @FXML
    private Button playButon;
    @FXML
    private Button recordButton;
    
    @FXML
    private void handlePlayAction(ActionEvent e) throws IOException {
        MainApp.setRoot("game");
    }

    @FXML
    private void handleRecordAction(ActionEvent e) throws IOException {
        MainApp.setRoot("record");
    }

    // TODO: メニュー画面のロジック
}