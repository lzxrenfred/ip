package larry;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class for the JavaFX application.
 */
public class Main extends javafx.application.Application {

    private Larry larry = new Larry();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    Main.class.getResource("/view/MainWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            MainWindow mainWindow = fxmlLoader.getController();
            mainWindow.setLarry(larry);

            stage.setTitle("Larry");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
