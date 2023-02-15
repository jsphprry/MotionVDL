package motionvdl.controller;

import motionvdl.display.Display;
import motionvdl.model.Video;

public class ScaleController extends Subcontroller {
	
	/**
	 * Constructor for ScaleController instance
	 * @param mc The main controller
	 * @param md The main display
	 * @param lv The local video
	 */
	public ScaleController(MainController mc, Display md, Video lv) {
		
		// setup components
		this.mainController = mc;
		this.mainDisplay = md;
		this.localVideo = lv;
		
		// setup variables
		this.frameIndex = 0;
		
		// update display
		this.mainDisplay.setTitle("MotionVDL Scale stage");
		this.mainDisplay.setFrame(this.localVideo.getFrame(this.frameIndex));
	}
	
	// TODO implement point, process and complete methods
}
