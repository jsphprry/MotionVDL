package motionvdl;

import motionvdl.display.InputView;
import motionvdl.display.SettingsView;
import motionvdl.display.WelcomeView;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Initialise all Scenes and set properties of the Stage.
 * @author Henri
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {

        // Initialise each individual Scene
        WelcomeView.initialise(stage);
        SettingsView.initialise(stage);
        InputView.initialise(stage);

        // Open the application with WelcomeView displayed
        WelcomeView.changeScene();

        // Set properties of the Stage
        stage.setTitle("MotionVDL");
        stage.getIcons().add(new Image("motionvdl/display/images/javaIcon.png"));
        stage.setOnCloseRequest(windowEvent -> System.exit(0));
        stage.show();
    }

    public static void main (String[] args) { launch(args); }
}