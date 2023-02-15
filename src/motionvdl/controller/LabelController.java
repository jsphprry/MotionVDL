package motionvdl.controller;

import motionvdl.display.Display;
import motionvdl.model.Label;
import motionvdl.model.Video;

/**
 * MotionVDL labelling subcontroller
 * @author Joseph
 */
public class LabelController extends Subcontroller {
	
	// variables
	private int pointIndex;
	private int pointsPerFrame;
	private Label label;
	
	/**
	 * Constructor for LabelController instance
	 * @param mc The main controller
	 * @param md The main display
	 * @param lv The local video
	 */
	public LabelController(MainController mc, Display md, Video lv) {
		
		// setup components
		this.mainController = mc;
		this.mainDisplay = md;
		this.localVideo = lv;
		
		// setup variables
		this.frameIndex = 0;
		this.pointIndex = 0;
		this.pointsPerFrame = 11;
		this.label = new Label(this.pointsPerFrame, this.localVideo.getDepth());
		
		// update display
		this.mainDisplay.setTitle("MotionVDL Labelling stage");
		this.mainDisplay.setFrame(this.localVideo.getFrame(this.frameIndex));
	}
	
	
	/**
	 * Record the coordinates of a click in the point label
	 * @param x The x axis of the click event
	 * @param y The y axis of the click event
	 */
	public void point(int x, int y) {
		// TODO implement frame click behaviour
		
	}
	
	
	/**
	 * Undo the last frame click action
	 */
	public void process() {
		// TODO undo point placement on screen and in label
		
	}
	
	
	/**
	 * Pass the video and label back to the main controller
	 */
	public void complete() {
		this.mainController.finalStage(this.localVideo, this.label);
	}
}
