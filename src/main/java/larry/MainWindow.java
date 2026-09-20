package larry;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Controller for the main GUI.
 */
public class MainWindow {

    @FXML
    private TextArea dialogArea;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private Larry larry;

    /**
     * Sets the Larry instance used by this GUI.
     *
     * @param larry Larry instance.
     */
    public void setLarry(Larry larry) {
        this.larry = larry;
        dialogArea.appendText("Hello! I'm Larry! :)\nWhat can I do for you?\n\n");
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();

        if (input.isBlank()) {
            return;
        }

        String response = larry.getResponse(input);

        dialogArea.appendText("You: " + input + "\n");
        dialogArea.appendText("Larry: " + response + "\n\n");

        userInput.clear();
    }
}
