package motionvdl.controller;

import java.awt.Point;

import motionvdl.display.Display;
import motionvdl.model.Video;

/**
 * MotionVDL scaling subcontroller
 * @author Joseph
 */
public class ScaleController extends Controller {
	
	/**
	 * Constructor for ScaleController instance
	 * @param mainController The main controller
	 * @param display The display
	 */
	public ScaleController(MainController mainController, Display display) {
		
		// setup components
		this.linkedController = mainController;
		this.display = display;
		this.video = null;
		
		// setup variables
		this.frameIndex = 0;
	}
	
	
	/*
	 * Scale the video
	 */
	public void process() {
		
		// get the target resolution
		Point target = this.display.getTarget();
		int targetX = (int) target.getX();
		int targetY = (int) target.getY();
		
		// if the target is invalid
		if (targetX > this.video.getWidth() || targetY > this.video.getWidth()) {
			this.display.setMessage("The target resolution must not be greater than the video resolution");
		}
		
		// scale the video
		this.video = this.video.downScale(targetX, targetY);
		
		// update the display
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
	
	
	/**
	 * Pass control to this controller
	 */
	protected void pass(Video video) {
		
		// set the video
		this.video = video;
		
		// update display
		this.display.setTitle("MotionVDL Scaling stage");
		this.display.setFrame(this.video.getFrame(this.frameIndex));
	}
}
