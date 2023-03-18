package motionvdl.controller;

import motionvdl.Debug;
import motionvdl.display.Display;
import motionvdl.model.Label;
import motionvdl.model.Video;

/**
 * MotionVDL labelling subcontroller
 * @author Joseph
 */
public class LabelController extends Controller {
	
	// constants
	private final static int MAX_POINTS = 11;
	
	// components
	private Label label;
	
	/**
	 * Construct label controller
	 * @param mainController The main controller
	 * @param mainDisplay The main display
	 */
	public LabelController(MainController mainController, Display mainDisplay) {
		
		// setup titles
		displayTitle = "Frame labelling";
		debugTitle = "Label controller";
		outputTitle = "label";
		
		// setup components
		linkedController = mainController;
		display = mainDisplay;
		
		// debug trace
		Debug.trace(String.format("Created LabelController '%s'", debugTitle));
	}
	
	
	/**
	 * Record a point on the current video frame
	 * @param x The normalised x axis of the click event
	 * @param y The normalised y axis of the click event
	 */
	public void click(double x, double y) {
		
		// debug trace
		Debug.trace(String.format("%s recieved click (%.2f,%.2f)",debugTitle, x, y));

		// if the current frame label is incomplete
		if (!label.checkStackFull(frameIndex)) {
			
			// record point
			label.push(frameIndex, x, y);
			display.drawPoint(x, y);
			
			// if auto-next is enabled, then check if the 
			// current stack is full and frame-up if true
			if (display.getRadio()) if (label.checkStackFull(frameIndex)) up();
		
		// else warn that the frame is complete
		} else {
			String message = String.format("Label stack%d is full", frameIndex);
			Debug.trace(debugTitle+" ignored next: "+message);
			display.setMessage(message);
		}
	}
	
	
	/**
	 * Display next frame and frame-label
	 */
	public void up() {
		
		// default behaviour
		super.up();
		
		// draw frame label
		display.clearGeometry();
		display.drawPoints(label.getPoints(frameIndex));
	}
	
	
	/**
	 * Display previous frame and frame-label
	 */
	public void down() {
		
		// default behaviour
		super.down();
		
		// draw frame label
		display.clearGeometry();
		display.drawPoints(label.getPoints(frameIndex));
	}
	
	
	/**
	 * Remove the last point on the current frame
	 */
	public void undo() {
		
		// debug trace
		Debug.trace(debugTitle+" recieved undo");

		// non-empty label
		if (!label.checkStackEmpty(frameIndex)) {
			
			// remove point
			label.delete(frameIndex);
			
			// update display
			display.clearGeometry();
			display.drawPoints(label.getPoints(frameIndex));
		
		// empty label
		} else {
			down();
		}
	}
	
	
	/**
	 * Export the label
	 */
	public void next() {
		
		// debug trace
		Debug.trace(debugTitle+" recieved next");
		
		// export the label if complete
		if (label.checkComplete()) {
			label.export(outputTitle);
			
		// else warn
		} else {
			String message = "Incomplete label";
			Debug.trace(debugTitle+" ignored next: "+message);
			display.setMessage(message);
		}
	}
	
	
	/**
	 * Setup label then pass control
	 */
	public void pass(Video tempVideo) {
		
		// setup label
		label = new Label(tempVideo.length, MAX_POINTS);
		
		// pass control
		super.pass(tempVideo);
	}
}
