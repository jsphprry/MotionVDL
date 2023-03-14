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
	
	// variables
	private Label label;
	
	/**
	 * Construct label controller
	 * @param mainController The main controller
	 * @param mainDisplay The main display
	 */
	public LabelController(MainController mainController, Display mainDisplay) {
		
		// setup titles
		displayTitle = "Labelling stage";
		debugTitle = "Label controller";
		outputTitle = "labelS4";
		
		// setup components
		linkedController = mainController;
		display = mainDisplay;
		
		// debug trace
		Debug.trace(String.format("Created LabelController '%s'", debugTitle));
	}
	
	
	/**
	 * Record a point on the current video frame
	 * @param x The x axis of the click event
	 * @param y The y axis of the click event
	 */
	public void click(int x, int y) {
		
		// debug trace
		Debug.trace(debugTitle+" recieved click");

		// record point if the frame label is incomplete
		if (!label.stackFull(frameIndex)) {
			
			// record point
			label.push(frameIndex, x, y);
			display.drawPoint(x, y);
			
			// if point completes frame label, display next frame
			if (display.getRadio() && label.stackFull(frameIndex)) up();
		
		// else warn that the frame is complete
		} else {
			Debug.trace(debugTitle+" skipped click: max points on frame");
			display.setMessage("*Max points on frame*");
		}
	}
	
	
	/**
	 * Display next frame and frame-label
	 */
	public void up() {
		
		// debug trace
		Debug.trace(debugTitle+" recieved up");
		
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
		
		// debug trace
		Debug.trace(debugTitle+" recieved down");
		
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
		if (!label.stackEmpty(frameIndex)) {
			
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
	 * Export the body label
	 */
	public void next() {
		
		// debug trace
		Debug.trace(debugTitle+" recieved next");
		
		// export the label if complete
		if (label.checkComplete()) {
			label.export(outputTitle);
			
		// else warn
		} else {
			Debug.trace(debugTitle+" skipped next: incomplete label");
			display.setMessage("*Incomplete label*");
		}
	}
	
	
	/**
	 * Setup label then pass control
	 */
	public void pass(Video tempVideo) {
		
		// setup label
		label = new Label(MAX_POINTS, tempVideo.length);
		
		// pass control
		super.pass(tempVideo);
	}
}
