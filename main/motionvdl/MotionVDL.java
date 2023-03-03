package motionvdl;

import javafx.application.Application;
import javafx.stage.Stage;
import motionvdl.controller.MainController;
import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL application starter 
 * @author Joseph
 */
public class MotionVDL extends Application {

	/**
	 * MotionVDL starter program
	 * defines the program arguments
	 * @param args[0] Video location on filesystem
	 * @param args[1] Debug setup flag
	 */
	public static void main(String[] args) {
		
		// throw insufficient arguments
		if (args.length == 0) throw new IllegalArgumentException("Insufficient arguments");
		
		// setup debug
		if (args.length >= 2) Debug.setup(Boolean.parseBoolean(args[1]));
		
		// setup display and controller
		Display display = new Display(675, 475, new Stage());
		MainController controller = new MainController(display);
		display.sendTo(controller);
		
		// start main controller with video file
		controller.pass(Video.fromFile(args[0]));
	}



	@Override
	public void start(Stage stage) {
		Display display = new Display(675, 475, stage);
	}

	public static void tempDisplay(String[] args) {
		launch(args);
	}
}
