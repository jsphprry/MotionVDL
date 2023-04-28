package motionvdl.controller;

import java.io.IOException;

import motionvdl.Debug;
import motionvdl.model.data.LabeledVideo;

/**
 * MotionVDL labelling subcontroller
 * @author Joseph
 */
public class LabelController extends Controller {

	/**
	 * Construct label controller
	 * @param mc Pointer to main controller
	 */
	public LabelController(MainController mc) {

		// setup titles
		displayTitle = "Frame labelling";
		debugTitle = "LSC";

		// setup components
		linkedController = mc;
		display = linkedController.display;

		// debug trace
		Debug.trace(String.format("Created LabelController '%s'", debugTitle));
	}
	
	
	/**
	 * Setup frame label in addition to default behaviour
	 * @param temp Temporary pointer to labelled video data
	 */
	@Override
	public void pass(LabeledVideo temp) {
		
		// default behaviour
		super.pass(temp);
		
		// go to first frame with incomplete label
		int index = 0;
		while (data.label.getSize(index) == data.label.capacity && index < data.video.length-1) index += 1;
		setFrame(index);
		
		// draw label
		display.drawBody(data.label.getPoints(frameIndex), LabeledVideo.CONNECTOR_SEQUENCE);
	}


	/**
	 * Record point on current frame label
	 * @param x Normalised x coordinate
	 * @param y Normalised y coordinate
	 */
	@Override
	public void click(double x, double y) {
		
		// debug trace
		Debug.trace(String.format("%s click (%.2f,%.2f)",debugTitle, x, y));
		
		// if the current frame label is incomplete
		if (!data.label.checkFull(frameIndex)) {
			
			// record point
			data.label.push(frameIndex, x, y);
			
			// draw frame label
			display.clearGeometry();
			display.drawBody(data.label.getPoints(frameIndex), LabeledVideo.CONNECTOR_SEQUENCE);
			
			// go to next frame if current frame label is full when radio is true
			if (display.getAuto()) if (data.label.checkFull(frameIndex)) setNextFrame();
			
		// warn if the current frame label is incomplete
		} else {
			Debug.trace(debugTitle+" ignored click: label full");
		}
	}
	
	
	/**
	 * Undo last point on current frame label
	 */
	@Override
	public void undo() {
		
		// debug trace
		Debug.trace(String.format("%s undo",debugTitle));
		
		// if non-empty label
		if (!data.label.checkEmpty(frameIndex)) {
			
			// remove point
			data.label.pop(frameIndex);
			
			// draw frame label
			display.clearGeometry();
			display.drawBody(data.label.getPoints(frameIndex), LabeledVideo.CONNECTOR_SEQUENCE);
			
		// go to previous frame if empty label
		} else {
			setPrevFrame();
		}
	}
	
	
	/**
	 * Calibrate the label
	 * @param x Normalised x coordinate
	 * @param y Normalised y coordinate
	 */
	public void calibrate(double x, double y) {

		// debug trace
		Debug.trace(debugTitle+" calibrate");
		
		// try label calibration
		try {
			data.label.calibrate(frameIndex, x, y);
		} catch (IllegalStateException e) {
			Debug.trace(e.getMessage());
		}
		
		// redraw frame label
		display.clearGeometry();
		display.drawBody(data.label.getPoints(frameIndex), LabeledVideo.CONNECTOR_SEQUENCE);
	}


	/**
	 * Export the label and exit program
	 */
	@Override
	public void complete() {

		// debug trace
		Debug.trace(debugTitle+" complete");

		try {
			
			// export data
			data.export(outputFile);
			
			// close display
			display.setVisible(false);
			display.dispose();
			
			// debug trace
			Debug.trace("Exit program");
			
		} catch (IOException e) {
			Debug.trace(e.getMessage());
		}
	}


	/**
	 * Display next frame and frame-label
	 */
	@Override
	public void setFrame(int index) {
		
		// default behaviour
		super.setFrame(index);
		
		// draw frame label
		display.clearGeometry();
		display.drawBody(data.label.getPoints(frameIndex), LabeledVideo.CONNECTOR_SEQUENCE);
	}
}
