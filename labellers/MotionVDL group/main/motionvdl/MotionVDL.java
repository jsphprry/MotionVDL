package motionvdl;

import javafx.application.Application;
import javafx.stage.Stage;
import motionvdl.controller.MainController;
import motionvdl.display.Display;

import java.util.List;

/**
 * MotionVDL application starter 
 * @author Henri, Joseph
 */
public class MotionVDL extends Application {

	/**
	 * MotionVDL starter program
	 * defines the program arguments
	 * @param args [0] - Video location on filesystem, [1] - Debug setup flag
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		
		// need to use Parameters to get args[], and then save in List
		List<String> args = getParameters().getRaw();
		
		// setup debug
		Debug.setup(true);
		
		// initialise main controller with display
		MainController controller = new MainController(new Display(675, 475, stage));
		
		// start with file if given
		if (args.size() > 0) controller.open(args.get(0));
		
//		// bypass open method to load encoded data into initial controller state
//		try {
//			controller.pass(new LabeledVideo(new Video(FileSystem.readBytes("output.mvdl"))));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
