import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Use this template to create Apps with Graphical User Interfaces.
 *
 * @author Rodrigo Wong Mac
 */
public class Calculator extends Application {

    // TODO: Instance Variables for View Components and Model
    private TextField tfOutput;
    private String temp;
    private String result;
    private String operator;
    private Button previousButton;
    private Button[] btnNumbers;
    private Button[] btnReset;
    private final String btnNumbersStyle = "-fx-font-size: 1.3em; -fx-background-color: #575757; -fx-text-fill: #ffffff; ";
    private final String btnResetStyle = "-fx-font-size: 1.3em; -fx-background-color: #858282; -fx-text-fill: #ffffff; ";
    private final String btnOperatorsStyle = "-fx-font-size: 1.3em; -fx-background-color: #ffa836; -fx-text-fill: #ffffff; ";
    private final String btnClickedStyle = "-fx-opacity: 0.8";

    // TODO: Private Event Handlers and Helper Methods
    private boolean contains(Button[] buttons, Button b) {
        for (Button button : buttons) {
            if (button == b) return true;
        }
        return false;
    }

    private void defaultStyle() {
        if (previousButton != null) {
            if (contains(btnNumbers, previousButton)) previousButton.setStyle(btnNumbersStyle);
            else if (contains(btnReset, previousButton)) previousButton.setStyle(btnResetStyle);
            else previousButton.setStyle(btnOperatorsStyle);
        }
    }

    public void numbersHandler(ActionEvent e) {
        defaultStyle();
        Button currentButton = (Button) e.getSource();
        currentButton.setStyle(btnNumbersStyle + btnClickedStyle);

        previousButton = currentButton;

        String value = currentButton.getText();

        if (operator == null) {
            if (result == null) {
                result = value;
                tfOutput.setText(result);

            } else {
                result += value;
                tfOutput.setText(result);
            }
        } else {
            tfOutput.setAlignment(Pos.CENTER_RIGHT);
            if (temp == null) {
                temp = value;
                tfOutput.setText(temp);
            } else {
                temp += value;
                tfOutput.setText(temp);
            }
        }
    }

    public void operatorsHandler(ActionEvent e) {
        defaultStyle();
        Button currentButton = (Button) e.getSource();
        currentButton.setStyle(btnOperatorsStyle + btnClickedStyle);

        previousButton = currentButton;

        if (temp != null) equalHandler(e);
        operator = currentButton.getText();
    }

    public void equalHandler(ActionEvent e) {
        defaultStyle();
        Button currentButton = (Button) e.getSource();
        currentButton.setStyle(btnOperatorsStyle + btnClickedStyle);

        previousButton = currentButton;

        if (result == null) {
            tfOutput.setText("0");
            return;
        }
        if (operator == null || temp == null) tfOutput.setText(result);
        else {
            double value1 = Double.parseDouble(result);
            double value2 = Double.parseDouble(temp);
            switch (operator) {
                case "+" -> result = String.valueOf(value1 + value2);
                case "-" -> result = String.valueOf(value1 - value2);
                case "x" -> result = String.valueOf(value1 * value2);
                case "/" -> result = String.valueOf(value1 / value2);
            }

            temp = null;

            if (Double.parseDouble(result) % 1 == 0) {
                result = String.valueOf((int) (Double.parseDouble(result)));
                tfOutput.setAlignment(Pos.CENTER_RIGHT);
            } else {
                if(result.length() > 13){
                    tfOutput.setAlignment(Pos.CENTER_LEFT);
                } else {
                    tfOutput.setAlignment(Pos.CENTER_RIGHT);
                }
            }
        }
        tfOutput.setText(result);
    }

    public void resetHandler(ActionEvent e) {
        defaultStyle();
        Button currentButton = (Button) e.getSource();
        currentButton.setStyle(btnResetStyle + btnClickedStyle);

        previousButton = currentButton;

        String value = currentButton.getText();

        if (value.equals("AC")) {
            tfOutput.setAlignment(Pos.CENTER_RIGHT);
            result = null;
            operator = null;
            tfOutput.setText("0");
        }
        if (value.equals("CE")) {
            tfOutput.setAlignment(Pos.CENTER_RIGHT);
            temp = null;
            tfOutput.setText("0");
        }
    }

    /**
     * This is where you create your components and the model and add event
     * handlers.
     *
     * @param stage The main stage
     * @throws Exception exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Pane root = new Pane();
        Scene scene = new Scene(root, 250, 370); // set the size here
        stage.setTitle("Calculator"); // set the window title here
        stage.setScene(scene);
        stage.setResizable(false);
        // TODO: Add your GUI-building code here

        // 1. Create the model

        // 2. Create the GUI components
        tfOutput = new TextField("0");

        btnNumbers = new Button[11];
        for (int i = 0; i < btnNumbers.length; i++) {
            if (i == btnNumbers.length - 1) {
                btnNumbers[i] = new Button(".");
                continue;
            }
            btnNumbers[i] = new Button(String.valueOf(i));
        }
        Button buttonAC = new Button("AC");
        Button buttonCE = new Button("CE");
        Button buttonDivision = new Button("/");
        Button buttonMultiply = new Button("x");
        Button buttonPlus = new Button("+");
        Button buttonEqual = new Button("=");
        Button buttonMinus = new Button("-");

        HBox layer1 = new HBox(10, buttonAC, buttonCE);
        HBox layer2 = new HBox(10, btnNumbers[7], btnNumbers[8], btnNumbers[9], buttonDivision);
        HBox layer3 = new HBox(10, btnNumbers[4], btnNumbers[5], btnNumbers[6], buttonMultiply);
        HBox layer4 = new HBox(10, btnNumbers[1], btnNumbers[2], btnNumbers[3], buttonPlus);
        HBox layer5 = new HBox(10, btnNumbers[0], btnNumbers[btnNumbers.length - 1], buttonEqual, buttonMinus);

        btnReset = new Button[]{buttonAC, buttonCE};
        Button[] btnOperators = {buttonDivision, buttonMultiply, buttonMinus, buttonPlus};
        Button[] btnEqual = {buttonEqual};
        Button[][] allButtons = {btnNumbers, btnOperators, btnEqual, btnReset};

        // 3. Add components to the root
        root.getChildren().addAll(tfOutput, layer1, layer2, layer3, layer4, layer5);

        // 4. Configure the components (colors, fonts, size, location)
        root.setStyle("-fx-background-color: #212121");

        tfOutput.setPrefSize(230, 50);
        tfOutput.relocate(10, 10);
        tfOutput.setEditable(false);
        tfOutput.setFont(new Font(25));
        tfOutput.setAlignment(Pos.CENTER_RIGHT);
        tfOutput.setFocusTraversable(false);

        for (Button b : btnOperators) b.setStyle(btnOperatorsStyle);
        for (Button b : btnNumbers) b.setStyle(btnNumbersStyle);
        for (Button b : btnReset) b.setStyle(btnResetStyle);
        buttonEqual.setStyle(btnOperatorsStyle);

        for (Button[] buttons : allButtons) {
            for (Button b : buttons) {
                b.setPrefSize(50, 50);
            }
        }

        layer1.relocate(130, 70);
        layer2.relocate(10, 130);
        layer3.relocate(10, 190);
        layer4.relocate(10, 250);
        layer5.relocate(10, 310);

        // 5. Add Event Handlers and do final setup
        for (Button b : btnNumbers) b.setOnAction(this::numbersHandler);
        for (Button b : btnOperators) b.setOnAction(this::operatorsHandler);
        for (Button b : btnReset) b.setOnAction(this::resetHandler);
        buttonEqual.setOnAction(this::equalHandler);

        // 6. Show the stage
        stage.show();
    }

    /**
     * Make no changes here.
     *
     * @param args unused
     */
    public static void main(String[] args) {
        launch(args);
    }
}
