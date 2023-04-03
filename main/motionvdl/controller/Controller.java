package motionvdl.controller;

import motionvdl.Debug;
import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL controller superclass
 * @author Joseph
 */
public abstract class Controller {
	
	// titles
	protected String displayTitle = "MotionVDL";  // controller title on display
	protected String debugTitle = "[controller]"; // controller title in debug output
	protected String outputTitle = "video";       // filesystem location for exported encodings, appends .mvdl
	
	// components
	protected Controller linkedController;
	protected Display display;
	protected Video video;
	
	// variables
	protected int frameIndex;
	
	
	/**
	 * Respond to click event
	 * default: Do nothing
	 * @param x The normalised x-axis of the click event
	 * @param y The normalised y-axis of the click event
	 */
	public void click(double x, double y) {
		
		// debug trace
		Debug.trace(debugTitle + " skipped click: no action");
	}
	
	
	/**
	 * Display next video frame
	 * default: Display next possible frame
	 */
	public void up() {
		
		// debug trace
		Debug.trace(debugTitle + " received up");
		
		// display next frame
		frameIndex = Math.min(video.length - 1, frameIndex + 1);
		display.setFrame(video.getFrame(frameIndex));
		Debug.trace(debugTitle + " set to frame " + frameIndex);
	}
	
	
	/**
	 * Display previous video frame.
	 * Default: Display previous possible frame
	 */
	public void down() {
		
		// debug trace
		Debug.trace(debugTitle + " received down");
		
		// display previous frame
		frameIndex = Math.max(0, frameIndex - 1);
		display.setFrame(video.getFrame(frameIndex));
		Debug.trace(debugTitle + " set to frame " + frameIndex);
	}
	
	
	/**
	 * Undo the most recent change to the data
	 * default: Clear display geometry.
	 */
	public void undo() {
		
		// debug trace
		Debug.trace(debugTitle+" received undo");
		
		// update display
		display.clearGeometry();
	}
	
	
	/**
	 * Switch to the next controller stage
	 * default: Export video data and pass control to the linked controller
	 */
	public void next() {
		
		// debug trace
		Debug.trace(debugTitle+" received next");
		
		// export and free video buffer
		video.export(outputTitle);
		Video temp = video;
		video = null;
		
		// pass temporary video to the linked controller
		linkedController.pass(temp);
	}
	
	
	/**
	 * Pass control to this controller
	 * default: Setup display and video
	 */
	public void pass(Video tempVideo) {
		
		// debug trace
		Debug.trace(debugTitle+" received pass");
		
		// setup video
		video = tempVideo;
		
		// update display
		display.clearGeometry();
		display.setTitle(displayTitle);
		display.setFrame(video.getFrame(frameIndex));
	}
}
