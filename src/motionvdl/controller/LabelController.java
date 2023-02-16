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
	private int pointIndex;
	private int pointsPerFrame;
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
		this.frameIndex = 0;
		this.pointIndex = 0;
		this.pointsPerFrame = 11;
		this.label = new Label(this.pointsPerFrame, this.video.getDepth());
		
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
		
		// TODO implement frame click behaviour
		throw new UnsupportedOperationException("Label controller point is not implemented");
	}
	
	
	/**
	 * Undo the last frame click action
	 */
	public void process() {
		
		// TODO undo point placement on screen and in label
		throw new UnsupportedOperationException("Label controller process is not implemented");
	}
	
	
	/**
	 * Export the video and label to file and exit program
	 */
	public void complete() {
		
		// TODO implement program exit
		throw new UnsupportedOperationException("Program exit is not implemented");
	}
}
