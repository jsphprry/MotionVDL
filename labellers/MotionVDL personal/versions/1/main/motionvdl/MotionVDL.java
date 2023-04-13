package motionvdl;

import motionvdl.controller.MainController;
import motionvdl.display.Display;

public final class MotionVDL {
	
	// uninstantiable object
	private MotionVDL() {}
	
	/**
	 * MotionVDL starter program
	 * @param args[0] Video location on filesystem
	 * @param args[1] Debug setup flag
	 */
	public static void main(String[] args) {

		// setup debug
		Debug.setup(true);

		// initialise main controller with display
		MainController controller = new MainController(new Display(800,400));

		// start with given file
		if (args.length > 0) controller.open(args[0]);
	}
}
