package com.programming.advanced.wordle.controller;

import com.programming.advanced.wordle.model.Word;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

public class RecordController {
    @FXML
    private ListView<String> listView;
    @FXML
    private TableView<Word> tableView;
    @FXML
    private TableColumn<Word, Integer> rankColumn; // 順位
    @FXML
    private TableColumn<Word, String> nameColumn; // 単語
    @FXML
    private TableColumn<Word, Integer> countColumn; // 出題された階数
    @FXML
    private TableColumn<Word, Double> successRateColumn; // 成功率

    @FXML
    public void initialize() {
        // ListViewの初期化
        listView.setEditable(false);
        listView.setFocusTraversable(false);
        listView.setMouseTransparent(true);

        // サンプルデータ
        ObservableList<String> playRecords = FXCollections.observableArrayList(
                "Single", "Double", "inukaki");
        listView.setItems(playRecords);

        // TableViewの初期化
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("playCount"));
        successRateColumn.setCellValueFactory(new PropertyValueFactory<>("clearCount")); // TODO: 成功率を計算する

        tableView.setEditable(false);
        tableView.setFocusTraversable(false);

        // サンプルデータ
        ObservableList<Word> words = FXCollections.observableArrayList(
                new Word(1, "apple", 10, 5, 5),
                new Word(2, "house", 8, 6, 4),
                new Word(3, "phone", 15, 12, 8));
        tableView.setItems(words);

        // テーブルの列の幅を自動で調整
        tableView.setTableMenuButtonVisible(false);

        // テーブルの列の並び替えを無効化
        for (TableColumn<?, ?> col : tableView.getColumns()) {
            col.setSortable(true);
            col.setReorderable(false);
        }
    }
}