package motionvdl.controller;

import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL greyscale-conversion subcontroller
 * @author Joseph
 */
public class GreyscaleController extends Controller {
	
	// variables
	private Video greyscaleVideo;
	private boolean greyscaleSet;
	
	/**
	 * Constructor for GreyscaleController instance
	 * @param mc The main controller
	 * @param md The main display
	 * @param v The subject video
	 */
	public GreyscaleController(MainController mc, Display md) {
		
		// setup components
		this.linkedController = mc;
		this.display = md;
		this.video = null;
		
		// setup variables
		this.frameIndex = 0;
		this.greyscaleSet = false;
	}
	
	
	/*
	 * Toggle between greyscale and color video
	 */
	@Override
	public void process() {
		
		// if greyscale video does not exist create it
		if (this.greyscaleVideo == null) this.greyscaleVideo = this.video.greyScale();
		
		// toggle between greyscale and color video
		if (!this.greyscaleSet) {
			this.display.setFrame(this.greyscaleVideo.getFrame(this.frameIndex));
			this.greyscaleSet = true;
		} else {
			this.display.setFrame(this.video.getFrame(this.frameIndex));
			this.greyscaleSet = false;
		}
	}
	
	
	/**
	 * Pass the either the greyscale or color video back to the main controller
	 */
	@Override
	public void complete() {
		
		// if greyscale flag is true then set video to greyscale video
		if (this.greyscaleSet) this.video = this.greyscaleVideo;
		
		// pass video back to main controller
		super.complete();
	}
	
	
	/**
	 * Pass control to this controller
	 */
	protected void pass(Video video) {
		
		// set the video
		this.video = video;
		
		// update display
		this.display.setTitle("MotionVDL Greyscaling stage");
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
}