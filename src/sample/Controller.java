package sample;

import GoogleAPI.Audio;
import GoogleAPI.GoogleTranslate;
import GoogleAPI.Language;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Pair;
import javazoom.jl.decoder.JavaLayerException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Controller {
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
    @FXML
    private Button edit;
    @FXML
    private Button remove;
    @FXML
    private Button exit;
    private WebEngine engine;
    private List<Word> list;
    private ListWordManager t;
    private KeyEvent keyevent;
    private static int num;
    private Voice voice;
    private VoiceManager voiceManager;
    private String val;
    private String ex;
    private boolean press;
    private String newString;
    private  BufferedReader file;
    private FileOutputStream File;
    private int count, count1, count2;
    private boolean m;

    public Controller() {
        num=0;
        val="";
        ex="";
        press=true;
        count=0;
    }
    public int getNum() {
        return num;
    }

    public String getNewString() {
        if(getNum()==0) { newString="src\\data\\Anh-Viet.txt";}
        else { newString="src\\data\\Viet-Anh.txt";}
        return newString;
    }
    public void Exit() {
        Platform.exit();
        System.exit(0);
    }
    // chuyển anh-việt
    public void Eng() throws Exception {
        num=0;
        initialize();
    }
    public void Viet() throws Exception {
        num=1;
        initialize();
    }
    @FXML
    public void initialize() throws Exception {
        newString=getNewString();
        t = new ListWordManager();
        t.CreatListWord(newString);
        list = new ArrayList<Word>();
        Set<String> set = t.getList1().keySet();
        for (String key : set) {
            Word newWord = new Word(key, t.getList1().get(key));
            list.add(newWord);
        }
        // add list lên 1 tập observable theo dõi sự thay đổi của list
        ObservableList<Word> observableList = FXCollections.observableArrayList(list);
        // đưa ob vào fil để đc phép lọc danh sách
        FilteredList<Word> filteredList = new FilteredList<>(observableList,/*predicate*/ p -> true);
        // slấy các giá trị từ thuộc tính của lớp Word add vào cột của table
        TableCl.setCellValueFactory(new PropertyValueFactory<>("English"));
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // set các gt trong fil có kiểu word
            filteredList.setPredicate(word -> {
                // xét trường hợp ô nhập vào rỗng
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String Word1 = newValue.toLowerCase();
                if (word.getEnglish().toLowerCase().contains(Word1)) {
                    return true;

                }
                return false;
            });
        });
        MouseClick();
        Pressenter();
        SortedList<Word> sortedData = new SortedList<>(filteredList);
        sortedData.comparatorProperty().bind(table.comparatorProperty()); // ràng buộc sự thay đổi của table vào sort
        table.setItems(sortedData);

    }
    //su kien nhap chuot vao tu can tim kiem tren table


    public void MouseClick() { // chú ý chuyển va, a-v và về textfile

        engine = Explain.getEngine();
        table.setOnMouseClicked(event -> {
            press=false;
            try {
                engine.reload();
                Word chooseWord = table.getSelectionModel().getSelectedItem();
                String explain = t.getList1().get(chooseWord.getEnglish());
                engine.loadContent(explain);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        System.out.println(press);
    }
    public void search() {
        try {
            ObservableList<Word> list1 = table.getItems();
            if (list1.size() == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notice");
                alert.setHeaderText("Oh!! Sorry :((((");
                alert.setContentText("We don't find your word in dictionary");
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(
                        getClass().getResource("application.css").toExternalForm());
                dialogPane.getStyleClass().add("myDialogSearch");

                alert.showAndWait();
            }
            else {
                ex= t.getList1().get(val);
                if(ex!=null) { engine.loadContent(ex);}
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void Pressenter() {
        textField.setOnKeyPressed(event -> {
            press=true;
            if(event.getCode()== KeyCode.ENTER) {
                val= textField.getText();
                search();
            }
        });
    }
    public void butEnter() {
        val= textField.getText();
        search();
    }
    public void clickspeak() {
        String voice1;
        Word chooseWord = table.getSelectionModel().getSelectedItem();
        System.setProperty("mbrola.base", "FreeTTS\\mbrola");
        voiceManager = VoiceManager.getInstance();
        voice = voiceManager.getVoice("mbrola_us3");
        voice.allocate();
        if (press== false) {
            voice1= chooseWord.getEnglish();
        } else {
            voice1= val;
        }
        voice.speak(voice1);
        voice.allocate();
    }

    public void add(ActionEvent evt) throws Exception {
        newString=getNewString();
        javafx.scene.control.Dialog<Pair<String, String>> dialog = new Dialog<>();//thiet l
        dialog.setTitle("Add new Word");
        dialog.setHeaderText("Input your Word");
        ButtonType input= new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(input, ButtonType.CANCEL);

        GridPane gridPane= new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(20, 150, 10,10));

        TextField word= new TextField();
        word.setPromptText("input your word");
        TextField ex= new TextField();
        ex.setPromptText("mean");
        TextField pro= new TextField();
        pro.setPromptText("pronuciation");
        TextField kind= new TextField();
        kind.setPromptText("kind of word");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("application.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialogSearch");

        gridPane.add(new Label("Word: "),0,0);
        gridPane.add(word,1,0);
        gridPane.add(new Label("Mean: "),0,1);
        gridPane.add(ex,1,1);
        gridPane.add(new Label("Pronunciation: "),0,2);
        gridPane.add(pro,1,2);
        gridPane.add(new Label("Kind: "),0,3);
        gridPane.add(kind,1,3);
        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(dialogBut->{
            if(dialogBut==input) {
                try {
                    file = new BufferedReader(new FileReader(newString));
                    File = new FileOutputStream(newString, true);
                    String line;
                    String input1 ="";
                    input1=System.lineSeparator()+word.getText()+"<html><i>"+word.getText()+" "+pro.getText()+"</i><br/><ul><li><b><i>"+ kind.getText()+"</i></b><ul><li><font color='#cc0000'><b>"+ ex.getText()+"</b></font></li></ul></li></ul></html>";
                    File.write(input1.getBytes());

                    file.close();
                    File.close();
                    System.out.println("oki");
                } catch (Exception e) {
                    System.out.println("Add not word");
                }
            }
            return null;
        });
        Optional<Pair<String, String>> result =dialog.showAndWait();
//        t.CreatListWord("C:\\BT lon UET diction\\asg1-rua-d-t\\src\\Sourse\\Anh-Viet.txt");
        initialize();

    }
    public void edit(ActionEvent evt) throws Exception{
        m=false;
        newString=getNewString();
        count2=0;
        javafx.scene.control.Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("edit Word");
        dialog.setHeaderText("Input your Word");
        ButtonType input= new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(input, ButtonType.CANCEL);

        GridPane gridPane= new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(20, 150, 10,10));

        TextField word= new TextField();
        word.setPromptText("your word");
        TextField ex= new TextField();
        ex.setPromptText("change to");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("application.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialogSearch");
        gridPane.add(new Label("Word: "),0,0);
        gridPane.add(word,1,0);
        gridPane.add(new Label("Change to: "),0,1);
        gridPane.add(ex,1,1);
        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(dialogBut->{
            if(dialogBut==input) {
                try
                {
                    file = new BufferedReader(new FileReader(newString));
                    String line;
                    StringBuffer input1=new StringBuffer("");
                    while ((line = file.readLine()) != null)
                    {
                        String ns="", ns1="";
                        if(line.indexOf("<")>0) {
                            ns = line.substring(0, line.indexOf("<")/*tim vi tri*/);
                            ns1 = line.substring(line.indexOf("<"), line.length());
                        }
                        if (ns.equals(word.getText()))
                        {
                            line = ex.getText()+ns1 ;
                            System.out.println("Line changed.");
                            m=true;
                        }
                        if(count2==0) {input1.append(line);}
                        else { input1.append(System.lineSeparator()); input1.append(line);}
                        count2++;
                    }
                    if(m== false) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Notice");
                        alert.setHeaderText("Oh!! Sorry :((((");
                        alert.setContentText("We don't find your word you want edit");
                        alert.showAndWait();
                    }
                    else {
                        FileWriter fw = new FileWriter(newString);
                        fw.write(input1.toString());
                        file.close();
                        fw.close();
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Problem reading file.");
                }
            }
            return null;
        });
        Optional<Pair<String, String>> result =dialog.showAndWait();
        initialize();
    }




    public void Remove(ActionEvent evt)  throws Exception {
        m=false;
        newString=getNewString();
        count1=0;
        javafx.scene.control.Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Delete new Word");
        dialog.setHeaderText("Input your Word");
        ButtonType input = new ButtonType("delete", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(input, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField word = new TextField();
        word.setPromptText("input your word");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("application.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialogSearch");
        gridPane.add(new Label("Word: "), 0, 0);
        gridPane.add(word, 1, 0);

        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(dialogBut -> {
            if (dialogBut == input) {
                try
                {
                    file = new BufferedReader(new FileReader(newString));
                    String line;
                    StringBuffer input1=new StringBuffer("");
                    while ((line = file.readLine()) != null)
                    {
                        String ns="";
                        if(line.indexOf("<")>0) {
                            ns = line.substring(0, line.indexOf("<")/*tim vi tri*/);
                        }
                        if (ns.equals(word.getText()))
                        {
                            System.out.println(word.getText());
                            line = "";
                            System.out.println("Line deleted.");
                            m=true;
                        }
                        if(count1==0) {input1.append(line);}
                        else { input1.append(System.lineSeparator()); input1.append(line);}
                        count1++;
                    }
                    if(m== false) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Notice");
                        alert.setHeaderText("Oh!! Sorry :((((");
                        alert.setContentText("We don't find your word you want delete");
                        alert.showAndWait();
                    }
                    else {
                        FileWriter fw = new FileWriter(newString);
                        fw.write(input1.toString());
                        file.close();
                        fw.close();
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Problem reading file.");
                }
            }
            return null;
        });
        Optional<Pair<String, String>> result =dialog.showAndWait();
        initialize();
    }



    @FXML
    private void exit(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);//khởi tạo một hộp thoiaj alert mới
        alert.setTitle("Delete Word");
        alert.setHeaderText("Are you sure want to exit ?");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("application.css").toExternalForm());
        dialogPane.getStyleClass().add("myDialogSearch");//liên kết Css với họp thoại
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

    }
    public void Wiki() {
        System.out.println(press);
        String URL="https://vi.wikipedia.org/wiki/";//lấy link
        Word chooseWord = table.getSelectionModel().getSelectedItem();//lấy tư tim kiem trên table
        String str1="";
        if (press == false) {//nếu false lấy  tư trong table để tìm kiếm
            str1 = chooseWord.getEnglish();
        }
        else {// lấy từ trên text để tìm kiếm
            str1 = textField.getText();
        }
        System.out.println(str1);
        URL+= str1;// lấy link truy cập wiki
        engine=Explain.getEngine();
        engine.load(URL);
    }
    public void ggTrans() throws IOException, JavaLayerException {
        InputStream sound;
        String str="",str1="";
        Word chooseWord = table.getSelectionModel().getSelectedItem();
        if (press == false) {
            str1 = chooseWord.getEnglish();
        }
        else {
            str1 = textField.getText();
        }
        System.out.println(str1+ press);
            Audio audio = Audio.getInstance();
            if (num == 1) {
                sound = audio.getAudio(str1, Language.VIETNAMESE);
                str = GoogleTranslate.translate("vi", "en", str1);
            } else {
                sound = audio.getAudio(str1, Language.ENGLISH);
                str = GoogleTranslate.translate("en", "vi", str1);
            }
            audio.play(sound);

            engine.loadContent(str);
    }
}
