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
		
		// setup metadata
		displayTitle = "Labelling stage";
		debugTitle = "LabelController";
		exportLocation = "labelS4";
		
		// setup components
		linkedController = mainController;
		display = mainDisplay;
		
		// debug trace
		Debug.trace("Created "+debugTitle);
	}
	
	
	/**
	 * Record a point
	 * @param x The x axis of the click event
	 * @param y The y axis of the click event
	 */
	@Override
	public void click(int x, int y) {
		
		// debug trace
		Debug.trace(debugTitle+" recieved click instruction");

		// record point if the frame label is incomplete
		if (!label.stackFull(frameIndex)) {
			
			// record point
			label.push(frameIndex, x, y);
			display.drawPoint(x, y);
			
			// if point completes frame label, display next frame
			if (label.stackFull(frameIndex)) nextFrame();
		
		// else warn that the frame is complete
		} else {
			Debug.trace("Label controller ignores point instruction");
			display.setMessage("Warning! Frame is full");
		}
	}
	
	
	/**
	 * Delete last point
	 */
	@Override
	public void process() {
		
		// debug trace
		Debug.trace(debugTitle+" recieved process instruction");

		// if the frame label is not empty
		if (!label.stackEmpty(frameIndex)) {
			
			// remove point
			label.delete(frameIndex);
			
			// update display
			display.clearGeometry();
			display.drawPoints(label.getPoints(frameIndex));
		
		// if the frame label is empty
		} else {
			prevFrame();
		}
	}
	
	
	/**
	 * Try to export the labelled video
	 */
	@Override
	public void complete() {
		
		// debug trace
		Debug.trace(debugTitle+" recieved complete instruction");
		
		// export the label if complete
		if (label.checkComplete()) {
			Debug.trace("Label is complete");
			label.export(exportLocation);
			
		// else warn
		} else {
			Debug.trace(debugTitle+" ignores complete instruction");
			display.setMessage("Warning! The label must be complete to export");
		}
	}
	
	
	/**
	 * Pass control to this controller
	 */
	@Override
	public void pass(Video tempVideo) {
		
		// setup label
		label = new Label(MAX_POINTS, tempVideo.length);
		
		super.pass(tempVideo);
	}
	
	
	/**
	 * Display next frame up from current frame
	 */
	@Override
	public void nextFrame() {
		
		// draw frame label
		display.clearGeometry();
		display.drawPoints(label.getPoints(frameIndex));
		
		super.nextFrame();
	}
	
	
	/**
	 * Display next frame down from current frame
	 */
	@Override
	public void prevFrame() {
		
		// draw frame label
		display.clearGeometry();
		display.drawPoints(label.getPoints(frameIndex));
		
		super.prevFrame();
	}
}
