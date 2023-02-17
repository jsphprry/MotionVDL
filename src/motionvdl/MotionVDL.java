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
		Video video = new Video(args[0]);
		Display display = new Display();
		MainController mainController = new MainController(display);
		
		// start controller sequence
		mainController.start(video);
	}
}
