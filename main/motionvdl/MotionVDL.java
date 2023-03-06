package motionvdl;

import javafx.application.Application;
import javafx.stage.Stage;
import motionvdl.controller.MainController;
import motionvdl.display.Display;
import motionvdl.model.Video;

import java.util.List;

/**
 * MotionVDL application starter 
 * @author Henri, Joseph
 */
// Joseph - Is it standard javafx design for the whole 
// MVC be inside an Application-inheriting class like this? 
// Maybe you could explain this to me next time we meet

// Henri - Yes it needs to extend Application,
// that's just how JavaFX functions - will explain further

// Henri - I will also need to slightly alter the structure
// of this class - will explain further, but should be fine
public class MotionVDL extends Application {

	/**
	 * MotionVDL starter program
	 * defines the program arguments
	 * @param args[0] Video location on filesystem
	 * @param args[1] Debug setup flag
	 */
	@Override
	public void start(Stage stage) {

		// need to use Parameters to get args[], and then save in List
		Parameters params = getParameters();
		List<String> args = params.getRaw();

		// throw insufficient arguments
		if (args.size() == 0) throw new IllegalArgumentException("Insufficient arguments");

		// setup debug
		if (args.size() >= 2) Debug.setup(Boolean.parseBoolean(args.get(1)));

		// setup display and controller
		Display display = new Display(675, 475, stage);
		MainController controller = new MainController(display);
		display.sendTo(controller);

		// start main controller with video file
		controller.pass(Video.fromFile(args.get(0)));
	}
}
