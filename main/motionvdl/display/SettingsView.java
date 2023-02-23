package motionvdl.display;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The Settings View displays a settings page,
 * used for adjusting frame settings.
 * @author Henri
 */
public class SettingsView {
    private static final Pane pane = new Pane();
    private static Stage primaryStage = null;
    private static Scene settingsScene = null;
    private static final Label titleLab = new Label();
    private static final Button inputSceneBut = new Button();
    private static final Button sendResBut = new Button();
    private static final ImageView imageView = new ImageView();
    private static final TextField widthTextField = new TextField();
    private static final TextField heightTextField = new TextField();

    /**
     * Initialise the nodes of this Scene.
     */
    public static void initialise(Stage stage) {
        // Title Label
        titleLab.setLayoutX(5);
        titleLab.setLayoutY(5);
        titleLab.setText("Settings. \n" +
                "This page can be used to \n" +
                "adjust settings of the video \n" +
                "to be used (e.g. cropping).");
        pane.getChildren().add(titleLab);

        // ImageView to show frame example
        imageView.setLayoutX(280);
        imageView.setLayoutY(10);
        imageView.setImage(new Image("motionvdl/display/images/javaIcon.png")); // Set image to first frame as reference
        imageView.setFitHeight(400);
        imageView.setFitWidth(400);
        imageView.setPreserveRatio(false);
        pane.getChildren().add(imageView);

        // Button for switching to InputView
        inputSceneBut.setLayoutX(420);
        inputSceneBut.setLayoutY(460);
        inputSceneBut.setMinSize(120, 60);
        inputSceneBut.setText("Confirm Settings");
        inputSceneBut.setOnAction(
                actionEvent -> InputView.changeScene() );
        pane.getChildren().add(inputSceneBut);

        // TextField for specifying resolution width
        widthTextField.setLayoutX(720);
        widthTextField.setLayoutY(150);
        widthTextField.setMinSize(5, 5);
        widthTextField.setMaxWidth(60);
        pane.getChildren().add(widthTextField);

        // TextField for specifying resolution height
        heightTextField.setLayoutX(795);
        heightTextField.setLayoutY(150);
        heightTextField.setMinSize(5, 5);
        heightTextField.setMaxWidth(60);
        pane.getChildren().add(heightTextField);

        // Button for confirming and checking values in TextFields above
        sendResBut.setLayoutX(730);
        sendResBut.setLayoutY(220);
        sendResBut.setMinSize(120, 60);
        sendResBut.setText("Confirm Values");
        sendResBut.setOnAction(
                actionEvent -> sendTargetRes() );
        pane.getChildren().add(sendResBut);

        pane.setId("paneID");
        settingsScene = new Scene(pane, 16*60, 9*60);
        settingsScene.getStylesheets().add("motionvdl/display/styleSheets/settingsStyle.css");
        primaryStage = stage;
    }

    /**
     * Send the target resolution to [class it's sent to].
     */
    public static void sendTargetRes() {
        // Get target resolution from two text fields and send to Display
        try {
            int widthInt = Integer.parseInt(widthTextField.getText());
            int heightInt = Integer.parseInt(heightTextField.getText());
            System.out.println(widthInt + ", " + heightInt);
        } catch (NumberFormatException e) {
            System.out.println("Not valid - " + e);
        }
    }

    /**
     * The method called to make this Scene active on the Stage.
     */
    public static void changeScene() {
        primaryStage.setScene(settingsScene);
    }
}