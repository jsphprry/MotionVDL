package motionvdl.controller;

import motionvdl.Debug;
import motionvdl.display.Display;

/**
 * MotionVDL scaling subcontroller
 * @author Joseph
 */
public class ScaleController extends Controller {
	
	// constants
	// the maximum target resolution, one value for x and y axis because video should be square by this point
	private static final int MAX_RES = 255;
	
	/**
	 * Construct scale controller
	 * @param mainController The main controller
	 * @param mainDisplay The display
	 */
	public ScaleController(MainController mainController, Display mainDisplay) {
		
		// setup titles
		displayTitle = "Scaling stage";
		debugTitle = "Scale controller";
		outputTitle = "videoS2";
		
		// setup components
		linkedController = mainController;
		display = mainDisplay;
		
		// debug trace
		Debug.trace(String.format("Created ScaleController '%s'", debugTitle));
	}
	
	
	/**
	 * Downscale the video data
	 */
	public void next() {
		
		// debug trace
		Debug.trace(debugTitle+" recieved next");
		
		// get the target resolution
		// placeholder
		int target = 50;//display.getTarget();
		
		// valid target resolution
		if (target > 0 && target < buffer.width && target < MAX_RES) {
			
			// scale the video
			buffer = buffer.downScale(target, target);
			
			// next stage
			super.next();
			
		// invalid target resolution
		} else {
			Debug.trace(debugTitle+" skipped next: inavlid target resolution");
			display.setMessage("*Invalid target resolution*");
		}
	}
}
