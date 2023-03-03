package motionvdl;

import motionvdl.display.Display;
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

        new Display(stage);

    }

    public static void main (String[] args) { launch(args); }
}