package com.programming.advanced.wordle.controller;

import com.programming.advanced.wordle.model.Record;
import com.programming.advanced.wordle.model.Word;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class RecordController {
    @FXML
    private TableView<Word> tableView;
    @FXML
    private TableColumn<Word, Integer> rankColumn; //　順位
    @FXML
    private TableColumn<Word, String> nameColumn; //　単語
    @FXML
    private TableColumn<Word, Integer> countColumn; //　出題された階数
    @FXML
    private TableColumn<Word, Double> successRateColumn; // 成功率

    @FXML
    public void initialize() {
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("playCount"));
        successRateColumn.setCellValueFactory(new PropertyValueFactory<>("successRate"));
        
        tableView.getItems().add(new Word(01, "apple", 10, 5, 5));

        // テーブルの列の幅を自動で調整
        tableView.setTableMenuButtonVisible(false);

        // テーブルの列の並び替えを無効化
        for (Object col : tableView.getColumns()) {
            ((TableColumn<?, ?>) col).setReorderable(false);
        }
    }
}