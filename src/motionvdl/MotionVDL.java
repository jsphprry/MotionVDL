package motionvdl;

import motionvdl.controller.MainController;
import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL application starter
 * @author Joseph
 */
public class MotionVDL {
	
	/**
	 * The starter program
	 * @param args Location of the video on disk
	 */
	public static void main(String[] args) {

		// setup components
		Display display = new Display();
		MainController controller = new MainController(display);
		Video video = new Video(args[0]);
		
		// start
		controller.pass(video);
	}
}
