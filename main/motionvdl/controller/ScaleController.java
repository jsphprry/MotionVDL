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
		
		// setup metadata
		displayTitle = "Scaling stage";
		debugTitle = "ScaleController";
		exportLocation = "videoS2";
		
		// setup components
		linkedController = mainController;
		display = mainDisplay;
		
		// debug trace
		Debug.trace("Created "+debugTitle);
	}
	
	
	/**
	 * Scale the video
	 */
	@Override
	public void process() {
		
		// debug trace
		Debug.trace(debugTitle+" recieved process instruction");
		
		// get the target resolution
		// placeholder
		int target = 50;//display.getTarget();
		
		// if the target is valid
		if (target > 0 && target < buffer.width && target < MAX_RES) {
			
			// scale the video
			buffer = buffer.reduce(target, target);
			
			// update the display
			display.setFrame(buffer.getFrame(frameIndex));
			
		// else warn
		} else {
			Debug.trace(debugTitle+" ignores process instruction, invalid target resolution");
			display.setMessage("Warning! Invalid target resolution");
		}
	}
}
