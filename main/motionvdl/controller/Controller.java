package motionvdl.controller;

import motionvdl.Debug;
import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL controller superclass
 * @author Joseph
 */
public abstract class Controller {
	
	// labels
	protected String displayTitle = "MotionVDL";  // controller title on display
	protected String debugTitle = "[controller]"; // controller title in debug output
	protected String exportLocation = "video";    // filesystem location for exported encodings
	
	// components
	protected Controller linkedController;
	protected Display display;
	protected Video buffer;
	
	// variables
	protected int frameIndex;
	
	/**
	 * Default controller click instruction
	 * No axis limits because of the assumption that clicks cannot come from outside the frame
	 * @param x The x axis of the click
	 * @param y The y axis of the click
	 */
	public void click(int x, int y) {
		
		// debug trace
		Debug.trace(debugTitle+" recieved click instruction");
		Debug.trace(debugTitle+" ignored click instruction");
	}
	
	
	/**
	 * Default controller process instruction
	 */
	public void process() {
		
		// debug trace
		Debug.trace(debugTitle+" recieved process instruction");
		Debug.trace(debugTitle+" ignored process instruction");
	}
	
	
	/**
	 * Default controller complete instruction
	 * For subcontrollers this should pass control to the main controller
	 * For the main controller this should pass instruction to current subcontroller
	 */
	public void complete() {

		// debug trace
		Debug.trace(debugTitle+" recieved complete instruction");
		
		// export and free video buffer
		buffer.export(exportLocation);
		Video temp = buffer;
		buffer = null;
		
		// pass temporary video to the linked controller
		linkedController.pass(temp);
	}
	
	
	/**
	 * Default controller pass instruction
	 * In the subcontrollers this should setup the controller with a video
	 * In the main controller this should switch control to the next subcontroller
	 */
	public void pass(Video tempVideo) {
		
		// debug trace
		Debug.trace(debugTitle+" recieved pass instruction");
		
		// setup video
		buffer = tempVideo;
		
		// update display
		display.setTitle(displayTitle);
		display.setFrame(buffer.getFrame(frameIndex));
	}
	
	
	/**
	 * Default controller nextFrame instruction
	 * Display next frame up from current frame
	 */
	public void nextFrame() {
		
		// debug trace
		Debug.trace(debugTitle+" recieved nextFrame instruction");
		
		// display next frame
		frameIndex = Math.min(buffer.length - 1, frameIndex + 1);
		display.setFrame(buffer.getFrame(frameIndex));
		Debug.trace(debugTitle+" set to frame "+frameIndex);
	}
	
	
	/**
	 * Default controller prevFrame instruction
	 * Display next frame down from current frame
	 */
	public void prevFrame() {
		
		// debug trace
		Debug.trace(debugTitle+" recieved prevFrame instruction");
		
		// display previous frame
		frameIndex = Math.max(0, frameIndex - 1);
		display.setFrame(buffer.getFrame(frameIndex));
		Debug.trace(debugTitle+" set to frame "+frameIndex);
	}
}
