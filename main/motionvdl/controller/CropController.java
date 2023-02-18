package motionvdl.controller;

import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL cropping subcontroller
 * @author Joseph
 */
public class CropController extends Controller {
	
	// variables
	private int originX;
	private int originY;
	private int targetX;
	private int targetY;
	private boolean originSet;
	private boolean targetSet;
	
	/**
	 * Constructor for CropController instance
	 * @param mc The main controller
	 * @param md The main display
	 * @param v The subject video
	 */
	public CropController(MainController mc, Display md) {
		
		// setup components
		this.linkedController = mc;
		this.display = md;
		this.video = null;
		
		// setup variables
		this.frameIndex = 0;
		this.originSet = false;
		this.targetSet = false;
	}
	
	
	/**
	 * Set the crop frame coordinates
	 * @param x The x axis of the coordinate
	 * @param y The y axis of the coordinate
	 */
	@Override
	public void point(int x, int y) {
		
		// first click
		// if neither target or origin are set
		if (!this.originSet && !this.targetSet) {
			
			// set origin coordinate
			this.originX = x;
			this.originY = y;
			this.originSet = true;

			// update display
			this.display.setPoint(this.originX, this.originY);
		
		// second click
		// if the origin is set and the target is not
		} else if (this.originSet && !this.targetSet) {
			
			// set target coordinate
			this.targetX = x;
			this.targetY = y;
			this.targetSet = true;
			
			// handle target-not-in-fourth-quadrant-of-origin case
			int tempOriginX = Math.min(this.originX, this.targetX);
			int tempOriginY = Math.min(this.originY, this.targetY);
			int tempTargetX = Math.max(this.originX, this.targetX);
			int tempTargetY = Math.max(this.originY, this.targetY);
			this.originX = tempOriginX;
			this.originY = tempOriginY;
			this.targetX = tempTargetX;
			this.targetY = tempTargetY;
			
			// update display
			this.display.clearPoints();
			this.display.setPoint(this.originX, this.originY);
			this.display.setPoint(this.targetX, this.targetY);
		
		// third click
		// if both are set then clear both coordinates
		} else {
			
			// unset coordinates
			this.originSet = false;
			this.targetSet = false;
			
			// update display
			this.display.clearPoints();
		}
	}
	
	
	/*
	 * Crop the video around the crop frame
	 */
	@Override
	public void process() {
		
		// throw undefined crop frame
		if (this.originSet == false || this.targetSet == false) throw new IllegalStateException("Undefined crop frame");
		
		// crop video
		this.video = this.video.crop(this.originX, this.originY, (this.targetX - this.originX), (this.targetY - this.originY));
		
		// update display
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
	
	
	/**
	 * Pass control to this controller
	 */
	protected void pass(Video video) {
		
		// set the video
		this.video = video;
		
		// update display
		this.display.setTitle("MotionVDL Cropping stage");
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
}