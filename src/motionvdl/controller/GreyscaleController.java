package motionvdl.controller;

import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL greyscale-conversion subcontroller
 * @author Joseph
 */
public class GreyscaleController extends Subcontroller {
	
	// variables
	private Video greyscaleVideo;
	private boolean greyscaleSet;
	
	/**
	 * Constructor for GreyscaleController instance
	 * @param mc The main controller
	 * @param md The main display
	 * @param lv The local video
	 */
	public GreyscaleController(MainController mc, Display md, Video lv) {
		
		// setup components
		this.mainController = mc;
		this.mainDisplay = md;
		this.localVideo = lv;
		
		// setup variables
		this.frameIndex = 0;
		this.greyscaleSet = false;
		
		// update display
		this.mainDisplay.setTitle("MotionVDL Greyscale stage");
		this.mainDisplay.setFrame(this.localVideo.getFrame(this.frameIndex));
	}
	
	
	/*
	 * Toggle between greyscale and color video
	 */
	public void process() {
		
		// if greyscale video does not exist create it
		if (this.greyscaleVideo == null) this.greyscaleVideo = this.localVideo.greyScale();
		
		// toggle between greyscale and color video
		if (!this.greyscaleSet) {
			this.mainDisplay.setFrame(this.greyscaleVideo.getFrame(this.frameIndex));
			this.greyscaleSet = true;
		} else {
			this.mainDisplay.setFrame(this.localVideo.getFrame(this.frameIndex));
			this.greyscaleSet = false;
		}
	}
	
	
	/**
	 * Pass the either the greyscale or color video back to the main controller
	 */
	public void complete() {
		
		// if greyscale flag is true then set video to greyscale video
		if (this.greyscaleSet) this.localVideo = this.greyscaleVideo;
		
		// pass video back to main controller
		this.mainController.nextController(this.localVideo);
	}
}
