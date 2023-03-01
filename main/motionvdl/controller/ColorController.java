package motionvdl.controller;

import motionvdl.Debug;
import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL color processing subcontroller
 * @author Joseph
 */
public class ColorController extends Controller {
	
	// video work buffer
	private Video workBuffer;
	
	/**
	 * Construct color controller
	 * @param mainController The main controller
	 * @param mainDisplay The display
	 */
	public ColorController(MainController mainController, Display mainDisplay) {
		
		// setup metadata
		displayTitle = "Color stage";
		debugTitle = "Color controller";
		exportLocation = "videoS3";
		
		// setup components
		linkedController = mainController;
		display = mainDisplay;
		
		// debug trace
		Debug.trace("Created ColorController '"+debugTitle+"'");
	}
	
	
	/**
	 * Toggle between greyscale and color video
	 */
	@Override
	public void process() {
		
		// debug trace
		Debug.trace(debugTitle+" recieved process instruction");
		
		// if greyscale video does not exist create it
		if (workBuffer == null) workBuffer = buffer.greyscale();
		
		// swap video buffers
		Video temp = buffer;
		buffer = workBuffer;
		workBuffer = temp;
		Debug.trace(debugTitle+" displaying "+((buffer.greyscale) ? "greyscale" : "color")+" video");
		
		// update display
		display.setFrame(buffer.getFrame(frameIndex));
	}
}
