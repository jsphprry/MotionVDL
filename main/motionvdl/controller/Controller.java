package motionvdl.controller;

import motionvdl.Debug;
import motionvdl.display.Display;
import motionvdl.model.LabelledVideo;

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
	protected LabelledVideo data;          // pointer to the labelled video data

	// variables
	protected int frameIndex; // index of current frame


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
	 * Default nextFrame action, display next possible frame
	 */
	public void nextFrame() {

		// display next frame
		frameIndex = Math.min(data.video.length - 1, frameIndex + 1);
		display.setFrame(data.video.getFrame(frameIndex));

		// debug trace
		Debug.trace(String.format("%s nextFrame: frame %d",debugTitle,frameIndex));
	}


	/**
	 * Default prevFrame action, display previous possible frame
	 */
	public void prevFrame() {

		// display previous frame
		frameIndex = Math.max(0, frameIndex - 1);
		display.setFrame(data.video.getFrame(frameIndex));

		// debug trace
		Debug.trace(String.format("%s prevFrame: frame %d",debugTitle,frameIndex));
	}


	/**
	 * Default complete action, export data and pass to the linked controller
	 */
	public void complete() {

		// debug trace
		Debug.trace(debugTitle+" complete");

		// pass temporary pointer to the linked controller
		LabelledVideo temp = data;
		data = null;
		linkedController.pass(temp);
	}


	/**
	 * Default pass action, setup data pointer and display properties
	 * @param video Reference to the video
	 */
	public void pass(LabelledVideo temp) {

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
	}
}
