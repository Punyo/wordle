package com.programming.advanced.wordle.controller;

import java.io.IOException;

import com.programming.advanced.wordle.MainApp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button; 

public class BaseController {
    @FXML
    private Button returnToMenuButton; // 戻るボタン
    @FXML
    private void handleReturnToMenuButton(ActionEvent e) throws IOException {
        MainApp.setRoot("menu");
    }
}

