package com.programming.advanced.wordle.controller;

import com.programming.advanced.wordle.model.Record;
import com.programming.advanced.wordle.MainApp;
import com.programming.advanced.wordle.dao.RecordDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RecordController {
    @FXML
    private Button backButton; // 戻るボタン
    @FXML
    private void onBackButtonClicked(ActionEvent e) throws IOException {
        MainApp.setRoot("menu");
    }

    @FXML
    private TableView<Record> playTableView;
    @FXML
    private TableColumn<Record, LocalDate> dateColumn; // 日付
    @FXML
    private TableColumn<Record, String> answerColumn; // 解答
    @FXML
    private TableColumn<Record, Integer> tryCountColumn; // 試行回数
    @FXML
    private TableColumn<Record, Boolean> isClearColumn; // クリアフラグ

    @FXML
    public void initialize() {
        // playTableViewの初期化
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        answerColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        tryCountColumn.setCellValueFactory(new PropertyValueFactory<>("answerCount"));
        isClearColumn.setCellValueFactory(new PropertyValueFactory<>("clear"));
        
        // クリア状態の表示設定
        isClearColumn.setCellFactory(column -> new TableCell<Record, Boolean>() {
            @Override
            protected void updateItem(Boolean isClear, boolean empty) {
                super.updateItem(isClear, empty);
                if (empty || isClear == null) {
                    setText(null);
                } else {
                    setText(isClear ? "クリア" : "失敗");
                }
            }
        });

        // テーブルの設定
        playTableView.setEditable(false);
        playTableView.setFocusTraversable(false);

        // データの取得と設定
        try {
            RecordDAO recordDAO = new RecordDAO();
            List<Record> records = recordDAO.getAllRecords();
            ObservableList<Record> playRecords = FXCollections.observableArrayList(records);
            playTableView.setItems(playRecords);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        // テーブルの表示設定
        playTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        playTableView.setTableMenuButtonVisible(false);

        // カラムの設定
        for (TableColumn<?, ?> col : playTableView.getColumns()) {
            col.setSortable(true);
            col.setReorderable(false);
        }
    }
}