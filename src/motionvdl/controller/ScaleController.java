package motionvdl.controller;

import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL scaling subcontroller
 * @author Joseph
 */
public class ScaleController extends Controller {
	
	/**
	 * Constructor for ScaleController instance
	 * @param mc The main controller
	 * @param md The main display
	 * @param v The subject video
	 */
	public ScaleController(MainController mc, Display md) {
		
		// setup components
		this.linkedController = mc;
		this.display = md;
		this.video = null;
		
		// setup variables
		this.frameIndex = 0;
	}
	
	// TODO implement methods
	
	
	/**
	 * Pass control to this controller
	 */
	public void pass(Video video) {
		
		// set the video
		this.video = video;
		
		// update display
		this.display.setTitle("MotionVDL Scaling stage");
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
}
