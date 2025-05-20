package com.programming.advanced.wordle.controller;

import com.programming.advanced.wordle.model.Word;
import com.programming.advanced.wordle.model.Record;
import com.programming.advanced.wordle.dao.RecordDAO;
import com.programming.advanced.wordle.dao.WordDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.control.TableCell;

public class RecordController {
    @FXML
    private TableView<Record> playTableView;
    @FXML
    private TableColumn<Record, String> dateColumn; // 日付
    @FXML
    private TableColumn<Record, String> answerColumn; // 解答
    @FXML
    private TableColumn<Record, Integer> tryCountColumn; // 試行回数
    @FXML
    private TableColumn<Record, Boolean> isClearColumn; // クリアフラグ
    
    @FXML
    private TableView<Word> wordTableView;
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
        // playTableViewの初期化
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        answerColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        tryCountColumn.setCellValueFactory(new PropertyValueFactory<>("answerCount"));
        isClearColumn.setCellValueFactory(new PropertyValueFactory<>("clear"));
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

        playTableView.setEditable(false);
        playTableView.setFocusTraversable(false);

        RecordDAO recordDAO = new RecordDAO();
        List<Record> records = new ArrayList<>();
        try {
            records = recordDAO.getAllRecords();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        ObservableList<Record> playRecords = FXCollections.observableArrayList(records);
        playTableView.setItems(playRecords);

        // テーブルの列の幅を自動で調整
        playTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);   
        playTableView.setTableMenuButtonVisible(false);

        // テーブルの列の並び替えを無効化
        for (TableColumn<?, ?> col : playTableView.getColumns()) {
            col.setSortable(true);
            col.setReorderable(false);
        }

        // // wordTableViewの初期化
        // rankColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        // nameColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        // countColumn.setCellValueFactory(new PropertyValueFactory<>("playCount"));
        // successRateColumn.setCellValueFactory(new PropertyValueFactory<>("clearCount")); // TODO: 成功率を計算する

        // wordTableView.setEditable(false);
        // wordTableView.setFocusTraversable(false);

        // // サンプルデータ
        // WordDAO wordDAO = new WordDAO();
        // List<Word> words = new ArrayList<>();
        // try {
        //     words = wordDAO.getAllWords();
        // } catch (java.sql.SQLException e) {
        //     e.printStackTrace();
        // }
        // ObservableList<Word> wordRecords = FXCollections.observableArrayList(words);
        // wordTableView.setItems(wordRecords);

        // // テーブルの列の幅を自動で調整
        // wordTableView.setTableMenuButtonVisible(false);

        // // テーブルの列の並び替えを無効化
        // for (TableColumn<?, ?> col : wordTableView.getColumns()) {
        //     col.setSortable(true);
        //     col.setReorderable(false);
        // }
    }
}