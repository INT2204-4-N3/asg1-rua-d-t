package sample;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.awt.*;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.*;

import javafx.collections.transformation.SortedList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.stage.Window;
import  javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.List;

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
    private Button add;
    private WebEngine engine;
    private List<Word> list;
    ListWordManager t;
    //private JEditorPane label;
    @FXML
    private Button edit;
    @FXML
    private Button remove;
    @FXML
    private Button exit;
    private JEditorPane label;

    public Controller() {
    }
    public void Exit() {
        Platform.exit();
        System.exit(0);
    }
    @FXML
    public void initialize() throws Exception {
        t= new ListWordManager();
        t.CreatListWord("C:\\Users\\Administrator\\IdeaProjects\\asg1-rua-d-t\\data\\E_V\\E_V.txt");
        list = new ArrayList<Word>();
        Set<String> set = t.getList1().keySet();
        for (String key : set) {
            Word newWord= new Word(key, t.getList1().get(key));
            list.add(newWord);
        }// add list lên 1 tập observable theo dõi sự thay đổi của list
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
    public void add(ActionEvent evt){
        StackPane secondaryLayout = new StackPane();
        Scene secondScene = new Scene(secondaryLayout, 300, 200);
        // Một cửa sổ mới (Stage)
        Stage Add= new Stage();
        Add.setTitle("Add new word");
        Add.setScene(secondScene);

        // Sét đặt vị trí cho cửa sổ thứ 2.
        // Có vị trí tương đối đối với cửa sổ chính.
        Add.setX( 600);
        Add.setY( 200);
    //

        Button SavaAdd= new Button();
        Button CancelAdd= new Button();

        Add.show();
        /*try {
            FileOutputStream fos = new FileOutputStream("C:\\Users\\Administrator\\IdeaProjects\\asg1-rua-d-t\\data\\E_V\\E_V.txt");
            DataOutputStream dos = new DataOutputStream(fos);

        }*/

    }
    public void edit(ActionEvent evt){
        TextField  word = new TextField();
        TextField mean = new TextField();
        StackPane secondaryLayout = new StackPane();
        Scene secondScene = new Scene(secondaryLayout, 300, 200);
        // Một cửa sổ mới (Stage)
        Stage Add= new Stage();
        Add.setTitle("Edit word");
        Add.setScene(secondScene);

        // Sét đặt vị trí cho cửa sổ thứ 2.
        // Có vị trí tương đối đối với cửa sổ chính.
        Add.setX( 600);
        Add.setY( 200);
        //

        Button SaveEdit = new Button();
        Button CancelEdit= new Button();
        //SavaAdd;
        // Add.getChildren().addAll(SavaAdd, CancelAdd);
        Add.show();

    }
    public void Remove(ActionEvent evt){
        //if(MouseClick()) {
            //chọn từ bằng cách click chuột trên table;
            TextInputDialog dialog = new TextInputDialog("Hello");
            dialog.setTitle("Remove word ");
            dialog.setHeaderText(null);
            dialog.setContentText("Word:");

            Optional<String> result = dialog.showAndWait();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Word");
            alert.setHeaderText("Are you sure want to remove this word ?");
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == null && option.get() == ButtonType.CANCEL) {

            } else if (option.get() == ButtonType.OK) {

            }

        //else{
            // Hiển thị Warning Alert với Header Text
            /*private void showAlertWithHeaderText() {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Không có từ được chọn");

                alert.showAndWait();
            }*/
        }



    @FXML
    private void exit(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Word");
        alert.setHeaderText("Are you sure want to move this word ?");
        //alert.setContentText("C:/MyFile.txt");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == null && option.get() ==ButtonType.CANCEL) {
            //this.label.setText("No selection!");
        } else if (option.get() == ButtonType.OK) {
            // get a handle to the stage
            Stage stage = (Stage) exit.getScene().getWindow();
            // do what you have to do
            stage.close();
        }

    }//oke rồi


    // nút ấn enter
    // phát âm
    // tích hợp wiki
}
