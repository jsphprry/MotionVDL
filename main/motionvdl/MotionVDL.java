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
	 * MotionVDL starter program
	 * @param args Location of the video on disk
	 */
	public static void main(String[] args) {
		
		/*// setup components
		Display display = new Display();
		MainController mainController = new MainController(display);
		
		// start controller sequence with video
		Video video = new Video(args[0]);
		mainController.start(video);*/
		
		// start program with video file
		new MainController(new Display()).start(Video.fromFile(args[0]));
	}
}
