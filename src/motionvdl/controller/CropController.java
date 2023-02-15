package motionvdl.controller;

import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL cropping subcontroller
 * @author Joseph
 */
public class CropController extends Subcontroller {
	
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
	 * @param d The crop stage display
	 * @param v The subject video
	 */
	public CropController(MainController mc, Display md, Video lv) {
		
		// setup components
		this.mainController = mc;
		this.mainDisplay = md;
		this.localVideo = lv;
		
		// setup variables
		this.frameIndex = 0;
		this.originSet = false;
		this.targetSet = false;
		
		// update display
		this.mainDisplay.setTitle("MotionVDL Cropping stage");
		this.mainDisplay.setFrame(this.localVideo.getFrame(this.frameIndex));
	}
	
	
	/**
	 * Set the crop frame coordinates
	 * @param x The x axis of the coordinate
	 * @param y The y axis of the coordinate
	 */
	public void point(int x, int y) {
		
		// first click
		// if neither target or origin are set
		if (!this.originSet && !this.targetSet) {
			
			// set origin coordinate
			this.originX = x;
			this.originY = y;
			this.originSet = true;

			// update display
			this.mainDisplay.setPoint(this.originX, this.originY);
		
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
			this.mainDisplay.clearPoints();
			this.mainDisplay.setPoint(this.originX, this.originY);
			this.mainDisplay.setPoint(this.targetX, this.targetY);
		
		// third click
		// if both are set then clear both coordinates
		} else {
			
			// unset coordinates
			this.originSet = false;
			this.targetSet = false;
			
			// update display
			this.mainDisplay.clearPoints();
		}
	}
	
	
	/*
	 * Crop the video around the crop frame
	 */
	public void process() {
		
		// throw undefined crop frame
		if (this.originSet == false || this.targetSet == false) throw new IllegalStateException("Undefined crop frame");
		
		// crop video
		this.localVideo = this.localVideo.crop(this.originX, this.originY, (this.targetX - this.originX), (this.targetY - this.originY));
		
		// update display
		this.mainDisplay.setFrame(this.localVideo.getFrame(this.frameIndex));
	}
}
