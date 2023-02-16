package motionvdl.controller;

import motionvdl.display.Display;
import motionvdl.model.Label;
import motionvdl.model.Video;

/**
 * MotionVDL labelling subcontroller
 * @author Joseph
 */
public class LabelController extends Controller {
	
	// variables
	private int[] framePoints;
	private final int maxPoints;
	private Label label;
	
	/**
	 * Constructor for LabelController instance
	 * @param mc The main controller
	 * @param md The main display
	 * @param v The subject video
	 */
	public LabelController(MainController mc, Display md, Video v) {
		
		// setup components
		this.linkedController = mc;
		this.display = md;
		this.video = v;
		
		// setup variables
		this.frameIndex  = 0;
		this.framePoints = new int[this.video.getDepth()]; // default int value is 0 so no need to populate array
		this.maxPoints   = 11;
		this.label       = new Label(this.maxPoints, this.video.getDepth());
		
		// update display
		this.display.setTitle("MotionVDL Labelling stage");
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
	
	
	/**
	 * Record the coordinates of a click in the point label
	 * @param x The x axis of the click event
	 * @param y The y axis of the click event
	 */
	public void point(int x, int y) {
		
		// get the next frame-wise point index
		int pointIndex = this.framePoints[this.frameIndex];
		
		// if the frame label is incomplete
		if (pointIndex < this.maxPoints) {

			// write point to label
			this.label.write(pointIndex, this.frameIndex, x, y);
			
			// update display
			this.display.setPoint(x, y);
			
			// increment frame-wise point counter
			this.framePoints[this.frameIndex] += 1;
		
		// otherwise go to next frame
		} else {
			this.frameUp();
		}
	}
	
	
	/**
	 * Undo the last frame click action
	 */
	public void process() {
		
		// get the previous frame-wise point index
		int pointIndex = this.framePoints[this.frameIndex] - 1;
		
		// if the frame label is not empty
		if (pointIndex >= 0) {

			// delete point from label
			this.label.delete(pointIndex, this.frameIndex);
			
			// update display
			this.display.clearPoints();
			this.display.setPoints(this.label.getRow(this.frameIndex));
			
			// decrement frame-wise point counter
			this.framePoints[this.frameIndex] -= 1;
		
		// otherwise go to prev frame
		} else {
			this.frameDown();
		}
	}
	
	
	/**
	 * Display next frame up from current frame
	 */
	public void frameUp() {
		
		// increment frameIndex
		this.frameIndex = Math.min(this.video.getDepth() - 1, frameIndex + 1);
		
		// update display
		this.display.clearPoints();
		this.display.setPoints(this.label.getRow(this.frameIndex));
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
	
	
	/**
	 * Display next frame down from current frame
	 */
	public void frameDown() {
		
		// decrement frameIndex
		this.frameIndex = Math.max(0, frameIndex - 1);
		
		// update display
		this.display.clearPoints();
		this.display.setPoints(this.label.getRow(this.frameIndex));
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
	
	
	/**
	 * Export the video and label to file then exit program
	 */
	public void complete() {
		
		// TODO implement program exit
		throw new UnsupportedOperationException("Program exit is not implemented");
	}
}
