package motionvdl.display;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * The Welcome View will be used as a welcome screen
 * (currently doesn't contain any content).
 * @author Henri
 */
public class WelcomeView {
    private static final Pane pane = new Pane();
    private static Stage primaryStage = null;
    private static Scene welcomeScene = null;
    private static final Label titleLab = new Label();
    private static final Button settingsSceneBut = new Button();

    /**
     * Initialise the nodes of this Scene.
     */
    public static void initialise(Stage stage) {
        // Title Label
        titleLab.setLayoutX(5);
        titleLab.setLayoutY(5);
        titleLab.setText("Welcome. \n" +
                "This page can be used as a welcome screen, \n" +
                "perhaps to select a certain file?");
        pane.getChildren().add(titleLab);

        // Button for switching to SettingsView
        settingsSceneBut.setLayoutX(420);
        settingsSceneBut.setLayoutY(460);
        settingsSceneBut.setMinSize(120,60);
        settingsSceneBut.setText("Next page");
        settingsSceneBut.setOnAction(
                actionEvent -> SettingsView.changeScene() );
        pane.getChildren().add(settingsSceneBut);

        pane.setId("paneID");
        welcomeScene = new Scene(pane, 16*60, 9*60);
        welcomeScene.getStylesheets().add("motionvdl/display/styleSheets/welcomeStyle.css");
        primaryStage = stage;
    }

    /**
     * The method called to make this Scene active on the Stage.
     */
    public static void changeScene() {
        primaryStage.setScene(welcomeScene);
    }
}