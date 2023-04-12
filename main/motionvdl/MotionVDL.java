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
		Parameters params = getParameters();
		List<String> args = params.getRaw();
		
		// setup debug
		Debug.setup(true);
		
		// setup display and controller
		Display display = new Display(675, 475, stage);
		MainController controller = new MainController(display);
		
		// start with file if given
		if (args.size() > 0) controller.open(args.get(0));
		
		// start main controller with mvdl file
		controller.open("output.mvdl");
		display.setViewPort();
		
		// bypass open method to load encoded data into initial controller state
//		try {
//			controller.pass(new LabeledVideo(new Video(FileSystem.readBytes("output.mvdl"))));
//			display.setViewPort();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
}
