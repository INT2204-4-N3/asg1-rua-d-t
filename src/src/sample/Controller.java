package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.awt.*;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.ArrayList;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.stage.Window;
import  javafx.stage.Stage;

import javafx.scene.control.Button;
import java.awt.event.KeyEvent;

public class Controller  {
    @FXML
    private TextField textField;
    @FXML
    private TableView<Word> table;
    @FXML
    private TableColumn<Word,String> TableCl;
    @FXML
    private WebView Explain;
    @FXML
    private Button ahihi;
    private WebEngine engine;
    private List<Word> list;
    ListWordManager t;
    public Controller() {
    }
    public void Exit() {
        Platform.exit();
        System.exit(0);
    }
    @FXML
    public void initialize() throws Exception {
        t= new ListWordManager();
        t.CreatListWord("C:\\Users\\Nguyen Thi Thanh Quy\\Desktop\\quy\\JAVAFX\\asg1-rua-d-t\\src\\src\\Sourse\\Anh-Viet.txt");
        list = new ArrayList<Word>();
        Set<String> set = t.getList1().keySet();
        for (String key : set) {
            Word newWord= new Word(key, t.getList1().get(key));
            list.add(newWord);
        }
        // add list lên 1 tập observable theo dõi sự thay đổi của list
        ObservableList<Word> observableList = FXCollections.observableArrayList(list);
        // đưa ob vào fil để đc phép lọc danh sách
        FilteredList<Word> filteredList = new FilteredList<>(observableList,/*predicate*/ p -> true);
        // slấy các giá trị từ thuộc tính của lớp Word add vào cột của table
        TableCl.setCellValueFactory(new PropertyValueFactory<>("English"));
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(word -> {
                // xét trường hợp ô nhập vào rỗng
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String Word1 = newValue.toLowerCase();
                if (word.getEnglish().toLowerCase().contains(Word1))  {
                    return true;

                }
                return false;
            });
        });
        MouseClick();
        SortedList<Word> sortedData = new SortedList<>(filteredList);
        sortedData.comparatorProperty().bind(table.comparatorProperty()); // ràng buộc sự thay đổi của table vào sort
        table.setItems(sortedData);

    }
    public void MouseClick() {
        engine= Explain.getEngine();
        table.setOnMouseClicked(event -> {
            try {
                engine.reload();
                Word chooseWord=table.getSelectionModel().getSelectedItem();
                String explain=t.getList1().get(chooseWord.getEnglish());
                engine.loadContent(explain);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        });
    }
    public void Pressenter(KeyEvent e) {
        if(e.getKeyCode()== KeyEvent.VK_ENTER) {

        }

    }
    // nút ấn enter
    // phát âm
    // tích hợp wiki
}
