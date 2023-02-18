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
	
	
	/*
	 * Scale the video
	 */
	public void process() {
		
		// get the target resolution
		Point target = this.display.getTarget();
		
		// scale the video
		this.video = this.video.downScale((int) target.getX(), (int) target.getY());
		
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
