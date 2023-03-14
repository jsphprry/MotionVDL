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
		
		// setup titles
		displayTitle = "Color stage";
		debugTitle = "Color controller";
		outputTitle = "videoS3";
		
		// setup components
		linkedController = mainController;
		display = mainDisplay;
		
		// debug trace
		Debug.trace(String.format("Created ColorController '%s'", debugTitle));
	}
	
	
	/**
	 * Toggle between greyscale and color video
	 */
	public void click(int x, int y) {
		
		// debug trace
		Debug.trace(debugTitle+" recieved click");
		
		// if greyscale video does not exist create it
		if (workBuffer == null) workBuffer = buffer.greyscale();
		
		// swap video buffers
		Video temp = buffer;
		buffer = workBuffer;
		workBuffer = temp;
		Debug.trace(debugTitle+" displayed "+((buffer.greyscale) ? "greyscale" : "color")+" video");
		
		// update display
		display.setFrame(buffer.getFrame(frameIndex));
	}
}
