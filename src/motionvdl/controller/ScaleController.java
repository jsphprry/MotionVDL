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
	public ScaleController(MainController mc, Display md, Video v) {
		
		// setup components
		this.linkedController = mc;
		this.display = md;
		this.video = v;
		
		// setup variables
		this.frameIndex = 0;
		
		// update display
		this.display.setTitle("MotionVDL Scale stage");
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
	
	// TODO implement methods
}
