package motionvdl.controller;

import motionvdl.Debug;
import motionvdl.display.Display;
import motionvdl.model.data.LabeledVideo;

/**
 * MotionVDL controller superclass
 * @author Joseph
 */
public abstract class Controller {

	// titles
	protected String displayTitle = "MotionVDL";        // title on display
	protected String debugTitle = "[controller]";       // title in debug trace
	protected static String outputFile = "output.mvdl"; // filesystem location for exported data

	// components
	protected Controller linkedController; // pointer to the linked controller
	protected Display display;             // pointer to the display
	protected LabeledVideo data;          // pointer to the labelled video data

	// variables
	protected int frameIndex; // index of current frame
	
	
	/**
	 * Default pass action, setup data pointer and display properties
	 * @param video Reference to the video
	 */
	public void pass(LabeledVideo temp) {

		// debug trace
		Debug.trace(debugTitle+" pass");

		// setup controller
		data = temp;
		frameIndex = 0;

		// setup display
		display.clearFrame();
		display.clearGeometry();
		display.setTitle(displayTitle);
		display.setFrame(data.video.getFrame(frameIndex));
		display.setTarget(data.video.width);
	}


	/**
	 * Default click action, do nothing
	 * @param x Normalised x coordinate
	 * @param y Normalised y coordinate
	 */
	public void click(double x, double y) {

		// debug trace
		Debug.trace(debugTitle+" ignored click: no action");
	}


	/**
	 * Default undo action, do nothing
	 */
	public void undo() {

		// debug trace
		Debug.trace(String.format("%s ignored undo: no action",debugTitle));
	}
	
	
	/**
	 * Default calibrate action, do nothing
	 * @param x Normalised x coordinate
	 * @param y Normalised y coordinate
	 */
	public void calibrate(double x, double y) {

		// debug trace
		Debug.trace(debugTitle+" ignored calibrate: no action");
	}
	
	
	/**
	 * Default complete action, export data and pass to the linked controller
	 */
	public void complete() {

		// debug trace
		Debug.trace(debugTitle+" complete");

		// pass temporary pointer to the linked controller
		LabeledVideo temp = data;
		data = null;
		linkedController.pass(temp);
	}
	
	
	/**
	 * Set the current video frame
	 * @param index Index of video frame
	 */
	public void setFrame(int index) {

		// set frame index
		int last = frameIndex;
		frameIndex = Math.min(Math.max(index, 0), data.video.length-1);
		
		// update display
		display.setFrame(data.video.getFrame(frameIndex));

		// debug trace
		Debug.trace(String.format("%s setFrame: %d -> %d",debugTitle,last,frameIndex));
		
	}
	
	
	/**
	 * Set next video frame
	 */
	public void setNextFrame() {
		
		// debug trace
		Debug.trace(String.format("%s setNextFrame ->",debugTitle));
		
		// set frame
		setFrame(frameIndex+1);
	}
	
	
	/**
	 * Set previous video frame
	 */
	public void setPrevFrame() {
		
		// debug trace
		Debug.trace(String.format("%s setPrevFrame ->",debugTitle));
		
		// set frame
		setFrame(frameIndex-1);
	}
	
	
	/**
	 * Set the minimum video frame
	 */
	public void setMinFrame() {
		
		// debug trace
		Debug.trace(String.format("%s setMinFrame ->",debugTitle));
		
		// set frame
		setFrame(0);
	}
	
	
	/**
	 * Set the maximum video frame
	 */
	public void setMaxFrame() {
		
		// debug trace
		Debug.trace(String.format("%s setMaxFrame ->",debugTitle));
		
		// set frame
		setFrame(data.video.length-1);
	}
}
