package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Dictionary");
        Scene sc1= new Scene(root, 965, 607);//thiêt lap cua so
        sc1.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(sc1);
        primaryStage.show();//shoe
    }


    public static void main(String[] args) {
        launch(args);
    }
}
